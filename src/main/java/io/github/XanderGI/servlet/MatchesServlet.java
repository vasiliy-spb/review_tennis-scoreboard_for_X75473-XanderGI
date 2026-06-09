package io.github.XanderGI.servlet;

import io.github.XanderGI.dto.MatchesPageDto;
import io.github.XanderGI.listener.ContextListener;
import io.github.XanderGI.service.FinishedMatchesPersistenceService;
import io.github.XanderGI.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/matches")
public class MatchesServlet extends BaseServlet {

    // Все повторяющиеся или важные строковые литералы лучше выносить в `private static final` константы с понятными именами.
        // Именованная константа делает код более семантически понятным.

    private static final String VIEW_MATCHES = "matches";
    private FinishedMatchesPersistenceService finishedMatchesService;

    @Override
    public void init() {

        // Для получения объектов из контекста можно использовать "естественные константы" — ClassName.class.getSimpleName() или ClassName.class.getName()
        finishedMatchesService = (FinishedMatchesPersistenceService) getServletContext().getAttribute(ContextListener.FINISHED_MATCHES_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String page = req.getParameter("page");
        String filterName = req.getParameter("filter_by_player_name");

        int pageNumber = ValidationUtil.parsePageNumber(page);

        MatchesPageDto dto = finishedMatchesService.getMatchesPage(pageNumber, filterName);

        // Можно добавить pageNumber и filterName в DTO и передавать все необходимые для JSP параметры в одном объекте.
        req.setAttribute("dto", dto);
        req.setAttribute("pageNumber", pageNumber);
        req.setAttribute("filterName", filterName);
        renderView(req, resp, VIEW_MATCHES);
    }

    @Override
    protected String getErrorPath() {
        return VIEW_MATCHES;
    }
}