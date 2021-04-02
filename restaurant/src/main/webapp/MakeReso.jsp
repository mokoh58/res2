<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div class="container">
  <h3>
    Make Reservation
  </h3>

  <form method="POST" action="${destination}" enctype="multipart/form-data">

    <div class="form-group">
      <label for="resoName">Name</label>
      <input type="text" name="resoName" id="resoName" value="${reservation.resoName}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="resoContact">Contact Number</label>
      <input type="text" name="resoContact" id="resoContact" value="${reservation.resoContact}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="resoDate">Reservation Date</label>
      <input type="text" name="resoDate" id="resoDate" value="${reservation.resoContact}" class="form-control"/>
    </div>

    <div class="form-group">
        <label for="resoTime">Reservation Time</label>
        <select name="resoTime" id="resoTime" value="${reservation.resoTime}">
            <option value="12-2">12:00 - 14:00</option>
            <option value="2-4">14:00 - 16:00</option>
            <option value="4-6">16:00 - 18:00</option>
            <option value="6-8">18:00 - 20:00</option>
            <option value="8-10">20:00 - 22:00</option>
        </select>
    </div>

    <input type="hidden" name="restId" value="${restaurant.id}" />
    <button type="submit" class="btn btn-success">Save</button>
  </form>
</div>
