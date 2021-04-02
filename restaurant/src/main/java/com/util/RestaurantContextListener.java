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

package com.util;

import com.example.getstarted.daos.BookDao;
import com.dao.FirestoreRestaurantDAO;
import com.dao.RestaurantDAO;
import com.google.common.base.Strings;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener("Creates restDAO and other servlet context objects for reuse.")
public class RestaurantContextListener implements ServletContextListener {
	@Override
	public void contextDestroyed(javax.servlet.ServletContextEvent event) {
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		// This function is called when the application starts and will safely set a few
		// required
		// context attributes such as the BookDao.

		RestaurantDAO dao = (RestaurantDAO) event.getServletContext().getAttribute("resDAO");
		if (dao == null) {
			dao = new FirestoreRestaurantDAO();
			event.getServletContext().setAttribute("resDAO", dao);
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
