
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<div class="container">
  <h3>Restautants</h3>
  <a href="/create" class="btn btn-success btn-sm">
    <i class="glyphicon glyphicon-plus"></i>
    Add Restaurant
  </a>
  <c:choose>
  <c:when test="${empty restaurants}">
  <p>No restaurant found</p>
  </c:when>
  <c:otherwise>
  <c:forEach items="${restaurants}" var="restaurant">
  <div class="media">
    <a href="/read?id=${restaurant.id}">
      <div class="media-left">
        <img alt="ahhh" src="${fn:escapeXml(not empty restaurant.imageUrl?restaurant.imageUrl:'http://placekitten.com/g/128/192')}">
      </div>
      <div class="media-body">
        <h4>${fn:escapeXml(restaurant.restName)}</h4>
        <p>${fn:escapeXml(restaurant.address)}</p>
      </div>
    </a>
  </div>
  </c:forEach>
  <c:if test="${not empty cursor}">
  <nav>
    <ul class="pager">
      <li><a href="?cursor=${fn:escapeXml(cursor)}">More</a></li>
    </ul>
  </nav>
  </c:if>
  </c:otherwise>
  </c:choose>
</div>
