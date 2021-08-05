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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import modelo.SeriesM;
import servicio.Conexion;
import servicio.Logger;
import servicio.Parametros;
import static servicio.Parametros.ambiente;

/**
 *
 * @author aprivera
 */
public class FormasNumeradas {

    ResultSet rs;
    Statement st = null;
    PreparedStatement ps;
    Logger logger;

    public BigDecimal ConsultMAXFN(Conexion cnx, Logger logger, String ambiente, String AIO) { //para consultar la ultima forma numerada

        SeriesM consult = new SeriesM();
        try {
   
            rs = cnx.getStmt().executeQuery("SELECT MAX(t1.FNMNUM) as FNMAX\n"
                    + "FROM "+ambiente+"USRLIB.BCPMFN AS t1 WHERE  t1.FNMAIO=" + AIO);

            while (rs.next()) {
                consult.setCFNM(rs.getBigDecimal("FNMAX") != null ? rs.getBigDecimal("FNMAX") : new BigDecimal(0));
            }

        } catch (Exception e) {
            e.printStackTrace();

        } 
        return consult.getCFNM();
    }

    public void AgregarFN(Conexion cnx, Logger logger, String ambiente, String OBS, String AIO) throws IOException, SQLException { //para agregar las formas numeradas 
        BigDecimal temp = new BigDecimal(1);
   
            BigDecimal CANTFN = ConsultMAXFN(cnx, logger,ambiente, AIO.trim()).add(temp);
            
            String codigo = "INSERT\n"
                    + "	INTO\n"
                    + "	"+ambiente+"USRLIB.BCPMFN (FNMNUM,\n"
                    + "	FNMFIN,\n"
                    + "	FNMIMP,\n"
                    + "	FNMUSR,\n"
                    + "	FNMOBS,\n"
                    + "	FNMCOD,\n"
                    + "	FNMAIO,\n"
                    + "	FNMEST)\n"
                    + "VALUES ( ?,\n"
                    + "?,\n"
                    + "?,\n"
                    + "?,\n"
                    + "?,\n"
                    + "?,\n"
                    + "?,\n"
                    + "?)";
            PreparedStatement ps = cnx.getConn().prepareStatement(codigo);

            ps.setBigDecimal(1, CANTFN);
            ps.setString(2, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-hh.mm.ss.SSS")));
            ps.setString(3, "");
            ps.setString(4, "");
            ps.setString(5, OBS);
            ps.setString(6, "");
            ps.setBigDecimal(7, new BigDecimal(AIO.trim()));
            ps.setString(8, "N");
            ps.addBatch();

            ps.executeBatch();
            cnx.getConn().commit();

           
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            logger.info("---No se han podido agregar las formas numeradas---", this.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName());
//        }
    }

    public List<SeriesM> FormasNUM(Conexion cnx, Logger logger, String ambiente) throws SQLException { //para mostrar la lista de formas numeradas
        
        List<SeriesM> ListCFN = new ArrayList<>();

      
        
             logger.info("---Consultando formas numeradas---", this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
            rs = cnx.getStmt().executeQuery("SELECT\n"
                    + "	t1.FNMNUM AS NUM,\n"
                    + "	t1.FNMFIN AS FIN,\n"
                    + "	t1.FNMIMP AS IMP,\n"
                    + "	t1.FNMUSR AS USR,\n"
                    + "	t1.FNMOBS AS OBS,\n"
                    + "	t1.FNMCOD AS COD,\n"
                    + "	t1.FNMAIO AS AIO,\n"
                    + "	t1.FNMEST AS EST\n"
                    + "FROM\n"
                    + "	" + ambiente + "USRLIB.BCPMFN AS t1 ORDER BY t1.FNMAIO, t1.FNMNUM");
            while (rs.next()) {
                SeriesM consult = new SeriesM();
                consult.setFNMNUM(rs.getBigDecimal("NUM"));
                consult.setFNMFIN(rs.getString("FIN"));
                consult.setFNMIMP(rs.getString("IMP"));
                consult.setFNMUSR(rs.getString("USR"));
                consult.setFNMOBS(rs.getString("OBS"));
                consult.setFNMCOD(rs.getString("COD"));
                consult.setFNMEST(rs.getString("EST"));
                consult.setFNMAIO(rs.getBigDecimal("AIO"));
                ListCFN.add(consult);

            }

//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName());
//        } 
        return ListCFN;
    }

    public List<SeriesM> IMPRFN(Conexion cnx, Logger logger,String ambiente, String AIO) {//para verificar que existan formas numeradas
        List<SeriesM> ListCFN = new ArrayList<>();

        try {

            rs = cnx.getStmt().executeQuery("SELECT\n"
                    + "	T1.FNMNUM AS CANTFN\n"
                    + "FROM\n"
                    + ""+ambiente+"USRLIB.BCPMFN AS T1 WHERE T1.FNMEST='N' AND FNMAIO=" + AIO);
            while (rs.next()) {

                SeriesM consult = new SeriesM();
                consult.setFNM(rs.getBigDecimal("CANTFN"));
                ListCFN.add(consult);

            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
        }
        return ListCFN;
    }

    public int ConsultMIXFN(Conexion cnx, Logger logger, String ambiente, String AIO) {//para tomar el ultimo numero 

        int element = 0;
        try {
            rs = cnx.getStmt().executeQuery("SELECT MIN(t1.FNMNUM) as FNMIN\n"
                    + "FROM " + ambiente + "USRLIB.BCPMFN AS t1 "
                    + "WHERE  t1.FNMAIO=" + AIO + " AND t1.FNMEST='N'");
            while (rs.next()) {
                element = rs.getInt("FNMIN");

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
        }
        return element;
    }

    public void ActEstFN(Conexion cnx, Logger logger, String ambiente, int FNMIN, String SER, String AIO,String user) throws SQLException {//para actuluzarlas cuando estas sean usadas

        try {

            String query = "UPDATE\n"
                    + "	" + ambiente + "USRLIB.BCPMFN t4\n"
                    + "SET\n"
                    + "	t4.FNMEST='S',\n"
                    + "	t4.FNMIMP='" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-hh.mm.ss.SSS")) + "',\n"
                    + "	t4.FNMUSR='"+user+"',\n"
                    + "	t4.FNMCOD=" + SER + "\n"
                    + "WHERE\n"
                    + "T4.FNMNUM=" + FNMIN + " AND t4.FNMEST='N' AND t4.FNMAIO=" + AIO + "";
            System.out.println(query);

            ps = cnx.getConn().prepareStatement(query);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
        }

    }
}
