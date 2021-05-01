package com.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.dao.RestaurantDAO;
import com.dao.TagsDAO;
import com.dao.UserAccountDAO;
import com.google.common.base.Strings;
import com.objects.UserAccount;
import com.objects.Favourite;
import com.objects.Restaurant;
import com.objects.Tags;
import com.util.CloudStorageHelper;

@SuppressWarnings("serial")
@WebServlet(
    name = "favRec",
    urlPatterns = {"/favRec"})
public class FavouriteRecommendationServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(FavouriteRecommendationServlet.class.getName());

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.removeAttribute("action");

    RestaurantDAO resDao = (RestaurantDAO) this.getServletContext().getAttribute("resDAO");
    UserAccountDAO dao = (UserAccountDAO) this.getServletContext().getAttribute("userAccountDAO");
    FavouriteDAO favDao = (FavouriteDAO) this.getServletContext().getAttribute("favouriteDAO");
    TagsDAO tagDAO = (TagsDAO) this.getServletContext().getAttribute("tagsDAO");
    UserAccount userAccount;

    String restaurantId = null;
    String finalResults = "";

    if (null != req.getSession().getAttribute("userAccount")) {
        userAccount = (UserAccount) req.getSession().getAttribute("userAccount");

        ArrayList<String> favouriteList = favDao.listFavouriteByUser(userAccount.getUserAccountId());

        List<String> allTagsList = new ArrayList<String>();
        for (String resId : favouriteList){
            ArrayList<Tags> tagsList = tagDAO.getTags(resId);
            for (Tags tag : tagsList){
                if(!allTagsList.contains(tag.getTag()))
                    allTagsList.add(tag.getTag());
            }
        }

        System.out.println("************** Tags" + allTagsList);

        // After getting all the tags, generate recommendations
        List<String> recList = new ArrayList<String>();
        for (String tag : allTagsList){
            List<String> resList = tagDAO.getRestaurants(tag);
            for (String res : resList){
                if (!recList.contains(res))
                    recList.add(res);
            }
        }

        System.out.println("************** Restaurant" + recList);

        // Convert list of String (Restaurant ID) to comma separated String
        finalResults = String.join(",", recList);
    }


    req.setAttribute("page", "view");
    resp.sendRedirect("/restaurants?recList=" + finalResults);
    //req.getRequestDispatcher("/base.jsp").forward(req, resp);
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    assert ServletFileUpload.isMultipartContent(req);

    resp.sendRedirect("/read");
    
  }

}