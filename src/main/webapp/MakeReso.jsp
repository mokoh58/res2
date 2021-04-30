<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div class="container">
  <h3>
    <c:out value="${action}" /> Reservation
  </h3>

  <form method="POST" action="${destination}" enctype="multipart/form-data">

    <div class="form-group hidden">
      <label for="restId">Id</label>
      <input type="hidden" name="restId" id="restId" value="${restId}" class="form-control" />
      <input type="hidden" name="id" value="${reservation.id}" />
    </div>

    <div class="form-group">
      <label for="resoName">Name</label>
      <input type="text" required="required" name="resoName" id="resoName" value="${reservation.resoName}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="resoContact">Contact Number</label>
      <input type="text" required="required" name="resoContact" id="resoContact" value="${reservation.resoContact}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="numPax">Number of Pax</label>
      <input type="text" required="required" name="numPax" id="numPax" value="${reservation.numPax}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="resoDate">Reservation Date</label>
      <input type="text" required="required" autocomplete="off" name="resoDate" id="resoDate" value="${reservation.resoDate}" class="form-control"/>
    </div>

    <div class="form-group">
        <label for="resoTime">Reservation Time</label>
        <select name="resoTime" required="required" id="resoTime" value="${reservation.resoTime}">
            <c:forEach items="${operatingHourList}" var="oh"> 
                <option value="${oh.code}" <c:if test="${oh.code eq reservation.resoTime}">selected="selected"</c:if>>${oh.value}</option>
            </c:forEach>
        </select>

        <!-- <select name="resoTime" id="resoTime" value="${reservation.resoTime}" selected="${reservation.resoTime}">
            <option value="12:00">12:00 - 14:00</option>
            <option value="14:00">14:00 - 16:00</option>
            <option value="16:00">16:00 - 18:00</option>
            <option value="18:00">18:00 - 20:00</option>
            <option value="20:00">20:00 - 22:00</option>
            <option value="22:00">22:00 - 00:00</option>
        </select> -->
    </div>

    <button type="submit" class="btn btn-success">Save</button>
  </form>
</div>
