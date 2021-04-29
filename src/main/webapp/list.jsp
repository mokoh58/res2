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
                    <div style="display:block; max-height:240px; max-width:252px; text-align:center;">                    
                    <img class="restaurant-image" alt="ahhh" src="${fn:escapeXml(not empty restaurant.imageUrl?restaurant.imageUrl:'http://placekitten.com/g/128/192')}">
                    </div>
                </div>
                <div class="column-desc">
                    <div style="border-top: 1px solid #ECECEC; padding-top:20px; padding-bottom:5px;">
                    <div style="text-overflow:ellipsis; max-width:170px; overflow:hidden; display:inline-block; font-size:1.17em; white-space:nowrap;">
                        ${fn:escapeXml(restaurant.restName)}
                    </div>
                    <c:choose>
                        <c:when test="${restaurant.crowdLevel == 'Available'}">
                            <div style="float:right; margin-bottom:0px;">
                                <p style="float:right; color:#94E185">Available</p>
                                <li style="float:right;" class="fa fa-circle available"></li>
                            </div>
                        </c:when>
                        <c:when test="${restaurant.crowdLevel == 'Filling Up'}">
                            <div style="float:right; margin-bottom:0px;">
                                <p style="float:right; color:#FFC182">Filling Up</p>
                                <li style="float:right;" class="fa fa-circle fairly-crowded"></li>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div style="float:right; margin-bottom:0px;">
                                <p style="float:right; color:#C9404D">Crowded</p>
                                <li style="float:right;" class="fa fa-circle crowded"></li>
                            </div>
                        </c:otherwise>
                    </c:choose>
                    </div>
                    <p>${fn:escapeXml(restaurant.address)}</p>
                </div>
            </div>
        </div>
        </a>
    </c:forEach>
  </div>
  </c:otherwise>
  </c:choose>
</div>
