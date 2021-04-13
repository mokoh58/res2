package com.servlets;

import com.dao.RestaurantDAO;

import com.google.common.base.Strings;
import com.objects.Restaurant;
import com.util.CloudStorageHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import com.dao.RestaurantDAO;
import com.google.common.base.Strings;
import com.objects.Restaurant;
import com.util.CloudStorageHelper;

import java.util.Date;

@SuppressWarnings("serial")
@WebServlet(name = "create", urlPatterns = { "/create" })
public class CreateRestaurantServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(CreateRestaurantServlet.class.getName());

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("action", "Add");
		req.setAttribute("destination", "create");
		req.setAttribute("page", "form");
		req.getRequestDispatcher("/base.jsp").forward(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

		String createdByString = "";
		String createdByIdString = "";
		HttpSession session = req.getSession();
		if (session.getAttribute("userEmail") != null) { // Does the user have a logged in session?
			createdByString = (String) session.getAttribute("userEmail");
			createdByIdString = (String) session.getAttribute("userId");
		}

        Restaurant res = new Restaurant.Builder().restName(params.get("restName"))
                .address(params.get("address"))
                .maxCapacity(params.get("maxCapacity"))
                .occupiedSeats("0")
                .imageUrl(null == newImageUrl ? params.get("imageUrl") : newImageUrl)
                .createdBy(createdByString)
                .createdById(createdByIdString)
                .createDt(new Date())
                .contactNumber(params.get("contactNumber"))
                .cuisine(params.get("cuisine"))
                .build();

		RestaurantDAO dao = (RestaurantDAO) this.getServletContext().getAttribute("resDAO");
		String id = dao.createRestaurant(res);
		logger.log(Level.INFO, "Created restaurant {0}", res);
		resp.sendRedirect("/read?id=" + id);
	}
}
