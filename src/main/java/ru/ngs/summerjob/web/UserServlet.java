package ru.ngs.summerjob.web;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ngs.summerjob.entity.User;
import ru.ngs.summerjob.service.UserService;
import ru.ngs.summerjob.service.UserServiceImpl;

import java.io.IOException;

@WebServlet(name = "user", urlPatterns = {"/user"})
public class UserServlet extends HttpServlet {
    UserService userService;

    @Override
    public void init(ServletConfig config) {
        userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idFromReq = req.getParameter("id");
        long id = Long.parseLong(idFromReq);
        User userById = userService.getUserById(id);
        resp.getWriter().println(userById);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = new User();
        user.setName(req.getParameter("name"));
        user.setLogin(req.getParameter("login"));
        user.setPassword(req.getParameter("password"));
        User userAfterSaving = userService.saveUser(user);
        resp.getWriter().println(userAfterSaving);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = new User();
        user.setId(Long.parseLong(req.getParameter("id")));
        user.setName(req.getParameter("name"));
        user.setLogin(req.getParameter("login"));
        user.setPassword(req.getParameter("password"));
        User userAfterUpdate = userService.updateUser(user);
        resp.getWriter().println(userAfterUpdate);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String answer = userService.deleteUserById(Long.parseLong(req.getParameter("id")));
        resp.getWriter().println(answer);
    }
}
