package io.github.XanderGI.service;

import io.github.XanderGI.entity.Player;
import io.github.XanderGI.exception.InvalidMatchException;
import io.github.XanderGI.model.MatchScore;
import io.github.XanderGI.model.PlayerScore;
import io.github.XanderGI.repository.OngoingMatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class OngoingMatchesService {

    // TODO: Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)

    // TODO: Класс отвечает за создание объекта текущего матча (доменной модели).
        // При этом он способствует смешению слоёв — передаёт JPA Entity в доменную модель.
        // (см. файл "separation-of-concerns-principle.md" в этом же пакете)

    private final OngoingMatchRepository ongoingMatchRepository;
    private final PlayerService playerService;

    // TODO: Этот метод должен выполняться в транзакции
    public UUID create(String nameOne, String nameTwo) {

        // Можно вынести этот if во вспомогательный метод,
            // а также проверять совпадение имён после .trim(),
            // чтобы имена "Петя" и "Петя  " считались одинаковыми.
        if (nameOne.equalsIgnoreCase(nameTwo)) {
            throw new InvalidMatchException("The names of the players must be different");
        }

        Player playerOne = playerService.getOrCreatePlayer(nameOne);
        Player playerTwo = playerService.getOrCreatePlayer(nameTwo);

        // TODO: Не нужно передавать JPA Entity в доменные модели
        MatchScore matchScore = new MatchScore(
                playerOne,
                playerTwo,
                PlayerScore.matchStart(),
                PlayerScore.matchStart()
        );

        UUID uuid = ongoingMatchRepository.add(matchScore);

        log.info("[Match {}] Created: {} vs {}", uuid, playerOne.getName(), playerTwo.getName());

        return uuid;
    }

    public Optional<MatchScore> get(UUID matchId) {
        return ongoingMatchRepository.get(matchId);
    }

    public void remove(UUID matchId) {
        ongoingMatchRepository.remove(matchId);
    }
}