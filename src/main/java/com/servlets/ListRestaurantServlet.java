package com.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import java.lang.Math;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.text.NumberFormat;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.RestaurantDAO;
import com.dao.ReviewDAO;
import com.dao.TagsDAO;
import com.dao.FavouriteDAO;
import com.dao.ReservationDAO;
import com.objects.Restaurant;
import com.objects.Reservation;
import com.objects.Result;
import com.objects.Review;
import com.objects.Tags;
import com.objects.UserAccount;
import com.util.DateUtil;

// a url pattern of "" makes this servlet the root servlet
@SuppressWarnings("serial")
@WebServlet(name = "list", urlPatterns = { "", "/restaurants" }, loadOnStartup = 1)
public class ListRestaurantServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(ListRestaurantServlet.class.getName());

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        RestaurantDAO dao = (RestaurantDAO) this.getServletContext().getAttribute("resDAO");
        ReservationDAO resoDAO = (ReservationDAO) this.getServletContext().getAttribute("resoDAO");
        FavouriteDAO favDao = (FavouriteDAO) this.getServletContext().getAttribute("favouriteDAO");
        TagsDAO tagsDAO = (TagsDAO) this.getServletContext().getAttribute("tagsDAO");
        String startCursor = req.getParameter("cursor");
        String searchRes = req.getParameter("searchRes");
        String userId = req.getParameter("userId");
        String ownerId = req.getParameter("ownerId");
        String recList = req.getParameter("recList");
        String category = req.getParameter("category");
        String sortByRating = req.getParameter("sortByRating");
        logger.log(Level.INFO, "searchRes = " + searchRes);
        List<Restaurant> filteredRes = new ArrayList<Restaurant>();
        List<Restaurant> favouriteRes = new ArrayList<Restaurant>();
        List<Restaurant> ownerRes = new ArrayList<Restaurant>();
        List<Restaurant> recRes = new ArrayList<Restaurant>();
        List<Restaurant> catRes = new ArrayList<Restaurant>();
		List<Restaurant> restaurants = null;
		String endCursor = null;
		try {
			Result<Restaurant> result = dao.listRestaurants(startCursor);
			logger.log(Level.INFO, "Retrieved list of all restaurants");
            restaurants = result.getResult();
            setRestoCrowdLevel(restaurants, resoDAO, startCursor);

            if (searchRes != null) {
                searchRes = searchRes.toLowerCase();
                for(Restaurant rest: restaurants) {
                    String resName = rest.getRestName().toLowerCase();
                    if (resName.contains(searchRes)) {
                        logger.log(Level.INFO, "filtered res = " + rest.getRestName());
                        filteredRes.add(rest);
                    }
                }
                restaurants = filteredRes;
            }
            // Favourites List
            if (userId != null) {
                ArrayList<String> favouriteList = favDao.listFavouriteByUser(userId);
                for(Restaurant rest: restaurants) {
                    if (favouriteList.contains(rest.getId())) {
                        //logger.log(Level.INFO, "favourite res = " + rest.getRestName());
                        favouriteRes.add(rest);
                    }
                }
                restaurants = favouriteRes;
            }

            // Category search
            if (category != null){
                for (Restaurant rest : restaurants){
                    List<String> tags = tagsDAO.getStringTags(rest.getId());
                    if (tags.contains(category))
                        catRes.add(rest);
                }
                restaurants = catRes;
            }

            // Display owner's restaurants
            if (ownerId != null){
                for(Restaurant rest: restaurants) {
                    if (ownerId.equals(rest.getCreatedById())) {
                        ownerRes.add(rest);
                    }
                }
                restaurants = ownerRes;
            }

            if (recList != null){
                for(Restaurant rest: restaurants) {
                    if (recList.contains(rest.getId())) {
                        recRes.add(rest);
                    }
                }
                restaurants = recRes;
            }
			endCursor = result.getCursor();
		} catch (Exception e) {
			throw new ServletException("Error listing restaurants", e);
        }

        ReviewDAO reviewDAO = (ReviewDAO) this.getServletContext().getAttribute("reviewDAO");
        for (Restaurant rest : restaurants){
            List<Review> reviewList = reviewDAO.getReviewsByRestaurant(rest.getId());
            int totalRating = 0;
            int totalReviews = reviewList.size();
            NumberFormat formatter = new DecimalFormat("#0.0");
            for (Review review : reviewList){
                totalRating = totalRating + Integer.parseInt(review.getRating());
            }  
            float averageRating = 0.0f;
            if (totalReviews > 0){
                averageRating = (float) totalRating / totalReviews;
            }
            String average = formatter.format(averageRating);
            rest.setTotalReviews(String.valueOf(totalReviews));
            rest.setAverageRating(average);

            int numOfStars = (int) averageRating;
            rest.setNumOfStars(numOfStars);
        }

        // Recommendations based on ratings
        if (sortByRating != null && restaurants != null && restaurants.size() > 0){
            restaurants.sort(Comparator.comparing(Restaurant::getAverageRating).reversed());
        }

        // if (userId != null){
        //     req.getSession().getServletContext().setAttribute("restaurants", favouriteRes);
        // }
        // else if (ownerId != null){
        //     req.getSession().getServletContext().setAttribute("restaurants", ownerRes);
        // }
        // else if (searchRes != null) {
        //     req.getSession().getServletContext().setAttribute("restaurants", filteredRes);
        // }
        // else if (recList != null) {
        //     req.getSession().getServletContext().setAttribute("restaurants", recRes);
        // }
        // else if (category != null){
        //     req.getSession().getServletContext().setAttribute("restaurants", catRes);
        // }
        // else {
        //     req.getSession().getServletContext().setAttribute("restaurants", restaurants);
        // }

        req.getSession().getServletContext().setAttribute("restaurants", restaurants);
		
        StringBuilder restNames = new StringBuilder();
        
        // if (userId != null){
        //     for (Restaurant res : favouriteRes) {
        //         restNames.append(res.getRestName()).append(" ");
        //     }
        // }
        // else if (ownerId != null){
        //     for (Restaurant res : ownerRes) {
        //         restNames.append(res.getRestName()).append(" ");
        //     }
        // }
        // else if (searchRes != null) {
        //     for (Restaurant res : filteredRes) {
        //         restNames.append(res.getRestName()).append(" ");
        //     }
        // }
        // else if (recList != null){
        //     for (Restaurant res : recRes) {
        //         restNames.append(res.getRestName()).append(" ");
        //     }
        // }
        // else if (category != null){
        //     for (Restaurant res : catRes) {
        //         restNames.append(res.getRestName()).append(" ");
        //     }
        // }
        // else {
        //     for (Restaurant res : restaurants) {
        //         restNames.append(res.getRestName()).append(" ");
        //     }
        // }

        for (Restaurant res : restaurants) {
            restNames.append(res.getRestName()).append(" ");
        }
        logger.log(Level.INFO, "Loaded restaurants: " + restNames.toString());
        
        // Only put cursor if not Favourite / Owner List
        if (null == userId && null == ownerId){
            req.setAttribute("cursor", endCursor);
        }
		
		req.setAttribute("page", "list");
		req.getRequestDispatcher("/base.jsp").forward(req, resp);
    }

    private void setRestoCrowdLevel(List<Restaurant> restoList, ReservationDAO resoDAO, String startCursor) {
        for(Restaurant item : restoList) {
            Result<Reservation> result = resoDAO.listReservationsByRestaurant(item.getId(), startCursor);
            List<Reservation> reservations = result.getResult();

            Integer activeResoPax = checkActiveReservations(reservations);

            logger.log(Level.INFO, "activeResoPax: " + activeResoPax);

            String maxCap = "";
            Integer maxCapInt = 0;
            if(item.getMaxCapacity() != null) {
                maxCap = item.getMaxCapacity();
                maxCapInt = Integer.parseInt(maxCap);
            }

            String occSeats = "";
            Integer occSeatsInt = 0;
            if(item.getOccupiedSeats() != null) {
                occSeats = item.getOccupiedSeats();
                occSeatsInt = Integer.parseInt(occSeats);
            }

            Integer percentageEmpty = 100;

            Integer currCapacity = maxCapInt - occSeatsInt - activeResoPax;

            logger.log(Level.INFO, "currCapacity: " + currCapacity + " for restaurant: " + item.getRestName());

            if(currCapacity <= 0)
                currCapacity = 0;

            if(maxCapInt != 0)
                percentageEmpty = (int)Math.round(currCapacity * 100.0/maxCapInt);

            if(percentageEmpty <= 20) {
                item.setCrowdLevel("Crowded");
            } else if(percentageEmpty >= 21 && percentageEmpty <= 51) {
                item.setCrowdLevel("Filling Up");
            } else {
                item.setCrowdLevel("Available");
            }
        }
    }

    public Integer checkActiveReservations(List<Reservation> resoList) {
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
}