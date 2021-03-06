package com.servlets;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.NumberFormat;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.FavouriteDAO;
import com.dao.ReservationDAO;
import com.dao.RestaurantDAO;
import com.dao.ReviewDAO;
import com.dao.TagsDAO;
import com.objects.Favourite;
import com.objects.Reservation;
import com.objects.Restaurant;
import com.objects.Result;
import com.objects.Review;
import com.objects.Tags;
import com.objects.UserAccount;
import com.util.DateUtil;

@SuppressWarnings("serial")
@WebServlet(name = "read", urlPatterns = { "/read" })
public class ReadRestaurantServlet extends HttpServlet {

	private final Logger logger = Logger.getLogger(ReadRestaurantServlet.class.getName());

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");

        String mapParam = "";

        Boolean canShowAddSubtract = false;

        if (null != id){
            req.getSession().setAttribute("currentViewingRestaurantId", id);
        }else {
            id = (String) req.getSession().getAttribute("currentViewingRestaurantId");
        }

        RestaurantDAO dao = (RestaurantDAO) this.getServletContext().getAttribute("resDAO");
        ReservationDAO resoDAO = (ReservationDAO) this.getServletContext().getAttribute("resoDAO");
        FavouriteDAO favDAO = (FavouriteDAO) this.getServletContext().getAttribute("favouriteDAO");
        ReviewDAO reviewDAO = (ReviewDAO) this.getServletContext().getAttribute("reviewDAO");
        TagsDAO tagDAO = (TagsDAO) this.getServletContext().getAttribute("tagsDAO");

        UserAccount user = null;

        String startCursor = req.getParameter("cursor");
        String endCursor = null;
        
        List<Reservation> reservations = null;
        try {
            Result<Reservation> result = resoDAO.listReservationsByRestaurant(id, startCursor);
            //logger.log(Level.INFO, "Retrieved list of all reservations");
            reservations = result.getResult();
            endCursor = result.getCursor();
        } catch (Exception e) {
            //throw new ServletException("Error listing reservations", e);
            e.printStackTrace();
        }

        Restaurant res = dao.readRestaurant(id);

        reservations = listResoNotEnded(reservations);

        if (req.getSession().getAttribute("userAccount") != null){
            user = (UserAccount)req.getSession().getAttribute("userAccount");
            //logger.log(Level.INFO, "User " + user.getUserAccountId());
            reservations = getUserReservations(reservations, user, res.getCreatedBy());
        } else {
            reservations.clear();
        }

        req.getSession().getServletContext().setAttribute("reservations", reservations);

        Integer activeResoPax = checkActiveReservations(reservations);

        String resAdd = res.getAddress();

        mapParam = URLEncoder.encode(resAdd);

        //logger.log(Level.INFO, "mapParam " + mapParam);

        Integer maxCap = Integer.parseInt(res.getMaxCapacity());
        
        // Prevent null pointer for res.getOccupiedSeats()
        if (null == res.getOccupiedSeats()){
            res.setOccupiedSeats("0");
        }
        Integer occSeats = Integer.parseInt(res.getOccupiedSeats());

        Integer currCapacity = maxCap - occSeats - activeResoPax;

        if(currCapacity <= 0)
            currCapacity = 0;

        // Handle favourite button
        req.getSession().removeAttribute("favourite"); // Clear first
        if (null != user){
            String hasFavourite = "N";
            if (null != favDAO.hasFavourite(id, user.getUserAccountId())){
                hasFavourite = "Y";
            }
            req.getSession().setAttribute("favourite", hasFavourite);
        }

        if(null != user && (user.getAccountType().equals("Administrator") 
        || (user.getAccountType().equals("Restaurant Owner") && user.getUsername().equals(res.getCreatedBy())))) {
            canShowAddSubtract = true;
        }

        // Set reviews to request attribute
        List<Review> reviewList = reviewDAO.getReviewsByRestaurant(id);
        req.setAttribute("reviews", reviewList);

        int rating1 = 0, rating2 = 0, rating3 = 0, rating4 = 0, rating5 = 0;
        int totalRating = 0;
        int totalReviews = reviewList.size();

        for (Review review : reviewList){
            totalRating = totalRating + Integer.parseInt(review.getRating());
            if (review.getRating().equals("1"))
                rating1++;
            else if (review.getRating().equals("2"))
                rating2++;
            else if (review.getRating().equals("3"))
                rating3++;
            else if (review.getRating().equals("4"))
                rating4++;
            else if (review.getRating().equals("5"))
                rating5++;
        }
        float averageRating = 0.0f;

        if (totalReviews > 0)
            averageRating = (float) totalRating / totalReviews;

        NumberFormat formatter = new DecimalFormat("#0.0");

        req.setAttribute("rating1", rating1);
        req.setAttribute("rating2", rating2);
        req.setAttribute("rating3", rating3);
        req.setAttribute("rating4", rating4);
        req.setAttribute("rating5", rating5);
        req.setAttribute("averateRating", formatter.format(averageRating));
        req.setAttribute("totalReviews", totalReviews);


        // Tags
        ArrayList<Tags> tags = tagDAO.getTags(id);
        if (null != tags){
            List<String> tagsList = new ArrayList<String>();
            for (Tags tag : tags){
                tagsList.add(tag.getTag());
            }
            String displayTags = String.join(",", tagsList);
            req.setAttribute("tags", displayTags);
            System.out.println("======================" + displayTags);
        }

        req.getSession().setAttribute("canShowAddSubtract", canShowAddSubtract);
        req.setAttribute("currCapacity", currCapacity.toString());
        req.setAttribute("mapParam", mapParam);
		req.setAttribute("restaurant", res);
		req.setAttribute("page", "view");
		req.getRequestDispatcher("/base.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        String addPax = req.getParameter("addPax");
        String id = req.getParameter("restId");
        String maxCap = req.getParameter("maxCapacity");

        String occSeats = req.getParameter("occupiedSeats");
        Integer maxCapInt = Integer.parseInt(maxCap);
        Integer occSeatsInt = Integer.parseInt(occSeats);

        logger.log(Level.INFO, "OccupiedSeats " + occSeatsInt);

        RestaurantDAO dao = (RestaurantDAO) this.getServletContext().getAttribute("resDAO");

        if(addPax != null) {
            logger.log(Level.INFO, "AddPax " + addPax);
            Integer numPax = Integer.parseInt(addPax);

            if(req.getParameter("add") != null)
                occSeatsInt += numPax;
            else if(req.getParameter("subtract") != null)
                occSeatsInt -= numPax;

            if(occSeatsInt < 0)
                occSeatsInt = 0;
            else if(occSeatsInt >= maxCapInt)
                occSeatsInt = maxCapInt;
        }

        logger.log(Level.INFO, "OccupiedSeats " + occSeatsInt);

        if(occSeatsInt >= 0 && (occSeatsInt <= maxCapInt || occSeatsInt >= maxCapInt))
            dao.UpdateOccupiedSeats(id,occSeatsInt);
        else 
            logger.log(Level.INFO, "FULL ALR LAH!");

        resp.sendRedirect("/read?id=" + id);
    }

    public Integer checkActiveReservations(List<Reservation> resoList) {
        if(resoList == null || resoList.size() <= 0)
            return 0;

        ZoneId zid = ZoneId.of("GMT+8");
        ZonedDateTime currTime = ZonedDateTime.now(zid);

        Integer totalPax = 0;

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));

            for(Reservation reso : resoList) {
                String numPax = reso.getNumPax();
                Integer numPaxInt = Integer.parseInt(numPax);
                Date resoDate = sdf.parse(reso.getResoDate() + " " + reso.getResoTime());

                ZonedDateTime resoLDTMin = DateUtil.convertToZonedDateTime(resoDate);
                ZonedDateTime resoLDTMax = resoLDTMin.plusHours(2); 

                if(currTime.isAfter(resoLDTMin) && currTime.isBefore(resoLDTMax)) {
                    totalPax += numPaxInt;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalPax;
    }

    private List<Reservation> getUserReservations(List<Reservation> resoList, UserAccount user, String createdBy) {
        List<Reservation> reservations = new ArrayList<>();

        logger.log(Level.INFO, "~~~~~Created By:" + createdBy);

        if(user.getAccountType().equals("Administrator") 
        || (user.getAccountType().equals("Restaurant Owner") && user.getUsername().equals(createdBy)))
            return resoList;

        if(user.getAccountType().equals("Restaurant Owner") && !user.getUsername().equals(createdBy))
            return reservations;

        for(Reservation reso: resoList) {
            //logger.log(Level.INFO, "reso account id " + reso.getUserAccountId());

            if(user.getUserAccountId().equals(reso.getUserAccountId()))
                reservations.add(reso);
        }

        return reservations;
    }

    private List<Reservation> listResoNotEnded(List<Reservation> resoList) {
        List<Reservation> reservations = new ArrayList<>();
        for(Reservation reso : resoList) {
            if(reso.getResoEnded().equals("N")) 
                reservations.add(reso);
        }

        return reservations;
    }
}
