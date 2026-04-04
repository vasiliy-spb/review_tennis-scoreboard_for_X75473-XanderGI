package io.github.XanderGI.servlet;

import io.github.XanderGI.dto.MatchesPageDto;
import io.github.XanderGI.service.FinishedMatchesPersistenceService;
import io.github.XanderGI.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/matches")
public class MatchesServlet extends BaseServlet {
    private static final String JSP_MATCHES = "/matches.jsp";
    private FinishedMatchesPersistenceService finishedMatchesService;

    @Override
    public void init() {
        finishedMatchesService = (FinishedMatchesPersistenceService) getServletContext().getAttribute("finishedMatchesService");
    }

    // todo: в фильтре обработать пробелы вокруг filterName - например "Novak ", " Novak "
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String page = req.getParameter("page");
        String filterName = req.getParameter("filter_by_player_name");

        int pageNumber = ValidationUtil.parsePageNumber(page);

        MatchesPageDto dto = finishedMatchesService.getMatchesPage(pageNumber, filterName);

        req.setAttribute("dto", dto);
        req.setAttribute("pageNumber", pageNumber);
        req.setAttribute("filterName", filterName);
        req.getRequestDispatcher(JSP_MATCHES).forward(req, resp);
    }

    @Override
    protected String getErrorPath() {
        return JSP_MATCHES;
    }
}