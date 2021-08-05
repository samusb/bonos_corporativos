/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import modelo.SeriesM;
import servicio.Conexion;
import servicio.Logger;
import servicio.Parametros;
import static servicio.Parametros.listaDeSeries;
import static servicio.Parametros.logger;

/**
 *
 * @author aprivera
 */
public class SeriesEmitidas {

    ResultSet rs;
    Statement st = null;
    PreparedStatement ps;
    Logger logger;
    
 public List<SeriesM> SerExist(Conexion cnx, Logger logger,String ambiente,String SER, String AIO) {//para verificar que existan formas numeradas
        List<SeriesM> ListSerE = new ArrayList<>();

        try {

            rs = cnx.getStmt().executeQuery("SELECT "
                    + "t1.SRSCOD AS SER, "
                    + "t1.SRSAIO AS AIO "
                    + "FROM FITUSRLIB.BCPSRS AS t1  "
                    + "WHERE t1.SRSCOD = '"+SER+"' and t1.SRSAIO ="+AIO+"");
            while (rs.next()) {

                SeriesM consult = new SeriesM();
                consult.setSRSAIO(rs.getBigDecimal("AIO"));
                consult.setSRSCOD(rs.getString("SER"));
                ListSerE.add(consult);

            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
        }
        return ListSerE;
    }
    public boolean AgregarSI(Conexion cnx, Logger logger, String ambiente, String SRSICD, String SRSCOD, String SRSFDE, String SRSTVR, String SRSAIOt, String SRSPLDt, String SRSMNEt, String SRSMONt, String SRSMBNt, String SRSBMPt, String SRSTMXt, String SRSTMNt, String SRSTLIBt, LocalDate FETemp, String user) throws SQLException {
        boolean respuesta = true;
       
            logger.info("----"+user+" Agregando Series----", this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());

            String SRSUSR = user;//pendiente   
            BigDecimal SRSAIO = new BigDecimal(SRSAIOt);//año
            BigDecimal SRSPLD = new BigDecimal(SRSPLDt);//plazo en dias
            long tempAnios = SRSPLD.longValue() - (SRSPLD.longValue() / 360) * 360;//mod en genexus, 
            long tempDias = SRSPLD.longValue() - (tempAnios * 360);
            LocalDate FETemp2 = FETemp.minus(tempAnios, ChronoUnit.YEARS);
            LocalDate FETemp3 = FETemp2.minus(tempDias, ChronoUnit.DAYS);
            String SRSFDV = FETemp3.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));//fecha de vencimiento
            String SRSFHI = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-hh.mm.ss.SSS"));
            String SRSFRG = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-hh.mm.ss.SSS"));
            BigDecimal SRSMBN = new BigDecimal(SRSMBNt.trim());//monto por cada macrotitulo
            BigDecimal SRSBMP = new BigDecimal(SRSBMPt.trim()); //bonos minimos por persona
            BigDecimal SRSMON = new BigDecimal(SRSMONt.trim()); //monto total del macrotitulo
            BigDecimal SRSMNE = new BigDecimal(SRSMNEt.trim()); //moneda
            BigDecimal SRSTLIB = new BigDecimal(SRSTLIBt.trim()); //tasa libor
            BigDecimal SRSTMX = new BigDecimal(SRSTMXt.trim()); //tasa maxima
            BigDecimal SRSTMN = new BigDecimal(SRSTMNt.trim());//tasa minima
            BigDecimal SRSDIF = new BigDecimal(SRSTMX.doubleValue()-SRSTLIB.doubleValue());//tasa diferencial
            BigDecimal SRSTBC = new BigDecimal(SRSMON.doubleValue() / SRSMBN.doubleValue()); //TOTAL DE BONOS CORPORATIVOS para cert en custodia
            BigDecimal SRSFNM = new BigDecimal(0); //forma numerada
            BigDecimal SRSTBV = new BigDecimal(0);//total bonos vendidos para cert en custodia //FLAG PARA MODIFICAR Y ELIMINAR SERIES.
            BigDecimal SRSCCC = new BigDecimal(0);//consecutivo cert en custodia 

            BigDecimal SRSTNL = SRSTLIB.add(SRSDIF);//Suma de tasalib y dif

            String query = "INSERT\n"
                    + "	INTO\n"
                    + "	" + ambiente + "USRLIB.BCPSRS (SRSCOD,\n"
                    + "	SRSAIO,\n"
                    + "	SRSPLD,\n"
                    + "	SRSMBN,\n"
                    + "	SRSMNE,\n"
                    + "	SRSTNL,\n"
                    + "	SRSTLIB,\n"
                    + "	SRSDIF,\n"
                    + "	SRSTMX,\n"
                    + "	SRSTMN,\n"
                    + "	SRSFDE,\n"
                    + "	SRSFDV,\n"
                    + "	SRSFNM,\n"
                    + "	SRSBMP,\n"
                    + "	SRSTBV,\n"
                    + "	SRSTBC,\n"
                    + "	SRSUSR,\n"
                    + "	SRSFHI,\n"
                    + "	SRSFRG,\n"
                    + "	SRSCCC,\n"
                    + "	SRSMON,\n"
                    + "	SRSTVR,\n"
                    + "	SRSICD)\n"
                    + "VALUES('" + SRSCOD + "',\n"
                    + "" + SRSAIO + ",\n"
                    + "" + SRSPLD + ",\n"
                    + "" + SRSMBN + ",\n"
                    + "" + SRSMNE + ",\n"
                    + "" + SRSTNL + ",\n"
                    + "" + SRSTLIB + ",\n"
                    + "" + SRSDIF + ",\n"
                    + "" + SRSTMX + ",\n"
                    + "" + SRSTMN + ",\n"
                    + "'" + SRSFDE + "',\n"
                    + "'" + SRSFDV + "',\n"
                    + "" + SRSFNM + ",\n"
                    + "" + SRSBMP + ",\n"
                    + "" + SRSTBV + ",\n"
                    + "" + SRSTBC + ",\n"
                    + "'" + SRSUSR + "',\n"
                    + "'" + SRSFHI + "',\n"
                    + "'" + SRSFRG + "',\n"
                    + "" + SRSCCC + ",\n"
                    + "" + SRSMON + ",\n"
                    + "'" + SRSTVR + "',\n"
                    + "'" + SRSICD + "')";

            System.out.println(query);

            ps = cnx.getConn().prepareStatement(query);
            ps.executeUpdate();

            logger.info("----El usuario: " + SRSUSR + " ha agregado la serie " + SRSCOD + " con año " + SRSAIO + " existosamente----", this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
//        } catch (Exception e) {
//            respuesta = false;
//            e.printStackTrace();
//            logger.error("----No se ha podido agregar la serie emitida---", this.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName());
//            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName());
//
//        }
        return respuesta;
    }

    public List<SeriesM> DatosSerie(Conexion cnx, String ambiente, Logger logger, String Accion) throws SQLException {

       

            logger.info(Accion, this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
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
                    + "	T1.SRSTNL AS TNL,\n"
                    + "	T1.SRSTVR AS TVR,\n"
                    + "	T1.SRSTBV AS TBV,\n"
                    + "	T1.SRSTBC AS MBN\n"
                    + "FROM\n"
                    + "	" + ambiente + "USRLIB.BCPSRS AS T1");
//SRSTNL
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
                consult.setSRSTNL(rs.getBigDecimal("TNL"));
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
//            logger.error("---No se pudieron consultar las series emitidas--", this.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName());
//            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName());
//
//        }
        return listaDeSeries;
    }

    public List<SeriesM> DatosSerieESPECIFIC(Conexion cnx, String ambiente, Logger logger, String Accion, String[] indicesSerie, List<SeriesM> listaSerie) throws SQLException {//Para obtener datos de una serie en especifico
        SeriesM consult = new SeriesM();
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
                    + "	T1.SRSTNL AS TNL,\n"
                    + "	T1.SRSTBV AS TBV,\n"
                    + "	T1.SRSMBN AS MBN\n"
                    + "FROM\n"
                    + "	" + ambiente + "USRLIB.BCPSRS AS T1 WHERE T1.SRSAIO=" + AIO + " AND T1.SRSCOD=" + SER + "");

            while (rs.next()) {

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
                consult.setSRSTNL(rs.getBigDecimal("TNL"));
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
//            logger.error("---No se pudieron consultar las series emitidas--", this.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName());
//            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName());
//
//        }

        return listaDeSeries;
    }

    public List<SeriesM> DatosSerie3(Conexion cnx, Logger logger, String ambiente, String SER, String AIO) {

        List<SeriesM> ListDATS = new ArrayList<>();

        BigDecimal AIOt = new BigDecimal(AIO);
        try {

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
                    + "	T1.SRSTNL AS TNL,\n"
                    + "	T1.SRSTBV AS TBV,\n"
                    + "	T1.SRSTBC AS MBN\n"
                    + "FROM\n"
                    + "	" + ambiente + "USRLIB.BCPSRS AS T1 WHERE T1.SRSCOD= " + SER + " AND T1.SRSAIO =" + AIOt);

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
                consult.setSRSTNL(rs.getBigDecimal("TNL"));
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

                ListDATS.add(consult);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
        }
        return ListDATS;
    }

    public boolean ModificarSE(Conexion cnx, Logger logger, String ambiente, String SER, String AIOt, String SRSICD, String SRSCOD, String SRSFDE, String SRSTVR, String SRSAIOt, String SRSMNEt, String SRSMONt, String SRSMBNt, String SRSBMPt, String SRSFDV, String user) throws SQLException {

        boolean respuesta = true;
   

            logger.info("----Modificando Serie " + SER + " con año " + AIOt + "----", this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());

            String SRSUSR = user;//pendiente

            BigDecimal SRSAIO = new BigDecimal(SRSAIOt.trim());
            BigDecimal AIO = new BigDecimal(AIOt.trim());

            // &dividend - int(&dividend/&divisor)*&diviso
            String SRSFHI = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-hh.mm.ss.SSS"));
            System.out.println(SRSMBNt);
            BigDecimal SRSMBN = new BigDecimal(SRSMBNt.trim());
            BigDecimal SRSMON = new BigDecimal(SRSMONt.trim());
            BigDecimal SRSMNE = new BigDecimal(0);
            if (SRSMNEt.trim().equals("LPS")) {
                SRSMNE = new BigDecimal(0);
            } else {
                SRSMNE = new BigDecimal(1);
            }

            BigDecimal SRSBMP = new BigDecimal(SRSBMPt.trim());
            BigDecimal SRSTBC = new BigDecimal(SRSMON.doubleValue() / SRSMBN.doubleValue());
           
            String query = "UPDATE\n"
                    + "	" + ambiente + "USRLIB.BCPSRS t4\n"
                    + "SET\n"
                    + "	t4.SRSCOD='" + SRSCOD.trim() + "',\n"
                    + "	t4.SRSAIO=" + SRSAIO + ",\n"
                    + "	t4.SRSMNE=" + SRSMNE + ",\n"
                    + "	t4.SRSFDE='" + SRSFDE.trim() + "',\n"
                    + "	t4.SRSFDV='" + SRSFDV.trim() + "',\n"
                    + "	t4.SRSBMP=" + SRSBMP + ",\n"
                    + "	t4.SRSTBC= " + SRSTBC + ",\n"
                    + "	t4.SRSUSR='" + SRSUSR.trim() + "',\n"
                    + "	t4.SRSFHI='" + SRSFHI.trim() + "',\n"
                    + "	t4.SRSMON=" + SRSMON + ",\n"
                    + "	t4.SRSMBN=" + SRSMBN + ",\n"
                    + "	t4.SRSTVR='" + SRSTVR.trim() + "',\n"
                    + "	t4.SRSICD='" + SRSICD.trim() + "'\n"
                    + "WHERE\n"
                    + "	t4.SRSAIO =" + AIO + "\n"
                    + "	AND t4.SRSCOD='" + SER + "'";
            System.out.println(query);

            ps = cnx.getConn().prepareStatement(query);
            ps.executeUpdate();

            logger.info("----El usuario: " + SRSUSR + " ha modificado la serie " + SRSCOD + " con año " + SRSAIO + " existosamente----", this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());

//        } catch (Exception e) {
//            respuesta = false;
//
//            e.printStackTrace();
//            logger.error("----No se ha podido modificar la serie emitida " + SER + " con año " + AIOt + "---", this.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName());
//            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName());
//
//        }
        return respuesta;

    }

    public boolean ModificarTSE(Conexion cnx, Logger logger, String ambiente, String SER, String AIOt, String SRSTMXt, String SRSTMNt, String SRSTLIBt, String SRSTVR, String user) throws SQLException {

        boolean respuesta = true;
        if (SRSTVR.trim().equals("S")) {
            respuesta = true;
          

                logger.info("----Modificando la tasa de la serie " + SER + " con año " + AIOt + "----", this.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName());

                BigDecimal SRSTLIB = new BigDecimal(SRSTLIBt);
                BigDecimal SRSTMX = new BigDecimal(SRSTMXt);
                BigDecimal SRSTMN = new BigDecimal(SRSTMNt);
                BigDecimal SRSDIF = new BigDecimal(SRSTMX.doubleValue()-SRSTLIB.doubleValue());
                BigDecimal AIO = new BigDecimal(AIOt.trim());

                String query = "UPDATE\n"
                        + "	" + ambiente + "USRLIB.BCPSRS t4\n"
                        + "SET\n"
                        + "	t4.SRSTLIB=" + SRSTLIB + ",\n"
                        + "	t4.SRSDIF=" + SRSDIF + ",\n"
                        + "	t4.SRSTMX=" + SRSTMX + ",\n"
                        + "	t4.SRSTMN=" + SRSTMN + "\n"
                        + "WHERE\n"
                        + "	t4.SRSAIO =" + AIO + "\n"
                        + "	AND t4.SRSCOD='" + SER + "'";
                System.out.println(query);

                ps = cnx.getConn().prepareStatement(query);
                ps.executeUpdate();

                logger.info("----el usuario "+user+" ha modificado la tasa de la serie " + SER + " con año " + AIOt + " existosamente----", this.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName());
//            } catch (Exception e) {
//                respuesta = false;
//                e.printStackTrace();
//                logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
//                }.getClass().getEnclosingMethod().getName());
//
//            }
        } else {

            respuesta = false;

        }

        return respuesta;
    }

    public boolean deleteSE(Conexion cnx, Logger logger, String ambiente, String[] indicesSerie, List<SeriesM> listaSerie, String user) throws SQLException {

        String SER = "";
        String AIO = "";
        String TBV = "";
        Integer contador = 0;
        for (String indice : indicesSerie) {
            if (indicesSerie.length > 1) {
                SER += (contador == 0 ? "'" : ", '") + listaSerie.get(Integer.valueOf(indice)).getSRSCOD() + "'";
                AIO += (contador == 0 ? "'" : ", '") + listaSerie.get(Integer.valueOf(indice)).getSRSAIO() + "'";
                TBV += (contador == 0 ? "'" : ", '") + listaSerie.get(Integer.valueOf(indice)).getSRSTBV() + "'";
            } else {
                SER = "'" + listaSerie.get(Integer.valueOf(indice)).getSRSCOD() + "'";
                AIO = listaSerie.get(Integer.valueOf(indice)).getSRSAIO().toString();
                TBV = listaSerie.get(Integer.valueOf(indice)).getSRSTBV().toString();
            }
            contador++;

        }

        boolean respuesta = true;
    

            logger.info("-----------"+user+" Eliminando Serie " + SER + " con año " + AIO + "---------", this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
            BigDecimal TBVt = new BigDecimal(TBV);
            if (TBVt.intValue() <= 0) {
                ps = cnx.getConn().prepareStatement("DELETE FITUSRLIB.BCPSRS t4 WHERE t4.SRSAIO=" + AIO + " AND t4.SRSCOD=" + SER + "");
                ps.executeUpdate();
            } else {
                respuesta = false;
            }
            logger.info("-----------Se termino de eliminar la serie " + SER + " con año " + AIO + "---------", this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName());
//
//            logger.error("-----------No se puedo eliminar la serie " + SER + " con año " + AIO + "---------", this.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName());
//
//        }
        return respuesta;
    }

}
