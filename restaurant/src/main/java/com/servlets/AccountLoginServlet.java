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
import javax.servlet.http.Cookie;

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
    name = "login",
    urlPatterns = {"/login"})
public class AccountLoginServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(AccountLoginServlet.class.getName());

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.setAttribute("action", "Add");
    req.setAttribute("destination", "login");
    req.setAttribute("page", "login");
    req.getRequestDispatcher("/base.jsp").forward(req, resp);
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    assert ServletFileUpload.isMultipartContent(req);
    CloudStorageHelper storageHelper = (CloudStorageHelper) getServletContext().getAttribute("storageHelper");

    String newImageUrl = null;
    Map<String, String> params = new HashMap<String, String>();
    try {
      FileItemIterator iter = new ServletFileUpload().getItemIterator(req);
      while (iter.hasNext()) {
        FileItemStream item = iter.next();
        if (item.isFormField()) {
          params.put(item.getFieldName(), Streams.asString(item.openStream()));
        } else if (!Strings.isNullOrEmpty(item.getName())) {
          newImageUrl =
              storageHelper.uploadFile(
                  item, System.getenv("RES_BUCKET"));
        }
      }
    } catch (FileUploadException e) {
      throw new IOException(e);
    }

    HttpSession session = req.getSession();

    UserAccountDAO dao = (UserAccountDAO) this.getServletContext().getAttribute("userAccountDAO");

    // Getting UserAccount object from DB (Check to see if account details is valid)
    UserAccount userAccount = dao.getUserAccount(params.get("username"), params.get("password"));

    if (null != userAccount){
        req.getSession().setAttribute("userAccount", userAccount);

        Cookie c_user = new Cookie("username", params.get("username"));
        Cookie c_pass = new Cookie("password", params.get("password"));

        c_user.setMaxAge(3600 * 24 * 30);
        c_pass.setMaxAge(3600 * 24 * 30);

        resp.addCookie(c_user);
        resp.addCookie(c_pass);

        logger.log(Level.INFO, "Account " + userAccount.getUsername() + " logged in.", userAccount);
        resp.sendRedirect("/restaurants");
    }
    else {
            logger.log(Level.INFO, "Account not found", userAccount);
            //resp.sendRedirect("/loginFailed");
    }
    
  }

}