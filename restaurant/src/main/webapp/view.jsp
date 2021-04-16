<!--
Copyright 2019 Google LLC

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->
<!-- [START bookshelf_jsp_view] -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<style type="text/css">	 


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
      </h4>
        <p class="res-address">Address: ${fn:escapeXml(restaurant.address)}</p>
        <p class="res-contact">Contact: ${fn:escapeXml(restaurant.contactNumber)}</p>
        <p class="res-operatingHours">Operating Hours: ${fn:escapeXml(restaurant.operatingHours)}</p>
        <p class="res-capacity">Current Capacity: ${currCapacity}</p>
        <div>
            <form method="POST">
            <input type="hidden" name="id" id="id" value=${restaurant.id} />
            <input type="hidden" name="maxCapacity" id="maxCapacity" value=${restaurant.maxCapacity} />
            <input type="hidden" name="occupiedSeats" id="occupiedSeats" value=${restaurant.occupiedSeats} />
            <input type="text" name="addPax" id="addPax" placeholder="0" value="${addPax}" size="3" maxlength="3" />

            <button type="submit" name="add" value="add" class="btn btn-success">Add Pax</button>
            <button type="submit" name="subtract" value="subract" class="btn btn-success">Minus Pax</button>
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
                        <a href="/update-reso?id=${reservation.id}" class="btn btn-primary btn-sm">
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
  </div>
</div>
