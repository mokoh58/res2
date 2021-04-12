package com.servlets;

import com.dao.RestaurantDAO;
import com.dao.OperatingHoursDAO;
import com.dao.ReservationDAO;
import com.google.common.base.Strings;
import com.objects.OperatingHoursCode;
import com.objects.Reservation;
import com.objects.Restaurant;
import com.util.CloudStorageHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
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
@WebServlet(name = "make-reso", urlPatterns = { "/make-reso" })
public class CreateReservationServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(CreateReservationServlet.class.getName());

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {        
        RestaurantDAO dao = (RestaurantDAO) this.getServletContext().getAttribute("resDAO");
        
        try {
            Restaurant res = dao.readRestaurant(req.getParameter("id"));

            String operatingHours = res.getOperatingHours();

            List<OperatingHoursCode> operatingHourList = null;

            if(operatingHours != null) {
                operatingHourList = getAvailableOperatingHours(operatingHours);
            }

            if(operatingHourList != null)
                req.setAttribute("operatingHourList", operatingHourList);

            req.setAttribute("restId", req.getParameter("id"));
            req.setAttribute("action", "Make");
            req.setAttribute("destination", "make-reso");
            req.setAttribute("page", "MakeReso");
            req.getRequestDispatcher("/base.jsp").forward(req, resp);
        } catch (Exception e) {
			throw new ServletException("Error loading restaurant for editing", e);
		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

		String createdByString = "";
		String createdByIdString = "";
		HttpSession session = req.getSession();
		if (session.getAttribute("userEmail") != null) { // Does the user have a logged in session?
			createdByString = (String) session.getAttribute("userEmail");
			createdByIdString = (String) session.getAttribute("userId");
        }
        
        String restId = params.get("restId");
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
                .createdBy(createdByString)
                .createdById(createdByIdString)
                .numPax(params.get("numPax"))
                .build();

        if(checkSpace(reso, resoList, res)) {
            String id = resoDAO.createReservation(reso);
            logger.log(Level.INFO, "Created reservation {0}", reso);
            resp.sendRedirect("/read?id=" + restId);
        }
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

    private List<OperatingHoursCode> getAvailableOperatingHours(String operatingHours) {
        OperatingHoursDAO dao = (OperatingHoursDAO) this.getServletContext().getAttribute("ohDAO");
        List<OperatingHoursCode> operatingHoursList = dao.listOperatingHours();
        List<OperatingHoursCode> activeOperatingHoursList = new ArrayList<>();
        String[] operatingHoursArray = operatingHours.split("-", 2);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");

        LocalTime startTime = LocalTime.parse(operatingHoursArray[0],dtf);
        LocalTime endTime = LocalTime.parse(operatingHoursArray[1],dtf);

        logger.log(Level.INFO, "endTime " + endTime.toString());

        // check start time
        for(OperatingHoursCode obj : operatingHoursList) {
            String code = obj.getCode();
            LocalTime currCode = LocalTime.parse(code,dtf);

            if(currCode.isBefore(startTime))
                continue;

            activeOperatingHoursList.add(obj);
        }

        // check end time
        for(int i = activeOperatingHoursList.size()-1; i >= 0; --i ) {
            OperatingHoursCode obj = activeOperatingHoursList.get(i);

            String code = obj.getCode();
            LocalTime currCode = LocalTime.parse(code,dtf);

            logger.log(Level.INFO, "currCode " + currCode.toString());

            if(currCode.equals(endTime) || currCode.isAfter(endTime)) {
                logger.log(Level.INFO, "currCode Removing at " + i);
                activeOperatingHoursList.remove(i);
            }
        }

        return activeOperatingHoursList;
    }
}
