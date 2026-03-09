package io.github.XanderGI.servlet;

import io.github.XanderGI.exception.MatchNotFoundException;
import io.github.XanderGI.model.MatchScore;
import io.github.XanderGI.service.FinishedMatchesPersistenceService;
import io.github.XanderGI.service.MatchScoreCalculationService;
import io.github.XanderGI.service.OngoingMatchesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {
    private MatchScoreCalculationService calculationMatchService;
    private OngoingMatchesService ongoingMatchesService;
    private FinishedMatchesPersistenceService finishedMatchesService;

    @Override
    public void init() {
        calculationMatchService = (MatchScoreCalculationService) getServletContext().getAttribute("calculationMatchService");
        ongoingMatchesService = (OngoingMatchesService) getServletContext().getAttribute("ongoingMatchesService");
        finishedMatchesService = (FinishedMatchesPersistenceService) getServletContext().getAttribute("finishedMatchesService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String stringMatchId = req.getParameter("uuid");
        try {
            UUID matchId = UUID.fromString(stringMatchId);
            MatchScore matchScore = ongoingMatchesService.get(matchId)
                    .orElseThrow(() -> new MatchNotFoundException("Match not found"));

            req.setAttribute("uuid", matchId);
            req.setAttribute("match", matchScore);
            req.getRequestDispatcher("/match-score.jsp").forward(req, resp);
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", "Incorrect matchId from path");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            req.getRequestDispatcher("/new-match.jsp").forward(req, resp);
        } catch (MatchNotFoundException e) {
            req.setAttribute("error", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            req .getRequestDispatcher("/new-match.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String stringMatchId = req.getParameter("uuid");
        String stringPlayedId = req.getParameter("playerId");

        try {
            UUID matchId = UUID.fromString(stringMatchId);
            Integer playerId = Integer.valueOf(stringPlayedId);

            calculationMatchService.addPoint(matchId, playerId);
            resp.sendRedirect("/match-score?uuid=" + matchId);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Incorrect playerId from path");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            req.getRequestDispatcher("/new-match.jsp").forward(req, resp);
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", "Incorrect matchId from path");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            req.getRequestDispatcher("/new-match.jsp").forward(req, resp);
        } catch (MatchNotFoundException e) {
            req.setAttribute("error", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            req .getRequestDispatcher("/new-match.jsp").forward(req, resp);
        }
    }
}