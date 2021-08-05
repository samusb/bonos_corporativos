<%-- 
    Document   : ModificarTSE
    Created on : 04-23-2021, 05:08:47 PM
    Author     : aprivera
--%>
<%@page import="java.util.List"%>
<%@page import="modelo.SeriesM"%>
<%@ include file="Plantilla.jsp" %> 
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Modificar Tasa</title>
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
                        <h1>MODIFICAR TASA DE SERIE EMITIDA</h1>
                        </font>
                    </Center>

                </div>

            </div>
        <form action="SeriesEmitidasSV" method="post">
        <input type="hidden" id="OpcionH" value="1" name="OpcionH" />
        <input type="hidden" id="opcion"  value ="6" name="opcion" />
            <%
                if (!(session.getAttribute("listaSeriesMC") == null)) {
                    List<SeriesM> listaSerMC;
                    listaSerMC = (List<SeriesM>) session.getAttribute("listaSeriesMC");

                    for (SeriesM d : listaSerMC) {
            %>
            <input type="hidden" id="SER" value="<%=d.getSRSCOD()%>" name="SER" />
            <input type="hidden" id="AIO"  value ="<%=String.format("%.0f", d.getSRSAIO())%>" name="AIO" />
            <input type="hidden" name="TVR" value="<%=d.getSRSTVR()%>">
            <input type="hidden" id="TBV"  value ="<%= d.getSRSTBV()%>" name="TBV" />
            <font color="black">
            <center><h2></h2></center>
            </font>
            <font color="black">
            <center><h2></h2></center>
            </font>
            <font color="black">
            <h2>TASA:</h2>
            </font>
            <div class="row">
                <div class="row">

                    <div class="col-sm-4">

                        <div class="form-group">
                            <label for="inputTM">Tasa Maxima</label>
                            <input class="form-control"  value="<%=d.getSRSTMX()%>" type="number" name="inputTM" required/>
                        </div>
                    </div>                

                    <div class="col-sm-4">

                        <div class="form-group">
                            <label for="inputTMin">Tasa Minima</label>
                            <input class="form-control"  value="<%=d.getSRSTMN()%>" type="number" name="inputTMin" required/>
                        </div>
                    </div>

                </div>
                <div class="row">

                    <div class="col-sm-4">

                        <div class="form-group">
                            <label for="inputLIB">Tasa Libor</label>
                            <input class="form-control"  value="<%=d.getSRSTLIB()%>" type="number" name="inputLIB" required/>
                        </div>
                    </div>                

                    <div class="col-sm-4">

                        <div class="form-group">
                            <label for="inputDif">Diferencial</label>
                            <input class="form-control"  value="<%=d.getSRSDIF()%>" type="number" name="inputDif" readonly=""/>
                        </div>
                    </div>


                </div>
                <center>
                    <label > </label>
                </center>

                <div class="row">
                    <center>
                        <div class="col-sm-4">  
                            <input type="image" src="imagenes/modificarTasa.JPG" onclick= "return confirm('¿Desea modificar la tasa de la serie?')" name="submit" width="100"  alt="submit"/>  
                         <!--<input type="image" src="imagenes/Salir.bmp" onclick= "evaluar();" name="submit2" width="100"     alt="submit2"/>--> 
</div>
                    </center>
                </div>
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

    </body>
</html>
