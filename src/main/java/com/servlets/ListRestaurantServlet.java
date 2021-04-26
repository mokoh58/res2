package com.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.Math;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.RestaurantDAO;
import com.dao.FavouriteDAO;
import com.objects.Restaurant;
import com.objects.Result;

// a url pattern of "" makes this servlet the root servlet
@SuppressWarnings("serial")
@WebServlet(name = "list", urlPatterns = { "", "/restaurants" }, loadOnStartup = 1)
public class ListRestaurantServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(ListRestaurantServlet.class.getName());

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        RestaurantDAO dao = (RestaurantDAO) this.getServletContext().getAttribute("resDAO");
        FavouriteDAO favDao = (FavouriteDAO) this.getServletContext().getAttribute("favouriteDAO");
        String startCursor = req.getParameter("cursor");
        String searchRes = req.getParameter("searchRes");
        String userId = req.getParameter("userId");
        logger.log(Level.INFO, "searchRes = " + searchRes);
        List<Restaurant> filteredRes = new ArrayList<Restaurant>();
        List<Restaurant> favouriteRes = new ArrayList<Restaurant>();
		List<Restaurant> restaurants = null;
		String endCursor = null;
		try {
			Result<Restaurant> result = dao.listRestaurants(startCursor);
			logger.log(Level.INFO, "Retrieved list of all restaurants");
            restaurants = result.getResult();
            if (searchRes != null) {
                for(Restaurant rest: restaurants) {
                    if (rest.getRestName().contains(searchRes)) {
                        logger.log(Level.INFO, "filtered res = " + rest.getRestName());
                        filteredRes.add(rest);
                    }
                }
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
            }
			endCursor = result.getCursor();
		} catch (Exception e) {
			throw new ServletException("Error listing restaurants", e);
        }

        

        if (userId != null){
            req.getSession().getServletContext().setAttribute("restaurants", favouriteRes);
        }
        else if (searchRes != null) {
            req.getSession().getServletContext().setAttribute("restaurants", filteredRes);
        } else {
            req.getSession().getServletContext().setAttribute("restaurants", restaurants);
        }
		
        StringBuilder restNames = new StringBuilder();
        
        if (userId != null){
            for (Restaurant res : favouriteRes) {
                restNames.append(res.getRestName()).append(" ");
            }
        }
        else if (searchRes != null) {
            for (Restaurant res : filteredRes) {
                restNames.append(res.getRestName()).append(" ");
            }
        } else {
            for (Restaurant res : restaurants) {
                restNames.append(res.getRestName()).append(" ");
            }
        }
        logger.log(Level.INFO, "Loaded restaurants: " + restNames.toString());
        
        // Only put cursor if not Favourite List
        if (null == userId){
            req.setAttribute("cursor", endCursor);
        }
		
		req.setAttribute("page", "list");
		req.getRequestDispatcher("/base.jsp").forward(req, resp);
    }

    private void setRestoCrowdLevel(List<Restaurant> restoList) {
        for(Restaurant item : restoList) {
            String maxCap = item.getMaxCapacity();
            Integer maxCapInt = Integer.parseInt(maxCap);

            String occSeats = item.getOccupiedSeats();
            Integer occSeatsInt = Integer.parseInt(occSeats);

            Integer percentage = (int)Math.round(occSeatsInt * 100.0/maxCapInt);

            if(percentage <= 50) {
                item.setCrowdLevel("Available");
            } else if(percentage >= 51 && percentage <= 80) {
                item.setCrowdLevel("Filling Up");
            } else {
                item.setCrowdLevel("Crowded");
            }
        }
    }
}