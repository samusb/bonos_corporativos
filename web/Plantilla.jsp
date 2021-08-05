<%-- 
    Document   : Plantilla
    Created on : 04-23-2021, 10:42:17 AM
    Author     : aprivera
--%>
<%
    if (session.getAttribute("usuario") == null) {
        response.sendRedirect("LogIn?sessionOp=2");
    } else {
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <META HTTP-EQUIV="refresh" CONTENT="<%= session.getMaxInactiveInterval() %>; URL=LogIn?sessionOp=2" />
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Bonos Corporativos</title>
      
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
        <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Patua+One">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/js/bootstrap.bundle.min.js" integrity="sha384-JEW9xMcG8R+pH31jmWH6WWP0WintQrMb4s7ZOdauHnUtxwoG2vI5DkLtS3qm9Ekf" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.1/dist/umd/popper.min.js" integrity="sha384-SR1sx49pcuLnqZUnnPwx6FCym0wLsk5JZuNx2bPPENzswTNFaQU1RDvt3wT4gWFG" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/js/bootstrap.min.js" integrity="sha384-j0CNLUeiqtyaRmlzUHCPZ+Gy5fQu0dQ6eZ/xAww941Ai1SxSY+0EQqNXNE6DZiVc" crossorigin="anonymous"></script>

        <link href="https://fonts.googleapis.com/css?family=Open+Sans:400,600" rel="stylesheet">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
        
        <style type="text/css">
            body{
                font-family: 'Open Sans', sans-serif;
            }
            .form-control {
                box-shadow: none;
                border-radius: 4px;        
                border-color: #dfe3e8;
            }
            .form-control:focus {
                border-color: #29c68c;
                box-shadow: 0 0 8px rgba(0,0,0,0.1);
            }
            .navbar-header.col {
                padding: 0 !important;
            }	
            .navbar {
                background: #fff;
                padding-left: 16px;
                padding-right: 16px;
                border-bottom: 1px solid #dfe3e8;
                border-radius: 0;
            }
            .navbar .navbar-brand {
                font-size: 20px;
                padding-left: 0;
                padding-right: 50px;
            }
            .navbar .navbar-brand b {
                font-weight: bold;
                color:#3342FF;		
            }
            .navbar ul.nav li a {
                color: #999;
            }
            .navbar ul.nav li a:hover, .navbar ul.nav li a:focus {
                color: #29c68c !important;
            }
            .navbar ul.nav li.active > a, .navbar ul.nav li.open > a {
                color: #010108 !important;
                background: transparent !important;
            }
            .navbar ul.nav li.activee > a, .navbar ul.nav li.open > a {
                color: #FA1212 !important;
                background: transparent !important;
            }
            .navbar .form-inline .input-group-addon {
                box-shadow: none;
                border-radius: 2px 0 0 2px;
                background: #f5f5f5;
                border-color: #dfe3e8;
                font-size: 16px;
            }
            .navbar .form-inline i {
                font-size: 16px;
            }
            .navbar .form-inline .btn {
                border-radius: 2px;
                color: #fff;
                border-color: #29c68c;
                background: #29c68c;
                outline: none;
            }
            .navbar .form-inline .btn:hover, .navbar .form-inline .btn:focus {
                border-color: #26bb84;
                background: #26bb84;
            }
            .navbar .search-form {
                display: inline-block;
            }
            .navbar .search-form .btn {
                margin-left: 4px;
            }
            .navbar-light .navbar-nav .nav-link {
                color: #dfe3e8;
            }
            .navbar .search-form .form-control {
                border-radius: 2px;
            }
            .navbar .login-form .input-group {
                margin-right: 4px;
                float: left;
            }
            .navbar .login-form .form-control {
                max-width: 158px;
                border-radius: 0 2px 2px 0;
            }    	
            .navbar .navbar-right .dropdown-toggle::after {
                display: none;
            }
            .navbar .dropdown-menu {
                border-radius: 1px;
                border-color: #e5e5e5;
                box-shadow: 0 2px 8px rgba(0,0,0,.05);
            }
            .navbar .dropdown-menu li a {
                padding: 6px 20px;
            }
            .navbar .navbar-right .dropdown-menu {
                width: 505px;
                padding: 20px;
                left: auto;
                right: 0;
                font-size: 14px;
            }
            @media (min-width: 1200px){
                .search-form .input-group {
                    width: 300px;
                    margin-left: 30px;
                }
            }
            @media (max-width: 768px){
                .navbar .navbar-right .dropdown-menu {
                    width: 100%;
                    background: transparent;
                    padding: 10px 20px;
                }
                .navbar .input-group {
                    width: 100%;
                    margin-bottom: 15px;
                }
                .navbar .input-group .form-control {
                    max-width: 20%;			
                }
                .navbar .login-form .btn {
                    width: 100%;
                }
            }
        </style>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <style>
            .alert {
                padding: 20px;
                background-color: #f44336;
                color: white;
            }

            .closebtn {
                margin-left: 15px;
                color: white;
                font-weight: bold;
                float: right;
                font-size: 22px;
                line-height: 20px;
                cursor: pointer;
                transition: 0.3s;
            }

            .closebtn:hover {
                color: black;
            }
        </style>
        <style>
            body {font-family: Arial, Helvetica, sans-serif;}
            * {box-sizing: border-box;}

            /* Button used to open the contact form - fixed at the bottom of the page */
            .open-button {
                background-color: #555;
                color: white;
                padding: 16px 20px;
                border: none;
                cursor: pointer;
                opacity: 0.8;
                position: fixed;
                bottom: 23px;
                right: 28px;
                width: 280px;
            }

            /* The popup form - hidden by default */
            .form-popup {
                display: none;
                position: fixed;
                bottom: 0;
                right: 15px;
                border: 3px solid #f1f1f1;
                z-index: 9;
            }

            /* Add styles to the form container */
            .form-container {
                max-width: 300px;
                padding: 10px;
                background-color: white;
            }

            /* Full-width input fields */
            .form-container input[type=text], .form-container input[type=password] {
                width: 100%;
                padding: 15px;
                margin: 5px 0 22px 0;
                border: none;
                background: #f1f1f1;
            }

            /* When the inputs get focus, do something */
            .form-container input[type=text]:focus, .form-container input[type=password]:focus {
                background-color: #ddd;
                outline: none;
            }

            /* Set a style for the submit/login button */
            .form-container .btn {
                background-color: #4CAF50;
                color: white;
                padding: 16px 20px;
                border: none;
                cursor: pointer;
                width: 100%;
                margin-bottom:10px;
                opacity: 0.8;
            }

            /* Add a red background color to the cancel button */
            .form-container .cancel {
                background-color: red;
            }

            /* Add some hover effects to buttons */
            .form-container .btn:hover, .open-button:hover {
                opacity: 1;
            }
        </style>
        <style type="text/css">    
            body {
                color: #333;
                background: #fafafa;
                font-family: "Patua One", sans-serif;
            }
            .contact-form {
                padding: 50px;
                margin: 30px 0;
            }
            .contact-form h1 {
                color: #19bc9d;
                font-weight: bold;
                margin: 0 0 15px;
            }
            .contact-form .form-control, .contact-form .btn {
                min-height: 38px;
                border-radius: 5px;
            }
            .contact-form .form-control:focus {
                border-color: #19bc9d;
            }
            .contact-form .btn-primary {
                color: #fff;
                min-width: 150px;
                font-size: 16px;
                background: #19bc9d;
                border: none;
            }
            .contact-form .btn-primary:hover {
                background: #15a487; 
            }
            .contact-form .btn i {
                margin-right: 5px;
            }
            .contact-form label {
                opacity: 0.7;
            }
            .contact-form textarea {
                resize: vertical;
            }
            .hint-text {
                font-size: 15px;
                padding-bottom: 20px;
                opacity: 0.6;
            }
            .alert-custom{
                background-color:#F5EA82;
                color:#000000;
            }

        </style>
        <style>
            body {
                color: #566787;
                background: #f5f5f5;
                font-family: 'Varela Round', sans-serif;
                font-size: 13px;
            }
            .table-responsive {
                margin: 30px 0;
            }
            .table-wrapper {
                background: #fff;
                padding: 20px 25px;
                border-radius: 3px;
                min-width: 1000px;
                box-shadow: 0 1px 1px rgba(0,0,0,.05);
            }
            .table-title {        
                padding-bottom: 15px;
                background: #6D80F7;
                color: #fff;
                padding: 16px 30px;
                min-width: 100%;
                margin: -20px -25px 10px;
                border-radius: 3px 3px 0 0;
            }
            .table-title h2 {
                margin: 5px 0 0;
                font-size: 24px;
            }
            .table-title .btn-group {
                float: right;
            }
            .table-title .btn {
                color: #fff;
                float: right;
                font-size: 13px;
                border: none;
                min-width: 50px;
                border-radius: 2px;
                border: none;
                outline: none !important;
                margin-left: 10px;
            }
            .table-title .btn i {
                float: left;
                font-size: 21px;
                margin-right: 5px;
            }
            .table-title .btn span {
                float: left;
                margin-top: 2px;
            }
            table.table tr th, table.table tr td {
                border-color: #e9e9e9;
                padding: 12px 15px;
                vertical-align: middle;
            }
            table.table tr th:first-child {
                width: 60px;
            }
            table.table tr th:last-child {
                width: 100px;
            }
            table.table-striped tbody tr:nth-of-type(odd) {
                background-color: #fcfcfc;
            }
            table.table-striped.table-hover tbody tr:hover {
                background: #f5f5f5;
            }
            table.table th i {
                font-size: 13px;
                margin: 0 5px;
                cursor: pointer;
            }	
            table.table td:last-child i {
                opacity: 0.9;
                font-size: 22px;
                margin: 0 5px;
            }
            table.table td a {
                font-weight: bold;
                color: #566787;
                display: inline-block;
                text-decoration: none;
                outline: none !important;
            }
            table.table td a:hover {
                color: #2196F3;
            }
            table.table td a.edit {
                color: #FFC107;
            }
            table.table td a.delete {
                color: #F44336;
            }
            table.table td i {
                font-size: 19px;
            }
            table.table .avatar {
                border-radius: 50%;
                vertical-align: middle;
                margin-right: 10px;
            }
            .pagination {
                float: right;
                margin: 0 0 5px;
            }
            .pagination li a {
                border: none;
                font-size: 13px;
                min-width: 30px;
                min-height: 30px;
                color: #999;
                margin: 0 2px;
                line-height: 30px;
                border-radius: 2px !important;
                text-align: center;
                padding: 0 6px;
            }
            .pagination li a:hover {
                color: #666;
            }	
            .pagination li.active a, .pagination li.active a.page-link {
                background: #03A9F4;
            }
            .pagination li.active a:hover {        
                background: #0397d6;
            }
            .pagination li.disabled i {
                color: #ccc;
            }
            .pagination li i {
                font-size: 16px;
                padding-top: 6px
            }
            .hint-text {
                float: left;
                margin-top: 10px;
                font-size: 13px;
            }    
            /* Custom checkbox */
            .custom-checkbox {
                position: relative;
            }
            .custom-checkbox input[type="checkbox"] {    
                opacity: 0;
                position: absolute;
                margin: 5px 0 0 3px;
                z-index: 9;
            }
            .custom-checkbox label:before{
                width: 18px;
                height: 18px;
            }
            .custom-checkbox label:before {
                content: '';
                margin-right: 10px;
                display: inline-block;
                vertical-align: text-top;
                background: white;
                border: 1px solid #bbb;
                border-radius: 2px;
                box-sizing: border-box;
                z-index: 2;
            }
            .custom-checkbox input[type="checkbox"]:checked + label:after {
                content: '';
                position: absolute;
                left: 6px;
                top: 3px;
                width: 6px;
                height: 11px;
                border: solid #000;
                border-width: 0 3px 3px 0;
                transform: inherit;
                z-index: 3;
                transform: rotateZ(45deg);
            }
            .custom-checkbox input[type="checkbox"]:checked + label:before {
                border-color: #03A9F4;
                background: #03A9F4;
            }
            .custom-checkbox input[type="checkbox"]:checked + label:after {
                border-color: #fff;
            }
            .custom-checkbox input[type="checkbox"]:disabled + label:before {
                color: #b8b8b8;
                cursor: auto;
                box-shadow: none;
                background: #ddd;
            }
            /* Modal styles */
            .modal .modal-dialog {
                max-width: 400px;
            }
            .modal .modal-header, .modal .modal-body, .modal .modal-footer {
                padding: 20px 30px;
            }
            .modal .modal-content {
                border-radius: 3px;
                font-size: 14px;
            }
            .modal .modal-footer {
                background: #ecf0f1;
                border-radius: 0 0 3px 3px;
            }
            .modal .modal-title {
                display: inline-block;
            }
            .modal .form-control {
                border-radius: 2px;
                box-shadow: none;
                border-color: #dddddd;
            }
            .modal textarea.form-control {
                resize: vertical;
            }
            .modal .btn {
                border-radius: 2px;
                min-width: 100px;
            }	
            .modal form label {
                font-weight: normal;
            }	
        </style>

    <body>

    </script>
    <script>
        $(document).ready(function () {
            $("#myInput").on("keyup", function () {
                var value = $(this).val().toLowerCase();
                $("#myTable tr").filter(function () {
                    $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
                });
            });
        });
        
        $(document).ready(function () {
  $('#myTable').DataTable();
  $('.dataTables_length').addClass('bs-select');
});
    </script>


    <nav class="navbar navbar-default navbar-expand-lg navbar-light">
        <div class="navbar-header d-flex col">
            <input type="image" src="imagenes/Ficensamini.png" alt="settings" width="40" height="40">
            <!--                <a class="navbar-brand" href="#">Banco<b>Ficensa</b></a>  		-->
            <button type="button" data-target="#navbarCollapse" data-toggle="collapse" class="navbar-toggle navbar-toggler ml-auto">
                <span class="navbar-toggler-icon"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
        </div>
        <!-- Collection of nav links, forms, and other content for toggling -->
        <div id="navbarCollapse" class="collapse navbar-collapse justify-content-start">
            <ul class="nav navbar-nav">



                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>

                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>

                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>

                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>

                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>

                <li class="nav-item active"><a class="nav-link"></a></li>

                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>

                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>

                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>

                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>

                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>

                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item active"><a class="nav-link"></a></li>
                <li class="nav-item activee" ><a href="LogIn?sessionOp=2" class="nav-link">CERRAR SESION</a></li> 	



            </ul>


        </div>
    </nav>

</body>
</html>
<%
    }
%>