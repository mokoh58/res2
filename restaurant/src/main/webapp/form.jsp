<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div class="container">
  <h3>
    <c:out value="${action}" /> Restaurant
  </h3>

  <form method="POST" action="${destination}" enctype="multipart/form-data">

    <div class="form-group">
      <label for="restName">Name</label>
      <input type="text" name="restName" id="restName" value="${restaurant.restName}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="address">Address</label>
      <input type="text" name="address" id="address" value="${restaurant.address}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="maxCapacity">Max Capacity</label>
      <input type="text" name="maxCapacity" id="maxCapacity" value="${restaurant.maxCapacity}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="contactNumber">Contact</label>
      <input type="text" name="contactNumber" id="contactNumber" value="${restaurant.contactNumber}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="operatingHours">Operating Hours</label>
      <input type="text" name="operatingHours" id="operatingHours" value="${restaurant.operatingHours}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="cuisine">Cuisine</label>
      <input type="text" name="cuisine" id="cuisine" value="${restaurant.cuisine}" class="form-control" />
    </div>

    <div class="form-group ${isCloudStorageConfigured ? '' : 'hidden'}">
      <label for="image">Cover Image</label>
      <input type="file" name="file" id="file" class="form-control" />
    </div>

    <div class="form-group hidden">
      <label for="imageUrl">Cover Image URL</label>
      <input type="hidden" name="id" value="${restaurant.id}" />
      <input type="text" name="imageUrl" id="imageUrl" value="${restaurant.imageUrl}" class="form-control" />
    </div>

    <button type="submit" class="btn btn-success">Save</button>
  </form>
</div>
