<%-- 
    Document   : AgregarSE
    Created on : 04-23-2021, 02:41:41 PM
    Author     : aprivera
--%>

<%@page import="java.time.LocalDate"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="Plantilla.jsp" %> 
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Agregar Series Emtidas</title>
    </head>
    <script>
        function evaluar() {
            window.location = "SeriesEmitidasSV?opcion=9";
        }
    </script>
    <body>
        <div class="container">

            <div class="row">
                <div class="col-md-10 col-md-offset-1 m-auto">
                    <Center>
                        <font color="black">
                        <h1>AGREGAR SERIES</h1>
                        </font>
                    </Center>

                </div>

            </div>

            <div class='row'>
                <label> </label>

                <form action="SeriesEmitidasSV" method="post">
                    <input type="hidden" id="OpcionH" value="1" name="OpcionH" />
                    <input type="hidden" id="opcion"  value ="4" name="opcion" />
                    <center>
                        <label>INGRESO DE SERIES:</label>
                    </center>
                    <div class="row">

                        <div class="col-sm-4">
                            <%
                                LocalDate temp = LocalDate.now();
                                int FechaToday= temp.getYear();
                                %>
                            <div class="form-group">
                                <label for="inputAio">Año</label>
                                <input class="form-control" min="1970"  id="inputAio" step="1" pattern="\d+" value="<%=FechaToday%>" type="number" name="inputAio"/>
                            </div>
                        </div>                

                        <div class="col-sm-4">

                            <div class="form-group">
                                <label for="inputSerie">Serie</label>
                                <input type="text" class="form-control" id="inputSerie" name="inputSerie"  maxlength="1" required>
                            </div>
                        </div>
                        <div class="col-sm-4">

                            <div class="form-group">
                                <label for="inputISIN">ISIN</label>
                                <input type="text" class="form-control" id="inputISIN" name ="inputISIN" maxlength="15" required >
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="datepicker">Fecha de emisión </label>
                        <p><input type="date" class="form-control" name="datepicker" id="datepicker" autocomplete="off"></p>
                    </div>

                    <div class="form-group">
                        <label for="inputPlazo">Plazo en dias</label>
                        <input class="form-control" min="1080"  step="1" pattern="\d+" value="1080" type="number" name="inputPlazo"/>


                    </div>

                    <label for="inputMoneda"> Moneda: </label>
                    <select class="form-control custom-select" name='inputMoneda'>
                        <option selected>-------</option>
                        <option value="0">Lempiras</option>
                        <option value="1">Extranjera</option>

                    </select>
                    <label > </label>
                    <label > </label>
                    <label > </label>
                    <label > </label>
                    <label > </label>

                    <div class="form-group">
                        <label for="inputMTM">Monto Total Del Macrotitulo</label>
                        <input class="form-control"  step="0.01" pattern="\d+" value="0.00" type="number" name="inputMTM"/>

                        <label for="inputMCB">Monto Por cada Bono</label>
                        <input class="form-control"  step="0.01" pattern="\d+" value="0.00" type="number" name="inputMCB"/>

                        <label for="inputBMI">Bonos Minimos Por Inversionista</label>
                        <input class="form-control" step="1" pattern="\d+" value="0" type="number" name="inputBMI"/>

                    </div>  
                    <label > </label>
                    <label > </label>
                    <label > </label>
                    <label > </label>
                    <label> </label>
                    <label > </label>
                    <label > </label>
                    <center>
                        <label >INGRESO DE TASA:</label>
                    </center>
                    <div class="row">

                        <div class="col-sm-4">

                            <div class="form-group">
                                <label for="inputTM">Tasa Maxima</label>
                                <input class="form-control"  step="0.01" pattern="\d+" value="0.00" type="number" name="inputTM"/>
                            </div>
                        </div>                

                        <div class="col-sm-4">

                            <div class="form-group">
                                <label for="inputTMin">Tasa Minima</label>
                                <input class="form-control"  step="0.01" pattern="\d+" value="0.00" type="number" name="inputTMin"/>
                            </div>
                        </div>

                    </div>
                    <div class="row">

                        <div class="col-sm-4">

                            <div class="form-group">
                                <label for="inputLIB">Tasa Libor</label>
                                <input class="form-control"  step="0.01" pattern="\d+" value="0.00" type="number" name="inputLIB"/>
                            </div>
                        </div>                

<!--                        <div class="col-sm-4">

                            <div class="form-group">
                                <label for="inputDif">Diferencial</label>
                                <input class="form-control" min="0.01" max="10000000000000"  step="0.01" pattern="\d+" value="0.00" type="number" name="inputDif"/>
                            </div>
                        </div>-->
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
                    <input type="image" src="imagenes/Aceptar.bmp" title="Agregar Serie Emitida" onclick= "return confirm('¿Confirmar Ingreso de la Serie?')" name="submit" width="100"  alt="submit"/> 
                     <input type="image" src="imagenes/Salir.bmp" title="Volver" onclick= "evaluar();" name="submit2" width="100"     alt="submit2"/> 


                </form>


            </div>

        </div>
    </body>
</html>
