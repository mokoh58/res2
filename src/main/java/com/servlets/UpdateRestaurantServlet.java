package com.servlets;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import com.dao.RestaurantDAO;
import com.dao.TagsDAO;
import com.google.common.base.Strings;
import com.objects.Restaurant;
import com.objects.Tags;
import com.util.CloudStorageHelper;

@SuppressWarnings("serial")
@WebServlet(name = "update", urlPatterns = { "/update" })
public class UpdateRestaurantServlet extends HttpServlet {

    private final Logger logger = Logger.getLogger(UpdateRestaurantServlet.class.getName());

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
		RestaurantDAO dao = (RestaurantDAO) this.getServletContext().getAttribute("resDAO");
		try {
			Restaurant res = dao.readRestaurant(req.getParameter("id"));
			req.setAttribute("restaurant", res);
			req.setAttribute("action", "Edit");
			req.setAttribute("destination", "update");
			req.setAttribute("page", "form");
			req.getRequestDispatcher("/base.jsp").forward(req, resp);
		} catch (Exception e) {
			throw new ServletException("Error loading book for editing", e);
		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RestaurantDAO dao = (RestaurantDAO) this.getServletContext().getAttribute("resDAO");

		assert ServletFileUpload.isMultipartContent(req);
		CloudStorageHelper storageHelper = (CloudStorageHelper) getServletContext().getAttribute("storageHelper");

		String newImageUrl = null;
		Map<String, String> params = new HashMap<String, String>();
		try {
			FileItemIterator iter = new ServletFileUpload().getItemIterator(req);
			while (iter.hasNext()) {
				FileItemStream item = iter.next();
				if (item.isFormField()) {
					params.put(item.getFieldName(), Streams.asString(item.openStream()));
				} else if (!Strings.isNullOrEmpty(item.getName())) {
					newImageUrl = storageHelper.uploadFile(item, System.getenv("RES_BUCKET"));
				}
			}
		} catch (FileUploadException e) {
			throw new IOException(e);
        }
        
        logger.log(Level.INFO, "UpdateRestaurantServlet IMG URL: " + newImageUrl);

		Restaurant oldRest = dao.readRestaurant(params.get("id"));

        Restaurant res = new Restaurant.Builder()
                .restName(params.get("restName"))
                .address(params.get("address"))
				.maxCapacity(params.get("maxCapacity"))
                .imageUrl(null == newImageUrl ? params.get("imageUrl") : newImageUrl)
                .id(params.get("id"))
                .createdBy(oldRest.getCreatedBy())
                .createdById(oldRest.getCreatedById())
                .contactNumber(params.get("contactNumber"))
                .cuisine(params.get("cuisine"))
                .operatingHours(params.get("operatingHours"))
                .occupiedSeats(null == oldRest.getOccupiedSeats() ? "0" : oldRest.getOccupiedSeats())
                .build();

        dao.updateRestaurant(res);
        
        // Updating of Tags
        TagsDAO tagsDAO = (TagsDAO) this.getServletContext().getAttribute("tagsDAO");

        // Delete all tags first
        tagsDAO.deleteTags(params.get("id"));

        String allTags = params.get("cuisine");
        List<String> tagsList = Arrays.asList(allTags.split(","));
        for (String tag : tagsList){
            Tags newTag = new Tags.Builder().restaurantId(params.get("id")).tag(tag).build();
            tagsDAO.createTags(newTag);
        }

		resp.sendRedirect("/read?id=" + params.get("id"));
	}
}
