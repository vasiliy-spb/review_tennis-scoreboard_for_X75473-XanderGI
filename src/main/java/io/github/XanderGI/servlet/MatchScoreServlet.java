package io.github.XanderGI.servlet;

import io.github.XanderGI.dto.MatchScoreDto;
import io.github.XanderGI.exception.MatchNotFoundException;
import io.github.XanderGI.listener.ContextListener;
import io.github.XanderGI.mapper.MatchMapper;
import io.github.XanderGI.model.MatchScore;
import io.github.XanderGI.service.MatchFacadeService;
import io.github.XanderGI.service.OngoingMatchesService;
import io.github.XanderGI.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreServlet extends BaseServlet {
    private static final String REDIRECT_URL_TEMPLATE = "/match-score?uuid=%s";
    private static final String VIEW_NEW_MATCH = "new-match";
    private static final String VIEW_MATCH_SCORE = "match-score";
    private OngoingMatchesService ongoingMatchesService;
    private MatchFacadeService matchFacadeService;
    private MatchMapper mapper;

    @Override
    public void init() {
        ongoingMatchesService = (OngoingMatchesService) getServletContext().getAttribute(ContextListener.ONGOING_MATCHES_SERVICE);
        matchFacadeService = (MatchFacadeService) getServletContext().getAttribute(ContextListener.MATCH_FACADE_SERVICE);
        mapper = (MatchMapper) getServletContext().getAttribute(ContextListener.MATCH_MAPPER);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String stringMatchId = req.getParameter("uuid");

        UUID matchId = ValidationUtil.parseUUID(stringMatchId);

        MatchScoreDto dto = ongoingMatchesService.get(matchId)
                .map(mapper::toMatchScoreDto)
                .orElseThrow(() -> new MatchNotFoundException("Match not found"));

        renderMatchScore(req, resp, matchId, dto);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String stringMatchId = req.getParameter("uuid");
        String stringPlayedId = req.getParameter("playerId");

        UUID matchId = ValidationUtil.parseUUID(stringMatchId);
        Integer playerId = ValidationUtil.parsePlayerId(stringPlayedId);

        MatchScore matchScore = matchFacadeService.playRally(matchId, playerId);

        if (matchScore.isMatchOver()) {
            MatchScoreDto dto = mapper.toMatchScoreDto(matchScore);
            renderMatchScore(req, resp, matchId, dto);
            return;
        }

        String url = REDIRECT_URL_TEMPLATE.formatted(matchId);
        resp.sendRedirect(url);
    }

    @Override
    protected String getErrorPath() {
        return VIEW_NEW_MATCH;
    }

    private void renderMatchScore(HttpServletRequest req, HttpServletResponse resp, UUID matchId, MatchScoreDto dto) throws ServletException, IOException {
        req.setAttribute("uuid", matchId);
        req.setAttribute("match", dto);
        renderView(req, resp, VIEW_MATCH_SCORE);
    }
}