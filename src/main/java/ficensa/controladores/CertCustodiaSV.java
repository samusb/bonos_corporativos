/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.SeriesM;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import principal.CertificadoCustodia;
import principal.Impresion;
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
public class CertCustodiaSV extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private String opcion = "", seleccionadosInput = "";
    String CTCCOD = "";
    String CTCCCD = "";
    String CTCAIO = "";
    String CTCIDN = "";
    String CTCNOM = "";
    String CTCACC = "";
//    String CTCFVT = "";
//    String CTCFIN = "";
//    String CTCFRG = "";
    String CTCFPA = "";
    String CTCFPI = "";
    String CTCEXC = "";
    String CTCIMP = "";
    String CTCUSR = "";
    String SRSTBV = "";
    String SRSBMP = "";
    String SRSCCC = "";
    String SRSTBC = "";
    String CTCNMB = "";
    String dir;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {

        dir = getServletContext().getRealPath("/") + "archivos/connection_BonCorp.properties";
        opcion = (request.getParameter("opcion") != null ? request.getParameter("opcion") : "");
        seleccionadosInput = (request.getParameter("seleccionadosInput") != null ? request.getParameter("seleccionadosInput").trim() : "");
        switch (Integer.valueOf(opcion.trim())) {
            case 1:
                getSerieAIO(request, response);
                break;
            case 2:
                SRSTBV = (request.getParameter("inputTBV") != null ? request.getParameter("inputTBV") : "");
                ;
                SRSBMP = (request.getParameter("BMP") != null ? request.getParameter("BMP") : "");
                ;
                CTCCOD = (request.getParameter("inputSerie") != null ? request.getParameter("inputSerie") : "");
                CTCAIO = (request.getParameter("inputAio") != null ? request.getParameter("inputAio") : "");
                ;
                SRSCCC = (request.getParameter("CCC") != null ? request.getParameter("CCC") : "");
                ;
                SRSTBC = (request.getParameter("TBC") != null ? request.getParameter("TBC") : "");
                ;
                getAG(request, response);
                break;
            case 3:

                CTCUSR = (String) request.getSession(false).getAttribute("usuario");
                CTCCOD = (request.getParameter("inputSerie") != null ? request.getParameter("inputSerie") : "");
//                    SRSCCC = request.getParameter("CCC");
                CTCNMB = (request.getParameter("inputNoBono") != null ? request.getParameter("inputNoBono") : "");
                CTCAIO = (request.getParameter("inputAio") != null ? request.getParameter("inputAio") : "");
                CTCIDN = (request.getParameter("inputIden") != null ? request.getParameter("inputIden") : "");
                CTCNOM = (request.getParameter("inputNombre") != null ? request.getParameter("inputNombre") : "");
                SRSTBV = (request.getParameter("inputTBV") != null ? request.getParameter("inputTBV") : "");
                SRSTBC = (request.getParameter("TBC") != null ? request.getParameter("TBC") : "");
                SRSBMP = (request.getParameter("BMP") != null ? request.getParameter("BMP") : "");
                Agregar(request, response);

                break;
            case 6:
                getMODTEN(request, response);
                break;
            case 7:

                CTCUSR = (String) request.getSession(false).getAttribute("usuario");
                CTCCOD = (request.getParameter("inputSerie") != null ? request.getParameter("inputSerie") : "");
                CTCCCD = (request.getParameter("inputNoBono") != null ? request.getParameter("inputNoBono") : "");
                CTCAIO = (request.getParameter("inputAio") != null ? request.getParameter("inputAio") : "");
                CTCNOM = (request.getParameter("inputNombre") != null ? request.getParameter("inputNombre") : "");
                CTCIDN = (request.getParameter("inputIden") != null ? request.getParameter("inputIden") : "");

                ModificarTEN(request, response);

                break;

            case 8:

                CTCCOD = (request.getParameter("inputSerie") != null ? request.getParameter("inputSerie") : "");
                CTCAIO = (request.getParameter("inputAio") != null ? request.getParameter("inputAio") : "");
                //Eliminar(request, response);
                break;
            case 9:
                SeriesServlet(request, response);
                break;
            case 10:
                Imprimir(request, response);
                break;
            case 11:
                CTCCOD = (request.getParameter("inputSerie") != null ? request.getParameter("inputSerie") : "");
                CTCAIO = (request.getParameter("inputAio") != null ? request.getParameter("inputAio") : "");
                getSerieAIOAG2(request, response);
                break;
            case 12:
                ImprimirRC(request, response);
                break;
        }

    }

    private void SeriesServlet(HttpServletRequest request, HttpServletResponse response) {
        Parametros param = new Parametros(dir);

        try {
            param.cnx.Conectar();
            logger = param.cnx.getLogger();
            logger.info("---" + (String) request.getSession(false).getAttribute("usuario") + "Ingresando a Series emitidas---", this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
            request.setAttribute("OpcionH", "0");
            RequestDispatcher dispatcher = request.getRequestDispatcher("SeriesEmitidasSV");
            dispatcher.forward(request, response);
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

    private void Agregar(HttpServletRequest request, HttpServletResponse response) {

        CertificadoCustodia SE = new CertificadoCustodia();
        try {
            Parametros.cnx.Conectar();
            logger = Parametros.cnx.getLogger();
            String Accion = "----Consultando nuevamente CC luego de agregar---";
            BigDecimal TBV = new BigDecimal(SRSTBV.trim());
            BigDecimal TBC = new BigDecimal(SRSTBC.trim());
            BigDecimal NMB = new BigDecimal(CTCNMB.trim());
            BigDecimal BMP = new BigDecimal(SRSBMP.trim());
            BigDecimal MAXBNV = new BigDecimal(TBV.doubleValue() + NMB.doubleValue());
            if (MAXBNV.doubleValue() > TBC.doubleValue()) {
                logger.info("---No se puede agregar CC porque: Excedio el limite de bonos corporativos para vender---", this.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName());
                msg(Parametros.cnx, logger, ambiente, request, response, "Excedio el limite de bonos corporativos para vender");

            } else {
               
                    SE.AgregarCC(Parametros.cnx, logger, ambiente,
                            SRSTBV,
                            CTCNMB,
                            CTCCOD,
                            CTCAIO,
                            CTCIDN,
                            CTCNOM,
                            CTCUSR);
                    getSerieAIOAG(Parametros.cnx, logger, ambiente, Accion, request, response);
                
            }
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

    private void getSerieAIOAG(Conexion cnx, Logger logger, String ambiente, String Accion, HttpServletRequest request, HttpServletResponse response) {
        try { //La conexion se hace al ser llamado

            logger.info(Accion, this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
            CertificadoCustodia CC = new CertificadoCustodia();
            List<SeriesM> listaSerieAIO = new ArrayList<>();
            List<SeriesM> listaCC = new ArrayList<>();
            HttpSession session = request.getSession();

            session.removeAttribute("listaSerieAIO");
            session.removeAttribute("listaCC");
            listaCC = CC.DatosCCESPECIFIC(cnx, logger, ambiente, CTCCOD, CTCAIO);
            listaSerieAIO = CC.DatosSerieESPECIFIC2(cnx, logger, ambiente, CTCCOD, CTCAIO);
            session.setAttribute("listaSerieAIO", listaSerieAIO);
            session.setAttribute("listaCC", listaCC);
            response.sendRedirect("CertCustodia.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());

        }
    }

    private void getAG(HttpServletRequest request, HttpServletResponse response) {
        Parametros param = new Parametros(dir);
        try {
            param.cnx.Conectar();
            logger = param.cnx.getLogger();

            BigDecimal SRSTBCt = new BigDecimal(SRSTBC.trim());
            BigDecimal SRSTBVt = new BigDecimal(SRSTBV.trim());
            BigDecimal BonoDisp = new BigDecimal(SRSTBCt.doubleValue() - SRSTBVt.doubleValue());
            if (BonoDisp.doubleValue() <= 0) {
                logger.info("---No se puede acceder al agregar CC porque: ya no existen mas bonos disponibles para vender---", this.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName());
                msg(param.cnx, logger, ambiente, request, response, "Ya no existen mas bonos disponibles para vender");
            } else {
                logger.info("---" + (String) request.getSession(false).getAttribute("usuario") + " Mostrando agregar de Certificado en custodia---", this.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName());
                HttpSession session = request.getSession();

                session.removeAttribute("inputSerie");
                session.setAttribute("inputSerie", CTCCOD);

                session.removeAttribute("inputAio");
                session.setAttribute("inputAio", CTCAIO);

                session.removeAttribute("SRSTBV");
                session.setAttribute("SRSTBV", SRSTBV);

                session.removeAttribute("SRSBMP");
                session.setAttribute("SRSBMP", SRSBMP);

                session.removeAttribute("SRSCCC");
                session.setAttribute("SRSCCC", SRSCCC);

                session.removeAttribute("SRSTBC");
                session.setAttribute("SRSTBC", SRSTBC);

                response.sendRedirect("AgregarCC.jsp");
            }
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

    private void getSerieAIOAG2(HttpServletRequest request, HttpServletResponse response) {
        Parametros param = new Parametros(dir);
        try {
            param.cnx.Conectar();
            logger = param.cnx.getLogger();
            CertificadoCustodia CC = new CertificadoCustodia();
            List<SeriesM> listaSerieAIO = new ArrayList<>();
            List<SeriesM> listaCC = new ArrayList<>();
            HttpSession session = request.getSession();

            session.removeAttribute("listaSerieAIO");
            session.removeAttribute("listaCC");
            listaCC = CC.DatosCCESPECIFIC(param.cnx, logger, ambiente, CTCCOD, CTCAIO);
            listaSerieAIO = CC.DatosSerieESPECIFIC2(param.cnx, logger, ambiente, CTCCOD, CTCAIO);
            session.setAttribute("listaSerieAIO", listaSerieAIO);
            session.setAttribute("listaCC", listaCC);
            response.sendRedirect("CertCustodia.jsp");
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

    private void getSerieAIO(HttpServletRequest request, HttpServletResponse response) {
        Parametros param = new Parametros(dir);
        try {
            param.cnx.Conectar();
            logger = param.cnx.getLogger();
            String Accion = "---" + (String) request.getSession(false).getAttribute("usuario") + " Consultando series emitidas en Certificado en custodia--";
            String Accion2 = "---" + (String) request.getSession(false).getAttribute("usuario") + " Consultando Certificado en custodia--";
            String[] indicesSerie = seleccionadosInput.split(",");
            SeriesEmitidas SE = new SeriesEmitidas();
            CertificadoCustodia CC = new CertificadoCustodia();
            List<SeriesM> listaSerieAIO = new ArrayList<>();
            List<SeriesM> listaCC = new ArrayList<>();
            HttpSession session = request.getSession();

            session.removeAttribute("listaCC");
            session.removeAttribute("listaSerieAIO");
            listaSerieAIO = SE.DatosSerieESPECIFIC(param.cnx, ambiente, logger, Accion, indicesSerie, (List<SeriesM>) request.getSession(false).getAttribute("listaSeries"));
            listaCC = CC.DatosCC(param.cnx, ambiente, logger, Accion2, indicesSerie, (List<SeriesM>) request.getSession(false).getAttribute("listaSeries"));
            session.setAttribute("listaCC", listaCC);
            session.setAttribute("listaSerieAIO", listaSerieAIO);
            response.sendRedirect("CertCustodia.jsp");

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

//    private void getMODCC(HttpServletRequest request, HttpServletResponse response) {
//        Parametros param = new Parametros();
//        try {
//            param.cnx.Conectar();
//            logger = param.cnx.getLogger();
//            String Accion = "---Consultando modificar en Certificado en custodia--";
//            String[] indicesSerie = seleccionadosInput.split(",");
//            CertificadoCustodia CC = new CertificadoCustodia();
//
//            List<SeriesM> listaCC = new ArrayList<>();
//            HttpSession session = request.getSession();
//
//            session.removeAttribute("listaMCC");
//            listaCC = CC.DatosMODCCESP(param.cnx, logger, ambiente, Accion, indicesSerie, (List<SeriesM>) request.getSession(false).getAttribute("listaCC"));
//            session.setAttribute("listaMCC", listaCC);
//
//            response.sendRedirect("ModificarCC.jsp");
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName());
//        } finally {
//            try {
//                param.cnx.Desconectar();
//            } catch (Exception e) {
//                e.printStackTrace();
//                logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
//                }.getClass().getEnclosingMethod().getName());
//            }
//        }
    //   }
    private void getMODTEN(HttpServletRequest request, HttpServletResponse response) {
        Parametros param = new Parametros(dir);
        try {
            param.cnx.Conectar();
            logger = param.cnx.getLogger();
            String Accion = "---" + (String) request.getSession(false).getAttribute("usuario") + " Consultando modificar Tenedor en Certificado en custodia--";
            String[] indicesSerie = seleccionadosInput.split(",");
            CertificadoCustodia CC = new CertificadoCustodia();

            List<SeriesM> listaCC = new ArrayList<>();
            HttpSession session = request.getSession();

            session.removeAttribute("listaMCC");
            listaCC = CC.DatosMODCCESP(param.cnx, logger, ambiente, Accion, indicesSerie, (List<SeriesM>) request.getSession(false).getAttribute("listaCC"));
            session.setAttribute("listaMCC", listaCC);

            response.sendRedirect("ModificarTenedor.jsp");
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

//    private void ModificarCC(HttpServletRequest request, HttpServletResponse response) throws SQLException {
//
//        CertificadoCustodia SE = new CertificadoCustodia();
//        Parametros param = new Parametros();
//        try {
//            param.cnx.Conectar();
//            logger = param.cnx.getLogger();
//            String Accion = "---Consultando Certificado en cudtodia despues de modificar--";
//            SE.ModificarCC(param.cnx, logger, ambiente, CTCCOD,
//                    CTCCCD,
//                    CTCAIO,
//                    CTCACC,
//                    CTCFPA,
//                    CTCFPI,
//                    CTCEXC,
//                    CTCIMP,
//                    CTCUSR);
//            getSerieAIOAG(param.cnx, logger, ambiente, Accion, request, response);
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName());
//        } finally {
//            try {
//                param.cnx.Desconectar();
//            } catch (Exception e) {
//                e.printStackTrace();
//                logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
//                }.getClass().getEnclosingMethod().getName());
//            }
//        }
//    }
    private void ModificarTEN(HttpServletRequest request, HttpServletResponse response) {
        Parametros param = new Parametros(dir);
        CertificadoCustodia SE = new CertificadoCustodia();

        try {
            param.cnx.Conectar();
            logger = param.cnx.getLogger();
            String Accion = "---" + (String) request.getSession(false).getAttribute("usuario") + "  Consultando Certificado en custodia despues de modificar Tenedor --";

            SE.ModificarTenedor(param.cnx, logger, ambiente,
                    CTCCOD,
                    CTCCCD,
                    CTCAIO,
                    CTCNOM,
                    CTCIDN,
                    CTCUSR);
            getSerieAIOAG(param.cnx, logger, ambiente, Accion, request, response);
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

//    private void Eliminar(HttpServletRequest request, HttpServletResponse response) {
//        CertificadoCustodia CC = new CertificadoCustodia();
//
//        try {
//            Parametros.cnx.Conectar();
//            logger = Parametros.cnx.getLogger();
//           // String Accion = "---Consultando Certificado en custodia despues de modificar Tenedor --";
//            String[] indicesSerie = seleccionadosInput.split(",");
//            
//            CC.deleteCC(indicesSerie, (List<SeriesM>) request.getSession(false).getAttribute("listaCC"));
//            getSerieAIOAG(Parametros.cnx, logger, ambiente, Accion, request, response);
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName());
//        } finally {
//            try {
//                Parametros.cnx.Desconectar();
//            } catch (Exception e) {
//                e.printStackTrace();
//                logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
//                }.getClass().getEnclosingMethod().getName());
//            }
//        }
//    }
    private void Imprimir(HttpServletRequest request, HttpServletResponse response) {
        Impresion imp = new Impresion();
        Parametros param = new Parametros(dir);
        try {
            param.cnx.Conectar();
            String path = getServletContext().getRealPath("/") + "archivos" + "/Certificado Custodia - Anverso.docx";
            logger = param.cnx.getLogger();
            String[] indicesSerie = seleccionadosInput.split(",");
            //String getFile = "C:\\Users\\aprivera\\OneDrive - FICENSA\\Respaldo de Ana Rivera\\Proyectos NB Ficensa\\Bonos Corporativos\\Codigo fuente\\BonCorpV3\\archivos\\Certificado Custodia - Anverso.docx";
            XWPFDocument workbook = imp.getCertificado(param.cnx, logger, ambiente, indicesSerie, (List<SeriesM>) request.getSession(false).getAttribute("listaCC"), path, (String) request.getSession(false).getAttribute("usuario"));

            if (workbook == null) {
                logger.info("No se puede imprimir porque no existen formas numeradas para el año de la serie del certificado en custodia selecionados", this.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName());
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<script type=\"text/javascript\">");
                out.println("alert('No se puede imprimir porque no existen formas numeradas para el año de la serie del certificado en custodia selecionado');");
                out.println("</script>");
                RequestDispatcher rd = request.getRequestDispatcher("CertCustodia.jsp");
                rd.include(request, response);
            } else {
                String horaTemp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh.mm.ss.SSS"));//Se toma la hora para el nombre al igual que la fecha
                String fechaTemp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")); // Se toma le fecha para colocarla en el nombre    
                String nombreArchivo = "\\Certificado Custodia_" + fechaTemp + "_" + horaTemp;
                response.setContentType("application/.zip");//Se le dice que tipo de archivo es (PDF, Word, PowerPoint etc.) en este caso un ZIP
                response.setHeader("Content-Disposition", "attachment; filename= " + nombreArchivo + ".zip"); // Le asigno el nombre que tendra el archivo
                ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());

                String filename = "\\Certificado Custodia - Anverso.docx";
                zos.putNextEntry(new ZipEntry(filename)); //Se va metiendo cada excel en el archivo zip
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                workbook.write(bos);
                bos.writeTo(zos);
                zos.closeEntry();
                zos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/html");
            PrintWriter out;
            try {
                out = response.getWriter();
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Ha ocurrido un error al momento de la descarga del documento ');");
                out.println("</script>");
            } catch (IOException ex) {
                logger.error(ex.toString(), this.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName());
            }

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
    
    private void ImprimirRC(HttpServletRequest request, HttpServletResponse response) {
        Parametros param = new Parametros(dir);
        Impresion imp = new Impresion();

        try {
            param.cnx.Conectar();
            String path = getServletContext().getRealPath("/") + "archivos";
            String[] indicesSerie = seleccionadosInput.split(",");
            logger = param.cnx.getLogger();
            List<SeriesM> listaSerie = (List<SeriesM>) request.getSession(false).getAttribute("listaSeries");

            // XWPFDocument workbook = imp.getMacrotitulo(param.cnx, logger, ambiente, path, indicesSerie, listaSerie,(String)request.getSession(false).getAttribute("usuario"));
            XWPFDocument workbookR = imp.getReversoC(param.cnx, logger, ambiente, path, indicesSerie, listaSerie, (String) request.getSession(false).getAttribute("usuario"));
            if (workbookR == null) {
                logger.info("No se puede imprimir porque no existen formas numeradas para el año de la serie del certificado en custodia selecionados", this.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName());
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<script type=\"text/javascript\">");
                out.println("alert('No se puede imprimir porque no existen formas numeradas para el año de la serie del certificado en custodia selecionados');");
                out.println("</script>");
                RequestDispatcher rd = request.getRequestDispatcher("CertCustodia.jsp");
                rd.include(request, response);

            } else {

                String horaTemp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh.mm.ss.SSS"));//Se toma la hora para el nombre al igual que la fecha
                String fechaTemp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")); // Se toma le fecha para colocarla en el nombre    
                String nombreArchivo = "\\Certificado Custodia_Reverso_" + fechaTemp + "_" + horaTemp;
                response.setContentType("application/.zip");//Se le dice que tipo de archivo es (PDF, Word, PowerPoint etc.) en este caso un ZIP
                response.setHeader("Content-Disposition", "attachment; filename= " + nombreArchivo + ".zip"); // Le asigno el nombre que tendra el archivo
                ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());

                // String filename = "\\Macrotitulo - Anverso.docx";
                String filenameR = "\\Certificado Custodia_Reverso.docx";
                //zos.putNextEntry(new ZipEntry(filename)); //Se va metiendo cada excel en el archivo zip
                zos.putNextEntry(new ZipEntry(filenameR));
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

//                workbook.write(bos);
                workbookR.write(bos);
                bos.writeTo(zos);
                zos.closeEntry();
                zos.close();

            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/html");
            PrintWriter out;
            try {
                out = response.getWriter();
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Ha ocurrido un error al momento de la descarga del documento ');");
                out.println("</script>");
            } catch (IOException ex) {
                logger.error(ex.toString(), this.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName());
            }

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

    private void msg(Conexion cnx, Logger logger, String ambiente, HttpServletRequest request, HttpServletResponse response, String mensaje) throws ServletException, IOException {
        String Accion = "---Consultando CC & Serie & año de serie emitida--- ";
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println("alert('" + mensaje + "');");
        out.println("</script>");
        RequestDispatcher rd = request.getRequestDispatcher("CertCustodia.jsp");
        rd.include(request, response);
        getSerieAIOAG(cnx, logger, ambiente, Accion, request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     *
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
