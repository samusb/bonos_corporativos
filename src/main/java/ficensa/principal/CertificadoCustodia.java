/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import modelo.SeriesM;
import servicio.Conexion;
import servicio.Logger;
import servicio.Parametros;
import static servicio.Parametros.listaDeCC;
import static servicio.Parametros.listaDeSeries;
import static servicio.Parametros.logger;

/**
 *
 * @author aprivera
 */
public class CertificadoCustodia {

    ResultSet rs;
    Statement st = null;
    PreparedStatement ps;
    Logger logger;

    public BigDecimal ConsultMAXCC(Conexion cnx, Logger logger, String ambiente, String SER, String AIO) { //para consultar la ultima forma numerada

        SeriesM consult = new SeriesM();
        try {
            rs = cnx.getStmt().executeQuery("SELECT\n"
                    + "	MAX(t1.CTCCCD) AS CCDMAX\n"
                    + "FROM\n"
                    + "	" + ambiente + "USRLIB.BCPCTC AS t1\n"
                    + "WHERE\n"
                    + "	t1.CTCCOD = '" + SER + "'\n"
                    + "	AND t1.CTCAIO =" + AIO + "");

            while (rs.next()) {
                consult.setCTCCCD(rs.getBigDecimal("CCDMAX") != null ? rs.getBigDecimal("CCDMAX") : new BigDecimal(0));
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
        }
        return consult.getCTCCCD();
    }

    public boolean AgregarCC(Conexion cnx, Logger logger, String ambiente, String SRSTBVt, String CTCNMBt, String CTCCOD, String CTCAIOt, String CTCIDN, String CTCNOM, String CTCUSR) throws SQLException {

        boolean respuesta = true;
        BigDecimal temp = new BigDecimal(1);
//        BigDecimal SRSCCCt = new BigDecimal(SRSCCC);
        BigDecimal SRSTBV = new BigDecimal(SRSTBVt);
        String CTCEXC = "0";//N/A
        String CTCFPA = "N/A";
        String CTCFPI = "N/A";
        BigDecimal CTCAIO = new BigDecimal(CTCAIOt);
        BigDecimal CTCACC = new BigDecimal(0);//N/A
        BigDecimal CTCIMP = new BigDecimal(0);//N/A
        BigDecimal CTCNMB = new BigDecimal(CTCNMBt);//numero bono 
        BigDecimal CTCBNI = SRSTBV.add(temp);//bono inicial
        BigDecimal CTCBNF = new BigDecimal(CTCBNI.doubleValue() + CTCNMB.doubleValue() - 1); //bono final
        BigDecimal SRSTBVACT = new BigDecimal(SRSTBV.doubleValue() + CTCNMB.doubleValue()); //SRSTBV
        String CTCFVT = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String CTCFIN = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-hh.mm.ss.SSS"));
        String CTCFRG = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-hh.mm.ss.SSS"));

        BigDecimal CTCCCD = new BigDecimal(ConsultMAXCC(cnx, logger, ambiente, CTCCOD, CTCAIOt).doubleValue() + 1);//Correlativo de CC

        logger.info("----" + CTCUSR + " Vendiendo certificado en custodia----", this.getClass().getSimpleName(), new Object() {
        }.getClass().getEnclosingMethod().getName());

        String query = "INSERT INTO " + ambiente + "USRLIB.BCPCTC (CTCCOD, "
                + "CTCCCD,"
                + "CTCAIO, "
                + "CTCNMB, "
                + "CTCIDN, "
                + "CTCNOM, "
                + "CTCACC, "
                + "CTCFVT, "
                + "CTCFIN, "
                + "CTCFRG, "
                + "CTCFPA, "
                + "CTCFPI, "
                + "CTCEXC, "
                + "CTCIMP, "
                + "CTCBNI, "
                + "CTCBNF, "
                + "CTCUSR)\n"
                + "VALUES('" + CTCCOD + "',"
                + "" + CTCCCD + ", "
                + "" + CTCAIO + ","
                + "" + CTCNMB + ","
                + "'" + CTCIDN + "', "
                + "'" + CTCNOM + "', "
                + "" + CTCACC + ","
                + "'" + CTCFVT + "', "
                + "'" + CTCFIN + "', "
                + "'" + CTCFRG + "', "
                + "'" + CTCFPA + "', "
                + "'" + CTCFPI + "', "
                + "'" + CTCEXC + "', "
                + "" + CTCIMP + ", "
                + "" + CTCBNI + ","
                + "" + CTCBNF + ", "
                + "'" + CTCUSR + "')";

        System.out.println(query);

        ps = cnx.getConn().prepareStatement(query);
        ps.executeUpdate();

        CertificadoCustodia CC = new CertificadoCustodia();
        CC.ActVendidosSRS(cnx, logger, ambiente, CTCCOD, CTCAIO, SRSTBVACT, CTCCCD); //FLAG
        logger.info("----se ha vendido exitosamente el certificado en  custodia----", this.getClass().getSimpleName(), new Object() {
        }.getClass().getEnclosingMethod().getName());
//        } catch (Exception e) {
//            e.printStackTrace();
//            respuesta = false;
//            logger.error("--No se a podido vender el certificado en custodia--", this.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName());
//            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName());
//
//        }

        return respuesta;

    }

    public List<SeriesM> DatosCC(Conexion cnx, String ambiente, Logger logger, String Accion, String[] indicesSerie, List<SeriesM> listaSerie) throws SQLException {

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

        logger.info(Accion, this.getClass().getSimpleName(), new Object() {
        }.getClass().getEnclosingMethod().getName());
        rs = cnx.getStmt().executeQuery("SELECT\n"
                + "	t1.CTCCOD AS COD,\n"
                + "	t1.CTCCCD AS CCD,\n"
                + "	t1.CTCAIO AS AIO,\n"
                + "	t1.CTCNMB AS NMB,\n"
                + "	t1.CTCIDN AS IDN,\n"
                + "	t1.CTCNOM AS NOM,\n"
                + "	t1.CTCACC AS ACC,\n"
                + "	t1.CTCFVT AS FVT,\n"
                + "	t1.CTCFIN AS FIN,\n"
                + "	t1.CTCFRG AS FRG,\n"
                + "	t1.CTCFPA AS FPA,\n"
                + "	t1.CTCFPI AS FPI,\n"
                + "	t1.CTCEXC AS EXC,\n"
                + "	t1.CTCIMP AS IMP,\n"
                + "	t1.CTCBNI AS BNI,\n"
                + "	t1.CTCBNF AS BNF,\n"
                + "	t1.CTCUSR AS USR\n"
                + "FROM " + ambiente + "USRLIB.BCPCTC t1 WHERE T1.CTCAIO=" + AIO.trim() + " AND T1.CTCCOD=" + SER.trim() + "");

        while (rs.next()) {
            SeriesM consult = new SeriesM();

            consult.setCTCCOD(rs.getString("COD") != null ? rs.getString("COD") : "");
            consult.setCTCCCD(rs.getBigDecimal("CCD"));
            consult.setCTCAIO(rs.getBigDecimal("AIO"));
            consult.setCTCNMB(rs.getBigDecimal("NMB"));
            consult.setCTCIDN(rs.getString("IDN") != null ? rs.getString("IDN") : "");
            consult.setCTCNOM(rs.getString("NOM") != null ? rs.getString("NOM") : "");
            consult.setCTCACC(rs.getBigDecimal("ACC"));
            consult.setCTCFVT(rs.getString("FVT") != null ? rs.getString("FVT") : "");
            consult.setCTCFIN(rs.getString("FIN") != null ? rs.getString("FIN") : "");
            consult.setCTCFRG(rs.getString("FRG") != null ? rs.getString("FRG") : "");
            consult.setCTCFPA(rs.getString("FPA") != null ? rs.getString("FPA") : "");
            consult.setCTCFPI(rs.getString("FPI") != null ? rs.getString("FPI") : "");
            consult.setCTCIMP(rs.getBigDecimal("IMP"));
            consult.setCTCBNI(rs.getBigDecimal("BNI"));
            consult.setCTCBNF(rs.getBigDecimal("BNF"));
            consult.setCTCUSR(rs.getString("USR") != null ? rs.getString("USR") : "");

            listaDeCC.add(consult);
        }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName());
//
//        }
        return listaDeCC;
    }

    public List<SeriesM> DatosCCESPECIFIC(Conexion cnx, Logger logger, String ambiente, String SER, String AIO) throws SQLException {

       
        rs = cnx.getStmt().executeQuery("SELECT\n"
                + "	t1.CTCCOD AS COD,\n"
                + "	t1.CTCCCD AS CCD,\n"
                + "	t1.CTCAIO AS AIO,\n"
                + "	t1.CTCNMB AS NMB,\n"
                + "	t1.CTCIDN AS IDN,\n"
                + "	t1.CTCNOM AS NOM,\n"
                + "	t1.CTCACC AS ACC,\n"
                + "	t1.CTCFVT AS FVT,\n"
                + "	t1.CTCFIN AS FIN,\n"
                + "	t1.CTCFRG AS FRG,\n"
                + "	t1.CTCFPA AS FPA,\n"
                + "	t1.CTCFPI AS FPI,\n"
                + "	t1.CTCEXC AS EXC,\n"
                + "	t1.CTCIMP AS IMP,\n"
                + "	t1.CTCBNI AS BNI,\n"
                + "	t1.CTCBNF AS BNF,\n"
                + "	t1.CTCUSR AS USR\n"
                + "FROM " + ambiente + "USRLIB.BCPCTC t1 WHERE T1.CTCAIO=" + AIO.trim() + " AND T1.CTCCOD='" + SER.trim() + "'");

        while (rs.next()) {
            SeriesM consult = new SeriesM();

            consult.setCTCCOD(rs.getString("COD") != null ? rs.getString("COD") : "");
            consult.setCTCCCD(rs.getBigDecimal("CCD"));
            consult.setCTCAIO(rs.getBigDecimal("AIO"));
            consult.setCTCNMB(rs.getBigDecimal("NMB"));
            consult.setCTCIDN(rs.getString("IDN") != null ? rs.getString("IDN") : "");
            consult.setCTCNOM(rs.getString("NOM") != null ? rs.getString("NOM") : "");
            consult.setCTCACC(rs.getBigDecimal("ACC"));
            consult.setCTCFVT(rs.getString("FVT") != null ? rs.getString("FVT") : "");
            consult.setCTCFIN(rs.getString("FIN") != null ? rs.getString("FIN") : "");
            consult.setCTCFRG(rs.getString("FRG") != null ? rs.getString("FRG") : "");
            consult.setCTCFPA(rs.getString("FPA") != null ? rs.getString("FPA") : "");
            consult.setCTCFPI(rs.getString("FPI") != null ? rs.getString("FPI") : "");
            consult.setCTCIMP(rs.getBigDecimal("IMP"));
            consult.setCTCBNI(rs.getBigDecimal("BNI"));
            consult.setCTCBNF(rs.getBigDecimal("BNF"));
            consult.setCTCUSR(rs.getString("USR") != null ? rs.getString("USR") : "");

            listaDeCC.add(consult);
        }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName());
//
//        }
        return listaDeCC;
    }

    public List<SeriesM> DatosSerieESPECIFIC2(Conexion cnx, Logger logger, String ambiente, String SER, String AIO) throws SQLException {//Para obtener datos de una serie en especifico

        rs = cnx.getStmt().executeQuery("SELECT\n"
                + "	T1.SRSAIO AS AIO,\n"
                + "	T1.SRSCOD AS COD,\n"
                + "	T1.SRSPLD AS PLD,\n"
                + "	T1.SRSICD AS ICD,\n"
                + "	T1.SRSMON AS MON,\n"
                + "	T1.SRSMNE AS MNE,\n"
                + "	T1.SRSTMX AS TMX,\n"
                + "	T1.SRSTMN AS TMN,\n"
                + "	T1.SRSTLIB AS TLIB,\n"
                + "	T1.SRSDIF AS DIF,\n"
                + "	T1.SRSFNM AS FNM,\n"
                + "	T1.SRSFDE AS FDE,\n"
                + "	T1.SRSFDV AS FDV,\n"
                + "	T1.SRSCCC AS CCC,\n"
                + "	T1.SRSBMP AS BMP,\n"
                + "	T1.SRSTBC AS TBC,\n"
                + "	T1.SRSTVR AS TVR,\n"
                + "	T1.SRSTBV AS TBV,\n"
                + "	T1.SRSMBN"
                + " AS MBN\n"
                + "FROM\n"
                + "	" + ambiente + "USRLIB.BCPSRS AS T1 WHERE T1.SRSAIO=" + AIO.trim() + " AND T1.SRSCOD='" + SER.trim() + "'");

        while (rs.next()) {
            SeriesM consult = new SeriesM();

            consult.setSRSCOD(rs.getString("COD") != null ? rs.getString("COD") : "");
            consult.setSRSICD(rs.getString("ICD") != null ? rs.getString("ICD") : "");
            consult.setSRSFDE(rs.getString("FDE").trim() != null ? rs.getString("FDE").trim() : "");
            consult.setSRSFDV(rs.getString("FDV").trim() != null ? rs.getString("FDV").trim() : "");
            consult.setSRSAIO(rs.getBigDecimal("AIO"));
            consult.setSRSPLD(rs.getBigDecimal("PLD"));
            consult.setSRSMON(rs.getBigDecimal("MON"));
            consult.setSRSTMX(rs.getBigDecimal("TMX"));
            consult.setSRSTMN(rs.getBigDecimal("TMN"));
            consult.setSRSTLIB(rs.getBigDecimal("TLIB"));
            consult.setSRSDIF(rs.getBigDecimal("DIF"));
            consult.setSRSCOD(rs.getString("COD") != null ? rs.getString("COD") : "");
            consult.setSRSCCC(rs.getBigDecimal("CCC"));
            consult.setSRSBMP(rs.getBigDecimal("BMP"));
            consult.setSRSTBC(rs.getBigDecimal("TBC"));
            consult.setSRSTBV(rs.getBigDecimal("TBV"));
            consult.setSRSTVR(rs.getString("TVR") != null ? rs.getString("TVR") : "");
            consult.setSRSMBN(rs.getBigDecimal("MBN"));
            BigDecimal MNEtemp = rs.getBigDecimal("MNE");
            BigDecimal tot = consult.getSRSTBC().subtract(consult.getSRSTBV());

            consult.setBONVIG(tot);

            if (MNEtemp.toString().trim().equals("0")) {
                consult.setSRSMNE("LPS");
            } else {
                consult.setSRSMNE("US$");
            }

            listaDeSeries.add(consult);
        }

//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName());
//        }
        return listaDeSeries;
    }

    public List<SeriesM> DatosMODCCESP(Conexion cnx, Logger logger, String ambiente, String Accion, String[] indicesSerie, List<SeriesM> listaSerie) throws SQLException {

        String SER = "";
        String AIO = "";
        String CCSER = "";

        Integer contador = 0;
        for (String indice : indicesSerie) {
            if (indicesSerie.length > 1) {

                SER += (contador == 0 ? "'" : ", '") + listaSerie.get(Integer.valueOf(indice)).getCTCCOD() + "'";
                AIO += (contador == 0 ? "'" : ", '") + listaSerie.get(Integer.valueOf(indice)).getCTCAIO() + "'";
                CCSER += (contador == 0 ? "'" : ", '") + listaSerie.get(Integer.valueOf(indice)).getCTCCCD() + "'";
            } else {

                SER = "'" + listaSerie.get(Integer.valueOf(indice)).getCTCCOD() + "'";
                AIO = listaSerie.get(Integer.valueOf(indice)).getCTCAIO().toString();
                CCSER = listaSerie.get(Integer.valueOf(indice)).getCTCCCD().toString();
            }
            contador++;

        }

        logger.info(Accion, this.getClass().getSimpleName(), new Object() {
        }.getClass().getEnclosingMethod().getName());
        rs = cnx.getStmt().executeQuery("SELECT\n"
                + "	t1.CTCCOD AS COD,\n"
                + "	t1.CTCCCD AS CCD,\n"
                + "	t1.CTCAIO AS AIO,\n"
                + "	t1.CTCNMB AS NMB,\n"
                + "	t1.CTCIDN AS IDN,\n"
                + "	t1.CTCNOM AS NOM,\n"
                + "	t1.CTCACC AS ACC,\n"
                + "	t1.CTCFVT AS FVT,\n"
                + "	t1.CTCFIN AS FIN,\n"
                + "	t1.CTCFRG AS FRG,\n"
                + "	t1.CTCFPA AS FPA,\n"
                + "	t1.CTCFPI AS FPI,\n"
                + "	t1.CTCEXC AS EXC,\n"
                + "	t1.CTCIMP AS IMP,\n"
                + "	t1.CTCBNI AS BNI,\n"
                + "	t1.CTCBNF AS BNF,\n"
                + "	t1.CTCUSR AS USR\n"
                + "FROM " + ambiente + "USRLIB.BCPCTC T1 WHERE T1.CTCAIO=" + AIO.trim() + " AND T1.CTCCOD=" + SER.trim() + " AND T1.CTCCCD=" + CCSER);

        while (rs.next()) {
            SeriesM consult = new SeriesM();

            consult.setCTCCOD(rs.getString("COD") != null ? rs.getString("COD") : "");
            consult.setCTCCCD(rs.getBigDecimal("CCD"));
            consult.setCTCAIO(rs.getBigDecimal("AIO"));
            consult.setCTCNMB(rs.getBigDecimal("NMB"));
            consult.setCTCIDN(rs.getString("IDN") != null ? rs.getString("IDN") : "");
            consult.setCTCNOM(rs.getString("NOM") != null ? rs.getString("NOM") : "");
            consult.setCTCACC(rs.getBigDecimal("ACC"));
            consult.setCTCFVT(rs.getString("FVT") != null ? rs.getString("FVT") : "");
            consult.setCTCFIN(rs.getString("FIN") != null ? rs.getString("FIN") : "");
            consult.setCTCFRG(rs.getString("FRG") != null ? rs.getString("FRG") : "");
            consult.setCTCFPA(rs.getString("FPA") != null ? rs.getString("FPA") : "");
            consult.setCTCFPI(rs.getString("FPI") != null ? rs.getString("FPI") : "");
            consult.setCTCIMP(rs.getBigDecimal("IMP"));
            consult.setCTCBNI(rs.getBigDecimal("BNI"));
            consult.setCTCBNF(rs.getBigDecimal("BNF"));
            consult.setCTCUSR(rs.getString("USR") != null ? rs.getString("USR") : "");

            listaDeCC.add(consult);
        }

//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName());
//        }
        return listaDeCC;
    }

    public List<SeriesM> ImpDatos(Conexion cnx, Logger logger, String ambiente, String Accion, String[] indicesSerie, List<SeriesM> listaSerie) {

        String SER = "";
        String AIO = "";
        String CCSER = "";

        Integer contador = 0;
        for (String indice : indicesSerie) {
            if (indicesSerie.length > 1) {

                SER += (contador == 0 ? "'" : ", '") + listaSerie.get(Integer.valueOf(indice)).getCTCCOD() + "'";
                AIO += (contador == 0 ? "'" : ", '") + listaSerie.get(Integer.valueOf(indice)).getCTCAIO() + "'";
                CCSER += (contador == 0 ? "'" : ", '") + listaSerie.get(Integer.valueOf(indice)).getCTCCCD() + "'";
            } else {

                SER = "'" + listaSerie.get(Integer.valueOf(indice)).getCTCCOD() + "'";
                AIO = listaSerie.get(Integer.valueOf(indice)).getCTCAIO().toString();
                CCSER = listaSerie.get(Integer.valueOf(indice)).getCTCCCD().toString();
            }
            contador++;

        }
        try {

            logger.info(Accion, this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
            rs = cnx.getStmt().executeQuery("SELECT\n"
                    + "	t1.CTCCOD AS COD,\n"
                    + "	t1.CTCCCD AS CCD,\n"
                    + "	t1.CTCAIO AS AIO,\n"
                    + "	t1.CTCNMB AS NMB,\n"
                    + "	t1.CTCIDN AS IDN,\n"
                    + "	t1.CTCNOM AS NOM,\n"
                    + "	t1.CTCACC AS ACC,\n"
                    + "	t1.CTCFVT AS FVT,\n"
                    + "	t1.CTCFIN AS FIN,\n"
                    + "	t1.CTCFRG AS FRG,\n"
                    + "	t1.CTCFPA AS FPA,\n"
                    + "	t1.CTCFPI AS FPI,\n"
                    + "	t1.CTCEXC AS EXC,\n"
                    + "	t1.CTCIMP AS IMP,\n"
                    + "	t1.CTCBNI AS BNI,\n"
                    + "	t1.CTCBNF AS BNF,\n"
                    + "	t1.CTCUSR AS USR,\n"
                    + "	t2.SRSMNE AS MNE,\n"
                    + "	t2.SRSMBN AS MBN\n"
                    + "FROM\n"
                    + "	" + ambiente + "USRLIB.BCPCTC AS t1\n"
                    + "LEFT JOIN FITUSRLIB.BCPSRS AS t2 ON\n"
                    + "	( t2.SRSAIO = t1.CTCAIO\n"
                    + "	AND t2.SRSCOD = t1.CTCCOD )\n"
                    + "WHERE\n"
                    + "	T1.CTCAIO = " + AIO.trim() + "\n"
                    + "	AND T1.CTCCOD = " + SER.trim() + "\n"
                    + "	AND T1.CTCCCD =" + CCSER.trim());

            while (rs.next()) {
                SeriesM consult = new SeriesM();

                consult.setCTCCOD(rs.getString("COD") != null ? rs.getString("COD") : "");
                consult.setCTCCCD(rs.getBigDecimal("CCD"));
                consult.setCTCAIO(rs.getBigDecimal("AIO"));
                consult.setCTCNMB(rs.getBigDecimal("NMB"));
                consult.setCTCIDN(rs.getString("IDN") != null ? rs.getString("IDN") : "");
                consult.setCTCNOM(rs.getString("NOM") != null ? rs.getString("NOM") : "");
                consult.setCTCACC(rs.getBigDecimal("ACC"));
                consult.setCTCFVT(rs.getString("FVT") != null ? rs.getString("FVT") : "");
                consult.setCTCFIN(rs.getString("FIN") != null ? rs.getString("FIN") : "");
                consult.setCTCFRG(rs.getString("FRG") != null ? rs.getString("FRG") : "");
                consult.setCTCFPA(rs.getString("FPA") != null ? rs.getString("FPA") : "");
                consult.setCTCFPI(rs.getString("FPI") != null ? rs.getString("FPI") : "");
                consult.setCTCIMP(rs.getBigDecimal("IMP"));
                consult.setCTCBNI(rs.getBigDecimal("BNI"));
                consult.setCTCBNF(rs.getBigDecimal("BNF"));
                consult.setCTCUSR(rs.getString("USR") != null ? rs.getString("USR") : "");
                BigDecimal MNEtemp = (rs.getBigDecimal("MNE"));
                consult.setSRSMBN(rs.getBigDecimal("MBN"));

                if (MNEtemp.toString().trim().equals("0")) {
                    consult.setSRSMNE("LPS");
                } else {
                    consult.setSRSMNE("US$");
                }

                listaDeCC.add(consult);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
        }
        return listaDeCC;
    }

    @Deprecated
    public boolean ModificarCC(Conexion cnx, Logger logger, String ambiente, String CTCCOD, String CTCCCD, String CTCAIO, String CTCACC, String CTCFPA, String CTCFPI, String CTCEXC, String CTCIMP, String CTCUSR) throws SQLException {

        boolean respuesta = true;

        String CTCFIN = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-hh.mm.ss.SSS"));

        try {
            logger.info("----Modificado el CC ----", this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
            String query = "UPDATE \n"
                    + "" + ambiente + "USRLIB.BCPCTC SET  \n"
                    + "CTCACC=" + CTCACC + ", \n"
                    + "CTCFIN='" + CTCFIN + "',  \n"
                    + "CTCFPA='" + CTCFPA + "', \n"
                    + "CTCFPI='" + CTCFPI + "', \n"
                    + "CTCEXC='" + CTCEXC + "', \n"
                    + "CTCIMP=" + CTCIMP + ", \n"
                    + "CTCUSR='" + CTCUSR + "'\n"
                    + "WHERE CTCCOD='" + CTCCOD + "'AND CTCAIO=" + CTCAIO + " AND CTCCCD=" + CTCCCD + "";
            System.out.println(query);

            ps = cnx.getConn().prepareStatement(query);
            ps.executeUpdate();

            logger.info("----Se ha modificado el CC existosamente----", this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
        } catch (Exception e) {
            respuesta = false;
            e.printStackTrace();
            logger.error("----No se ha podido modificar el tenedor de la CC ----", this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());

        }
        return respuesta;

    }

    public boolean ModificarTenedor(Conexion cnx, Logger logger, String ambiente, String CTCCOD, String CTCCCD, String CTCAIO, String CTCNOM, String CTCIDN, String CTCUSR) throws SQLException {
        boolean respuesta = true;
        String CTCFIN = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-hh.mm.ss.SSS"));

        String query = "UPDATE \n"
                + "FITUSRLIB.BCPCTC SET  \n"
                + "CTCIDN='" + CTCIDN + "', \n"
                + "CTCNOM='" + CTCNOM + "', \n"
                + "CTCFIN='" + CTCFIN + "',  \n"
                + "CTCUSR='" + CTCUSR + "'\n"
                + "WHERE CTCCOD='" + CTCCOD + "' AND CTCAIO=" + CTCAIO + " AND CTCCCD=" + CTCCCD + "";
        System.out.println(query);

        ps = cnx.getConn().prepareStatement(query);
        ps.executeUpdate();

        logger.info("----" + CTCUSR + " ha modificado el tenedor de la CC existosamente----", this.getClass().getSimpleName(), new Object() {
        }.getClass().getEnclosingMethod().getName());
//        } catch (Exception e) {
//            respuesta = false;
//            e.printStackTrace();
//            logger.error("----No se ha podido modificar el tenedor de la CC ----", this.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName());
//            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName());
//
//        }
        return respuesta;

    }

    public void ActVendidosSRS(Conexion cnx, Logger logger, String ambiente, String SER, BigDecimal AIO, BigDecimal SRSTVB, BigDecimal SRSCCC) throws SQLException {

        try {
            String query = "UPDATE\n"
                    + "	FITUSRLIB.BCPSRS t4\n"
                    + "SET\n"
                    + "	t4.SRSTBV=" + SRSTVB + ",\n"
                    + "	t4.SRSCCC=" + SRSCCC + "\n"
                    + "WHERE\n"
                    + "	t4.SRSAIO =" + AIO + "\n"
                    + "	AND t4.SRSCOD='" + SER + "'";
            System.out.println(query);

            ps = cnx.getConn().prepareStatement(query);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
        }
    }

//    @Deprecated
//    public boolean deleteCC(String[] indicesSerie, List<SeriesM> listaSerie) {
//
//        Parametros param = new Parametros();
////         String SER ="Y";
////         String AIO ="2020";
//        String SER = "";
//        String AIO = "";
//        String CCSER = "";
//
//        Integer contador = 0;
//        for (String indice : indicesSerie) {
//            if (indicesSerie.length > 1) {
//
//                SER += (contador == 0 ? "'" : ", '") + listaSerie.get(Integer.valueOf(indice)).getCTCCOD() + "'";
//                AIO += (contador == 0 ? "'" : ", '") + listaSerie.get(Integer.valueOf(indice)).getCTCAIO() + "'";
//                CCSER += (contador == 0 ? "'" : ", '") + listaSerie.get(Integer.valueOf(indice)).getCTCCCD() + "'";
//            } else {
//
//                SER = "'" + listaSerie.get(Integer.valueOf(indice)).getCTCCOD() + "'";
//                AIO = listaSerie.get(Integer.valueOf(indice)).getCTCAIO().toString();
//                CCSER = listaSerie.get(Integer.valueOf(indice)).getCTCCCD().toString();
//            }
//            contador++;
//
//        }
//
//        boolean respuesta = true;
//        try {
//            param.cnx.Conectar();
//            logger = param.cnx.getLogger();
//            logger.info("-----------Eliminando CC---------", this.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName());
//            ps = param.cnx.getConn().prepareStatement("DELETE FITUSRLIB.BCPCTC T1 WHERE T1.CTCAIO=" + AIO.trim() + " AND T1.CTCCOD=" + SER.trim() + " AND T1.CTCCCD=" + CCSER);
//            ps.executeUpdate();
//            logger.info("-----------Se termino de eliminar la serie---------", this.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName());
//        } catch (Exception e) {
//            respuesta = false;
//            e.printStackTrace();
//            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName());
//
//        } finally {
//            try {
//                param.cnx.Desconectar();
//            } catch (Exception e) {
//                respuesta = false;
//                logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
//                }.getClass().getEnclosingMethod().getName());
//
//                e.printStackTrace();
//            }
//            return respuesta;
//        }
//
//    }
}
