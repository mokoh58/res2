package main.java.com.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import main.java.com.objects.UserAccount;
import main.java.com.dao.UserAccountDAO;
import com.util.CloudStorageHelper;
import com.google.common.base.Strings;

@SuppressWarnings("serial")
@WebServlet(
    name = "logout",
    urlPatterns = {"/logout"})
public class AccountLogoutServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(AccountLogoutServlet.class.getName());

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.removeAttribute("action");
    req.removeAttribute("destination");
    req.setAttribute("page", "list");

    req.getSession().removeAttribute("userAccount");
    if (null == req.getSession().getAttribute("userAccount")){
        logger.log(Level.INFO, "userAccount is null in session");
    }
    
    req.getRequestDispatcher("/base.jsp").forward(req, resp);
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    req.getSession().removeAttribute("userAccount");
    if (null == req.getSession().getAttribute("userAccount")){
        logger.log(Level.INFO, "userAccount is null in session");
    }
    resp.sendRedirect("/restaurants");
    
  }

}