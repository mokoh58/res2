<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="com.objects.UserAccount" %>
<%@ page import="com.objects.Favourite" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>

<link rel="stylesheet" href="/css/view.css" />
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link href="https://fonts.googleapis.com/css?family=Lora:400,700|Montserrat:300" rel="stylesheet">
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

function setBar1(x){
    var bar1 = document.getElementById("bar1");
    bar1.style.width = x + "%";
}

function setBar2(x){
    var bar2 = document.getElementById("bar2");
    bar2.style.width = x + "%";
}

function setBar3(x){
    var bar3 = document.getElementById("bar3");
    bar3.style.width = x + "%";
}

function setBar4(x){
    var bar4 = document.getElementById("bar4");
    bar4.style.width = x + "%";
}

function setBar5(x){
    var bar5 = document.getElementById("bar5");
    bar5.style.width = x + "%";
}

$(window).resize(function(){
    if ($(window).width() < $('.layout').width()){
        $('.sidebar').css('position', 'absolute');
    }
    else {
        $('.sidebar').css('position', 'fixed');
    }
});
</script> 

<div class="container" id="info">
  <h3>Restaurant</h3>
  <div class="media"> 
    <div class="media-header">
        <div class="btn-group">
            <c:if test="${userAccount.accountType != 'Consumer'}">
                <a href="/update?id=${restaurant.id}" class="btn btn-primary btn-sm">
                <i class="glyphicon glyphicon-edit"></i>
                    Edit Restaurant
                </a>
                <a href="/delete?id=${restaurant.id}" class="btn btn-danger btn-sm">
                <i class="glyphicon glyphicon-trash"></i>
                    Delete Restaurant
                </a>
            </c:if>
        </div>
    </div>    
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
            <% 
            String tags = (String) request.getAttribute("tags");
            if (tags != null && !tags.trim().isEmpty()){
                List<String> tagsList = Arrays.asList(tags.split(","));
                for (String tag : tagsList){
            %>
                <a href="/restaurants?category=<%=tag %>"><button type="button" class="btn btn-info btn-sm"> <b><%=tag %></b></button></a>

            <%
                }
            }
            %>
      
        <p class="res-address">Address: ${fn:escapeXml(restaurant.address)}</p>
        <p class="res-contact">Contact: ${fn:escapeXml(restaurant.contactNumber)}</p>
        <p class="res-operatingHours">Operating Hours: ${fn:escapeXml(restaurant.operatingHours)}</p>
        <p class="res-capacity">Available Seats: ${currCapacity}</p>
        <div>
            <form method="POST">
            <input type="hidden" name="restId" id="restId" value=${restaurant.id} />
            <input type="hidden" name="maxCapacity" id="maxCapacity" value=${restaurant.maxCapacity} />
            <input type="hidden" name="occupiedSeats" id="occupiedSeats" value=${restaurant.occupiedSeats} />
            
                <c:if test="${userAccount.accountType != 'Consumer'}">
                    <input type="text" required="required" name="addPax" id="addPax" placeholder="0" value="${addPax}" size="3" maxlength="3" />

                    <button type="submit" name="add" value="add" class="btn btn-success">Add Pax</button>
                    <button type="submit" name="subtract" value="subtract" class="btn btn-success">Minus Pax</button>
                </c:if>
            </form>
        </div>
    </div>
    <div>
        <div>
            <h4 style="border-top:1px solid #ECECEC; padding-top:20px;">List of Reservations</h4>
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
                        <a href="/delete-reso?id=${reservation.id}&restId=${restaurant.id}" class="btn btn-danger btn-sm">
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
    
</div>
<div id="review" style="padding:45px">
</div>
<div class="review">
    <h3>Reviews</h3>
    <%
    if (null != request.getSession().getAttribute("userAccount")){
    %>        
    <button type="button" onclick="toggleReview()" class="btn btn-outline-info">Write A Review <i class="fa fa-toggle-down"></i></button>
    <br><br>
    <div class="stars" id="reviewSubmission">
        <div>
        <form method="POST" action="/review" enctype="multipart/form-data">
            <label>YOUR RATING (Required)</label><br>
            <div style="display:inline-block;">
            <input class="star star-5" value="5"  id="star-5" type="radio" name="star" required="required" />
            <label title="Excellent" class="star star-5" for="star-5"></label>
            <input class="star star-4" value="4"  id="star-4" type="radio" name="star"/>
            <label title="Good" class="star star-4" for="star-4"></label>
            <input class="star star-3" value="3"  id="star-3" type="radio" name="star"/>
            <label title="Average" class="star star-3" for="star-3"></label>
            <input class="star star-2" value="2" id="star-2" type="radio" name="star"/>
            <label title="Poor" class="star star-2" for="star-2"></label>
            <input class="star star-1" value="1" id="star-1" type="radio" name="star"/>
            <label title="Terrible" class="star star-1" for="star-1"></label>
            </div>
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
    </div>
    <% } %>

    <%
    String rating1 = String.valueOf(request.getAttribute("rating1"));
    String rating2 = String.valueOf(request.getAttribute("rating2"));
    String rating3 = String.valueOf(request.getAttribute("rating3"));
    String rating4 = String.valueOf(request.getAttribute("rating4"));
    String rating5 = String.valueOf(request.getAttribute("rating5"));
    String averateRating = String.valueOf(request.getAttribute("averateRating"));
    String totalReviews = String.valueOf(request.getAttribute("totalReviews"));
    %>
    <%-- Testing New Review Section --%>
    <br><br>
    <div class="px-1 py-5 mx-auto">
    <div class="row justify-content-left">
        <div class="col-xl-7 col-lg-8 col-md-10 col-12 text-center mb-5" style="width:100%;">
            <div class="card-review-summary">
                <div class="row justify-content-left d-flex">
                    <div class="col-md-4 d-flex flex-column">
                        <div class="rating-box">
                            <h1 class="pt-4"><%=averateRating %></h1>
                            <p class="">out of 5</p>
                        </div>
                        <%-- <div> <span class="fa fa-star star-active mx-1"></span> <span class="fa fa-star star-active mx-1"></span> <span class="fa fa-star star-active mx-1"></span> <span class="fa fa-star star-active mx-1"></span> <span class="fa fa-star star-inactive mx-1"></span> </div> --%>
                    </div>
                    <div class="col-md-8">
                        <div class="rating-bar0 justify-content-left">
                            <table class="text-left mx-auto">
                                <tr>
                                    <td class="rating-label">Excellent</td>
                                    <td class="rating-bar">
                                        <div class="bar-container">
                                            <div id="bar5" class="bar-5"></div>
                                        </div>
                                    </td>
                                    <td class="text-right"><%=rating5 %></td>
                                </tr>
                                <tr>
                                    <td class="rating-label">Good</td>
                                    <td class="rating-bar">
                                        <div class="bar-container">
                                            <div id="bar4" class="bar-4"></div>
                                        </div>
                                    </td>
                                    <td class="text-right"><%=rating4 %></td>
                                </tr>
                                <tr>
                                    <td class="rating-label">Average</td>
                                    <td class="rating-bar">
                                        <div class="bar-container">
                                            <div id="bar3" class="bar-3"></div>
                                        </div>
                                    </td>
                                    <td class="text-right"><%=rating3 %></td>
                                </tr>
                                <tr>
                                    <td class="rating-label">Poor</td>
                                    <td class="rating-bar">
                                        <div class="bar-container">
                                            <div id="bar2" class="bar-2"></div>
                                        </div>
                                    </td>
                                    <td class="text-right"><%=rating2 %></td>
                                </tr>
                                <tr>
                                    <td class="rating-label">Terrible</td>
                                    <td class="rating-bar">
                                        <div class="bar-container">
                                            <div id="bar1" class="bar-1"></div>
                                        </div>
                                    </td>
                                    <td class="text-right"><%=rating1 %></td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <%
            int totalReviewCount = Integer.parseInt(totalReviews);
            if (totalReviewCount != 0){
                int rating1Int = (Integer.parseInt(rating1) * 100) / totalReviewCount;
                int rating2Int = (Integer.parseInt(rating2) * 100) / totalReviewCount;
                int rating3Int = (Integer.parseInt(rating3) * 100) / totalReviewCount;
                int rating4Int = (Integer.parseInt(rating4) * 100) / totalReviewCount;
                int rating5Int = (Integer.parseInt(rating5) * 100) / totalReviewCount;
                %>
                <script>
                setBar1(<%=rating1Int %>);
                setBar2(<%=rating2Int %>);
                setBar3(<%=rating3Int %>);
                setBar4(<%=rating4Int %>);
                setBar5(<%=rating5Int %>);
                </script>
                <%
            } else {
            %>
                <script>
                setBar1(0);
                setBar2(0);
                setBar3(0);
                setBar4(0);
                setBar5(0);
                </script>
            <% } %>

            <c:choose>
            <c:when test="${empty reviews}">
            <p style="margin-top:30px;">No reviews found</p>
            </c:when>
            <c:otherwise>
            <div>
                <c:forEach items="${reviews}" var="review">
                    <div class="card">
                        <div style="display:inline-table; width:10%; float:left; padding-top:20px;">
                            <!-- can put user profile pic next time ? lol -->
                            <img style="width:80px;" src="https://imgur.com/IaeXwXE.png">
                        </div>
                        <div style="display:inline-table; width:80%;">
                        <div class="d-flex">
                            <div class="d-flex flex-column">
                                <h3 class="text-left">${fn:escapeXml(review.username)}</h3>
                                <div>
                                    <p class="text-left"><span class="text-muted">${fn:escapeXml(review.rating)}.0</span> 
                                        <c:if test = "${fn:escapeXml(review.rating) == 0}">
                                            <span class="fa fa-star star-inactive ml-3"></span>
                                            <span class="fa fa-star star-inactive ml-3"></span>
                                            <span class="fa fa-star star-inactive ml-3"></span>
                                            <span class="fa fa-star star-inactive ml-3"></span>
                                            <span class="fa fa-star star-inactive ml-3"></span>
                                        </c:if>
                                        <c:if test = "${fn:escapeXml(review.rating) == 1}">
                                            <span class="fa fa-star star-active ml-3"></span>
                                            <span class="fa fa-star star-inactive ml-3"></span>
                                            <span class="fa fa-star star-inactive ml-3"></span>
                                            <span class="fa fa-star star-inactive ml-3"></span>
                                            <span class="fa fa-star star-inactive ml-3"></span>
                                        </c:if>
                                        <c:if test = "${fn:escapeXml(review.rating) == 2}">
                                            <span class="fa fa-star star-active ml-3"></span>
                                            <span class="fa fa-star star-active ml-3"></span>
                                            <span class="fa fa-star star-inactive ml-3"></span>
                                            <span class="fa fa-star star-inactive ml-3"></span>
                                            <span class="fa fa-star star-inactive ml-3"></span>
                                        </c:if>
                                        <c:if test = "${fn:escapeXml(review.rating) == 3}">
                                            <span class="fa fa-star star-active ml-3"></span>
                                            <span class="fa fa-star star-active ml-3"></span>
                                            <span class="fa fa-star star-active ml-3"></span>
                                            <span class="fa fa-star star-inactive ml-3"></span>
                                            <span class="fa fa-star star-inactive ml-3"></span>
                                        </c:if>
                                        <c:if test = "${fn:escapeXml(review.rating) == 4}">
                                            <span class="fa fa-star star-active ml-3"></span>
                                            <span class="fa fa-star star-active ml-3"></span>
                                            <span class="fa fa-star star-active ml-3"></span>
                                            <span class="fa fa-star star-active ml-3"></span>
                                            <span class="fa fa-star star-inactive ml-3"></span>
                                        </c:if>
                                        <c:if test = "${fn:escapeXml(review.rating) == 5}">
                                            <span class="fa fa-star star-active ml-3"></span>
                                            <span class="fa fa-star star-active ml-3"></span>
                                            <span class="fa fa-star star-active ml-3"></span>
                                            <span class="fa fa-star star-active ml-3"></span>
                                            <span class="fa fa-star star-active ml-3"></span>
                                        </c:if>
                                    </p>
                                </div>
                            </div>
                            <div class="ml-auto">
                                <p class="text-left" style="color:lightgrey">${fn:escapeXml(review.dateOfVisit)}</p>
                            </div>
                        </div>
                        <div class="text-left">
                            <p>${fn:escapeXml(review.remarks)}</p>
                        </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            </c:otherwise>
            </c:choose>

        </div>
    </div>
</div>
</div>   

<script>
    window.onload = setReviewOff;
</script>

<div class="sidebar">
    <a href="#info" title="Info">
        <div class="sidebar-icon">
            <img class="sidebar-image" src="https://i.imgur.com/YiE5VvN.png"/>
        </div>
    </a> 
    <a href="#review" title="Reviews">
        <div class="sidebar-icon">
            <img class="sidebar-image" src="https://i.imgur.com/0cSd7Sp.png"/>
        </div>
    </a> 
</div>
