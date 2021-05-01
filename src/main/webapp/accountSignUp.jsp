<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<link rel="stylesheet" href="/css/account.css" />
<div class="container-signup">
  <div class="media-signup"> 
  <h3 class="header-signup" style="padding-bottom:10px;">
    <c:out value="${action}" /> Account
  </h3>
      
  <form method="POST" action="${destination}" enctype="multipart/form-data">

    <div>
      <label for="username">Username</label>
      <input type="text" required="required" name="username" id="username" value="${fn:escapeXml(userAccount.username)}" class="form-control signup-form" />
    </div>

    <div>
      <label for="password">Password</label>
      <input type="password" required="required" name="password" id="password" value="${fn:escapeXml(userAccount.password)}" class="form-control signup-form" />
    </div>

    <div>
      <label for="accountType">Account Type (select one)</label>
      <select name="accountType" required="required" id="accountType" value="${fn:escapeXml(userAccount.accountType)}" class="form-control signup-form">
        <option>Restaurant Owner</option>
        <option>Consumer</option>
        <option>Administrator</option>
      </select>
    </div>

    <div>
    <div class="firstname-outer"> 
      <label for="firstName">First Name</label>
      <input type="text" required="required" name="firstName" id="firstName" value="${fn:escapeXml(userAccount.firstName)}" class="form-control signup-form firstname-box" />
    </div>

    <div class="lastname-outer">
      <label for="lastName">Last Name</label>
      <input type="text" required="required" name="lastName" id="lastName" value="${fn:escapeXml(userAccount.lastName)}" class="form-control signup-form lastname-box" />
    </div>
    </div>

    <div>
      <label for="email">Email</label>
      <input type="text" required="required" name="email" id="email" value="${fn:escapeXml(userAccount.email)}" class="form-control signup-form" />
    </div>

    <div>
      <label for="contactNumber">Contact Number</label>
      <input type="text" required="required" name="contactNumber" id="contactNumber" value="${fn:escapeXml(userAccount.contactNumber)}" class="form-control signup-form" />
    </div>

    <button type="submit" class="btn signup-label">Create Account</button>
  </form>
  </div>
</div>
