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

import com.dao.ReservationDAO;
import com.dao.RestaurantDAO;
import com.objects.Reservation;
import com.objects.Restaurant;
import com.objects.Result;
import com.util.DateUtil;

@SuppressWarnings("serial")
@WebServlet(name = "read", urlPatterns = { "/read" })
public class ReadRestaurantServlet extends HttpServlet {

	private final Logger logger = Logger.getLogger(ReadRestaurantServlet.class.getName());

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        RestaurantDAO dao = (RestaurantDAO) this.getServletContext().getAttribute("resDAO");
        ReservationDAO resoDAO = (ReservationDAO) this.getServletContext().getAttribute("resoDAO");

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

        req.setAttribute("currCapacity", currCapacity.toString());
		req.setAttribute("restaurant", res);
		req.setAttribute("page", "view");
		req.getRequestDispatcher("/base.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        String addPax = req.getParameter("addPax");
        String id = req.getParameter("id");
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
}
