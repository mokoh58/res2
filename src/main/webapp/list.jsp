<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<link rel="stylesheet" href="/css/list.css" />
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
<link href="https://unpkg.com/aos@2.3.1/dist/aos.css" rel="stylesheet">
<script src="https://unpkg.com/aos@2.3.1/dist/aos.js"></script>

<script>
    AOS.init();    
</script>

<div class="container">
  <h3>Restaurants</h3>
  <div class="search">
    <form method="GET" action="/restaurants" style="width:100%; display:flex;">
    <input type="text" class="searchTerm" placeholder="Search for restaurant here" name="searchRes" id="searchRes"> 
    
    <button type="submit" class="searchButton">
        <i class="fa fa-search"></i>
    </button>
    
    <a href="/create" class="btn btn-success btn-sm">
        <i class="glyphicon glyphicon-plus"></i>
        Add Restaurant
    </a>
    </form>
  </div>
  
  <c:choose>
  <c:when test="${empty restaurants}">
  <p>No restaurant found</p>
  </c:when>
  <c:otherwise>
  <div class="row">
    <c:forEach items="${restaurants}" var="restaurant">
        <a href="/read?id=${restaurant.id}">
        <div class="column">
            <div class="card" data-aos="fade-up" data-aos-duration="2000">
                <div class="column-image">
                    <img alt="ahhh" src="${fn:escapeXml(not empty restaurant.imageUrl?restaurant.imageUrl:'http://placekitten.com/g/128/192')}">
                </div>
                <div class="column-desc">
                    <h4 style="display:inline-block">${fn:escapeXml(restaurant.restName)}</h4>
                    <c:choose>
                        <c:when test="${restaurant.crowdLevel == 'Available'}">
                            <div style="float:right; margin-top:10px; margin-bottom:0px;">
                                <p style="float:right; color:#94E185">Available</p>
                                <li style="float:right;" class="fa fa-circle available"></li>
                            </div>
                        </c:when>
                        <c:when test="${restaurant.crowdLevel == 'Filling Up'}">
                            <div style="float:right; margin-top:10px; margin-bottom:0px;">
                                <p style="float:right; color:#FFC182">Filling Up</p>
                                <li style="float:right;" class="fa fa-circle fairly-crowded"></li>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div style="float:right; margin-top:10px; margin-bottom:0px;">
                                <p style="float:right; color:#C9404D">Crowded</p>
                                <li style="float:right;" class="fa fa-circle crowded"></li>
                            </div>
                        </c:otherwise>
                    </c:choose>
                    <p>${fn:escapeXml(restaurant.address)}</p>
                </div>
            </div>
        </div>
        </a>
    </c:forEach>
  </div>
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
