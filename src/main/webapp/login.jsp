<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div class="container">
  <h3>
    <c:out value="${action}" /> Login
  </h3>

  <form method="POST" action="${destination}" enctype="multipart/form-data">

    <div class="form-group">
      <label for="username">Username</label>
      <input type="text" required="required" name="username" id="username" value="${fn:escapeXml(userAccount.username)}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="password">Password</label>
      <input type="password" required="required" name="password" id="password" value="${fn:escapeXml(userAccount.password)}" class="form-control" />
    </div>

    <button type="submit" class="btn btn-success">Login</button>
  </form>
</div>
