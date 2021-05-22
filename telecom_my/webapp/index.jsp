<!DOCTYPE html>
<html>
    <head>
        <title>Welcome page!</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <p>
            This is telecom project please login to get started.
        </p>
            <form method="POST" action="/telecom/service/login">
                          <label for="name">Login:</label><br>
                          <input type="text" id="login" name="login"><br><br>
                          <label for="pass">Password:</label><br>
                          <input type="password" id="password" name="password"><br><br>
                          <input type="submit" value="Login">
            </form>
   </body>
</html>