
package com.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("serial")
@WebServlet(name = "errors", urlPatterns = { "/errors" })
public class ErrorsRestaurantServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(CreateRestaurantServlet.class.getName());

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
		logger.log(Level.INFO, "Exception occured in Servlet: ");
	}
}
