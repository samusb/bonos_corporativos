<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">



<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Log In</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

        <style type="text/css">
            .login-form {
                width: 340px;
                margin: 50px auto;
            }
            .login-form form {
                margin-bottom: 15px;
                background: #f7f7f7;
                box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
                padding: 30px;
            }
            .login-form h2 {
                margin: 0 0 15px;
            }
            .form-control, .btn {
                min-height: 38px;
                border-radius: 2px;
            }
            .input-group-addon .fa {
                font-size: 18px;
            }
            .btn {
                font-size: 15px;
                font-weight: bold;
            }
            body {
                /*                background-image: url("imagenes/ficensa.png");*/
                background-repeat: no-repeat;
                background-position:-10px 660px;
                background-size: 300px 100px;
            }
        </style>
    </head>
    <body>
        <div class="login-form">
            <center>
                <img src="imagenes/ficensa.png" width="300" />
                <h1><font face="Century" size="60">Bonos Corporativos</font></h1>
            </center>
            <form class="login-container" action="LogIn" method="post">

                <h2 class="text-center">Iniciar sesión</h2>
                <div class="form-group">
                    <div class="input-group">
                        <input type="hidden" id="sessionOp" name="sessionOp" value="1" />
                        <span class="input-group-addon"><i class="fa fa-user"></i></span>
                        <input id="usuario" name="usuario" type="text" class="form-control" placeholder="Usuario" required="required">
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <span class="input-group-addon"><i class="fa fa-lock"></i> <strong class="capslock-warning" title="Caps-lock " style="display:none;"> Mayuculas desbloqueadas </strong></span>
                        <input id="clave" name="clave" type="password" class="form-control" placeholder="Contraseña" required="required" >

                    </div>
                </div>
                <div class="form-group">
                     <button type="submit" class="btn btn-primary btn-block">Iniciar</button>
                </div>
                <div class="clearfix">

                </div>
            </form>
            <p id="text" style="color: gold; display: none;  "><i class="fa fa-warning"></i> WARNING! Mayusculas activadas</p>
         <script>



                var input = document.getElementById("clave");
                var text = document.getElementById("text");
                window.addEventListener("keyup", function (event) {

                    if (event.getModifierState("CapsLock")) {
                        text.style.display = "block";
                    } else {
                        text.style.display = "none";
                    }
                });
                window.addEventListener("keyup", function (event) {

                    if (event.getModifierState("CapsLock")) {
                        text.style.display = "block";
                    } else {
                        text.style.display = "none"
                    }
                });

            </script>

        </div>
    </body>
</html>
