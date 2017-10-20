package murach.admin;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import murach.business.User;
import murach.data.UserDB;

public class UsersServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();

        String url = "/index.jsp";
        
        // get current action
        String action = request.getParameter("action");
        if (action == null) {
            action = "display_users";  // default action
        }
        
        // perform action and set URL to appropriate page
        if (action.equals("display_users")) {            
            List<User> users = UserDB.selectUsers();            
            request.setAttribute("users", users);
            this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
            url = "/index.jsp";
        } 
        else if (action.equals("display_user")) {
             request.getSession().setAttribute("user", UserDB.selectUser(request.getParameter("email")));
             url="/user.jsp";
        }
        else if (action.equals("update_user")) {
             User user = (User)request.getSession().getAttribute("user");
             user.setFirstName(request.getParameter("firstName"));
             user.setLastName(request.getParameter("lastName"));
             UserDB.update(user);
             List<User> list = UserDB.selectUsers();
             request.setAttribute("users", list);
             url="/index.jsp";
        }
        else if (action.equals("delete_user")) {
             User user = UserDB.selectUser(request.getParameter("email"));
             UserDB.delete(user);
             List<User> users = UserDB.selectUsers();
             request.setAttribute("users", users);
             url="/index.jsp";
        }
        
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }    
    
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }    
}