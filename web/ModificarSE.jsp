<%-- 
    Document   : ModificarSE
    Created on : 04-23-2021, 10:38:33 AM
    Author     : aprivera
--%>
<%@page import="modelo.SeriesM"%>
<%@page import="java.util.List"%>
<%@ include file="Plantilla.jsp" %> 
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Modificar Series Emitidas</title>
    </head>
    <script>
        function evaluar() {
            window.location = "SeriesEmitidasSV?opcion=9";
        }
    </script>
    <body>
        <div class="row">
            <div class="col-md-10 col-md-offset-1 m-auto">
                <Center>
                    <font color="black">
                    <h1>MODIFICAR SERIE EMITIDA</h1>
                    </font>
                </Center>

            </div>

        </div>
        <div class="mb-3">
            <form action="SeriesEmitidasSV" method="post">
                <input type="hidden" id="OpcionH" value="1" name="OpcionH" />
                <input type="hidden" id="opcion"  value ="2" name="opcion" />

                <%
                    if (!(session.getAttribute("listaSeriesMC") == null)) {
                        List<SeriesM> listaSerMC;
                        listaSerMC = (List<SeriesM>) session.getAttribute("listaSeriesMC");

                        for (SeriesM d : listaSerMC) {
                %>
                <input type="hidden" id="SER" value="<%=d.getSRSCOD()%>" name="SER" />
                <input type="hidden" id="AIO"  value ="<%=String.format("%.0f", d.getSRSAIO())%>" name="AIO" />
                <input type="hidden" id="TBV"  value ="<%= d.getSRSTBV()%>" name="TBV" />
                <div class="row">
                    <div class="col-sm-4">
                        <div class="form-group">
                            <label for="inputAio">Año</label>
                            <input class="form-control" min="1970"  id="inputAio" step="1" pattern="\d+" value="<%=String.format("%.0f", d.getSRSAIO())%>" type="number" name="inputAio" required/>
                        </div>
                    </div>                
                    <div class="col-sm-4">
                        <div class="form-group">
                            <label for="inputSerie">Serie</label>
                            <input type="text" class="form-control" id="inputSerie" value="<%=d.getSRSCOD()%>" name="inputSerie"  maxlength="1" required>
                        </div>
                    </div>
                    <div class="col-sm-4">
                        <div class="form-group">
                            <label for="inputISIN">ISIN</label>
                            <input type="text" class="form-control" value="<%=d.getSRSICD()%>" id="inputISIN" name ="inputISIN"  required>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-4">
                        <div class="form-group">
                            <label for="inputMTM">Monto Total Del Macrotitulo</label>
                            <input class="form-control"   id="inputMTM" step="1" pattern="\d+" value="<%=d.getSRSMON()%>" type="number" name="inputMTM" required/>
                            <!--<input class="form-control"   value =" //d.getSRSMON()" type="text" name="inputMTM"/>-->
                        </div>
                    </div>
                    <label for="inputMCB">Monto Por cada Bono</label>
                    <input class="form-control"  step="0.01" pattern="\d+" value="<%=d.getSRSMBN()%>" type="number" name="inputMCB"/>
                    
                    <div class="col-sm-4">
                        <label for="inputMoneda">Moneda:</label>
                        <input type="text" class="form-control" id="inputMoneda"  value ="<%=d.getSRSMNE()%>" name ="inputMoneda"  required>
                    </div>
                </div>
                <label > </label>
                <label > </label>
                <label > </label>
                <label > </label>
                <label> </label>
                <label > </label>
                <label > </label>

                <div class="row">    
                    <div class="col-sm-4">
                        <div class="form-group">
                            <label for="inputBMI">Bonos Minimos Por Inversionista</label>
                            <input class="form-control" min="0" max="100000" id="inputBMI" step="1" pattern="\d+" value ="<%=d.getSRSBMP()%>"  type="number" name="inputBMI" required/>
                            <!--<input class="form-control"  value =" //d.getSRSBMP()"  type="text" name="inputBMI"/>-->

                        </div>
                    </div>
                </div>
                <div class="row">    

                    <div class="col-sm-4">
                        <div class="form-group">
                            <label for="datepicker2">Fecha de vencimiento</label>
                            <input type="text"  class="form-control" id="datepicker2" value="<%=d.getSRSFDV()%>" maxlength = "10" name="datepicker2" required>
                        </div>
                    </div>
                    <div class="col-sm-4">
                        <div class="form-group">
                            <label for="datepicker">Fecha de emisión</label>
                            <input type="text" class="form-control" id="datepicker" value="<%=d.getSRSFDE()%>" maxlength = "10" name="datepicker"  required>
                        </div>
                    </div>
                    <div class="col-sm-4">
                        <div class="form-check">
                            <label class="form-check-label">
                                <input class="form-check-input" type="checkbox" value="S" name="inputTV" >
                                Tasa variable
                            </label>

                        </div>
                        <div class="form-group">

                        </div>
                    </div>
                </div>

                <label > </label>
                <label > </label>
                <label > </label>
                <div class="row">
                    <center>
                        <div class="col-sm-4">  
                            <input type="image" src="imagenes/Modificar.jpeg"  onclick= "return confirm('¿Desea modificar la serie?')" name="submit" width="100"  alt="submit"/>  
                            <!--<input type="image" src="imagenes/Salir.bmp" onclick= "evaluar();" name="submit2" width="100"     alt="submit2"/>--> 


                        </div>
                    </center>
                </div>

                <center>
                    <label > </label>
                </center>
                <%

                        }
                    }
                %>
            </form>

            <div class="row">
                <center>
                    <div class="col-sm-4">  
                        <input type="image" src="imagenes/Salir.bmp" onclick= "evaluar();" name="submit2" width="100"     alt="submit2"/> 
                    </div>
                </center>
            </div>
        </div>
    </body>
</html>
