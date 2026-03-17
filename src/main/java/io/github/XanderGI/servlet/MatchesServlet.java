package io.github.XanderGI.servlet;

import io.github.XanderGI.dto.MatchesPageDto;
import io.github.XanderGI.service.FinishedMatchesPersistenceService;
import io.github.XanderGI.utils.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/matches")
public class MatchesServlet extends HttpServlet {
    private FinishedMatchesPersistenceService finishedMatchesService;

    @Override
    public void init() {
        finishedMatchesService = (FinishedMatchesPersistenceService) getServletContext().getAttribute("finishedMatchesService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String page = req.getParameter("page");
        String filterName = req.getParameter("filter_by_player_name");

        try {
            int pageNumber = ValidationUtil.parsePageNumber(page);

            MatchesPageDto dto = finishedMatchesService.getMatchesPage(pageNumber, filterName);
            req.setAttribute("dto", dto);
            req.setAttribute("pageNumber", pageNumber);
            req.setAttribute("filterName", filterName);
            req.getRequestDispatcher("/matches.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Invalid page number format");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            req.getRequestDispatcher("/matches.jsp").forward(req, resp);
        }
    }
}