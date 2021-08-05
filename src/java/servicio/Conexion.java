


package servicio;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDriver;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexion 
{
    
    private Connection conn ;
    private static Statement stmt;
    private static AS400 sys;
    private String Usuario_DB2, Clave_DB2, IpServidor, Servidor;
     private Logger logger;
     
    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    public void setSys(AS400 sys) {
            this.sys = sys;
        }
    public void setStmt(Statement stmt) {
        this.stmt = stmt;
    }
    
    static public Statement getStmt() {
        return stmt;
    }
    static public AS400 getSys() {
        return sys;
    }

   public Conexion(String Usuario_DB2, String Clave_DB2, String IpServidor, String Servidor){
       this.Usuario_DB2 = Usuario_DB2;
       this.Clave_DB2   = Clave_DB2;
       this.IpServidor  = IpServidor;
       this.Servidor    = Servidor;
       this.logger = new Logger(this);
   }
    
  public Logger getLogger() {
        return this.logger;
    }
  
   public  void Conectar() throws IOException {  
        try 
        {
            // Inicialización del driver
            AS400JDBCDriver driver = new com.ibm.as400.access.AS400JDBCDriver();
            DriverManager.registerDriver(driver);

            // Conexión JDBC URL
            String url = "jdbc:as400://" + IpServidor + ";promt=false"; // Deshabilitar el GUI de la libreria jt400
            conn = DriverManager.getConnection(url, Usuario_DB2, Clave_DB2);
            stmt = conn.createStatement();
            sys = new AS400(IpServidor, Usuario_DB2, Clave_DB2);    //### conectar para llamar programas RPG
            
            if (conn != null)
            {
                logger.info("### Conexion exitosa a la base de datos DB2 (" + Servidor + ") ###", this.getClass().getSimpleName(), new Object() {}.getClass().getEnclosingMethod().getName());
                System.out.println("### Conexion exitosa a la base de datos DB2 (" + Servidor + ") ###");
            } 
            else
            {
                logger.error(" Falló la conexión a DB2 (" + Servidor + ")", this.getClass().getSimpleName(), new Object() {}.getClass().getEnclosingMethod().getName());
                System.out.println(" Falló la conexión a DB2 (" + Servidor + ")");
            } 
        } 
        catch (SQLException e) 
        {
            logger.error(" Falló la conexión a DB2 (" + Servidor + ") \n" + e.getMessage(), this.getClass().getSimpleName(), new Object() {}.getClass().getEnclosingMethod().getName());
            System.out.println(" Falló la conexión a DB2 (" + Servidor + ")");
        }
    }
   public void Desconectar() throws SQLException{
        logger.info("### Conexion cerrada exitosamente ###", this.getClass().getSimpleName(), new Object() {}.getClass().getEnclosingMethod().getName());
        System.out.println("### Conexion cerrada exitosamente ###");
        sys.disconnectAllServices();
        stmt.close();  
        conn.close();  
   }
    
}// fin clase principal


