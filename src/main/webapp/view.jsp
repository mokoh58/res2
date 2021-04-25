<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="com.objects.UserAccount" %>
<%@ page import="com.objects.Favourite" %>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<script>
function setReviewOff(){
    var x = document.getElementById("reviewSubmission");
    x.style.display = "none";
}

function toggleReview() {
  var x = document.getElementById("reviewSubmission");
  if (x.style.display === "none") {
    x.style.display = "inline-block";
  } else {
    x.style.display = "none";
  }
}
</script>

<style type="text/css">	 

div.stars {
  width: 270px;
  display: inline-block;
}

input.star { display: none; }

label.star {
  float: right;
  padding: 10px;
  font-size: 36px;
  color: #444;
  transition: all .2s;
}

input.star:checked ~ label.star:before {
  content: '\f005';
  color: #FD4;
  transition: all .25s;
}

input.star-5:checked ~ label.star:before {
  color: #FE7;
  text-shadow: 0 0 20px #952;
}

input.star-1:checked ~ label.star:before { color: #F62; }

label.star:hover { transform: rotate(-15deg) scale(1.3); }

label.star:before {
  content: '\f006';
  font-family: FontAwesome;
}

th { 
    border-right-width:medium;
    width: 25%;
} 
</style> 

<div class="container">
  <h3>Restaurant</h3>
  <div class="btn-group">
    <a href="/update?id=${restaurant.id}" class="btn btn-primary btn-sm">
      <i class="glyphicon glyphicon-edit"></i>
      Edit Restaurant
    </a>
    <a href="/delete?id=${restaurant.id}" class="btn btn-danger btn-sm">
      <i class="glyphicon glyphicon-trash"></i>
      Delete Restaurant
    </a>
  </div>

  <div class="media">
    <div class="media-left">
      <img class="book-image" src="${fn:escapeXml(not empty restaurant.imageUrl?restaurant.imageUrl:'http://placekitten.com/g/128/192')}">
    </div>
    <div class="media-body">
      <h4 class="res-name">
        ${fn:escapeXml(restaurant.restName)}
        <% 
        String favouriteInd = (String) request.getSession().getAttribute("favourite");
        if (null != favouriteInd) {
            if ("N".equals(favouriteInd)){
                %> 
                <a href="/favourite" class="btn btn-outline-danger btn-sm"><i class="fa fa-heart-o"></i> </a>
                <%
            }else {
                %>
                <a href="/favourite" class="btn btn-danger btn-sm"><i class="fa fa-heart"></i> </a>
                <%
            }
        }
        %>

        </label>
      </h4>
        <p class="res-address">Address: ${fn:escapeXml(restaurant.address)}</p>
        <p class="res-contact">Contact: ${fn:escapeXml(restaurant.contactNumber)}</p>
        <p class="res-operatingHours">Operating Hours: ${fn:escapeXml(restaurant.operatingHours)}</p>
        <p class="res-capacity">Current Capacity: ${currCapacity}</p>
        <div>
            <form method="POST">
            <input type="hidden" name="restId" id="restId" value=${restaurant.id} />
            <input type="hidden" name="maxCapacity" id="maxCapacity" value=${restaurant.maxCapacity} />
            <input type="hidden" name="occupiedSeats" id="occupiedSeats" value=${restaurant.occupiedSeats} />
            <input type="text" name="addPax" id="addPax" placeholder="0" value="${addPax}" size="3" maxlength="3" />

            <button type="submit" name="add" value="add" class="btn btn-success">Add Pax</button>
            <button type="submit" name="subtract" value="subtract" class="btn btn-success">Minus Pax</button>
            </form>
        </div>
    </div>
    <div>
        <div>
            <h4>List of Reservations</h4>
            <table>
                <tr>
                    <th>Reservation Name</th>
                    <th>Contact Number</th>
                    <th>Reservation Date</th>
                    <th>Reservation Time</th>
                    <th>Number of Pax</th>
                </tr>
                <c:choose>
                <c:when test="${empty reservations}">
                    <p>No reservation found</p>
                </c:when>
                <c:otherwise>
                <c:forEach items="${reservations}" var="reservation"> 
                <c:choose>
                    <c:when test="${reservation.isActive == true}">
                    <tr style="background-color: yellow;">
                    <td>${fn:escapeXml(reservation.resoName)}</td>
                    <td>${fn:escapeXml(reservation.resoContact)} </td>
                    <td>${fn:escapeXml(reservation.resoDate)} </td>
                    <td>${fn:escapeXml(reservation.resoTime)} </td>
                    <td>${fn:escapeXml(reservation.numPax)}</td>
                    </tr>
                    </c:when>
                    <c:otherwise>
                    <tr>
                    <td>${fn:escapeXml(reservation.resoName)}</td>
                    <td>${fn:escapeXml(reservation.resoContact)} </td>
                    <td>${fn:escapeXml(reservation.resoDate)} </td>
                    <td>${fn:escapeXml(reservation.resoTime)} </td>
                    <td>${fn:escapeXml(reservation.numPax)}</td>
                    <td> 
                        <a href="/update-reso?id=${reservation.id}&restId=${restaurant.id}" class="btn btn-primary btn-sm">
                        <i class="glyphicon glyphicon-edit"></i>
                        </a>
                    </td>
                    <td> 
                        <a href="/delete?id=${restaurant.id}" class="btn btn-danger btn-sm">
                        <i class="glyphicon glyphicon-trash"></i>
                        </a>
                    </td>                
                    </tr>
                    </c:otherwise>
                </c:choose>
                </c:forEach>
                <tr>
                    <td>
                        <c:if test="${not empty cursor}">
                    <nav>
                        <ul class="pager">
                        <li><a href="?cursor=${fn:escapeXml(cursor)}">More</a></li>
                        </ul>
                    </nav>
                    </c:if>
                    </c:otherwise>
                    </c:choose>
                    </td>
                </tr>      
            </table>
        </div>
        <a href="/make-reso?id=${restaurant.id}" class="btn btn-success btn-sm">
        <i class="glyphicon glyphicon-plus"></i>
        Make Reservation
        </a>
    </div>
        <br>
        <h3>Reviews</h3>
        <%
        if (null != request.getSession().getAttribute("userAccount")){
        %>        
        <button type="button" onclick="toggleReview()" class="btn btn-outline-info">Write A Review <i class="fa fa-toggle-down"></i></button>
        <br><br>
        <div class="stars" id="reviewSubmission">
        <form method="POST" action="/review" enctype="multipart/form-data">
            <label>YOUR RATING (Required)</label><br>
            <input class="star star-5" value="5"  id="star-5" type="radio" name="star" required="required"/>
            <label class="star star-5" for="star-5"></label>
            <input class="star star-4" value="4"  id="star-4" type="radio" name="star"/>
            <label class="star star-4" for="star-4"></label>
            <input class="star star-3" value="3"  id="star-3" type="radio" name="star"/>
            <label class="star star-3" for="star-3"></label>
            <input class="star star-2" value="2" id="star-2" type="radio" name="star"/>
            <label class="star star-2" for="star-2"></label>
            <input class="star star-1" value="1" id="star-1" type="radio" name="star"/>
            <label class="star star-1" for="star-1"></label>

            <br>
            <label>Date Of Visit (Required)</label>
            <br>
            <input type="date" id="dateOfVisit" required="required" name="dateOfVisit">

            <br><br>
            <label>YOUR REMARKS (Required)</label>
            <br>
            <textarea class="form-control" rows="8" id="remarks" name="remarks" required="required"></textarea>

            <br><br>
            <button class="btn btn-primary" type="submit">Submit</button>
        </form>
        </div>
        <% } %>
    <div>

    <script>
        window.onload = setReviewOff;
    </script>

    </div>
  </div>
</div>
