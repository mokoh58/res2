<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div class="container">
  <h3>
    <c:out value="${action}" /> Reservation
  </h3>

  <form method="POST" action="${destination}" enctype="multipart/form-data">mokoh58

    <div class="form-group hidden">
      <label for="resoName">Id</label>
      <input type="hidden" name="restId" id="restId" value="${restId}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="resoName">Name</label>
      <input type="text" name="resoName" id="resoName" value="${reservation.resoName}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="resoContact">Contact Number</label>
      <input type="text" name="resoContact" id="resoContact" value="${reservation.resoContact}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="numPax">Number of Pax</label>
      <input type="text" name="numPax" id="numPax" value="${reservation.numPax}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="resoDate">Reservation Date</label>
      <input type="text" autocomplete="off" name="resoDate" id="resoDate" value="${reservation.resoDate}" class="form-control"/>
    </div>

    <div class="form-group">
        <label for="resoTime">Reservation Time</label>
        <select name="resoTime" id="resoTime" value="${reservation.resoTime}" selected="${reservation.resoTime}">
            <option value="12:00">12:00 - 14:00</option>
            <option value="14:00">14:00 - 16:00</option>
            <option value="16:00">16:00 - 18:00</option>
            <option value="18:00">18:00 - 20:00</option>
            <option value="20:00">20:00 - 22:00</option>
            <option value="22:00">22:00 - 00:00</option>
        </select>
    </div>

    <button type="submit" class="btn btn-success">Save</button>
  </form>
</div>
