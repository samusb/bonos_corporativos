/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import com.oreilly.servlet.MultipartRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servicio.Parametros;
import static servicio.Parametros.logger;

/**
 *
 * @author aprivera
 */
public class DefEmisionesSV extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    InputStream archivo;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {

        String dir = getServletContext().getRealPath("/") + "archivos";
        String dir2 = getServletContext().getRealPath("/") + "archivos/connection_BonCorp.properties";
        //archivo = context.getResourceAsStream("/archivos/connection_BonCorp.properties");

        Parametros param = new Parametros(dir2);

        boolean res = true;

        try {
            param.cnx.Conectar();
            logger = param.cnx.getLogger();

            //MultipartRequest m = new MultipartRequest(request, "C:\\Users\\aprivera\\OneDrive - FICENSA\\Respaldo de Ana Rivera\\Proyectos NB Ficensa\\Bonos Corporativos\\Codigo fuente\\BonCorpV3\\archivos");
            MultipartRequest m = new MultipartRequest(request, dir);

            logger.info("----"+(String)request.getSession(false).getAttribute("usuario")+" ha enviado el documento al servidor----", this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());

        } catch (Exception e) {

            res = false;
            e.printStackTrace();
            response.setContentType("text/html");
            PrintWriter out;
            try {
                out = response.getWriter();
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Ha ocurrido un error en el envio del archivo');");
                out.println("</script>");
                RequestDispatcher rd = request.getRequestDispatcher("DefEmisiones.jsp");
                rd.include(request, response);
            } catch (Exception ex) {
                logger.error(ex.toString(), this.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName());
            }

            logger.error("---No se ha podido enviar el archivo al servidor---", this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());

        } finally {
            try {
                param.cnx.Desconectar();
                response.sendRedirect("DefEmisiones.jsp");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName());
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response

     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
