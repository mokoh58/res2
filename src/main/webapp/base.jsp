
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="com.objects.UserAccount" %>
<html lang="en">
  <head>
    <title>Res</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
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
  <body>
    <nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="#">Res.</a>
    </div>
    <ul class="nav navbar-nav">
      <li class="active"><a href="/restaurants">Home</a></li>
      <li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#">Categories <span class="caret"></span></a>
        <ul class="dropdown-menu">
          <li><a href="#">Western</a></li>
          <li><a href="#">Chinese</a></li>
          <li><a href="#">Indian</a></li>
        </ul>
      </li>
      <li><a href="#">Recommendations</a></li>
    </ul>

    <form method="GET" action="/restaurants" class="navbar-form navbar-left">
      <div class="form-group">
        <input type="text" class="form-control" placeholder="Search for restaurant here" name="searchRes" id="searchRes">
      </div>
      <button type="submit" class="btn btn-default">Submit</button>
    </form>

    <%
    UserAccount userAccount = (UserAccount) request.getSession().getAttribute("userAccount");
    if (null == userAccount){
    %>

    <ul class="nav navbar-nav navbar-right">
      <li><a href="/createAccount"><span class="glyphicon glyphicon-user"></span> Sign Up</a></li>
      <li><a href="/login"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>
    </ul>

    <%
    } else {
        String username = userAccount.getUsername();
        String userId = userAccount.getUserAccountId();
        String accountType = userAccount.getAccountType();
    %>
    <ul class="nav navbar-nav navbar-right">
        <%
        if ("Restaurant Owner".equals(accountType) || "Administrator".equals(accountType)){
            %>
            <li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#"><span class="glyphicon glyphicon glyphicon-briefcase"></span> Restaurant </a>
                <ul class="dropdown-menu">
                    <li><a href="/create">List New Restaurant</a></li>
                    <li><a href="/restaurants?ownerId=<%=userId %>">My Restaurants</a></li>
                </ul>
            </li>
            <%
        }
        %>
        <li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#"><span class="glyphicon glyphicon-user"></span> <%=username %> </a>
            <ul class="dropdown-menu">
                <li><a href="/restaurants?userId=<%=userId %>">Favourites</a></li>
                <li><a href="#">Edit Account</a></li>
            </ul>
        </li>
      <li><a href="/logout"><span class="glyphicon glyphicon-log-out"></span> Logout</a></li>
    </ul>

    <% } %>

  </div>
    </nav>

    <div class="bg"></div>
    <div class="content" style="display:contents;">
        <img src="https://i.imgur.com/QWtA5Wd.png" style="height:350px; width:350px; margin-right:auto; margin:auto; display:block;"/>
    </div>

    <div class="layout" style="padding:20px;">
        <c:import url="/${page}.jsp" />
    </div>
  </body>
</html>
