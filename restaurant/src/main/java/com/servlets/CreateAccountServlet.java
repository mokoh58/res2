package main.java.com.servlets;

// [START account_create_servlet]


import com.google.common.base.Strings;
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
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

@SuppressWarnings("serial")
@WebServlet(
    name = "createAccount",
    urlPatterns = {"/createAccount"})
public class CreateAccountServlet extends HttpServlet {

  private static final Logger logger = Logger.getLogger(CreateAccountServlet.class.getName());

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.setAttribute("action", "Add");
    req.setAttribute("destination", "createAccount");
    req.setAttribute("page", "accountSignUp");
    req.getRequestDispatcher("/base.jsp").forward(req, resp);
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // assert ServletFileUpload.isMultipartContent(req);
    // CloudStorageHelper storageHelper =
    //     (CloudStorageHelper) getServletContext().getAttribute("storageHelper");

    // String newImageUrl = null;
    // Map<String, String> params = new HashMap<String, String>();
    // try {
    //   FileItemIterator iter = new ServletFileUpload().getItemIterator(req);
    //   while (iter.hasNext()) {
    //     FileItemStream item = iter.next();
    //     if (item.isFormField()) {
    //       params.put(item.getFieldName(), Streams.asString(item.openStream()));
    //     } else if (!Strings.isNullOrEmpty(item.getName())) {
    //       newImageUrl =
    //           storageHelper.uploadFile(
    //               item, System.getenv("BOOKSHELF_BUCKET"));
    //     }
    //   }
    // } catch (FileUploadException e) {
    //   throw new IOException(e);
    // }

    // String createdByString = "";
    // String createdByIdString = "";
    // HttpSession session = req.getSession();
    // if (session.getAttribute("userEmail") != null) { // Does the user have a logged in session?
    //   createdByString = (String) session.getAttribute("userEmail");
    //   createdByIdString = (String) session.getAttribute("userId");
    // }

    // Book book =
    //     new Book.Builder()
    //         .author(params.get("author"))
    //         .description(params.get("description"))
    //         .publishedDate(params.get("publishedDate"))
    //         .title(params.get("title"))
    //         .imageUrl(null == newImageUrl ? params.get("imageUrl") : newImageUrl)
    //         .createdBy(createdByString)
    //         .createdById(createdByIdString)
    //         .build();

    // BookDao dao = (BookDao) this.getServletContext().getAttribute("dao");
    // String id = dao.createBook(book);
    // logger.log(Level.INFO, "Created book {0}", book);
    // resp.sendRedirect("/read?id=" + id);
  }
}
// [END account_create_servlet]
