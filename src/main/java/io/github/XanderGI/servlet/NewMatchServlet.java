package io.github.XanderGI.servlet;

import io.github.XanderGI.service.OngoingMatchesService;
import io.github.XanderGI.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;


@WebServlet("/new-match")
public class NewMatchServlet extends BaseServlet {
    private static final String REDIRECT_URL_TEMPLATE = "/match-score?uuid=%s";
    private static final String JSP_PATH = "/new-match.jsp";
    private OngoingMatchesService ongoingMatchesService;

    @Override
    public void init() {
        ongoingMatchesService = (OngoingMatchesService) getServletContext().getAttribute("ongoingMatchesService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JSP_PATH).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String firstName = req.getParameter("nameOne");
        String secondName = req.getParameter("nameTwo");

        ValidationUtil.checkNamesIsValid(firstName, secondName);
        firstName = firstName.strip();
        secondName = secondName.strip();

        UUID uuid = ongoingMatchesService.create(firstName, secondName);

        String url = REDIRECT_URL_TEMPLATE.formatted(uuid);
        resp.sendRedirect(url);
    }

    @Override
    protected String getErrorPath() {
        return JSP_PATH;
    }
}