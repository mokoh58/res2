<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div class="container">
  <h3>
    <c:out value="${action}" /> Restaurant
  </h3>

  <form method="POST" action="${destination}" enctype="multipart/form-data">

    <div class="form-group">
      <label for="restName">Name</label>
      <input type="text" name="title" id="title" value="${fn:escapeXml(restaurant.resName)}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="Address">Address</label>
      <input type="text" name="author" id="author" value="${fn:escapeXml(restaurant.author)}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="maxCapacity">Max Capacity</label>
      <input type="text" name="publishedDate" id="publishedDate" value="${fn:escapeXml(restaurant.maxCapacity)}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="contactNumber">Contact</label>
      <textarea name="contactNumber" id="contactNumber" class="form-control">${fn:escapeXml(restaurant.contactNumber)}</textarea>
    </div>

    <div class="form-group ${isCloudStorageConfigured ? '' : 'hidden'}">
      <label for="image">Cover Image</label>
      <input type="file" name="file" id="file" class="form-control" />
    </div>

    <div class="form-group hidden">
      <label for="imageUrl">Cover Image URL</label>
      <input type="hidden" name="id" value="${book.id}" />
      <input type="text" name="imageUrl" id="imageUrl" value="${fn:escapeXml(restaurant.imageUrl)}" class="form-control" />
    </div>

    <button type="submit" class="btn btn-success">Save</button>
  </form>
</div>
