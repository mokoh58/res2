package com.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.ReservationDAO;;

@SuppressWarnings("serial")
@WebServlet(name = "delete-reso", urlPatterns = { "/delete-reso" })
public class DeleteReservationServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        String restId = req.getParameter("restId");
		ReservationDAO dao = (ReservationDAO) this.getServletContext().getAttribute("resoDAO");
		dao.deleteReservation(id);
		resp.sendRedirect("/read?id="+restId); 
	}
}

