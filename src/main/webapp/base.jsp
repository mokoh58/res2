
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="com.objects.UserAccount" %>
<html lang="en">
  <head>
    <title>Res</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <link rel="stylesheet" href="/css/base.css" />

    <script>
    $( function() {
        $( "#resoDate" ).datepicker();
    } );
    </script>
  </head>
  
  <header class="general-header">
    <div class="logo-link">
      <a href="#">
          <img style="height: 80px;" src="https://i.imgur.com/QeXixJ9.png">
      </a>
    </div>
    <div class="right-nav">    
      <span class="sub-header"><a class="sub-header-links" href="/restaurants">Home</a></span>
      <span class="sub-header dropdown"><a class="dropdown-toggle sub-header-links" data-toggle="dropdown" href="#">Categories <span class="caret"></span></a>
        <ul class="dropdown-menu">
          <li><a href="#">Western</a></li>
          <li><a href="#">Chinese</a></li>
          <li><a href="#">Indian</a></li>
        </ul>
      </span>
      <span class="sub-header"><a class="sub-header-links" href="#">Recommendations</a></span>
      <span class="sub-header">
          <div>
          <form method="GET" action="/restaurants" style="margin-block-end:0px;">
            <input class="placeholder-header" type="text" placeholder="Search" name="searchRes" id="searchRes">
            <button class="fa fa-search button-header" type="submit"></button>
          </form>
          </div>
      </span>
    
    <%
    UserAccount userAccount = (UserAccount) request.getSession().getAttribute("userAccount");
    if (null == userAccount){
    %>

      <span class="sub-header"><a class="sub-header-links" href="/createAccount"><span class="glyphicon glyphicon-user"></span> Sign Up</a></span>
      <span class="sub-header"><a class="sub-header-links" href="/login"><span class="glyphicon glyphicon-log-in"></span> Login</a></span>

    <%
    } else {
        String username = userAccount.getUsername();
        String userId = userAccount.getUserAccountId();
    %>
      <span class="sub-header dropdown"><a class="dropdown-toggle sub-header-links" data-toggle="dropdown" href="#"><span class="glyphicon glyphicon-user"></span> <%=username %> </a>
        <ul class="dropdown-menu">
          <li><a href="/restaurants?userId=<%=userId %>">Favourites</a></li>
          <li><a href="#">Edit Account</a></li>
        </ul>
      </span>
      <span class="sub-header"><a class="sub-header-links" href="/logout"><span class="glyphicon glyphicon-log-out"></span> Logout</a></span>

    <% } %>
    </div>
  </div>
  </header>

  <body style="background-image: none;">
    <!--
    <div class="content" style="display:contents;">
        <img src="https://i.imgur.com/QWtA5Wd.png" style="height:350px; width:350px; margin-right:auto; margin:auto; display:block;"/>
    </div>
    -->
    <div class="layout" style="padding:20px;">
        <c:import url="/${page}.jsp" />
    </div>
  </body>
</html>
