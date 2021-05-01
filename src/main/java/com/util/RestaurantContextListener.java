package com.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import com.google.common.base.Strings;

import com.dao.FirestoreOperatingHoursDAO;
import com.dao.FirestoreTagsDAO;
import com.dao.FirestoreReservationDAO;
import com.dao.FirestoreRestaurantDAO;
import com.dao.FirestoreReviewDAO;
import com.dao.FirestoreUserAccountDAO;
import com.dao.FirestoreFavouriteDAO;
import com.dao.OperatingHoursDAO;
import com.dao.ReservationDAO;
import com.dao.TagsDAO;
import com.dao.RestaurantDAO;
import com.dao.UserAccountDAO;
import com.dao.FavouriteDAO;
import com.dao.ReviewDAO;

@WebListener("Creates restDAO and other servlet context objects for reuse.")
public class RestaurantContextListener implements ServletContextListener {
	@Override
	public void contextDestroyed(javax.servlet.ServletContextEvent event) {
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {

		RestaurantDAO restDAO = (RestaurantDAO) event.getServletContext().getAttribute("resDAO");
		if (restDAO == null) {
			restDAO = new FirestoreRestaurantDAO();
			event.getServletContext().setAttribute("resDAO", restDAO);
        }
        
        ReservationDAO resoDAO = (ReservationDAO) event.getServletContext().getAttribute("resoDAO");
		if (resoDAO == null) {
			resoDAO = new FirestoreReservationDAO();
			event.getServletContext().setAttribute("resoDAO", resoDAO);
        }
        
        UserAccountDAO userAccountDAO = (UserAccountDAO) event.getServletContext().getAttribute("userAccountDAO");
		if (userAccountDAO == null) {
			userAccountDAO = new FirestoreUserAccountDAO();
            event.getServletContext().setAttribute("userAccountDAO", userAccountDAO);
            
        FavouriteDAO favouriteDAO = (FavouriteDAO) event.getServletContext().getAttribute("favouriteDAO");
		if (favouriteDAO == null) {
			favouriteDAO = new FirestoreFavouriteDAO();
            event.getServletContext().setAttribute("favouriteDAO", favouriteDAO);
        }

        ReviewDAO reviewDAO = (ReviewDAO) event.getServletContext().getAttribute("reviewDAO");
		if (reviewDAO == null) {
			reviewDAO = new FirestoreReviewDAO();
            event.getServletContext().setAttribute("reviewDAO", reviewDAO);
        }

        TagsDAO tagsDAO = (TagsDAO) event.getServletContext().getAttribute("tagsDAO");
		if (tagsDAO == null) {
			tagsDAO = new FirestoreTagsDAO();
            event.getServletContext().setAttribute("tagsDAO", tagsDAO);
        }

        OperatingHoursDAO ohDAO = (OperatingHoursDAO) event.getServletContext().getAttribute("ohDAO");
		if (ohDAO == null) {
			ohDAO = new FirestoreOperatingHoursDAO();
			event.getServletContext().setAttribute("ohDAO", ohDAO);
		}

		Boolean isCloudStorageConfigured = (Boolean) event.getServletContext().getAttribute("isCloudStorageConfigured");
		if (isCloudStorageConfigured == null) {
            event.getServletContext().setAttribute("isCloudStorageConfigured",
                                                !Strings.isNullOrEmpty(System.getenv("RES_BUCKET")));
		}

		CloudStorageHelper storageHelper = (CloudStorageHelper) event.getServletContext().getAttribute("storageHelper");
		if (storageHelper == null) {
			storageHelper = new CloudStorageHelper();
			event.getServletContext().setAttribute("storageHelper", storageHelper);
		}
	}
}
}