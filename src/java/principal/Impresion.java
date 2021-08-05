/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import java.io.IOException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import modelo.SeriesM;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;

import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import servicio.Conexion;
import servicio.Logger;

/**
 *
 * @author aprivera
 */
public class Impresion {

    Logger logger;

    public static String cantidadConLetra(String s) {
        StringBuilder result = new StringBuilder();
        BigDecimal totalBigDecimal = new BigDecimal(s).setScale(2, BigDecimal.ROUND_DOWN);
        long parteEntera = totalBigDecimal.toBigInteger().longValue();
        int triUnidades = (int) ((parteEntera % 1000));
        int triMiles = (int) ((parteEntera / 1000) % 1000);
        int triMillones = (int) ((parteEntera / 1000000) % 1000);
        int triMilMillones = (int) ((parteEntera / 1000000000) % 1000);

        if (parteEntera == 0) {
            result.append("Cero ");
            return result.toString();
        }

        if (triMilMillones > 0) {
            result.append(triTexto(triMilMillones).toString() + "Mil ");
        }
        if (triMillones > 0) {
            result.append(triTexto(triMillones).toString());
        }

        if (triMilMillones == 0 && triMillones == 1) {
            result.append("Millón ");
        } else if (triMilMillones > 0 || triMillones > 0) {
            result.append("Millones ");
        }

        if (triMiles > 0) {
            result.append(triTexto(triMiles).toString() + "Mil ");
        }
        if (triUnidades > 0) {
            result.append(triTexto(triUnidades).toString());
        }

        return result.toString();
    }

    /**
     * Convierte una cantidad de tres cifras a su representación escrita con
     * letra.
     *
     * @param n La cantidad a convertir.
     * @return Una cadena de texto que contiene la representación con letra del
     * número que se recibió como argumento.
     */
    private static StringBuilder triTexto(int n) {
        StringBuilder result = new StringBuilder();
        int centenas = n / 100;
        int decenas = (n % 100) / 10;
        int unidades = (n % 10);

        switch (centenas) {
            case 0:
                break;
            case 1:
                if (decenas == 0 && unidades == 0) {
                    result.append("Cien ");
                    return result;
                } else {
                    result.append("Ciento ");
                }
                break;
            case 2:
                result.append("Doscientos ");
                break;
            case 3:
                result.append("Trescientos ");
                break;
            case 4:
                result.append("Cuatrocientos ");
                break;
            case 5:
                result.append("Quinientos ");
                break;
            case 6:
                result.append("Seiscientos ");
                break;
            case 7:
                result.append("Setecientos ");
                break;
            case 8:
                result.append("Ochocientos ");
                break;
            case 9:
                result.append("Novecientos ");
                break;
        }

        switch (decenas) {
            case 0:
                break;
            case 1:
                if (unidades == 0) {
                    result.append("Diez ");
                    return result;
                } else if (unidades == 1) {
                    result.append("Once ");
                    return result;
                } else if (unidades == 2) {
                    result.append("Doce ");
                    return result;
                } else if (unidades == 3) {
                    result.append("Trece ");
                    return result;
                } else if (unidades == 4) {
                    result.append("Catorce ");
                    return result;
                } else if (unidades == 5) {
                    result.append("Quince ");
                    return result;
                } else {
                    result.append("Dieci");
                }
                break;
            case 2:
                if (unidades == 0) {
                    result.append("Veinte ");
                    return result;
                } else {
                    result.append("Veinti");
                }
                break;
            case 3:
                result.append("Treinta ");
                break;
            case 4:
                result.append("Cuarenta ");
                break;
            case 5:
                result.append("Cincuenta ");
                break;
            case 6:
                result.append("Sesenta ");
                break;
            case 7:
                result.append("Setenta ");
                break;
            case 8:
                result.append("Ochenta ");
                break;
            case 9:
                result.append("Noventa ");
                break;
        }

        if (decenas > 2 && unidades > 0) {
            result.append("y ");
        }

        switch (unidades) {
            case 0:
                break;
            case 1:
                result.append("Un ");
                break;
            case 2:
                result.append("Dos ");
                break;
            case 3:
                result.append("Tres ");
                break;
            case 4:
                result.append("Cuatro ");
                break;
            case 5:
                result.append("Cinco ");
                break;
            case 6:
                result.append("Seis ");
                break;
            case 7:
                result.append("Siete ");
                break;
            case 8:
                result.append("Ocho ");
                break;
            case 9:
                result.append("Nueve ");
                break;
        }

        return result;
    }

    public XWPFDocument getMacrotitulo(Conexion cnx, Logger logger, String ambiente, String path, String[] indicesSerie, List<SeriesM> listaSerie, String user) throws IOException, SQLException, InvalidFormatException {
        String filename = "";
        SeriesEmitidas SE = new SeriesEmitidas();
        FormasNumeradas FN = new FormasNumeradas();
        XWPFDocument doc = new XWPFDocument();

        String SER = "";
        String AIO = "";
        Integer contador = 0;
        for (String indice : indicesSerie) {
            if (indicesSerie.length > 1) {
                SER += (contador == 0 ? "'" : ", '") + listaSerie.get(Integer.valueOf(indice)).getSRSCOD() + "'";
                AIO += (contador == 0 ? "'" : ", '") + listaSerie.get(Integer.valueOf(indice)).getSRSAIO() + "'";
            } else {
                SER = "'" + listaSerie.get(Integer.valueOf(indice)).getSRSCOD() + "'";
                AIO = listaSerie.get(Integer.valueOf(indice)).getSRSAIO().toString();
            }
            contador++;

        }

        List<SeriesM> data = SE.DatosSerie3(cnx, logger, ambiente, SER, AIO);

        if (!FN.IMPRFN(cnx, logger, ambiente, AIO).isEmpty()) {
            for (SeriesM d : data) {

                if (d.getSRSTVR().equals("S")) {
                    filename = path + "/Macrotitulo Tasa Variable - Anverso.docx";
                    doc = getMacrotituloTV(cnx, logger, ambiente, data, indicesSerie, listaSerie, filename, user);
                } else {
                    filename = path + "/Macrotitulo Tasa Fija - Anverso.docx";
                    doc = getMacrotituloT(cnx, logger, ambiente, data, indicesSerie, listaSerie, filename, user);

                }

            }
        } else {
            doc = null;
        }

//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName());
//        }
        return doc;

    }

    public XWPFDocument getReversoM(Conexion cnx, Logger logger, String ambiente, String path, String[] indicesSerie, List<SeriesM> listaSerie, String user) throws IOException, SQLException, InvalidFormatException {
        logger.info("---Usuario:" + user + " Preparando el reverso del macrotitulo para la descarga del documento de impresion---", this.getClass().getSimpleName(), new Object() {
        }.getClass().getEnclosingMethod().getName());
        FormasNumeradas FN = new FormasNumeradas();
        XWPFDocument doc;
        String SER = "";
        String AIO = "";
        Integer contador = 0;
        for (String indice : indicesSerie) {
            if (indicesSerie.length > 1) {
                SER += (contador == 0 ? "'" : ", '") + listaSerie.get(Integer.valueOf(indice)).getSRSCOD() + "'";
                AIO += (contador == 0 ? "'" : ", '") + listaSerie.get(Integer.valueOf(indice)).getSRSAIO() + "'";
            } else {
                SER = "'" + listaSerie.get(Integer.valueOf(indice)).getSRSCOD() + "'";
                AIO = listaSerie.get(Integer.valueOf(indice)).getSRSAIO().toString();
            }
            contador++;

        }
        if (!FN.IMPRFN(cnx, logger, ambiente, AIO).isEmpty()) {
            String filename = path + "/Macrotitulo - Reverso.docx";
            doc = new XWPFDocument(OPCPackage.open(filename));

        } else {
            doc = null;
        }
        return doc;
    }

    public XWPFDocument getMacrotituloT(Conexion cnx, Logger logger, String ambiente, List<SeriesM> data, String[] indicesSerie, List<SeriesM> listaSerie, String filename, String user) throws IOException, SQLException, InvalidFormatException {
        SeriesEmitidas SE = new SeriesEmitidas();
        FormasNumeradas FN = new FormasNumeradas();
        XWPFDocument doc = new XWPFDocument(OPCPackage.open(filename));
        try {
            String SER = "";
            String AIO = "";
            Integer contador = 0;
            for (String indice : indicesSerie) {
                if (indicesSerie.length > 1) {
                    SER += (contador == 0 ? "'" : ", '") + listaSerie.get(Integer.valueOf(indice)).getSRSCOD() + "'";
                    AIO += (contador == 0 ? "'" : ", '") + listaSerie.get(Integer.valueOf(indice)).getSRSAIO() + "'";
                } else {
                    SER = "'" + listaSerie.get(Integer.valueOf(indice)).getSRSCOD() + "'";
                    AIO = listaSerie.get(Integer.valueOf(indice)).getSRSAIO().toString();
                }
                contador++;

            }
            logger.info("---Usuario:" + user + " Preparando Serie " + SER + " del año " + AIO + " para la descarga del documento de impresion---", this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
            // create a document

            String SRSMNEt = null;
            String MONEDALET = null;
            DecimalFormat format = new DecimalFormat("0.#");
            String FACT = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            for (SeriesM d : data) {

                if (d.getSRSMNE().trim().equals("LPS")) {
                    SRSMNEt = "Lps";
                    MONEDALET = "Lempiras";
                } else {
                    SRSMNEt = "US$";
                    MONEDALET = "Dolares";
                }

                for (XWPFParagraph p : doc.getParagraphs()) {
                    List<XWPFRun> runs = p.getRuns();
                    if (runs != null) {
                        for (XWPFRun r : runs) {
                            String text = r.getText(0);
                            if (text != null && text.contains("<COD_MACRO>")) {
                                text = text.replace("<COD_MACRO>", "" + d.getSRSAIO().toString() + d.getSRSCOD() + "");
                                r.setText(text, 0);
                            }

                            if (text != null && text.contains("<SERIE>")) {
                                text = text.replace("<SERIE>", "" + d.getSRSCOD() + "");
                                r.setText(text, 0);
                            }

                            if (text != null && text.contains("<MON>")) {
                                text = text.replace("<MON>", "" + SRSMNEt + "");
                                r.setText(text, 0);
                            }

                            if (text != null && text.contains("<CANTIDAD_NUMEROS>")) {
                                text = text.replace("<CANTIDAD_NUMEROS>", "" + String.valueOf(d.getSRSMBN()) + "");
                                r.setText(text, 0);
                            }

                            if (text != null && text.contains("<CANTIDAD_LETRAS1>")) {
                                text = text.replace("<CANTIDAD_LETRAS1>", cantidadConLetra(d.getSRSMBN().toString().toUpperCase()).toUpperCase());
                                r.setText(text, 0);
                            }
                            if (text != null && text.contains("<DIA>")) {
                                text = text.replace("<DIA>", FACT.substring(8, 10));
                                r.setText(text, 0);
                            }
                            
                            String mes =d.getSRSFDV().substring(5, 7);
                            int mesI = Integer.parseInt(mes);
//        switch(LocalDate.now().getMonthValue()){
                            switch (mesI) {
                                case 1:
                                    mes = "enero";
                                    break;
                                case 2:
                                    mes = "febrero";
                                    break;
                                case 3:
                                    mes = "marzo";
                                    break;
                                case 4:
                                    mes = "abril";
                                    break;
                                case 5:
                                    mes = "mayo";
                                    break;
                                case 6:
                                    mes = "junio";
                                    break;
                                case 7:
                                    mes = "julio";
                                    break;
                                case 8:
                                    mes = "agosto";
                                    break;
                                case 9:
                                    mes = "septiembre";
                                    break;
                                case 10:
                                    mes = "octubre";
                                    break;
                                case 11:
                                    mes = "noviembre";
                                    break;
                                case 12:
                                    mes = "diciembre";
                                    break;

                            }
                            
                            if (text != null && text.contains("<MES>")) {
                                text = text.replace("<MES>", "" + mes + "");
                                r.setText(text, 0);
                            }
                            if (text != null && text.contains("<AIO>")) {
                                text = text.replace("<AIO>", "" + d.getSRSFDV().substring(0, 4) + "");
                                r.setText(text, 0);
                            }
                            if (text != null && text.contains("<TASAIN>")) {
                                text = text.replace("<TASAIN>", "" + d.getSRSTNL() + "");
                                r.setText(text, 0);
                            }
                            if (text != null && text.contains("<DIA>")) {
                                text = text.replace("<DIA>", FACT.substring(8, 10));
                                r.setText(text, 0);
                            }
                            String mes2 =FACT.substring(5, 7);
                            int mesI2 = Integer.parseInt(mes2);
//        switch(LocalDate.now().getMonthValue()){
                            switch (mesI2) {
                                case 1:
                                    mes2 = "enero";
                                    break;
                                case 2:
                                    mes2 = "febrero";
                                    break;
                                case 3:
                                    mes2 = "marzo";
                                    break;
                                case 4:
                                    mes2 = "abril";
                                    break;
                                case 5:
                                    mes2 = "mayo";
                                    break;
                                case 6:
                                    mes2 = "junio";
                                    break;
                                case 7:
                                    mes2 = "julio";
                                    break;
                                case 8:
                                    mes2 = "agosto";
                                    break;
                                case 9:
                                    mes2 = "septiembre";
                                    break;
                                case 10:
                                    mes2 = "octubre";
                                    break;
                                case 11:
                                    mes2 = "noviembre";
                                    break;
                                case 12:
                                    mes2 = "diciembre";
                                    break;

                            }
                            
                            if (text != null && text.contains("<MES_T>")) {
                                text = text.replace("<MES_T>", "" + mes2 + "");
                                r.setText(text, 0);
                            }
                            if (text != null && text.contains("<AIO_T>")) {
                                text = text.replace("<AIO_T>", "" + FACT.substring(0, 4) + "");
                                r.setText(text, 0);
                            }

                        }
                    }
                }
            }
            FN.ActEstFN(cnx, logger, ambiente, FN.ConsultMIXFN(cnx, logger, ambiente, AIO), SER, AIO, user);
            logger.info("---El usuario " + user + "  ha descargado exitosamente el documento de impresion de la serie " + SER + " del año " + AIO + "  ---", this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("---El documento de impresion no se pudo descargar ---", this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
        }
        return doc;
    }

    public XWPFDocument getMacrotituloTV(Conexion cnx, Logger logger, String ambiente, List<SeriesM> data, String[] indicesSerie, List<SeriesM> listaSerie, String filename, String user) throws IOException, SQLException, InvalidFormatException {
        SeriesEmitidas SE = new SeriesEmitidas();
        FormasNumeradas FN = new FormasNumeradas();
        XWPFDocument doc = new XWPFDocument(OPCPackage.open(filename));
        try {

            String SER = "";
            String AIO = "";
            Integer contador = 0;
            for (String indice : indicesSerie) {
                if (indicesSerie.length > 1) {
                    SER += (contador == 0 ? "'" : ", '") + listaSerie.get(Integer.valueOf(indice)).getSRSCOD() + "'";
                    AIO += (contador == 0 ? "'" : ", '") + listaSerie.get(Integer.valueOf(indice)).getSRSAIO() + "'";
                } else {
                    SER = "'" + listaSerie.get(Integer.valueOf(indice)).getSRSCOD() + "'";
                    AIO = listaSerie.get(Integer.valueOf(indice)).getSRSAIO().toString();
                }
                contador++;

            }
            logger.info("---" + user + " Preparando Serie " + SER + " del año " + AIO + " para la descarga del documento de impresion---", this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());

            // create a document
            String SRSMNEt = null;
            String MONEDALET = null;
            DecimalFormat format = new DecimalFormat("0.#");
            String FACT = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            for (SeriesM d : data) {

                if (d.getSRSMNE().trim().equals("LPS")) {
                    SRSMNEt = "Lps";
                    MONEDALET = "Lempiras";
                } else {
                    SRSMNEt = "US$";
                    MONEDALET = "Dolares";
                }

                for (XWPFParagraph p : doc.getParagraphs()) {
                    List<XWPFRun> runs = p.getRuns();
                    if (runs != null) {
                        for (XWPFRun r : runs) {
                            String text = r.getText(0);
                            if (text != null && text.contains("<COD_MACRO>")) {
                                text = text.replace("<COD_MACRO>", "" + d.getSRSAIO().toString() + d.getSRSCOD() + "");
                                r.setText(text, 0);
                            }

                            if (text != null && text.contains("<SERIE>")) {
                                text = text.replace("<SERIE>", "" + d.getSRSCOD() + "");
                                r.setText(text, 0);
                            }

                            if (text != null && text.contains("<MON>")) {
                                text = text.replace("<MON>", "" + SRSMNEt + "");
                                r.setText(text, 0);
                            }

                            if (text != null && text.contains("<CANTIDAD_NUMEROS>")) {
                                text = text.replace("<CANTIDAD_NUMEROS>", "" + String.valueOf(d.getSRSMBN()) + "");
                                r.setText(text, 0);
                            }

                            if (text != null && text.contains("<CANTIDAD_LETRAS1> <CANTIDAD_LETRAS2>")) {
                                text = text.replace("<CANTIDAD_LETRAS1> <CANTIDAD_LETRAS2>", "" + cantidadConLetra(d.getSRSMBN().toString().toUpperCase().toUpperCase()) + "");
                                r.setText(text, 0);
                            }
                            if (text != null && text.contains("<DIA> de ")) {
                                text = text.replace("<DIA> de ", "" + d.getSRSFDE().substring(8, 10) + " de ");
                                r.setText(text, 0);
                            }
                            
                            String mes =d.getSRSFDE().substring(5, 7);
                            int mesI = Integer.parseInt(mes);
//        switch(LocalDate.now().getMonthValue()){
                            switch (mesI) {
                                case 1:
                                    mes = "enero";
                                    break;
                                case 2:
                                    mes = "febrero";
                                    break;
                                case 3:
                                    mes = "marzo";
                                    break;
                                case 4:
                                    mes = "abril";
                                    break;
                                case 5:
                                    mes = "mayo";
                                    break;
                                case 6:
                                    mes = "junio";
                                    break;
                                case 7:
                                    mes = "julio";
                                    break;
                                case 8:
                                    mes = "agosto";
                                    break;
                                case 9:
                                    mes = "septiembre";
                                    break;
                                case 10:
                                    mes = "octubre";
                                    break;
                                case 11:
                                    mes = "noviembre";
                                    break;
                                case 12:
                                    mes = "diciembre";
                                    break;

                            }
                            
                            if (text != null && text.contains("<MES>")) {
                                text = text.replace("<MES>", "" + mes + "");
                                r.setText(text, 0);
                            }
                            if (text != null && text.contains("<AIO>")) {
                                text = text.replace("<AIO>", "" + d.getSRSFDE().substring(0, 4) + "");
                                r.setText(text, 0);
                            }
                            if (text != null && text.contains("<TASAIN>")) {
                                text = text.replace("<TASAIN>", "" + d.getSRSTNL() + "");
                                r.setText(text, 0);
                            }
                            if (text != null && text.contains("<DIFERENCIAL>")) {
                                text = text.replace("<DIFERENCIAL>", "" + d.getSRSDIF() + "");
                                r.setText(text, 0);
                            }
                            if (text != null && text.contains("<MINIMA>")) {
                                text = text.replace("<MINIMA>", "" + d.getSRSTMN() + "");
                                r.setText(text, 0);
                            }
                            if (text != null && text.contains("<MAXIMA>")) {
                                text = text.replace("<MAXIMA>", "" + d.getSRSTMX() + "");
                                r.setText(text, 0);
                            }
                            if (text != null && text.contains("<DIA>")) {
                                text = text.replace("<DIA>", "" + FACT.substring(8, 10) + "");
                                r.setText(text, 0);
                            }
                            
                             String mes2 =FACT.substring(5, 7);
                            int mesI2 = Integer.parseInt(mes2);
//        switch(LocalDate.now().getMonthValue()){
                            switch (mesI2) {
                                case 1:
                                    mes2 = "enero";
                                    break;
                                case 2:
                                    mes2 = "febrero";
                                    break;
                                case 3:
                                    mes2 = "marzo";
                                    break;
                                case 4:
                                    mes2 = "abril";
                                    break;
                                case 5:
                                    mes2 = "mayo";
                                    break;
                                case 6:
                                    mes2 = "junio";
                                    break;
                                case 7:
                                    mes2 = "julio";
                                    break;
                                case 8:
                                    mes2 = "agosto";
                                    break;
                                case 9:
                                    mes2 = "septiembre";
                                    break;
                                case 10:
                                    mes2 = "octubre";
                                    break;
                                case 11:
                                    mes2 = "noviembre";
                                    break;
                                case 12:
                                    mes2 = "diciembre";
                                    break;

                            }
                            
                            
                            if (text != null && text.contains("<MES_T>")) {
                                text = text.replace("<MES_T>", "" + mes2 + "");
                                r.setText(text, 0);
                            }
                            if (text != null && text.contains("<AIO_T>")) {
                                text = text.replace("<AIO_T>", "" + FACT.substring(0, 4) + "");
                                r.setText(text, 0);
                            }

                        }
                    }
                }
            }
            FN.ActEstFN(cnx, logger, ambiente, FN.ConsultMIXFN(cnx, logger, ambiente, AIO), SER, AIO, user);
            logger.info("---El usuario " + user + "  ha descargado exitosamente el documento de impresion de la serie " + SER + " del año " + AIO + "  ---", this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("---El documento de impresion no se pudo descargar ---", this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
        }
        return doc;
    }

    public XWPFDocument getReversoC(Conexion cnx, Logger logger, String ambiente, String path, String[] indicesSerie, List<SeriesM> listaSerie, String user) throws IOException, SQLException, InvalidFormatException {
        logger.info("---Usuario:" + user + " Preparando el reverso del certificado en custodia para la descarga del documento de impresion---", this.getClass().getSimpleName(), new Object() {
        }.getClass().getEnclosingMethod().getName());
        FormasNumeradas FN = new FormasNumeradas();
        XWPFDocument doc;
        String SER = "";
        String AIO = "";
        Integer contador = 0;
        for (String indice : indicesSerie) {
            if (indicesSerie.length > 1) {
                SER += (contador == 0 ? "'" : ", '") + listaSerie.get(Integer.valueOf(indice)).getSRSCOD() + "'";
                AIO += (contador == 0 ? "'" : ", '") + listaSerie.get(Integer.valueOf(indice)).getSRSAIO() + "'";
            } else {
                SER = "'" + listaSerie.get(Integer.valueOf(indice)).getSRSCOD() + "'";
                AIO = listaSerie.get(Integer.valueOf(indice)).getSRSAIO().toString();
            }
            contador++;

        }
        if (!FN.IMPRFN(cnx, logger, ambiente, AIO).isEmpty()) {
            String filename = path + "/Certificado Custodia - Reverso.docx";
            doc = new XWPFDocument(OPCPackage.open(filename));

        } else {
            doc = null;
        }
        return doc;
    }

    public XWPFDocument getCertificado(Conexion cnx, Logger logger, String ambiente, String[] indicesSerie, List<SeriesM> listaSerie, String filename, String user) throws SQLException, InvalidFormatException, IOException {
        CertificadoCustodia CC = new CertificadoCustodia();
        SeriesEmitidas SE = new SeriesEmitidas();
        FormasNumeradas FN = new FormasNumeradas();
        XWPFDocument doc = new XWPFDocument(OPCPackage.open(filename));
        String Accion = "--- " + user + " Preparando certificado en custodia para la descarga del documento de impresion---";
        String SER = "";
        String AIO = "";
        Integer contador = 0;
        for (String indice : indicesSerie) {
            if (indicesSerie.length > 1) {
                SER += (contador == 0 ? "'" : ", '") + listaSerie.get(Integer.valueOf(indice)).getCTCCOD() + "'";
                AIO += (contador == 0 ? "'" : ", '") + listaSerie.get(Integer.valueOf(indice)).getCTCAIO() + "'";
            } else {
                SER = "'" + listaSerie.get(Integer.valueOf(indice)).getCTCCOD() + "'";
                AIO = listaSerie.get(Integer.valueOf(indice)).getCTCAIO().toString();
            }
            contador++;

        }
        if (!FN.IMPRFN(cnx, logger, ambiente, AIO).isEmpty()) {
//            try {

            List<SeriesM> data = CC.ImpDatos(cnx, logger, ambiente, Accion, indicesSerie, listaSerie);
            for (SeriesM d : data) {
                String SRSMNEt = null;
                String MONEDALET = null;
                if (d.getSRSMNE().trim().equals("LPS")) {
                    SRSMNEt = " Lps";
                    MONEDALET = "Lempiras";
                } else {
                    SRSMNEt = " US$";
                    MONEDALET = "Dolares";
                }
                String FACT = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                for (XWPFParagraph p : doc.getParagraphs()) {
                    List<XWPFRun> runs = p.getRuns();
                    if (runs != null) {
                        for (XWPFRun r : runs) {
                            String text = r.getText(0);
                            if (text != null && text.contains("<NUMCUSTODIA>")) {
                                text = text.replace("<NUMCUSTODIA>", "" + d.getCTCCCD() + "");
                                r.setText(text, 0);
                            }
                            if (text != null && text.contains("<MON>.")) {
                                text = text.replace("<MON>.", "" + SRSMNEt + "");
                                r.setText(text, 0);
                            }
                            if (text != null && text.contains("<CANTIDAD_NUMEROS_TOTAL>")) {
                                text = text.replace("<CANTIDAD_NUMEROS_TOTAL>", "" + String.valueOf(d.getSRSMBN()) + "");
                                r.setText(text, 0);
                            }
                            if (text != null && text.contains("<NOMBRE> <NOMBRE2>")) {
                                text = text.replace("<NOMBRE> <NOMBRE2>", "" + d.getCTCNOM().trim() + "");
                                r.setText(text, 0);
                            }
                            if (text != null && text.contains("<SERIE>")) {
                                text = text.replace("<SERIE>", "" + d.getCTCCOD() + "");
                                r.setText(text, 0);
                            }

                            if (text != null && text.contains("<INICIAL>")) {
                                text = text.replace("<INICIAL>", "" + d.getCTCBNI() + "");
                                r.setText(text, 0);
                            }
                            if (text != null && text.contains("<FINAL>")) {
                                text = text.replace("<FINAL>", "" + d.getCTCBNF() + "");
                                r.setText(text, 0);
                            }
                            if (text != null && text.contains("<CANTIDAD_LETRAS1> <CANTIDAD_LETRAS2>")) {
                                text = text.replace("<CANTIDAD_LETRAS1> <CANTIDAD_LETRAS2>", "" + cantidadConLetra(d.getSRSMBN().toString().toUpperCase()).toUpperCase() + MONEDALET.toUpperCase() + " EXACTOS");
                                r.setText(text, 0);
                            }

                            if (text != null && text.contains("<MON>")) {
                                text = text.replace("<MON>", "" + SRSMNEt + "");
                                r.setText(text, 0);
                            }

                            if (text != null && text.contains("<CANTIDAD_NUMEROS>")) {
                                text = text.replace("<CANTIDAD_NUMEROS>", "" + String.valueOf(d.getSRSMBN()) + "");
                                r.setText(text, 0);
                            }
                            if (text != null && text.contains("<CANTIDAD_LETRAS_TOTAL1> <CANTIDAD_LETRAS_TOTAL2>")) {
                                text = text.replace("<CANTIDAD_LETRAS_TOTAL1> <CANTIDAD_LETRAS_TOTAL2>", "" + cantidadConLetra(d.getSRSMBN().toString().toUpperCase()).toUpperCase() + MONEDALET.toUpperCase() + " EXACTOS");
                                r.setText(text, 0);
                            }
//                                if (text != null && text.contains("<CANTIDAD_LETRAS_TOTAL1> <CANTIDAD_LETRAS_TOTAL2>")) {
//                                    text = text.replace("<CANTIDAD_LETRAS_TOTAL1> <CANTIDAD_LETRAS_TOTAL2>", "NUEVE MIL EXACTOS");
//                                    r.setText(text, 0);
//                                }
//                                if (text != null && text.contains("<CANTIDAD_NUMEROS_TOTAL>")) {
//                                    text = text.replace("<CANTIDAD_NUMEROS_TOTAL>", "" + String.valueOf(d.getSRSMBN()) + "");
//                                    r.setText(text, 0);
//                                }
                            if (text != null && text.contains("<DIA>")) {
                                text = text.replace("<DIA>", "" + FACT.substring(8, 10) + "");
                                r.setText(text, 0);
                            }

                            String mes = FACT.substring(5, 7);
                            int mesI = Integer.parseInt(mes);
//        switch(LocalDate.now().getMonthValue()){
                            switch (mesI) {
                                case 1:
                                    mes = "enero";
                                    break;
                                case 2:
                                    mes = "febrero";
                                    break;
                                case 3:
                                    mes = "marzo";
                                    break;
                                case 4:
                                    mes = "abril";
                                    break;
                                case 5:
                                    mes = "mayo";
                                    break;
                                case 6:
                                    mes = "junio";
                                    break;
                                case 7:
                                    mes = "julio";
                                    break;
                                case 8:
                                    mes = "agosto";
                                    break;
                                case 9:
                                    mes = "septiembre";
                                    break;
                                case 10:
                                    mes = "octubre";
                                    break;
                                case 11:
                                    mes = "noviembre";
                                    break;
                                case 12:
                                    mes = "didicmebre";
                                    break;

                            }
                            if (text != null && text.contains("<MES>")) {
                                text = text.replace("<MES>", "" + mes + "");
                                r.setText(text, 0);
                            }
                            if (text != null && text.contains("<AIO>")) {
                                text = text.replace("<AIO>", "" + FACT.substring(0, 4) + "");
                                r.setText(text, 0);
                            }
                        }

                    }
                }
            }
            FN.ActEstFN(cnx, logger, ambiente, FN.ConsultMIXFN(cnx, logger, ambiente, AIO), SER, AIO, user);
            logger.info("---El usuario " + user + "  ha descargado exitosamente el documento de impresion de certificado en custodia con serie " + SER + " del año " + AIO + "  ---", this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());

//            } catch (Exception e) {
//                e.printStackTrace();
//                logger.error("---El documento de impresion no se pudo descargar ---", this.getClass().getSimpleName(), new Object() {
//                }.getClass().getEnclosingMethod().getName());
//                logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
//                }.getClass().getEnclosingMethod().getName());
//            }
        } else {
            doc = null;
        }
        return doc;
    }
}
