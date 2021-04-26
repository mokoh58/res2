package com.servlets;

import java.io.IOException;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

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

import com.dao.ReviewDAO;
import com.dao.FavouriteDAO;
import com.dao.UserAccountDAO;
import com.google.common.base.Strings;
import com.objects.UserAccount;
import com.objects.Favourite;
import com.objects.Review;
import com.util.CloudStorageHelper;

@SuppressWarnings("serial")
@WebServlet(
    name = "review",
    urlPatterns = {"/review"})
public class ReviewServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(ReviewServlet.class.getName());

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.removeAttribute("action");

    


    req.setAttribute("page", "view");
    //resp.sendRedirect("/read?id=" + restaurantId);
    //req.getRequestDispatcher("/base.jsp").forward(req, resp);
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    assert ServletFileUpload.isMultipartContent(req);

    Map<String, String> params = new HashMap<String, String>();
    try {
        FileItemIterator iter = new ServletFileUpload().getItemIterator(req);
        while (iter.hasNext()) {
            FileItemStream item = iter.next();
            if (item.isFormField()) {
                params.put(item.getFieldName(), Streams.asString(item.openStream()));
            }
        }
    } catch (FileUploadException e) {
        throw new IOException(e);
    }

    UserAccountDAO dao = (UserAccountDAO) this.getServletContext().getAttribute("userAccountDAO");
    FavouriteDAO favDao = (FavouriteDAO) this.getServletContext().getAttribute("favouriteDAO");
    ReviewDAO reviewDao = (ReviewDAO) this.getServletContext().getAttribute("reviewDAO");
    UserAccount userAccount;

    if (reviewDao == null){
        System.out.println("reviewDao is null");
    }

    String restaurantId = null;

    if (null != req.getSession().getAttribute("userAccount")) {
        userAccount = (UserAccount) req.getSession().getAttribute("userAccount");

        restaurantId = (String) req.getSession().getAttribute("currentViewingRestaurantId");

        String dateOfVisit = params.get("dateOfVisit");
        try{ // Try to format it correctly. If exception, then nevermind.
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(params.get("dateOfVisit"));
            dateOfVisit = new SimpleDateFormat("dd-MM-yyyy").format(date1);
        }catch (Exception e){
            
        }
        

        String createDt = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String rating = params.get("star");
        System.out.println("Rating = " + rating);

        if (null != restaurantId){
            Review rev = new Review.Builder()
            .restaurantId(restaurantId)
            .userId(userAccount.getUserAccountId())
            .username(userAccount.getUsername())
            .createDt(createDt)
            .dateOfVisit(dateOfVisit)
            .remarks(params.get("remarks"))
            .rating(rating)
            .build();

            reviewDao.createReview(rev);
        }
    }

    resp.sendRedirect("/read?id=" + restaurantId);
    
  }

}