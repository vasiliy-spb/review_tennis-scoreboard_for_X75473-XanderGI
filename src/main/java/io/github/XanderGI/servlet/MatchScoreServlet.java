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

    // Все повторяющиеся или важные строковые литералы лучше выносить в `private static final` константы с понятными именами.
        // Именованная константа делает код более семантически понятным.

    // TODO: Сервлет работает с доменной моделью `MatchScore`.
        // Это нарушает границы между слоями приложения и Принцип разделения ответственности
        // (см. файл "separation-of-concerns-principle.md" в этом же пакете)
        // Сервлет не должен работать с доменными моделями.
        // Вместо этого он должен "общаться" с другими слоями через DTO.

    // TODO: Сервлет берёт на себя лишнюю ответственность — преобразует доменные модели в DTO,
        // хотя его задача — только принимать HTTP-запросы и делегировать их обработку. Это нарушает принцип единственной ответственности (SRP)
        // и делает код сервлета более сложным и трудным для тестирования.
        // Сервлет должен быть "тонким контроллером", делегирующим всю бизнес-логику одному фасадному сервису.
        // (см. файл "fat-controller.md" в этом же пакете)

    private static final String REDIRECT_URL_TEMPLATE = "%s/match-score?uuid=%s";
    private static final String VIEW_NEW_MATCH = "new-match";
    private static final String VIEW_MATCH_SCORE = "match-score";
    private OngoingMatchesService ongoingMatchesService;
    private MatchFacadeService matchFacadeService;
    private MatchMapper mapper;

    @Override
    public void init() {

        // Для получения объектов из контекста можно использовать "естественные константы" — ClassName.class.getSimpleName() или ClassName.class.getName()
        ongoingMatchesService = (OngoingMatchesService) getServletContext().getAttribute(ContextListener.ONGOING_MATCHES_SERVICE);
        matchFacadeService = (MatchFacadeService) getServletContext().getAttribute(ContextListener.MATCH_FACADE_SERVICE);
        mapper = (MatchMapper) getServletContext().getAttribute(ContextListener.MATCH_MAPPER);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String stringMatchId = req.getParameter("uuid");

        UUID matchId = ValidationUtil.parseUUID(stringMatchId);

        // Сервлет не должен работать с доменными моделями и заниматься преобразованием в DTO
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

        // Сервлет не должен работать с доменными моделями
        MatchScore matchScore = matchFacadeService.playRally(matchId, playerId);

        if (matchScore.isMatchOver()) {

            // Сервлет не должен заниматься преобразованием в DTO
            MatchScoreDto dto = mapper.toMatchScoreDto(matchScore);
            renderMatchScore(req, resp, matchId, dto);
            return;
        }

        String url = REDIRECT_URL_TEMPLATE.formatted(req.getContextPath(), matchId);
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