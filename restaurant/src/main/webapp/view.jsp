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
      <p class="res-address">Contact: ${fn:escapeXml(restaurant.contactNumber)}</p>
    </div>
  </div>
</div>
