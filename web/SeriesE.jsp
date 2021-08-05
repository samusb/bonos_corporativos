<%-- 
    Document   : parametros
    Created on : 04-21-2021, 02:42:16 PM
    Author     : aprivera
--%>

<%@page import="principal.FormasNumeradas"%>
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

        <title>JSP Page</title>
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

            case 0://Eliminar, se envia lo mismo que modificar al final 

                if (seleccionados.length >= 1) {
                    if (seleccionados.length < 2) {
                        if (confirm("¿Esta seguro de eliminar la Serie?")) {
                            var listaSeleccionada = document.getElementById('seleccionadosInput').value;

                            window.location = "SeriesEmitidasSV?opcion=" + codigo + "&OpcionH=1&seleccionadosInput=" + listaSeleccionada;
                        }
                    } else {
                        alert('Favor no seleccionar mas de una sola serie');
                    }
                } else {
                    alert('Favor seleccionar una serie');
                }
                break;
            case 1://Modificar
                if (seleccionados.length >= 1) {
                    if (seleccionados.length < 2) {

                        var listaSeleccionada = document.getElementById('seleccionadosInput').value;
                        window.location = "SeriesEmitidasSV?opcion=" + codigo + "&OpcionH=1&seleccionadosInput=" + listaSeleccionada;
                    } else {
                        alert('Favor no seleccionar mas de una sola serie');
                    }
                } else {
                    alert('Favor seleccionar una serie');
                }
                break;
            case 2://Agregar
                window.location = "SeriesEmitidasSV?opcion=3&OpcionH=1";
                break;
            case 3://Modificar Tasa
                if (seleccionados.length >= 1) {
                    if (seleccionados.length < 2) {

                        var listaSeleccionada = document.getElementById('seleccionadosInput').value;
                        window.location = "SeriesEmitidasSV?opcion=5&OpcionH=1&seleccionadosInput=" + listaSeleccionada;
                    } else {
                        alert('Favor no seleccionar mas de una sola serie');
                    }
                } else {
                    alert('Favor seleccionar una serie');
                }
                break;
            case 4://ceritificados en custodia (venta)
                if (seleccionados.length >= 1) {
                    if (seleccionados.length < 2) {

                        var listaSeleccionada = document.getElementById('seleccionadosInput').value;
                        window.location = "CertCustodiaSV?opcion=1&seleccionadosInput=" + listaSeleccionada;
                    } else {
                        alert('Favor no seleccionar mas de una sola serie');
                    }
                } else {
                    alert('Favor seleccionar una serie');
                }
                break;
            case 5://Imprimir serie
                if (seleccionados.length >= 1) {
                    if (seleccionados.length < 2) {
                        if (confirm("¿Esta seguro de imprimir la Serie?")) {
                            var listaSeleccionada = document.getElementById('seleccionadosInput').value;
                            window.location = "SeriesEmitidasSV?opcion=7&OpcionH=1&seleccionadosInput=" + listaSeleccionada;
                        }
                    } else {
                        alert('Favor no seleccionar mas de una sola serie');
                    }
                } else {
                    alert('Favor seleccionar una serie');
                }
                break;
            case 6://Formas numeradas
                window.location = "FormNumSV?opcion=0";

                break;
            case 7://Definicion de emisiones
                window.location = "SeriesEmitidasSV?opcion=8";

                break;
            case 8://Imprimir serie Reverso
                if (seleccionados.length >= 1) {
                    if (seleccionados.length < 2) {
                        if (confirm("¿Esta seguro de imprimir el reverso de la serie?")) {
                            var listaSeleccionada = document.getElementById('seleccionadosInput').value;
                            window.location = "SeriesEmitidasSV?opcion=10&OpcionH=1&seleccionadosInput=" + listaSeleccionada;
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
<script>
    function openForm() {
        document.getElementById("myForm").style.display = "block";
    }

    function closeForm() {
        document.getElementById("myForm").style.display = "none";
    }





</script>
<body>
    <div class="container-xl">
        <div class="table-responsive">
            <div class="table-wrapper">
                <div class="table-title">



                    <div class="clearfix">
                        <center>
                            <h1>SERIES EMITIDAS</h1>

                            <!--<a href="#" onclick="evaluar(19);" class="btn btn-secondary " ><i title="Certificado en custodia" class="material-icons">&#xE15C;</i><span>Forma Num</span></a>-->
                            <!--<a href="#" onclick="evaluar(5);" class="btn btn-secondary" ><i title="Imprimir" class="material-icons">&#xE15C;</i><span>Imprimir</span></a>-->
                            <!--<a href="#" onclick="evaluar(3);" class="btn btn-warning"><i title="Modificar Tasa" class="material-icons">&#xE254;</i><span>Modificar TASA</span></a>-->
                            <!--<a href="#" onclick="evaluar(1);" class="btn btn-warning"><i title="Modificar" class="material-icons">&#xE254;</i><span>Modificar</span></a>-->
                            <input type="image" src="imagenes/document.png" title="Imprimir Reverso" onclick= "evaluar(8);"width="50"/> 

                            <input type="image" src="imagenes/Impresora.bmp" title="Imprimir" onclick= "evaluar(5);"width="50"/> 

                            <input type="image" src="imagenes/modificarTasa.JPG" title="Modificar Tasa" onclick= "evaluar(3);"width="50"/> 

                            <input type="image" src="imagenes/Modificar.jpeg" title="Modificar Serie" onclick= "evaluar(1);"width="50"/> 

                            <input type="image" src="imagenes/Eliminar.bmp" title="Eliminar serie" onclick= "evaluar(0);"width="50"/> 

                            <input type="image" src="imagenes/formaNum.bmp" title="Formas Numeradas"   onclick="evaluar(6);" width="50"/> 
                            <!--                              
                            <!--<a href="#" onclick="evaluar(0);" class="btn btn-danger" ><i title="Eliminar" class="material-icons">&#xE15C;</i><span>Eliminar</span></a>-->

                            <!--<a href="#" onclick="evaluar(2);" class="btn btn-success  float-left"><i title="Agregar Serie Emitida" class="material-icons">&#xE147;</i> <span>Agregar Serie Emitida</span></a>-->
                            <input type="image" src="imagenes/formato.png" title="Definicion de emisiones" onclick= "evaluar(7);"width="50"/> 

                            <input type="image" src="imagenes/Agregar.bmp" title="Agregar serie emitida" onclick= "evaluar(2);"width="50"/> 
                            <input type="image" src="imagenes/newsletter.JPG" title="Certificado en custodia" onclick= "evaluar(4);"width="50"/> 
                        </center>
                        <!--<a href="#" onclick="evaluar(4);" class="btn btn-secondary float-left" ><i title="Certificado en custodia" class="material-icons">&#xE147;</i><span>Certificado en custodia</span></a>-->

                    </div>




                </div>
                <!--<input class="form-control" id="myInput" type="text" placeholder="Search..">-->
                <table id="myTable" class="table table-striped" >
                    <input type="hidden" id="seleccionadosInput" name="seleccionadosInput" />

                    <thead>
                        <tr>
                            <th>Año</th>
                            <th>Serie</th>
                            <th>Emitidos</th>
                            <th>Disponibles</th>
                            <th>Monedas</th>
                            <th>Acción</th>

                        </tr>
                    </thead>
                    <tbody >
                        <%                                Integer fila = 0;
//                                    
                            if (!(session.getAttribute("listaSeries") == null)) {
                                List<SeriesM> listaSer;
                                listaSer = (List<SeriesM>) session.getAttribute("listaSeries");

                                for (SeriesM s : listaSer) {
                        %>

                        <tr>
                            <td><%=String.format("%.0f", s.getSRSAIO())%></td>
                            <td><%=s.getSRSCOD()%></td>
                            <td><%=String.format("%.0f", s.getSRSTBC())%></td> 
                            <td><%=String.format("%.0f", s.getBONVIG())%></td>
                            <td><%=s.getSRSMNE()%></td>
                            <td>
                                <input type="checkbox" id="checkbox_<%= fila%>" name="checkbox_<%= fila%>" value="<%= listaSer.indexOf(s)%>" onchange="agregarSeleccionado('checkbox_<%= fila%>');"/>
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
        <div class="col-md-12 text-center">
            <ul class="pagination pagination-lg pager" id="Table_page"></ul>
        </div>

</body>
</html>
