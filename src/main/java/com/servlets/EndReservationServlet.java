package com.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.dao.ReservationDAO;;

@SuppressWarnings("serial")
@WebServlet(name = "end-reso", urlPatterns = { "/end-reso" })
public class EndReservationServlet extends HttpServlet {

    private final Logger logger = Logger.getLogger(EndReservationServlet.class.getName());

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.log(Level.INFO, "IN END RESO");

        String id = req.getParameter("id");
        String restId = req.getParameter("restId");
		ReservationDAO dao = (ReservationDAO) this.getServletContext().getAttribute("resoDAO");
		dao.endReservation(id);
		resp.sendRedirect("/read?id="+restId); 
	}
}

