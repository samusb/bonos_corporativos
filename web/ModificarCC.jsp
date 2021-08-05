<%-- 
    Document   : ModificarCC
    Created on : 04-28-2021, 02:27:06 PM
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
        <title>Modificar </title>
    </head>
    <script>
        function evaluar() {
            var SER = document.getElementById("inputSerie").value;
            var AIO = document.getElementById("inputAio").value;

            window.location = "CertCustodiaSV?opcion=11&inputSerie=" + SER + "&inputAio=" + AIO;
        }
    </script>
    <body>
        

        <label> </label>

        <div class="row">
            <div class="col-md-10 col-md-offset-1 m-auto">
                <Center>
                    <font color="black">
                    <h1>MODIFICAR CERT. EN CUST.</h1>
                    </font>
                </Center>                
               
               
                <div class="row">
                    <form action="CertCustodiaSV" method="post">  
                        <input type="hidden" id="opcion"  value ="5" name="opcion" />
                         <%
                           
//                                    
                                if (!(session.getAttribute("listaMCC") == null)) {
                                    List<SeriesM> listaCC;
                                    listaCC = (List<SeriesM>) session.getAttribute("listaMCC");

                                    for (SeriesM c : listaCC) {
                            %>

                        <div class="row">
                            <div class="col-sm-4">

                                <div class="form-group">
                                    <label for="inputAio">Año</label>
                                    <input class="form-control" min="1970" max="100000" id="inputAio" step="1" pattern="\d+" value="<%=String.format("%.0f", c.getCTCAIO())%>" type="number" name="inputAio" readonly=""/>
                                </div>

                            </div>                

                            <div class="col-sm-4">

                                <div class="form-group">
                                    <label for="inputSerie">Serie</label>
                                    <input type="text" class="form-control"  id="inputSerie" name="inputSerie"  maxlength="1" value="<%=c.getCTCCOD()%>" readonly=""d>
                                </div>
                            </div>

                        </div>

                        </select>
                        <label > </label>
                        <label > </label>
                        <label > </label>
                        <label > </label>
                        <label > </label>

                        <div class="row">
                            <Center>
                                <label for="inputName">DATOS DEL CLIENTE:</label>
                            </Center>
                            <div class="col-sm-4">

                                <div class="form-group">

                                    <label for="inputNombre">Nombre</label>
                                    <input type="text" class="form-control" id="inputNombre" name="inputNombre" value="<%=c.getCTCNOM()%>" readonly="">
                                </div>
                            </div>                

                            <div class="col-sm-4">

                                <div class="form-group">
                                    <label for="inputIden">Identidad</label>
                                    <input type="text" class="form-control" id="inputIden" name="inputIden"  value="<%=c.getCTCIDN()%>" readonly="">
                                </div>
                            </div>
                            <div class="col-sm-4">

                                <div class="form-group">
                                    <label for="inputNoBono">No. de Bono</label>
                                    <input type="text" class="form-control" id="inputNoBono" name ="inputNoBono" value="<%=c.getCTCCCD()%>" readonly="">
                                </div>
                            </div>
                        </div>
                        <label > </label>
                        <label > </label>
                        <label > </label>
                        <label > </label>
                        <label> </label>
                        <label > </label>
                        <label > </label>
                        <center>

                        </center>
                        <div class="row">


                            <label for="inputFPA"> F/P Inversionista: </label>
                            <select class="form-control custom-select" name='inputFPA'>
                                <option selected><%=c.getCTCFPA()%></option>
                                <option value="CREDITO A CUENTA">CREDITO A CUENTA</option>
                                <option value="PAGO CHEQUE DE CAJA">PAGO CHEQUE DE CAJA</option>
                                <option value="TRANSFERENCIA">TRANSFERENCIA</option>
                            </select>

                            <label for="inputFPI"> F/P Intereses: </label>
                            <select class="form-control custom-select" name='inputFPI'>
                                <option selected><%=c.getCTCFPI()%></option>
                                <option value="CREDITO A CUENTA">CREDITO A CUENTA</option>
                                <option value="PAGO CHEQUE DE CAJA">PAGO CHEQUE DE CAJA</option>
                                <option value="TRANSFERENCIA">TRANSFERENCIA</option>

                            </select>

                        </div>

                        <div class="col-sm-4">

                            <div class="form-group">
                                <label for="inputACC">En caso de credito la cuenta sera:</label>
                                <input class="form-control" min="0"  id="inputAio" step="1" pattern="\d+" value="<%=c.getCTCACC()%>" type="number" name="inputACC"/>
                            </div>
                        </div>

                        <!--                <table class="table table-bordered"  id="tables">
                                            <Center>
                                    <label for="inputName">DETALLES DE CUENTAS:</label>
                                            </Center>
                                            <tr  id="first-tr">
                        
                                                <th>CUENTA</th>
                                                <th>TIPO</th>
                                                <th>MONEDA</th>
                                                <th>DESCUENTO</th>
                                                <th>ELEGIR</th>
                                            </tr>
                        
                                            <tbody>
                                            <tr>
                        
                                                <td>N/A</td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td>
                                                    <input type="radio" name="cuenta" value="" required/>
                                                </td>
                                            </tr>
                                            
                                            </tbody>
                                        </table>-->

                        <div class="row">

                            <div class="col-sm-4">

                                <div class="form-group">
                                    <label for="inputImp">Impuesto</label>
                                    <input class="form-control" min="0.00" max="10000000000000" value="<%=c.getCTCIMP()%>" step="0.01" pattern="\d+" value="0.00" type="number" name="inputImp"/>
                                </div>
                            </div>                
                            <div class="col-sm-4">
                                <div class="form-check">
                                    <label class="form-check-label">
                                        <input class="form-check-input" type="checkbox" value="S" name="inputEXC" >
                                        Extento
                                    </label>

                                </div>
                                <div class="form-group">

                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <center>
                                <div class="col-sm-4">  
                                    <!--                     <input type="image" src="imagenes/Aceptar.bmp" onclick= "return confirm('¿Confirmar Ingreso de la Serie?')" name="submit" width="2"  alt="submit"/> -->
                                    <button type="submit" class="btn btn-primary" onclick= "return confirm('¿Confirmar la modificacion de certificado de custodia?')">ACEPTAR</button>
                                       <button type="button" class="btn btn-primary" onclick= "evaluar();">VOLVER</button>

                                </div>
                            </center>
                        </div>
                        <%
                                     
                                    }
                                }
                            %>
                    </form>


                </div>

                </body>
                </html>
