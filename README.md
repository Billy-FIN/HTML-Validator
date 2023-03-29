# HTML-Validator
This is a Java program which builds a simplified HTML validator.

If this is the input:

<!DOCTYPE html public "-//W3C//DTD HTML 4.01 Transitional//EN">
</!doctype> 
<!-- This is a comment --> 
<html> 
  <head>
    <title>Marty Stepp
    <meta http-equiv="Content-Type" content="text/html" />
    <link href="style.css" type="text/css" rel="stylesheet" /> 
 </head> 
 </head> 
 <body>
    <p>My name is Marty Stepp. I teach at 
    <a href="http://www.washington.edu/">UW</a>.</p> 
    <p>Here is a picture of my cat:
    <img src="images/kitteh.jpg" width="100" height="100"> </p>
</html>

Then the output will be:

<!DOCTYPE> 
ERROR unexpected tag: </!doctype> 
<!-- -->
<html>
  <head>
    <title>
    <meta> 
    <link> 
ERROR unexpected tag: </head> 
ERROR unexpected tag: </head> 
  <body> 
    <p>
    <a>
    </a>
    </p>
    <p>
    <img>
    </p>
ERROR unexpected tag: </html> 
ERROR unclosed tag: <body> 
ERROR unclosed tag: <title> 
ERROR unclosed tag: <head> 
ERROR unclosed tag: <html> 
