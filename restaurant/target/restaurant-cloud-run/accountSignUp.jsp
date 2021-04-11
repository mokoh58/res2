<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div class="container">
  <h3>
    <c:out value="${action}" /> Account
  </h3>

  <form method="POST" action="${destination}" enctype="multipart/form-data">

    <div class="form-group">
      <label for="username">Username</label>
      <input type="text" name="username" id="username" value="${fn:escapeXml(userAccount.username)}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="password">Password</label>
      <input type="password" name="password" id="password" value="${fn:escapeXml(userAccount.password)}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="accountType">Account Type (select one)</label>
      <select name="accountType" id="accountType" value="${fn:escapeXml(userAccount.accountType)}" class="form-control">
        <option>Restaurant Owner</option>
        <option>Consumer</option>
        <option>Administrator</option>
      </select>
    </div>

    <div class="form-group">
      <label for="firstName">First Name</label>
      <input type="text" name="firstName" id="firstName" value="${fn:escapeXml(userAccount.firstName)}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="lastName">Last Name</label>
      <input type="text" name="lastName" id="lastName" value="${fn:escapeXml(userAccount.lastName)}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="email">Email</label>
      <input type="text" name="email" id="email" value="${fn:escapeXml(userAccount.email)}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="contactNumber">Contact Number</label>
      <input type="text" name="contactNumber" id="contactNumber" value="${fn:escapeXml(userAccount.contactNumber)}" class="form-control" />
    </div>

    <button type="submit" class="btn btn-success">Create Account</button>
  </form>
</div>
