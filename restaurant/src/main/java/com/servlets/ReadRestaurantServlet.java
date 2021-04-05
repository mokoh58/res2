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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.*;
import com.objects.*;

@SuppressWarnings("serial")
@WebServlet(name = "read", urlPatterns = { "/read" })
public class ReadRestaurantServlet extends HttpServlet {

	private final Logger logger = Logger.getLogger(ReadRestaurantServlet.class.getName());

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");
        RestaurantDAO dao = (RestaurantDAO) this.getServletContext().getAttribute("resDAO");
        ReservationDAO resoDAO = (ReservationDAO) this.getServletContext().getAttribute("resoDAO");
        Restaurant res = dao.readRestaurant(id);
        String startCursor = req.getParameter("cursor");
        String endCursor = null;
        logger.log(Level.INFO, "Read restaurant with id {0}", id);

        List<Reservation> reservations = null;

       try {
			Result<Reservation> result = resoDAO.listReservationsByRestaurant(id, startCursor);
			logger.log(Level.INFO, "Retrieved list of all reservations");
			reservations = result.getResult();
			endCursor = result.getCursor();
		} catch (Exception e) {
			throw new ServletException("Error listing reservations", e);
        }
        
        StringBuilder resoNames = new StringBuilder();
		for (Reservation reso : reservations) {
			resoNames.append(reso.getResoName()).append(" ");
		}
		logger.log(Level.INFO, "Loaded reservations: " + resoNames.toString());

        req.getSession().getServletContext().setAttribute("reservations", reservations);

        Integer maxCap = Integer.parseInt(res.getMaxCapacity());
        Integer occSeats = Integer.parseInt(res.getOccSeats());

        Integer currCapacity = maxCap - occSeats;

        req.setAttribute("currCapacity", currCapacity.toString());
		req.setAttribute("restaurant", res);
		req.setAttribute("page", "view");
		req.getRequestDispatcher("/base.jsp").forward(req, resp);
    }
}
