package io.github.XanderGI.service;

import io.github.XanderGI.entity.Player;
import io.github.XanderGI.exception.MatchNotFoundException;
import io.github.XanderGI.model.MatchScore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class MatchFacadeService {

    // TODO: Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)

    // TODO: Класс способствует смешению слоёв — передаёт доменную модель в слой контроллеров.
        // (см. файл "separation-of-concerns-principle.md" в этом же пакете)

    // Класс работает с объектами текущих матчей (MatchScore), поэтому его логику можно перенести в OngoingMatchesService.

    private final OngoingMatchesService ongoingMatchesService;
    private final MatchScoreCalculationService calculationMatchService;
    private final FinishedMatchesPersistenceService finishedMatchesService;

    // Неинформативное название метода
    // TODO: Метод не должен возвращать доменную модель
    public MatchScore playRally(UUID matchId, Integer playerId) {
        MatchScore matchScore = ongoingMatchesService.get(matchId)
                .orElseThrow(() -> new MatchNotFoundException("Match not found"));

        synchronized (matchScore) {
            if (matchScore.isMatchOver()) {
                return matchScore;
            }

            calculationMatchService.addPoint(matchScore, playerId);

            if (matchScore.isMatchOver()) {
                finishedMatchesService.save(matchScore);
                ongoingMatchesService.remove(matchId);

                String winnerName = matchScore.getWinner()
                        .map(Player::getName)
                        .orElse("<UNKNOWN_WINNER_STATE>");

                log.info("[Match {}] finished with winner: {}", matchId, winnerName);
            }
        }

        return matchScore;
    }
}