/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicio;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static servicio.Parametros.ambiente;
import static servicio.Parametros.cnx;

/**
 * @author ugomez
 */
public class Logger {

    private LocalDateTime fechaHora;
    private StringBuilder mensaje;
    private String nombreClase;
    private String nombreMetodo;
    private String nivel;
    private Conexion cnx;
    private String ambiente = "FIT";
   
    
     public Logger(Conexion cnx) {
        this.cnx = cnx;  
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public void setMensaje(StringBuilder mensaje) {
        this.mensaje = mensaje;
    }

    public void setNombreClase(String nombreClase) {
        this.nombreClase = nombreClase;
    }

    public void setNombreMetodo(String nombreMetodo) {
        this.nombreMetodo = nombreMetodo;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getNivel() {
        return nivel;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public StringBuilder getMensaje() {
        return mensaje;
    }

    public String getNombreClase() {
        return nombreClase;
    }

    public String getNombreMetodo() {
        return nombreMetodo;
    }

    

    public void info(String mensaje, String nombreClase, String nombreMetodo) {

        try {
            String codigo = ""
                    + "INSERT INTO "+ambiente+"USRLIB.BCPLOG (LOGFEC, LOGMSG, LOGCLA, LOGMET, LOGNIV) \n"
                    + "VALUES( ?, ?, ?, ?, ?)"
                    + "";
            PreparedStatement ps = cnx.getConn().prepareStatement(codigo);

            ps.setString(1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-hh.mm.ss.SSS")).toString());
            ps.setString(2, mensaje);
            ps.setString(3, nombreClase);
            ps.setString(4, nombreMetodo);
            ps.setString(5, "Informacion");
            ps.addBatch();

            ps.executeBatch();
            cnx.getConn().commit();

        } catch (SQLException ex) {
            System.out.println(ex.toString());
            ex.printStackTrace();
        }
    }

    public void error(String mensaje, String nombreClase, String nombreMetodo) {

        try {
            String codigo = ""
                    + "INSERT INTO "+ambiente+"USRLIB.BCPLOG (LOGFEC, LOGMSG, LOGCLA, LOGMET, LOGNIV) \n"
                    + "VALUES( ?, ?, ?, ?, ?)"
                    + "";
            PreparedStatement ps = cnx.getConn().prepareStatement(codigo);

            ps.setString(1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-hh.mm.ss.SSS")).toString());
            ps.setString(2, mensaje);
            ps.setString(3, nombreClase);
            ps.setString(4, nombreMetodo);
            ps.setString(5, "Error");
            ps.addBatch();

            ps.executeBatch();
            cnx.getConn().commit();

        } catch (SQLException ex) {
            System.out.println(ex.toString());
            ex.printStackTrace();
        }
    }

}
