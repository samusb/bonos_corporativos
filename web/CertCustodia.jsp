<%-- 
    Document   : CertCustodia
    Created on : 04-26-2021, 12:32:56 PM
    Author     : aprivera
--%>

<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.List"%>
<%@page import="modelo.SeriesM"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="Plantilla.jsp" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Certificado en custodia</title>
        <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto|Varela+Round">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
        <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
        <link rel="stylesheet"  href="http://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css"></style>
    <script type="text/javascript" src="http://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>

</head>
<script>
    $(document).ready(function () {
        $('#myTable').DataTable();
        $('.dataTables_length').addClass('bs-select');
    });
</script>
<script>

    var seleccionados = [];

    function evaluar(codigo) {
        switch (codigo)
        {

            case 0://Agregar
                var TBV = document.getElementById("inputTBV").value;
                var SER = document.getElementById("inputSerie").value;
                var AIO = document.getElementById("inputAio").value;
                var BMP = document.getElementById("BMP").value;
                var CCC = document.getElementById("CCC").value;
                var TBC = document.getElementById("TBC").value;
                window.location = "CertCustodiaSV?opcion=2&inputTBV=" + TBV + "&CCC=" + CCC + "&TBC=" + TBC + "&BMP=" + BMP + "&inputSerie=" + SER + "&inputAio=" + AIO;
                break;
            case 1://Modificar certificado
//                    if (seleccionados.length >= 1) {
//                        if (seleccionados.length < 2) {
//
//                            var listaSeleccionada = document.getElementById('seleccionadosInput').value;
//                            window.location = "CertCustodiaSV?opcion=4&seleccionadosInput=" + listaSeleccionada;
//                        } else {
//                            alert('Favor no seleccionar mas de una sola serie');
//                        }
//                    } else {
//                        alert('Favor seleccionar una serie');
//                    }
//                    break;
            case 2://Modificar Tenedor
                if (seleccionados.length >= 1) {
                    if (seleccionados.length < 2) {

                        var listaSeleccionada = document.getElementById('seleccionadosInput').value;
                        window.location = "CertCustodiaSV?opcion=6&seleccionadosInput=" + listaSeleccionada;
                    } else {
                        alert('Favor no seleccionar mas de una sola serie');
                    }
                } else {
                    alert('Favor seleccionar una serie');
                }
                break;
            case 3://Eliminar CC, 
//                    if (seleccionados.length >= 1) {
//                        if (seleccionados.length < 2) {
//                            if (confirm("¿Esta seguro de eliminar el tenedor?")) {
//                                var SER = document.getElementById("inputSerie").value;
//                                var AIO = document.getElementById("inputAio").value;
//                                var listaSeleccionada = document.getElementById('seleccionadosInput').value;
//                                window.location = "CertCustodiaSV?opcion=8&seleccionadosInput=" + listaSeleccionada+"&inputSerie=" + SER + "&inputAio=" + AIO;
//                            }
//                        } else {
//                            alert('Favor no seleccionar mas de una sola serie');
//                        }
//                    } else {
//                        alert('Favor seleccionar una serie');
//                    }
                break;
            case 4: //Regresar a Series Emitidas
                window.location = "CertCustodiaSV?opcion=9";
                break;
            case 5: //Imprimir
                if (seleccionados.length >= 1) {
                    if (seleccionados.length < 2) {
                        if (confirm("¿Esta seguro de imprimir el certificado custodia?")) {
                            var listaSeleccionada = document.getElementById('seleccionadosInput').value;
                            window.location = "CertCustodiaSV?opcion=10&seleccionadosInput=" + listaSeleccionada;
                        }
                    } else {
                        alert('Favor no seleccionar mas de una sola serie');
                    }
                } else {
                    alert('Favor seleccionar una serie');
                }
                break;
                
                 case 6: //Imprimir Reverso
                if (seleccionados.length >= 1) {
                    if (seleccionados.length < 2) {
                        if (confirm("¿Esta seguro de imprimir el reverso del certificado en custodia?")) {
                            var listaSeleccionada = document.getElementById('seleccionadosInput').value;
                            window.location = "CertCustodiaSV?opcion=12&seleccionadosInput=" + listaSeleccionada;
                        }
                    } else {
                        alert('Favor no seleccionar mas de una sola serie');
                    }
                } else {
                    alert('Favor seleccionar una serie');
                }
                break;

        }
    }
    function agregarSeleccionado(idElemento_) {
        var valor_ = document.getElementById(idElemento_).value;

        if (document.getElementById(idElemento_).checked)
        {
            //alert('Entro a checked: ' + valor_.toString());
            seleccionados.push(valor_);
        } else
        {
            // alert('Entro a NO checked: ' + valor_.toString());
            var tem = seleccionados.indexOf(valor_);
            seleccionados.splice(tem, 1);
        }

        seleccionados.sort(function (a, b) {
            return a - b;
        });    //<-- Ordena ascendente

        $("#seleccionadosInput").val(seleccionados);//para que el controlador reciba el array con los indices que deben procesarse.

    }

</script>

<body>
    <%            BigDecimal SMBN = new BigDecimal(0);
        if (!(session.getAttribute("listaSerieAIO") == null)) {
            List<SeriesM> listaSerAIO;
            listaSerAIO = (List<SeriesM>) session.getAttribute("listaSerieAIO");

            for (SeriesM s : listaSerAIO) {
    %>
    <div class="row">
        <div class="col-sm-4">
            <div class="form-group">
                <label for="inputSerie">Serie</label>
                <input type="text" class="form-control" id="inputSerie" name="inputSerie" value="<%=s.getSRSCOD()%>" maxlength="1" readonly="">
            </div>
        </div>
        <div class="col-sm-4">
            <div class="form-group">
                <label for="inputAio">Año</label>
                <input class="form-control" min="1970" max="100000" id="inputAio" step="1" pattern="\d+" value="<%=String.format("%.0f", s.getSRSAIO())%>" type="number" name="inputAio" readonly=""/>
            </div>
        </div>
        <input type="hidden" class="form-control" id="inputTBV" name="inputTBV" value="<%=s.getSRSTBV()%>" readonly="">
        <input type="hidden" class="form-control" id="BMP" name="BMP" value="<%=s.getSRSBMP()%>"  readonly="">
        <input type="hidden" class="form-control" id="CCC" name="CCC" value="<%=s.getSRSCCC()%>"  readonly="">
        <input type="hidden" class="form-control" id="TBC" name="TBC" value="<%=s.getSRSTBC()%>"  readonly="">
        <input type="hidden" class="form-control" id="MBN" name="MBN" value="<%=s.getSRSMBN()%>"  readonly="">
    </div>
    <%
                SMBN = s.getSRSMBN();
            }
        }
    %>
    <div class="container-xl">
        <div class="table-responsive">
            <div class="table-wrapper">
                <div class="table-title">



                    <div class="clearfix">
                        <center>
                            <h1>CERTIFICADOS  EN CUSTODIA</h1>

                            <!--<a href="#" onclick="evaluar(0);" class="btn btn-secondary " ><i title="Vender Cert. en Cust." class="material-icons">&#xE15C;</i><span>Vender Cert. en Cust.</span></a>-->
                            <!--<a href="#" onclick="evaluar(5);" class="btn btn-secondary" ><i title="Imprimir" class="material-icons">&#xE15C;</i><span>Imprimir</span></a>-->
                            <!--<a href="#" onclick="evaluar(2);" class="btn btn-warning"><i title="Modificar Tenedor" class="material-icons">&#xE254;</i><span>Modificar Tenedor</span></a>-->
                            <!--<a href="#" onclick="evaluar(1);" class="btn btn-warning"><i title="Modificar Cert. en Cust." class="material-icons">&#xE254;</i><span>Modificar Cert. en Cust.</span></a>-->
                            <!--<a href="#" onclick="evaluar(3);" class="btn btn-danger" ><i title="Eliminar" class="material-icons">&#xE15C;</i><span>Eliminar</span></a>-->
                            <!--<a href="#" onclick="evaluar(4);" class="btn btn-danger" ><i title="Series Emitidas" class="material-icons">&#xE15C;</i><span>Series Emitidas</span></a>-->

                             <input type="image" src="imagenes/document.png" title="Imprimir Reverso" onclick= "evaluar(6);"width="50"/> 

                            <input type="image" src="imagenes/Impresora.bmp" title="Imprimir" onclick= "evaluar(5);"width="50"/> 
                            <input type="image" src="imagenes/Modificar.jpeg" title="Modificar Tenedor" onclick= "evaluar(2);"width="50"/> 
                            <!--<input type="image" src="imagenes/Modificar.jpeg" title="Modificar cert." onclick= "evaluar(1);"width="50"/>--> 
                            <!--<input type="image" src="imagenes/Eliminar.bmp" title="Eliminar cert." onclick= "evaluar(3);"width="50"/>--> 

                            <input type="image" src="imagenes/Agregar.bmp" title="Series Emitidas" onclick= "evaluar(4);"width="50"/> 
                            <input type="image" src="imagenes/Vender.jpg" title=" Vender Certificado en custodia" onclick= "evaluar(0);"width="50"/> 



                        </center>
                    </div>




                </div>
                <!--<input class="form-control" id="myInput" type="text" placeholder="Search..">-->
                <table id="myTable" class="table table-striped">
                    <input type="hidden" id="seleccionadosInput" name="seleccionadosInput" />
                    <thead>
                        <tr>
                            <th>Cert.</th>
                            <th>Inversionista</th>
                            <th>Vendidos</th>
                            <th>Bono Inicial</th>
                            <th>Bono Final</th>
                            <th>Inversión</th>
                            <th>Fecha de venta</th>
                            <th>Accion</th>

                        </tr>
                    </thead>
                    <tbody >
                        <%
                            Integer fila = 0;
//                                    
                            if (!(session.getAttribute("listaCC") == null)) {
                                List<SeriesM> listaCC;
                                listaCC = (List<SeriesM>) session.getAttribute("listaCC");

                                for (SeriesM c : listaCC) {
                                    BigDecimal Vendidos = new BigDecimal(0);
                                    BigDecimal Inver = new BigDecimal(0);
                                    Vendidos = new BigDecimal((c.getCTCBNF().doubleValue() - c.getCTCBNI().doubleValue()) + 1);
                                    Inver = new BigDecimal(c.getCTCNMB().doubleValue() * SMBN.doubleValue());
                                    
                        %>

                        <tr>
                            <td><%=c.getCTCCCD()%></td>
                            <td><%=c.getCTCNOM()%></td>
                            <td><%=Vendidos%></td>
                            <td><%=String.format("%.0f", c.getCTCBNI())%></td> 
                            <td><%=String.format("%.0f", c.getCTCBNF())%></td>
                            <td><%=Inver%></td>
                            <td><%=c.getCTCFVT()%></td>
                            <td>
                                <input type="checkbox" id="checkbox_<%= fila%>" name="checkbox_<%= fila%>" value="<%= listaCC.indexOf(c)%>" onchange="agregarSeleccionado('checkbox_<%= fila%>');"/>
                            </td>
                        </tr>
                        <%
                                    fila++;
                                }
                            }
                        %>
                    </tbody>
                </table>



            </div>
        </div> 

    </div>              


</body>
</html>
