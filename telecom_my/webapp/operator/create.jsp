<!DOCTYPE html>
<html>
    <head>
        <title>Enter to system</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <p>
            Write login and password for new subscriber
        </p>
            <form method="POST" action="/telecom/service/subscriber">
              <label for="name">Login:</label><br>
              <input type="text" id="login" name="login"><br><br>
              <label for="pass">Password:</label><br>
              <input type="password" id="password" name="password"><br><br>
              <input type="submit" value="create">
            </form>

    </body>
</html>