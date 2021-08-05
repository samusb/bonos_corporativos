/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.SeriesM;
import principal.FormasNumeradas;
import principal.SeriesEmitidas;
import servicio.Conexion;
import servicio.Logger;
import servicio.Parametros;
import static servicio.Parametros.ambiente;
import static servicio.Parametros.logger;

/**
 *
 * @author aprivera
 */
public class FormNumSV extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private String opcion = "";
    private String FNM1 = "";
    private String OBS = "";
    private String AIO = "";
    String dir;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        dir = getServletContext().getRealPath("/") + "archivos/connection_BonCorp.properties";
        opcion = (request.getParameter("opcion") != null ? request.getParameter("opcion") : "");

        switch (Integer.valueOf(opcion.trim())) {
            case 0:
                getFormasNumeradas(request, response);
                break;
            case 1:
                FNM1 = (request.getParameter("FNM") != null ? request.getParameter("FNM") : "");
                OBS = (request.getParameter("OBS") != null ? request.getParameter("OBS") : "");
                AIO = (request.getParameter("AIO") != null ? request.getParameter("AIO") : "");
                AgregarFN(request, response);
                break;
        }
    }

    private void AgregarFN(HttpServletRequest request, HttpServletResponse response) {
        FormasNumeradas FN = new FormasNumeradas();
        double FNM2 = Double.parseDouble(FNM1);

        try {
            Parametros.cnx.Conectar();
            logger = Parametros.cnx.getLogger();
            String Accion = "----"+(String)request.getSession(false).getAttribute("usuario")+" Consultando series emitidas---";
            logger.info("------"+(String)request.getSession(false).getAttribute("usuario")+" Agregando formas numeradas----", this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
            for (int i = 0; i < FNM2; i++) {
                FN.AgregarFN(Parametros.cnx, logger, ambiente, OBS, AIO);
            }
            logger.info("---Se han agregado las formas numeradas---", this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
            getListaSeries(Parametros.cnx, logger, ambiente, Accion, request, response);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
        } finally {
            try {
                Parametros.cnx.Desconectar();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName());
            }
        }
    }

    private void getFormasNumeradas(HttpServletRequest request, HttpServletResponse response) {
        Parametros param = new Parametros(dir);
        try {
            param.cnx.Conectar();
            logger = param.cnx.getLogger();
            FormasNumeradas FN = new FormasNumeradas();
            List<SeriesM> listaFN = new ArrayList<>();
            HttpSession session = request.getSession();

            session.removeAttribute("listaFN");
            listaFN = FN.FormasNUM(param.cnx, logger, ambiente);
            session.setAttribute("listaFN", listaFN);
            response.sendRedirect("FormasNum.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
        } finally {
            try {
                param.cnx.Desconectar();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName());
            }
        }
    }

    private void getListaSeries(Conexion cnx, Logger logger, String ambiente, String Accion, HttpServletRequest request, HttpServletResponse response) {
        try {
            SeriesEmitidas SE = new SeriesEmitidas();
            List<SeriesM> listaSeries = new ArrayList<>();
            HttpSession session = request.getSession();

            session.removeAttribute("listaSeries");
            listaSeries = SE.DatosSerie(cnx, ambiente, logger, Accion);
            session.setAttribute("listaSeries", listaSeries);
            response.sendRedirect("SeriesE.jsp");
        } catch (Exception ex) {
            logger.error(ex.toString(), this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
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
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
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
