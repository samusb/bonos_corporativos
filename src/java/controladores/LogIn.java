/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import servicio.LDAP;


/**
 *
 * @author aprivera
 */
public class LogIn extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
      protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sessionOp_ = (request.getParameter("sessionOp") != null ? request.getParameter("sessionOp") : "");

        switch (Integer.valueOf(sessionOp_)) {
            case 1:
                validar(request, response);
                break;
            case 2:
                Salir(request, response);
                break;
        }
    }

    private void Salir(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();
        session.removeAttribute("usuario");
        session.invalidate();
        response.sendRedirect("index.jsp");
    }

    public void validar(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        LDAP l = new LDAP();
       
        String usuario_ = request.getParameter("usuario").trim();
        String clave_ = request.getParameter("clave").trim();
        String groupDistinguishedName_ = "CN=BonosCorp,CN=Builtin,DC=ficensa,DC=com,DC=hn";   //<--- Grupo de Active Directory
        
        if (l.authenticate("FICENSA", usuario_, clave_, groupDistinguishedName_)) {
           
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuario_);
            session.setMaxInactiveInterval(30*60); // Formula = ( Minutos * segundos )
            
//           request.setAttribute("usuarioLogIn",usuario_);
            request.setAttribute("OpcionH","0");
            RequestDispatcher dispatcher = request.getRequestDispatcher("SeriesEmitidasSV");
            dispatcher.forward(request, response);
            
        } else {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Usuario y/o contraseña incorrecta');");
            out.println("</script>");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.include(request, response);
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
