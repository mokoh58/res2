/* Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.servlets;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.FavouriteDAO;
import com.dao.ReservationDAO;
import com.dao.RestaurantDAO;
import com.dao.ReviewDAO;
import com.objects.Favourite;
import com.objects.Reservation;
import com.objects.Restaurant;
import com.objects.Result;
import com.objects.Review;
import com.objects.UserAccount;
import com.util.DateUtil;

@SuppressWarnings("serial")
@WebServlet(name = "read", urlPatterns = { "/read" })
public class ReadRestaurantServlet extends HttpServlet {

	private final Logger logger = Logger.getLogger(ReadRestaurantServlet.class.getName());

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");

        if (null != id){
            req.getSession().setAttribute("currentViewingRestaurantId", id);
        }else {
            id = (String) req.getSession().getAttribute("currentViewingRestaurantId");
        }

        RestaurantDAO dao = (RestaurantDAO) this.getServletContext().getAttribute("resDAO");
        ReservationDAO resoDAO = (ReservationDAO) this.getServletContext().getAttribute("resoDAO");
        FavouriteDAO favDAO = (FavouriteDAO) this.getServletContext().getAttribute("favouriteDAO");
        ReviewDAO reviewDAO = (ReviewDAO) this.getServletContext().getAttribute("reviewDAO");

        UserAccount user = null;

        String startCursor = req.getParameter("cursor");
        String endCursor = null;
        
       List<Reservation> reservations = null;
       try {
			Result<Reservation> result = resoDAO.listReservationsByRestaurant(id, startCursor);
			logger.log(Level.INFO, "Retrieved list of all reservations");
			reservations = result.getResult();
			endCursor = result.getCursor();
		} catch (Exception e) {
			throw new ServletException("Error listing reservations", e);
        }

        if (req.getSession().getAttribute("userAccount") != null){
            user = (UserAccount)req.getSession().getAttribute("userAccount");
            logger.log(Level.INFO, "User " + user.getUserAccountId());
            reservations = getUserReservations(reservations, user);
        } else {
            reservations.clear();
        }

        req.getSession().getServletContext().setAttribute("reservations", reservations);

        Integer activeResoPax = checkActiveReservations(reservations);

        Restaurant res = dao.readRestaurant(id);

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
        int averageRating = 0;
        
        if (totalReviews > 0)
            averageRating = totalRating / totalReviews;

        req.setAttribute("rating1", rating1);
        req.setAttribute("rating2", rating2);
        req.setAttribute("rating3", rating3);
        req.setAttribute("rating4", rating4);
        req.setAttribute("rating5", rating5);
        req.setAttribute("averateRating", averageRating);
        req.setAttribute("totalReviews", totalReviews);

        req.setAttribute("currCapacity", currCapacity.toString());
		req.setAttribute("restaurant", res);
		req.setAttribute("page", "view");
		req.getRequestDispatcher("/base.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        String addPax = req.getParameter("addPax");
        String id = req.getParameter("restId");
        String maxCap = req.getParameter("maxCapacity");

        String oldOccSeats = req.getParameter("occupiedSeats");
        Integer maxCapInt = Integer.parseInt(maxCap);
        Integer oldOccSeatsInt = Integer.parseInt(oldOccSeats);

        logger.log(Level.INFO, "OldOccupiedSeats " + oldOccSeatsInt);

        RestaurantDAO dao = (RestaurantDAO) this.getServletContext().getAttribute("resDAO");

        if(addPax != null) {
            logger.log(Level.INFO, "AddPax " + addPax);
            Integer numPax = Integer.parseInt(addPax);

            if(req.getParameter("add") != null)
                oldOccSeatsInt += numPax;
            else if(req.getParameter("subtract") != null)
                oldOccSeatsInt -= numPax;
        }

        logger.log(Level.INFO, "OldOccupiedSeats " + oldOccSeatsInt);

        if(oldOccSeatsInt > 0 && (oldOccSeatsInt <= maxCapInt || oldOccSeatsInt >= maxCapInt))
            dao.UpdateOccupiedSeats(id,oldOccSeatsInt);
        else 
            logger.log(Level.INFO, "FULL ALR LAH!");

        resp.sendRedirect("/read?id=" + id);
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

    private List<Reservation> getUserReservations(List<Reservation> resoList, UserAccount user) {
        List<Reservation> reservations = new ArrayList<>();

        if(!user.getAccountType().equals("Consumer"))
            return resoList;

        for(Reservation reso: resoList) {
            logger.log(Level.INFO, "reso account id " + reso.getUserAccountId());

            if(user.getUserAccountId().equals(reso.getUserAccountId()))
                reservations.add(reso);
        }

        return reservations;
    }
}
