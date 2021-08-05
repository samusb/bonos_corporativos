<%-- 
    Document   : DefEmisiones
    Created on : 05-14-2021, 03:28:59 PM
    Author     : aprivera
--%>
<%@ include file="Plantilla.jsp" %> 
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="multipart/form-data; charset=iso-8859-1">
        <title>Definición de emisiones</title>
    </head>
    <script>

        var seleccionados = [];

        function evaluar() {
            window.location = "SeriesEmitidasSV?opcion=9";
        }

    </script>


    <body>
    <center>

        <h1><font face="Century" size="60">Definición de emisiones</font></h1>

        <form  action="DefEmisionesSV" method="post"  enctype= "Multipart/form-data">
            <div class="row">
                <div class="col-sm-4">

                </div>
                <div class="col-sm-4">  
                    <!--<input type="hidden" id="opcion"  value ="1" name="opcion" />-->
                    Seleccione documento:<input  type="file" name="fname"/><br/>  

                    <input type="submit" onclick= "return confirm('¿Esta seguro de enviar el documento seleccionado?')" class="btn btn-primary" value="upload"/>  
                </div>
                <div class="col-sm-4"> 
                </div>
            </div>
        </form>


        <div class="row">
            <div class="col-sm-4"> 
            </div>
            <div class="col-sm-4"> 

                <input type="button" class="btn btn-primary" onclick= "evaluar();" value="Volver"/> 
            </div>
        </div>

    </center>
</body>
</html>
