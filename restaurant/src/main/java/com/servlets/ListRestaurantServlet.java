package com.servlets;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.RestaurantDAO;
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
		String startCursor = req.getParameter("cursor");
		List<Restaurant> restaurants = null;
		String endCursor = null;
		try {
			Result<Restaurant> result = dao.listRestaurants(startCursor);
			logger.log(Level.INFO, "Retrieved list of all restaurants");
			restaurants = result.getResult();
			endCursor = result.getCursor();
		} catch (Exception e) {
			throw new ServletException("Error listing restaurants", e);
		}
		req.getSession().getServletContext().setAttribute("restaurants", restaurants);
		StringBuilder restNames = new StringBuilder();
		for (Restaurant res : restaurants) {
			restNames.append(res.getRestName()).append(" ");
		}
		logger.log(Level.INFO, "Loaded restaurants: " + restNames.toString());
		req.setAttribute("cursor", endCursor);
		req.setAttribute("page", "list");
		req.getRequestDispatcher("/base.jsp").forward(req, resp);
	}
}