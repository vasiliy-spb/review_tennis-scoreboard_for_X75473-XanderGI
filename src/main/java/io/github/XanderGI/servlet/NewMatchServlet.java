package io.github.XanderGI.servlet;

import io.github.XanderGI.listener.ContextListener;
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
    private static final String VIEW_NEW_MATCH = "new-match";
    private OngoingMatchesService ongoingMatchesService;

    @Override
    public void init() {
        ongoingMatchesService = (OngoingMatchesService) getServletContext().getAttribute(ContextListener.ONGOING_MATCHES_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        renderView(req, resp, VIEW_NEW_MATCH);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String firstName = req.getParameter("firstName");
        String secondName = req.getParameter("secondName");

        ValidationUtil.checkNamesIsValid(firstName, secondName);
        firstName = firstName.strip();
        secondName = secondName.strip();

        UUID uuid = ongoingMatchesService.create(firstName, secondName);

        String url = REDIRECT_URL_TEMPLATE.formatted(uuid);
        resp.sendRedirect(url);
    }

    @Override
    protected String getErrorPath() {
        return VIEW_NEW_MATCH;
    }
}