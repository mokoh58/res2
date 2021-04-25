package com.servlets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    resp.sendRedirect("/restaurants");
    //req.getRequestDispatcher("/base.jsp").forward(req, resp);
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