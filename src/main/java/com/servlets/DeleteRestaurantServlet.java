package com.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.RestaurantDAO;
import com.dao.TagsDAO;


@SuppressWarnings("serial")
@WebServlet(name = "delete", urlPatterns = { "/delete" })
public class DeleteRestaurantServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String id = req.getParameter("id");
		RestaurantDAO dao = (RestaurantDAO) this.getServletContext().getAttribute("resDAO");
        dao.deleteRestaurant(id);
        
        // Deletion of Tags
        TagsDAO tagsDAO = (TagsDAO) this.getServletContext().getAttribute("tagsDAO");

        // Delete all tags first
        tagsDAO.deleteTags(id);

		resp.sendRedirect("/restaurants");
	}
}

