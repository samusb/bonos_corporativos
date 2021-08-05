<%-- 
    Document   : FormasNum
    Created on : 05-21-2021, 11:43:32 AM
    Author     : aprivera
--%>

<%@page import="modelo.SeriesM"%>
<%@page import="java.util.List"%>
<%@page import="principal.FormasNumeradas"%>
<%@ include file="Plantilla.jsp" %> 
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
          <link rel="stylesheet"  href="http://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css"></style>
    <script type="text/javascript" src="http://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>

        <title>Formas numeradas</title>
    </head>
    <script>
    $(document).ready(function () {
        $('#myTable').DataTable();
        $('.dataTables_length').addClass('bs-select');
    });
</script>
    <script>

            function Evaluar() {
            window.location = "SeriesEmitidasSV?opcion=9";
        }
        </script>
    <body>
        <div class="container">

            <div class="row">
                <div class="col-md-10 col-md-offset-1 m-auto">
                    <Center>
                        <font color="black">
                        <h1>FORMAS NUMERADAS</h1>
                        </font>
                    </Center>

                </div>

            </div>
           <div class="row">
                 <div class="col-sm-4">  
                <form class="form-container" action="FormNumSV" method="post">
                    
                    <h1>Agregar Formas Numeradas</h1> 
                    <input  id="opcion" name="opcion" value="1" type="hidden">
                    
                    <label for="AIO"><b>Año de la serie para las que necesita formas numeradas:</b></label>
                    <input  id="AIO" name="AIO" type="text" placeholder=""  required>
                    
                    <label for="FNM"><b>Cantidad de formas numeradas a generar:</b></label>
                    <input  id="FNM" name="FNM" type="text" placeholder="ejem: 10"  required>

                    <label for="OBS"><b>Razon de creación de formas numeradas:</b></label>
                    <input  id="OBS" name="OBS" type="text" placeholder=""  required>

                    <button  type="submit" onclick= "return confirm('¿Desea crear nuevas formas numeradas?')" class="btn" >Aceptar</button>
                    <button  type="button" class="btn cancel" onclick= "Evaluar();" class="btn" >Volver</button>
                </form>
                 </div>
                 <div class="col-sm-4">  
                    <!--<input class="form-control" id="myInput" type="text" placeholder="Search..">-->
                     <table id="myTable" class="table table-striped">
                       
                        <thead>
                            <tr>
                                <th>FormNum.</th>
                                <th>Año Serie</th>
                                <th>Fecha Ingreso</th>
                                <th>Fecha Impresion</th>
                                <th>Usuario</th>
                                <th>Observación</th>
                                <th>Estatus</th>

                            </tr>
                        </thead>
                        <tbody>
                            <%
                                 if (!(session.getAttribute("listaFN") == null)) {
                                    List<SeriesM> listaSer;
                                    listaSer = (List<SeriesM>) session.getAttribute("listaFN");


                                    for (SeriesM s : listaSer) {
                            %>

                            <tr>
                                <td><%=String.format("%.0f", s.getFNMNUM())%></td>
                                <td><%=String.format("%.0f", s.getFNMAIO())%></td>
                                <td><%=s.getFNMFIN()%></td> 
                                <td><%=s.getFNMIMP()%></td>
                                <td><%=s.getFNMUSR()%></td>
                                <td><%=s.getFNMOBS()%></td>
                                <td><%=s.getFNMEST()%></td>
                               
                            </tr>
                            <%
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
