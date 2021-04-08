package com.servlets;

import com.dao.RestaurantDAO;
import com.dao.ReservationDAO;
import com.google.common.base.Strings;
import com.objects.Reservation;
import com.objects.Restaurant;
import com.util.CloudStorageHelper;
import java.util.Date;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.util.List;
import com.google.cloud.Timestamp;

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

@SuppressWarnings("serial")
@WebServlet(name = "update-reso", urlPatterns = { "/update-reso" })
public class UpdateReservationServlet extends HttpServlet {

    private final Logger logger = Logger.getLogger(UpdateReservationServlet.class.getName());

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
		ReservationDAO dao = (ReservationDAO) this.getServletContext().getAttribute("resoDAO");
		try {
			Reservation reservation = dao.readReservation(req.getParameter("id"));
			req.setAttribute("reservation", reservation);
			req.setAttribute("action", "Edit");
			req.setAttribute("destination", "update");
			req.setAttribute("page", "MakeReso");
			req.getRequestDispatcher("/base.jsp").forward(req, resp);
		} catch (Exception e) {
			throw new ServletException("Error loading Reso for editing", e);
		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// RestaurantDAO dao = (RestaurantDAO) this.getServletContext().getAttribute("resDAO");

		// assert ServletFileUpload.isMultipartContent(req);
		// CloudStorageHelper storageHelper = (CloudStorageHelper) getServletContext().getAttribute("storageHelper");

		// String newImageUrl = null;
		// Map<String, String> params = new HashMap<String, String>();
		// try {
		// 	FileItemIterator iter = new ServletFileUpload().getItemIterator(req);
		// 	while (iter.hasNext()) {
		// 		FileItemStream item = iter.next();
		// 		if (item.isFormField()) {
		// 			params.put(item.getFieldName(), Streams.asString(item.openStream()));
		// 		} else if (!Strings.isNullOrEmpty(item.getName())) {
		// 			newImageUrl = storageHelper.uploadFile(item, System.getenv("RES_BUCKET"));
		// 		}
		// 	}
		// } catch (FileUploadException e) {
		// 	throw new IOException(e);
        // }
        
        // logger.log(Level.INFO, "UpdateRestaurantServlet IMG URL: " + newImageUrl);

		// Restaurant oldRest = dao.readRestaurant(params.get("id"));

        // Restaurant res = new Restaurant.Builder()
        //         .restName(params.get("restName"))
        //         .address(params.get("address"))
		// 		.maxCapacity(params.get("maxCapacity"))
        //         .imageUrl(null == newImageUrl ? params.get("imageUrl") : newImageUrl)
        //         .id(params.get("id"))
        //         .createdBy(oldRest.getCreatedBy())
        //         .createdById(oldRest.getCreatedById())
        //         .contactNumber(params.get("contactNumber"))
		// 		.cuisine(params.get("cuisine")).build();

		// dao.updateRestaurant(res);
        // resp.sendRedirect("/read?id=" + params.get("id"));
        RestaurantDAO restDAO = (RestaurantDAO) this.getServletContext().getAttribute("resDAO");
        ReservationDAO resoDAO = (ReservationDAO) this.getServletContext().getAttribute("resoDAO");
        assert ServletFileUpload.isMultipartContent(req);
        CloudStorageHelper storageHelper = (CloudStorageHelper) getServletContext().getAttribute("storageHelper");
        
        Map<String, String> params = new HashMap<String, String>();
        Timestamp resoTS = null;

        try {
			FileItemIterator iter = new ServletFileUpload().getItemIterator(req);
			while (iter.hasNext()) {
				FileItemStream item = iter.next();
				if (item.isFormField()) {
					params.put(item.getFieldName(), Streams.asString(item.openStream()));
				}
			}
		} catch (FileUploadException e) {
			throw new IOException(e);
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            Date resoDate = sdf.parse(params.get("resoDate") + " " + params.get("resoTime"));

            logger.log(Level.INFO, "Date is " + resoDate.toString());

            resoTS = Timestamp.of(resoDate); 
        } catch (Exception e) {
            e.printStackTrace();
        }

        String restId = params.get("restId");
        Reservation oldReso = resoDAO.readReservation(params.get("id"));
        Restaurant res = null;
        List<Reservation> resoList = null;
        try {
            res = restDAO.readRestaurant(restId);
            resoList = resoDAO.getReservationsByRestaurant(restId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Reservation reso = new Reservation.Builder()
                .resoName(params.get("resoName"))
                .resoContact(params.get("resoContact"))
                .resoDate(params.get("resoDate"))
                .resoTime(params.get("resoTime"))
                .restId(restId)
                .createdBy(oldReso.getCreatedBy())
                .createdById(oldReso.getCreatedById())
                .numPax(params.get("numPax"))
                .build();

        if(checkSpace(reso, resoList, res)) {
            resoDAO.updateReservation(reso);
            logger.log(Level.INFO, "Updated reservation {0}", reso);
        } 
        else {
            //Error here
        }
        resp.sendRedirect("/read?id=" + restId);
    }
    
    private Boolean checkSpace(Reservation currReso, List<Reservation> resoList, Restaurant restaurant) {
        try {
            if(resoList!= null && resoList.size() > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                Date currResoDate = sdf.parse(currReso.getResoDate() + " " + currReso.getResoTime());
                //LocalDateTime currResoLDT = convertToLocalDateTime(currResoDate);

                Map<Date, Integer> resoMap = new HashMap<Date, Integer>();
                Integer totalPaxForDate = 0;
                Integer totalCapacity = Integer.parseInt(restaurant.getMaxCapacity());
                for(Reservation reso : resoList) {
                    Date resoDate = sdf.parse(reso.getResoDate() + " " + reso.getResoTime());
                    Integer numPax = Integer.parseInt(reso.getNumPax());
                    if(currReso.equals(resoDate)) {
                        totalPaxForDate += numPax;
                    }
                }
                logger.log(Level.INFO, "totalCapacity " + totalCapacity);
                logger.log(Level.INFO, "totalPaxForDate " + totalPaxForDate);

                Integer currCapacity = totalCapacity = totalPaxForDate;
                Integer currPax = Integer.parseInt(currReso.getNumPax());
                logger.log(Level.INFO, "currCapacity " + currCapacity);
                logger.log(Level.INFO, "currPax " + currPax);

                if(currPax < currCapacity) {
                    logger.log(Level.INFO, "No Space Lah!");
                    return false;
                }
                else {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}
