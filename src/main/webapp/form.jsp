<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<link rel="stylesheet" href="/css/form.css" />
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-tagsinput/0.8.0/bootstrap-tagsinput.css" crossorigin="anonymous">

<div class="container">
  <a href="/restaurants" class="previous">&laquo; Back</a>
  <h3>
    <c:out value="${action}" /> Restaurant
  </h3>

  <form method="POST" action="${destination}" enctype="multipart/form-data">

    <div class="form-group">
      <label for="restName">Name</label>
      <input type="text" required="required" name="restName" id="restName" value="${restaurant.restName}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="address">Address</label>
      <input type="text" required="required" name="address" id="address" value="${restaurant.address}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="maxCapacity">Max Capacity</label>
      <input type="text" required="required" name="maxCapacity" id="maxCapacity" value="${restaurant.maxCapacity}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="contactNumber">Contact</label>
      <input type="text" required="required" name="contactNumber" id="contactNumber" value="${restaurant.contactNumber}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="operatingHours">Operating Hours</label>
      <input type="text" required="required" name="operatingHours" id="operatingHours" value="${restaurant.operatingHours}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="cuisine">Tags</label><br>
      <input type="text" data-role="tagsinput" name="cuisine" id="cuisine" value="${restaurant.cuisine}" class="form-control" />
    </div>

<script src="https://code.jquery.com/jquery-2.2.4.min.js" integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44=" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-tagsinput/0.8.0/bootstrap-tagsinput.min.js" crossorigin="anonymous"></script>

    <div class="form-group ${isCloudStorageConfigured ? '' : 'hidden'}">
      <label for="image">Cover Image</label>
      <input type="file" required="required" name="file" id="file" class="form-control" />
    </div>

    <div class="form-group hidden">
      <label for="imageUrl">Cover Image URL</label>
      <input type="hidden" name="id" value="${restaurant.id}" />
      <input type="text" name="imageUrl" id="imageUrl" value="${restaurant.imageUrl}" class="form-control" />
    </div>

    <button type="submit" class="btn btn-success">Save</button>
  </form>
</div>
