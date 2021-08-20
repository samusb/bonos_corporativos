/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import static javax.servlet.SessionTrackingMode.URL;
import modelo.SeriesM;

/**
 * Desde aqui se inicializan todas las variables globales del aplicativo
 * incluyendo la conexion a la base de datos. Se instancian varios objetos de
 * los principales listados.
 */
public class Parametros {

    public static Conexion cnx;
    public static String ambiente;
    public static Logger logger;
    public static List<SeriesM> listaDeSeries;
    public static List<SeriesM> listaDeCC;

//public Parametros() {
//        try {
//            Properties props = new Properties();
//            String connection = System.getProperty("com.ficensa.boncorp.connection", System.getProperty("user.dir") + System.getProperty("file.separator") + "connection_BonCorp.properties");
//            System.out.println(connection);
//            props.load(new FileInputStream(connection));
//            cnx = new Conexion(props.getProperty("username"), props.getProperty("password"), props.getProperty("hostname"), props.getProperty("environment"));
//            ambiente = props.getProperty("environment");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            
//        }
//        listaDeSeries = new ArrayList<>();
//        listaDeCC = new ArrayList<>();
//    }
    public Parametros(String path) {
        try {
            FileReader archivo = new FileReader(path);
            Properties props = new Properties();
            props.load(archivo);

            cnx = new Conexion(props.getProperty("username"), props.getProperty("password"), props.getProperty("hostname"), props.getProperty("environment"));
            ambiente = props.getProperty("environment");
        } catch (Exception e) {
            e.printStackTrace();
        }
        listaDeSeries = new ArrayList<>();
        listaDeCC = new ArrayList<>();
    }
}
