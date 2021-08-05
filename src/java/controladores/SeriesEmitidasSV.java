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
import java.time.LocalDate;
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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.SeriesM;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
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
@WebServlet(name = "SeriesEmitidasSV", urlPatterns = {"/SeriesEmitidasSV", "/SeriesEmitidasSV/*"})
public class SeriesEmitidasSV extends HttpServlet {

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
    String SRSICD = "";//codigo ISIN
    String SRSCOD = "";//codigo serie
    String SRSFDE = "";//Fecha emisión
    String SRSFDV = "";//Fecha emisión
    String SRSTVR = "";//tasa variable S/N
    String SRSAio = "";//año
    String SRSPLD = "";//plaza en dias
    String SRSMNE = "";//moneda
    String SRSMON = "";//monto tot macrotitulo
    String SRSMBN = "";//monto por cada bono
    String SRSBMP = "";//bonos min por inversionista o persona
    String SER = "";
    String AIO = "";
    String TBV = "";
    DateTimeFormatter formatter;
    String SRSFDEt = "";//Fecha emisión
    LocalDate dateFE;
    String SRSTMX = "";//tasa maxima
    String SRSTMN = "";//tasa minima
    String SRSTLIB = "";//tasa libor
    String SRSDIF = "";//tasa diferencial
    String dir;

    //String Usuario;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        dir = getServletContext().getRealPath("/") + "archivos/connection_BonCorp.properties";

        String OpcionH = ((String) request.getAttribute("OpcionH") != null ? (String) request.getAttribute("OpcionH") : "1");
        // Usuario = ((String) request.getAttribute("usuarioLogIn") != null ? (String) request.getAttribute("usuarioLogIn") : "usuario");

        switch (Integer.valueOf(OpcionH.trim())) {
            case 0:
                getListaSeries2(request, response);
                break;
            case 1:

                opcion = (request.getParameter("opcion") != null ? request.getParameter("opcion") : "");
                seleccionadosInput = (request.getParameter("seleccionadosInput") != null ? request.getParameter("seleccionadosInput").trim() : "");

                switch (Integer.valueOf(opcion.trim())) {
                    case 0:

                        Eliminar(request, response);//*
                        break;
                    case 1:
                        ModificarConsult(request, response);//*
                        break;
                    case 2:
                        SRSICD = (request.getParameter("inputISIN") != null ? request.getParameter("inputISIN") : "");//codigo ISIN
                        SRSCOD = (request.getParameter("inputSerie") != null ? request.getParameter("inputSerie") : "");//codigo serie
                        SRSFDE = (request.getParameter("datepicker") != null ? request.getParameter("datepicker") : "");//Fecha emisión
                        SRSFDV = (request.getParameter("datepicker2") != null ? request.getParameter("datepicker2") : "");//Fecha emisión
                        SRSTVR = (request.getParameter("inputTV") != null ? request.getParameter("inputTV") : "N");//tasa variable S/N
                        SRSAio = (request.getParameter("inputAio") != null ? request.getParameter("inputAio") : "");//año
                        //SRSPLD = request.getParameter("inputPlazo")!= null ? request.getParameter("inputPlazo") : "");//plaza en dias
                        SRSMNE = (request.getParameter("inputMoneda") != null ? request.getParameter("inputMoneda") : "");//moneda
                        SRSMON = (request.getParameter("inputMTM") != null ? request.getParameter("inputMTM") : "");//monto tot macrotitulo
                        SRSMBN = (request.getParameter("inputMCB") != null ? request.getParameter("inputMCB") : "");//monto por cada bono
                        SRSBMP = (request.getParameter("inputBMI") != null ? request.getParameter("inputBMI") : "");//bonos min por inversionista o persona
                        TBV = (request.getParameter("TBV") != null ? request.getParameter("TBV") : "");  //total bonos vendidos
                        SER = (request.getParameter("SER") != null ? request.getParameter("SER") : "");
                        AIO = (request.getParameter("AIO") != null ? request.getParameter("AIO") : "");
                        Modificar(request, response);
                        break;
                    case 3:
                        redirectAGSer(request, response);
                        break;
                    case 4:
                        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        SRSICD = (request.getParameter("inputISIN") != null ? request.getParameter("inputISIN") : "");//codigo ISIN
                        SRSCOD = (request.getParameter("inputSerie") != null ? request.getParameter("inputSerie") : "");//codigo serie
                        SRSFDEt = (!request.getParameter("datepicker").equals("") ? request.getParameter("datepicker") : "2000-01-01");//Fecha emisión
                        System.out.println("SRSFDEt = " + SRSFDEt);
                        dateFE = LocalDate.parse(SRSFDEt, formatter);
                        SRSFDE = dateFE.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));//fecha de emision
                        SRSTVR = (request.getParameter("inputTV") != null ? request.getParameter("inputTV") : "N");//tasa variable S/N
                        SRSAio = (request.getParameter("inputAio") != null ? request.getParameter("inputAio") : "");//año
                        SRSPLD = (request.getParameter("inputPlazo") != null ? request.getParameter("inputPlazo") : "");//plaza en dias
                        SRSMNE = (request.getParameter("inputMoneda") != null ? request.getParameter("inputMoneda") : "");//moneda
                        SRSMON = (request.getParameter("inputMTM") != null ? request.getParameter("inputMTM") : "");//monto tot macrotitulo
                        SRSMBN = (request.getParameter("inputMCB") != null ? request.getParameter("inputMCB") : "");//monto por cada bono
                        SRSBMP = (request.getParameter("inputBMI") != null ? request.getParameter("inputBMI") : "");//bonos min por inversionista o persona
                        SRSTMX = (request.getParameter("inputTM") != null ? request.getParameter("inputTM") : "");//tasa maxima
                        SRSTMN = (request.getParameter("inputTMin") != null ? request.getParameter("inputTMin") : "");//tasa minima
                        SRSTLIB = (request.getParameter("inputLIB") != null ? request.getParameter("inputLIB") : "");//tasa libor
                        // SRSDIF = (request.getParameter("inputDif") != null ? request.getParameter("inputDif") : "");//tasa diferencial
                        Agregar(request, response);//*
                        break;

                    case 5:
                        ModificarTConsult(request, response);//*
                        break;
                    case 6:
                        SRSTMX = (request.getParameter("inputTM") != null ? request.getParameter("inputTM") : "");//tasa maxima
                        SRSTMN = (request.getParameter("inputTMin") != null ? request.getParameter("inputTMin") : "");//tasa minima
                        SRSTLIB = (request.getParameter("inputLIB") != null ? request.getParameter("inputLIB") : "");//tasa libor
                        //SRSDIF = (request.getParameter("inputDif") != null ? request.getParameter("inputDif") : "");//tasa diferencial

                        TBV = (request.getParameter("TBV") != null ? request.getParameter("TBV") : ""); //total bonos vendidosrequest.getParameter("TBV")       != null ? request.getParameter("inputTM") : "") ; //total bonos vendidos
                        SER = (request.getParameter("SER") != null ? request.getParameter("SER") : ""); //total bonos vendidosrequest.getParameter("TBV")       != null ? request.getParameter("inputTM") : "") ; //total bonos vendidosrequest.getParameter("SER")       != null ? request.getParameter("inputTM") : "") ;
                        AIO = (request.getParameter("AIO") != null ? request.getParameter("AIO") : "");
                        SRSTVR = (request.getParameter("TVR") != null ? request.getParameter("TVR") : "");
                        ModificarTasa(request, response);//*
                        break;
                    case 7:
                        Imprimir(request, response);//*
                        break;
                    case 8:
                        redirectDefE(request, response);
                        break;
                    case 9:
                        getListaSeries2(request, response);
                        break;
                    case 10:
                        ImprimirR(request, response);//*
                        break;

                }
                break;

        }

    }

    private void redirectAGSer(HttpServletRequest request, HttpServletResponse response) {
        Parametros param = new Parametros(dir);

        try {
            param.cnx.Conectar();
            logger = param.cnx.getLogger();
            logger.info("---" + (String) request.getSession(false).getAttribute("usuario") + " Ingresando a Agregar Series emitidas---", this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
            response.sendRedirect("AgregarSE.jsp");
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

    private void redirectDefE(HttpServletRequest request, HttpServletResponse response) {
        Parametros param = new Parametros(dir);

        try {
            param.cnx.Conectar();
            logger = param.cnx.getLogger();
            logger.info("---" + (String) request.getSession(false).getAttribute("usuario") + " Ingresando a Definicion de emisiones---", this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());
            response.sendRedirect("DefEmisiones.jsp");
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
        Parametros param = new Parametros(dir);
        SeriesEmitidas SE = new SeriesEmitidas();
        try {
            param.cnx.Conectar();
            logger = param.cnx.getLogger();
            String Accion = "----" + (String) request.getSession(false).getAttribute("usuario") + " Consultando nuevamente series emitidas luego de agregar---";
            if (!SE.SerExist(param.cnx, logger, ambiente, SRSCOD, SRSAio).isEmpty()) {
                logger.info("----No se puede agregar la serie porque ya existe---- ", this.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName());
                msg(request, response, "No se puede agregar la serie porque ya existe");

            } else {
                SE.AgregarSI(param.cnx, logger, ambiente,
                        SRSICD,
                        SRSCOD,
                        SRSFDE,
                        SRSTVR,
                        SRSAio,
                        SRSPLD,
                        SRSMNE,
                        SRSMON,
                        SRSMBN,
                        SRSBMP,
                        SRSTMX,
                        SRSTMN,
                        SRSTLIB,
                        dateFE, (String) request.getSession(false).getAttribute("usuario"));
                getListaSeries(param.cnx, logger, ambiente, Accion, request, response);

            }

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
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString(), this.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName());

        }
    }

    private void getListaSeries2(HttpServletRequest request, HttpServletResponse response) {
        Parametros param = new Parametros(dir);
        try {
            param.cnx.Conectar();
            logger = param.cnx.getLogger();
            String Accion = "----" + (String) request.getSession(false).getAttribute("usuario") + " Consultando series emitidas ---";
            SeriesEmitidas SE = new SeriesEmitidas();
            List<SeriesM> listaSeries = new ArrayList<>();
            HttpSession session = request.getSession();

            session.removeAttribute("listaSeries");
            listaSeries = SE.DatosSerie(Parametros.cnx, ambiente, logger, Accion);
            session.setAttribute("listaSeries", listaSeries);
            response.sendRedirect("SeriesE.jsp");
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

    private void ModificarConsult(HttpServletRequest request, HttpServletResponse response) { //flag
        Parametros param = new Parametros(dir);
        try {
            param.cnx.Conectar();
            logger = param.cnx.getLogger();
            String Accion = "----" + (String) request.getSession(false).getAttribute("usuario") + " Consultando modificar de series emitidas---";
            String[] indicesSerie = seleccionadosInput.split(",");
            SeriesEmitidas SE = new SeriesEmitidas();
            List<SeriesM> listaSeriesMC = new ArrayList<>();
            HttpSession session = request.getSession();

            session.removeAttribute("listaSeriesMC");
            listaSeriesMC = SE.DatosSerieESPECIFIC(param.cnx, ambiente, logger, Accion, indicesSerie, (List<SeriesM>) request.getSession(false).getAttribute("listaSeries"));
            session.setAttribute("listaSeriesMC", listaSeriesMC);
            response.sendRedirect("ModificarSE.jsp");
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

    private void ModificarTConsult(HttpServletRequest request, HttpServletResponse response) {
        Parametros param = new Parametros(dir);
        try {
            param.cnx.Conectar();
            logger = param.cnx.getLogger();
            String Accion = "----" + (String) request.getSession(false).getAttribute("usuario") + " Consultando modificar Tasa de series emitidas---";
            String[] indicesSerie = seleccionadosInput.split(",");
            SeriesEmitidas SE = new SeriesEmitidas();
            List<SeriesM> listaSeriesMC = new ArrayList<>();
            HttpSession session = request.getSession();

            session.removeAttribute("listaSeriesMC");
            listaSeriesMC = SE.DatosSerieESPECIFIC(Parametros.cnx, ambiente, logger, Accion, indicesSerie, (List<SeriesM>) request.getSession(false).getAttribute("listaSeries"));
            session.setAttribute("listaSeriesMC", listaSeriesMC);
            response.sendRedirect("ModificarTSE.jsp");
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

    private void Modificar(HttpServletRequest request, HttpServletResponse response) {
        SeriesEmitidas SE = new SeriesEmitidas();
        BigDecimal TBVt = new BigDecimal(TBV);
        Parametros param = new Parametros(dir);
        try {
            param.cnx.Conectar();
            logger = param.cnx.getLogger();
            String Accion = "----" + (String) request.getSession(false).getAttribute("usuario") + " Consultando nuevamente series emitidas luego modificar---";
            if (TBVt.intValue() <= 0) {
                SE.ModificarSE(param.cnx, logger, ambiente,
                        SER,
                        AIO,
                        SRSICD,
                        SRSCOD,
                        SRSFDE,
                        SRSTVR,
                        SRSAio,
                        SRSMNE,
                        SRSMON,
                        SRSMBN,
                        SRSBMP,
                        SRSFDV, (String) request.getSession(false).getAttribute("usuario"));
                getListaSeries(param.cnx, logger, ambiente, Accion, request, response);
            } else {
                logger.info("---No se ha podido modificar porque la serie ya esta vendida---", this.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName());
                msg(request, response, "No se puede mofifcar la serie porque esta ya fue vendida");
            }
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

    private void ModificarTasa(HttpServletRequest request, HttpServletResponse response) {
        SeriesEmitidas SE = new SeriesEmitidas();
       // BigDecimal TBVt = new BigDecimal(TBV);
        Parametros param = new Parametros(dir);
        try {
            param.cnx.Conectar();
            logger = param.cnx.getLogger();

//            if (TBVt.intValue() <= 0) {
                String Accion = "----" + (String) request.getSession(false).getAttribute("usuario") + " Consultando nuevamente series emitidas luego modificar---";
                boolean res = SE.ModificarTSE(param.cnx, logger, ambiente,
                        SER,
                        AIO,
                        SRSTMX,
                        SRSTMN,
                        SRSTLIB,
                        SRSTVR, (String) request.getSession(false).getAttribute("usuario"));
                if (res) {
                    getListaSeries(param.cnx, logger, ambiente, Accion, request, response);
                } else {
                    logger.info("----No se puede modificar la tasa porque esta no es variable---- ", this.getClass().getSimpleName(), new Object() {
                    }.getClass().getEnclosingMethod().getName());
                    msg(request, response, "No se puede modificar la tasa porque esta no es variable");

                }
//            } else {
//                logger.info("----No se puede modificar la tasa de la serie porque esta ya fue vendida---- ", this.getClass().getSimpleName(), new Object() {
//                }.getClass().getEnclosingMethod().getName());
//                msg(request, response, "No se puede modificar la tasa de la serie porque esta ya fue vendida");
//            }

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

    private void Eliminar(HttpServletRequest request, HttpServletResponse response) {
        Parametros param = new Parametros(dir);
        try {
            param.cnx.Conectar();
            logger = param.cnx.getLogger();
            String Accion = "----" + (String) request.getSession(false).getAttribute("usuario") + " Consultando nuevamente series emitidas luego de eliminar---";
            String[] indicesSerie = seleccionadosInput.split(",");
            SeriesEmitidas SE = new SeriesEmitidas();
            boolean res = SE.deleteSE(param.cnx, logger, ambiente, indicesSerie, (List<SeriesM>) request.getSession(false).getAttribute("listaSeries"), (String) request.getSession(false).getAttribute("usuario"));
            if (res) {
                getListaSeries(param.cnx, logger, ambiente, Accion, request, response);
            } else {
                logger.info("----No se puede eliminar la serie emitida porque ya fue vendida---- ", this.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName());
                msg(request, response, "No se puede borrar la serie porque esta ya fue vendida");
            }

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

    private void Imprimir(HttpServletRequest request, HttpServletResponse response) {
        Parametros param = new Parametros(dir);
        Impresion imp = new Impresion();

        try {
            param.cnx.Conectar();
            String path = getServletContext().getRealPath("/") + "archivos";
            String[] indicesSerie = seleccionadosInput.split(",");
            logger = param.cnx.getLogger();
            List<SeriesM> listaSerie = (List<SeriesM>) request.getSession(false).getAttribute("listaSeries");

            XWPFDocument workbook = imp.getMacrotitulo(param.cnx, logger, ambiente, path, indicesSerie, listaSerie, (String) request.getSession(false).getAttribute("usuario"));
            if (workbook == null) {
                logger.info("No se puede imprimir porque no existen formas numeradas para el año de la serie del certificado en custodia selecionados", this.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName());
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<script type=\"text/javascript\">");
                out.println("alert('No se puede imprimir porque no existen formas numeradas para el año de la serie selecionada');");
                out.println("</script>");
                RequestDispatcher rd = request.getRequestDispatcher("SeriesE.jsp");
                rd.include(request, response);

            } else {

                String horaTemp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh.mm.ss.SSS"));//Se toma la hora para el nombre al igual que la fecha
                String fechaTemp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")); // Se toma le fecha para colocarla en el nombre    
                String nombreArchivo = "\\Macrotitulo_Anverso_" + fechaTemp + "_" + horaTemp;
                response.setContentType("application/.zip");//Se le dice que tipo de archivo es (PDF, Word, PowerPoint etc.) en este caso un ZIP
                response.setHeader("Content-Disposition", "attachment; filename= " + nombreArchivo + ".zip"); // Le asigno el nombre que tendra el archivo
                ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());

                String filename = "\\Macrotitulo - Anverso.docx";

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

    private void ImprimirR(HttpServletRequest request, HttpServletResponse response) {
        Parametros param = new Parametros(dir);
        Impresion imp = new Impresion();

        try {
            param.cnx.Conectar();
            String path = getServletContext().getRealPath("/") + "archivos";
            String[] indicesSerie = seleccionadosInput.split(",");
            logger = param.cnx.getLogger();
            List<SeriesM> listaSerie = (List<SeriesM>) request.getSession(false).getAttribute("listaSeries");

            // XWPFDocument workbook = imp.getMacrotitulo(param.cnx, logger, ambiente, path, indicesSerie, listaSerie,(String)request.getSession(false).getAttribute("usuario"));
            XWPFDocument workbookR = imp.getReversoM(param.cnx, logger, ambiente, path, indicesSerie, listaSerie, (String) request.getSession(false).getAttribute("usuario"));
            if (workbookR == null) {
                logger.info("No se puede imprimir porque no existen formas numeradas para el año de la serie selecionados", this.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName());
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<script type=\"text/javascript\">");
                out.println("alert('No se puede imprimir porque no existen formas numeradas para el año de la serie selecionada');");
                out.println("</script>");
                RequestDispatcher rd = request.getRequestDispatcher("SeriesE.jsp");
                rd.include(request, response);

            } else {

                String horaTemp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh.mm.ss.SSS"));//Se toma la hora para el nombre al igual que la fecha
                String fechaTemp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")); // Se toma le fecha para colocarla en el nombre    
                String nombreArchivo = "\\Macrotitulo_Reverso_" + fechaTemp + "_" + horaTemp;
                response.setContentType("application/.zip");//Se le dice que tipo de archivo es (PDF, Word, PowerPoint etc.) en este caso un ZIP
                response.setHeader("Content-Disposition", "attachment; filename= " + nombreArchivo + ".zip"); // Le asigno el nombre que tendra el archivo
                ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());

                // String filename = "\\Macrotitulo - Anverso.docx";
                String filenameR = "\\Macrotitulo - Reverso.docx";
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

    private void msg(HttpServletRequest request, HttpServletResponse response, String mensaje) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println("alert('" + mensaje + "');");
        out.println("</script>");
        RequestDispatcher rd = request.getRequestDispatcher("SeriesE.jsp");
        rd.include(request, response);
        //  getListaSeries(request, response);
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
