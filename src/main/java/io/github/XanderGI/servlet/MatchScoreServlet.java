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
    private static final String JSP_NEW_MATCH = "/new-match.jsp";
    private static final String JSP_MATCH_SCORE = "/match-score.jsp";
    private OngoingMatchesService ongoingMatchesService;
    private MatchFacadeService matchFacadeService;

    @Override
    public void init() {
        ongoingMatchesService = (OngoingMatchesService) getServletContext().getAttribute(ContextListener.ONGOING_MATCHES_SERVICE);
        matchFacadeService = (MatchFacadeService) getServletContext().getAttribute(ContextListener.MATCH_FACADE_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String stringMatchId = req.getParameter("uuid");

        UUID matchId = ValidationUtil.parseUUID(stringMatchId);

        MatchScoreDto dto = getMatchOrThrow(matchId, req);

        req.setAttribute("uuid", matchId);
        req.setAttribute("match", dto);
        req.getRequestDispatcher(JSP_MATCH_SCORE).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String stringMatchId = req.getParameter("uuid");
        String stringPlayedId = req.getParameter("playerId");

        UUID matchId = ValidationUtil.parseUUID(stringMatchId);
        Integer playerId = ValidationUtil.parsePlayerId(stringPlayedId);

        MatchScore matchScore = matchFacadeService.playRally(matchId, playerId);

        if (matchScore.isMatchOver()) {
            MatchScoreDto dto = MatchMapper.toMatchScoreDto(matchScore);
            req.getSession().setAttribute("finishedMatch_" + matchId, dto);
        }

        String url = REDIRECT_URL_TEMPLATE.formatted(matchId);
        resp.sendRedirect(url);
    }

    private MatchScoreDto getMatchOrThrow(UUID matchId, HttpServletRequest req) {
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
        return dto;
    }

    @Override
    protected String getErrorPath() {
        return JSP_NEW_MATCH;
    }
}