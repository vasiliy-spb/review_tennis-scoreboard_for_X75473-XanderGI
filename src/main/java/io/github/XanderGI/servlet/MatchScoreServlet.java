package io.github.XanderGI.servlet;

import io.github.XanderGI.dto.MatchScoreDto;
import io.github.XanderGI.exception.MatchNotFoundException;
import io.github.XanderGI.mapper.MatchMapper;
import io.github.XanderGI.model.MatchScore;
import io.github.XanderGI.service.MatchFacadeService;
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
    private OngoingMatchesService ongoingMatchesService;
    private MatchFacadeService matchFacadeService;

    @Override
    public void init() {
        ongoingMatchesService = (OngoingMatchesService) getServletContext().getAttribute("ongoingMatchesService");
        matchFacadeService = (MatchFacadeService) getServletContext().getAttribute("matchFacadeService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String stringMatchId = req.getParameter("uuid");
        try {
            UUID matchId = UUID.fromString(stringMatchId);
            MatchScore matchScore = ongoingMatchesService.get(matchId)
                    .orElse(null);

            MatchScoreDto dto;
            if (matchScore != null) {
                dto = MatchMapper.toMatchScoreDto(matchScore);
            } else {
                dto = (MatchScoreDto) req.getSession().getAttribute("finishedMatch_" + matchId);

            }

            if (dto == null) {
                throw new MatchNotFoundException("Match not found");
            }


            req.setAttribute("uuid", matchId);
            req.setAttribute("match", dto);
            req.getRequestDispatcher("/match-score.jsp").forward(req, resp);
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", "Incorrect matchId from path");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            req.getRequestDispatcher("/new-match.jsp").forward(req, resp);
        } catch (MatchNotFoundException e) {
            req.setAttribute("error", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            req.getRequestDispatcher("/new-match.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String stringMatchId = req.getParameter("uuid");
        String stringPlayedId = req.getParameter("playerId");

        try {
            UUID matchId = UUID.fromString(stringMatchId);
            Integer playerId = Integer.valueOf(stringPlayedId);

            MatchScore matchScore = matchFacadeService.playRally(matchId, playerId);

            if (matchScore.isMatchOver()) {
                MatchScoreDto dto = MatchMapper.toMatchScoreDto(matchScore);
                req.getSession().setAttribute("finishedMatch_" + matchId, dto);
            }

            resp.sendRedirect("/match-score?uuid=" + matchId);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Invalid playerId format");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            req.getRequestDispatcher("/new-match.jsp").forward(req, resp);
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", "Invalid matchId format");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            req.getRequestDispatcher("/new-match.jsp").forward(req, resp);
        } catch (MatchNotFoundException e) {
            req.setAttribute("error", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            req.getRequestDispatcher("/new-match.jsp").forward(req, resp);
        }
    }
}