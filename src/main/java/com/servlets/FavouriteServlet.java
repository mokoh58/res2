package com.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import com.dao.FavouriteDAO;
import com.dao.UserAccountDAO;
import com.google.common.base.Strings;
import com.objects.UserAccount;
import com.objects.Favourite;
import com.util.CloudStorageHelper;

@SuppressWarnings("serial")
@WebServlet(
    name = "favourite",
    urlPatterns = {"/favourite"})
public class FavouriteServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(FavouriteServlet.class.getName());

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.removeAttribute("action");

    UserAccountDAO dao = (UserAccountDAO) this.getServletContext().getAttribute("userAccountDAO");
    FavouriteDAO favDao = (FavouriteDAO) this.getServletContext().getAttribute("favouriteDAO");
    UserAccount userAccount;

    String restaurantId = null;

    if (null != req.getSession().getAttribute("userAccount")) {
        userAccount = (UserAccount) req.getSession().getAttribute("userAccount");

        if (null != userAccount){
            if (null != req.getSession().getAttribute("currentViewingRestaurantId")){
                restaurantId = (String) req.getSession().getAttribute("currentViewingRestaurantId");
            }

            Favourite favourite = favDao.hasFavourite(restaurantId, userAccount.getUserAccountId());

            if (null != favourite){
                favDao.deleteFavourite(favourite.getId());
                logger.log(Level.INFO, "Account " + userAccount.getUsername() + " unfavourited ", restaurantId);
            }
            else {
                Favourite newFavourite =
                    new Favourite.Builder()
                        .restaurantId(restaurantId)
                        .userId(userAccount.getUserAccountId())
                        .build();
                favDao.createFavourite(newFavourite);
                logger.log(Level.INFO, "Account " + userAccount.getUsername() + " favourited ", restaurantId);
            }

            
        }
    }


    req.setAttribute("page", "view");
    resp.sendRedirect("/read?id=" + restaurantId);
    //req.getRequestDispatcher("/base.jsp").forward(req, resp);
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    assert ServletFileUpload.isMultipartContent(req);

    resp.sendRedirect("/read");
    
  }

}