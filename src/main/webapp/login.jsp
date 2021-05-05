<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<link rel="stylesheet" href="/css/account.css" />

<div class="login-logo">
    <img class="image-login" src="https://i.imgur.com/osIpmMb.png">
</div>
<div class="container-login">
  <div class="media-login"> 
  <h3 class="header-login">
    <c:out value="${action}" /> Login
  </h3>
  <form method="POST" action="${destination}" enctype="multipart/form-data" style="display:inline-block;">
    <c:if test="${not empty loginError}">
        <div class="alert">
            <span class="closebtn">&times;</span>  
            ${loginError}
            <script>
            var close = document.getElementsByClassName("closebtn");
            var i;

            for (i = 0; i < close.length; i++) {
                close[i].onclick = function(){
                    var div = this.parentElement;
                    div.style.opacity = "0";
                    setTimeout(function(){ div.style.display = "none"; }, 600);
                }
            }
            </script> 
        </div>
    </c:if>

    <% request.getSession().removeAttribute("loginError"); %>

    <div>
      <input type="text" required="required" name="username" id="username" placeholder="Username" value="${fn:escapeXml(userAccount.username)}" class="form-control login-form" />
    </div>

    <div>
      <input type="password" required="required" name="password" id="password" placeholder="Password" value="${fn:escapeXml(userAccount.password)}" class="form-control login-form" />
    </div>

    <button type="submit" class="btn login-label">Login</button>
    </div>
  </form>
</div>
