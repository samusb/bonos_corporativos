<%-- 
    Document   : AgregarCC
    Created on : 04-26-2021, 04:39:06 PM
    Author     : aprivera
--%>

<%@page import="modelo.SeriesM"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="Plantilla.jsp" %> 
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Vender Certificado en custodia</title>
    </head>
    <script>
        function evaluar() {
            var SER = document.getElementById("inputSerie").value;
            var AIO = document.getElementById("inputAio").value;

            window.location = "CertCustodiaSV?opcion=11&inputSerie=" + SER + "&inputAio=" + AIO;
        }
    </script>
    <body>
        <%String iden = request.getParameter("iden");%>

        <label> </label>

        <div class="row">
            <div class="col-md-10 col-md-offset-1 m-auto">
                <Center>
                    <font color="black">
                    <h1>AGREGAR CERT. EN CUST.</h1>
                    </font>
                </Center>                

                <!--                <div class="contact-form">
                
                                    <Center>
                                        <form action='AgregarCC.jsp' id='search' class="navbar-form form-inline search-form">
                                            <label for="inputName">IDENTIDAD</label>
                                            <div class="input-group">
                                                <input type="text" class="form-control"  id="ID" name="iden" placeholder="Busqueda..." value="<%=(iden != null ? iden : "")%>"required>
                                                <span class="input-group-btn" >
                                                    <button class="btn btn-outline-secondary" type="submit">Buscar</button> </span>
                                            </div>
                                        </form> 
                                    </Center>
                
                
                                </div>
                            </div>
                        </div>-->

                <div class="row">
                    <form action="CertCustodiaSV" method="post">
                        <%String TBV = (String) session.getAttribute("SRSTBV");
                            String AIO = (String) session.getAttribute("inputAio");
                            String SER = (String) session.getAttribute("inputSerie");
                            String BMP = (String) session.getAttribute("SRSBMP");
                            String CCC = (String) session.getAttribute("SRSCCC");
                            String TBC = (String) session.getAttribute("SRSTBC");
                        %>
                        <input type="hidden" id="opcion"  value ="3" name="opcion" />
                        <input type="hidden" id="inputTBV"  value ="<%=TBV%>" name="inputTBV" />
                        <input type="hidden" id="CCC"  value ="<%=CCC%>" name="CCC" />
                        <input type="hidden" id="TBC"  value ="<%=TBC%>" name="TBC" />
                        <input type="hidden" id="BMP"  value ="<%=BMP%>" name="BMP" />
                        <div class="row">
                            <div class="col-sm-4">

                                <div class="form-group">
                                    <label for="inputAio">Año</label>
                                    <input class="form-control" min="1970" max="100000" id="inputAio" step="1" pattern="\d+" value="<%=AIO%>" type="number" name="inputAio" readonly="">
                                </div>

                            </div>                

                            <div class="col-sm-4">

                                <div class="form-group">
                                    <label for="inputSerie">Serie</label>
                                    <input type="text" class="form-control"  id="inputSerie" name="inputSerie"  maxlength="1" value="<%=SER%>" readonly="">
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <Center>
                                <label for="inputName">DATOS DEL CLIENTE:</label>
                            </Center>
                            <div class="col-sm-4">

                                <div class="form-group">

                                    <label for="inputNombre">Nombre</label>
                                    <input type="text" class="form-control" id="inputNombre" name="inputNombre"  required>
                                </div>
                            </div>                

                            <div class="col-sm-4">

                                <div class="form-group">
                                    <label for="inputIden">Identidad</label>
                                    <input type="text" class="form-control" id="inputIden" name="inputIden"  required>
                                </div>
                            </div>
                            <div class="col-sm-4">

                                <div class="form-group">
                                    <label for="inputNoBono">No. de Bono</label>
                                    <input type="number" class="form-control" min="<%=BMP%>" value="<%=BMP%>" id="inputNoBono" name ="inputNoBono" >
                                </div>
                            </div>
                        </div> 
                        </div>
                        <div class="row">
                            <center>
                                <div class="col-sm-4">  
                                    <!--                     <input type="image" src="imagenes/Aceptar.bmp" onclick= "return confirm('¿Confirmar Ingreso de la Serie?')" name="submit" width="2"  alt="submit"/> -->
                                    <button type="submit" class="btn btn-primary" onclick= "return confirm('¿Confirmar venta de certificado de custodia?')">ACEPTAR</button>
                                    <button type="button" class="btn btn-primary" onclick= "evaluar();">VOLVER</button>

                                </div>
                            </center>
                        </div>

                    </form>


                </div>

                </body>
                </html>
