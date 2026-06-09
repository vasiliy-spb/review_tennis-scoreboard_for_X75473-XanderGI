package io.github.XanderGI.model;

import io.github.XanderGI.entity.Player;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class MatchScore {

    // TODO: Класс хранит ссылки на JPA-сущности (`Player`). Использование объектов JPA Entity в доменной логике
        // создаёт прямую зависимость доменного слоя от слоя персистентности (долговременного хранения данных)
        // и смешивает слои приложения, что нарушает чистоту архитектуры.
        // Это может привести к проблемам с ленивой загрузкой (`LazyInitializationException`)
        // или к неожиданным изменениям в базе данных, если состояние `Player` будет изменено в ходе бизнес-логики.
        // Доменные модели должны оперировать другими доменными моделями, а не сущностями, привязанными к базе данных.
        // (см. файл "model-types.md" в этом же пакете)

    // TODO: Класс отвечает за обработку очков на всех этапах игрового процесса в матче —
        // это слишком большая ответственность для одного класса и нарушает SRP (Single Responsibility Principle).
        // Лучшим решением в этом направлении было бы, чтобы за счёт на каждом уровне (матч-сет-гейм) отвечал отдельный класс.
        // Такой подход больше соответствовал бы ООП-стилю и принципу единственной ответственности для каждого класса.

    // TODO: Логика подсчёта очков не учитывает минимальную необходимую разницу в счёте для победы в тай-брейке

    private final static int GAMES_TO_WIN_SET = 6;
    private final static int SETS_TO_WIN_MATCH = 2;
    private final static int MIN_GAMES_DIFFERENCE = 2;
    private final static int POINTS_TO_WIN_TIEBREAK = 7;

    private final Player playerOne;
    private final Player playerTwo;
    private final PlayerScore playerScoreOne;
    private final PlayerScore playerScoreTwo;
    private Instant lastActivityAt = Instant.now();

    public boolean isMatchOver() {
        return playerScoreOne.getSet().equals(SETS_TO_WIN_MATCH) || playerScoreTwo.getSet().equals(SETS_TO_WIN_MATCH);
    }

    public Optional<Player> getWinner() {
        if (playerScoreOne.getSet().equals(SETS_TO_WIN_MATCH)) {
            return Optional.of(playerOne);
        } else if (playerScoreTwo.getSet().equals(SETS_TO_WIN_MATCH)) {
            return Optional.of(playerTwo);
        }

        return Optional.empty();
    }

    // TODO: Блокировка в этом методе не нужна, так как внешняя блокировка ('synchronized (matchScore)')
        // в MatchFacadeService уже гарантирует, что только один поток может работать с этим объектом matchScore.
    public synchronized void pointWonBy(Integer playerId) {
        if (isMatchOver()) {
            // Попытка начислить очко в уже завершённом матче — это не нормальная ситуация и должна приводить к исключению, а не молчаливому бездействию.
            return;
        }

        lastActivityAt = Instant.now();

        PlayerScore winner;
        PlayerScore loser;

        if (playerOne.getId().equals(playerId)) {
            winner = playerScoreOne;
            loser = playerScoreTwo;
        } else if (playerTwo.getId().equals(playerId)) {
            winner = playerScoreTwo;
            loser = playerScoreOne;
        } else {
            throw new IllegalArgumentException("No player with such ID in this match");
        }

        if (isTieBreak()) {
            updateTieBreakScore(winner, loser);
        } else {
            updateScore(winner, loser);
        }
    }

    private void updateScore(PlayerScore winner, PlayerScore loser) {
        switch (winner.getPoint()) {
            case ZERO, FIFTEEN, THIRTY -> winner.incrementPoint();
            case FORTY -> handleWinnerForty(winner, loser);
            case ADVANTAGE -> processGameWin(winner, loser);
        }
    }

    private void handleWinnerForty(PlayerScore winner, PlayerScore loser) {
        switch (loser.getPoint()) {
            case FORTY -> winner.incrementPoint();
            case ADVANTAGE -> loser.decrementPoint();
            case ZERO, FIFTEEN, THIRTY -> processGameWin(winner, loser);
        }
    }

    private void processGameWin(PlayerScore winner, PlayerScore loser) {
        finishGame(winner, loser);

        if (isSetWin(winner, loser)) {
            finishSet(winner, loser);
        }
    }

    private void finishGame(PlayerScore winner, PlayerScore loser) {
        winner.incrementGame();
        winner.clearPoints();
        loser.clearPoints();
    }

    private void finishSet(PlayerScore winner, PlayerScore loser) {
        winner.incrementSet();
        winner.clearGames();
        loser.clearGames();
    }

    private void updateTieBreakScore(PlayerScore winner, PlayerScore loser) {
        winner.incrementTieBreakPoint();

        if (isTieBreakWin(winner)) {
            finishSet(winner, loser);

            winner.clearTieBreakPoints();
            loser.clearTieBreakPoints();
        }
    }

    public boolean isTieBreak() {
        return playerScoreOne.getGame().equals(GAMES_TO_WIN_SET) && playerScoreTwo.getGame().equals(GAMES_TO_WIN_SET);
    }

    private boolean isSetWin(PlayerScore winner, PlayerScore loser) {
        return winner.getGame() >= GAMES_TO_WIN_SET && Math.abs(winner.getGame() - loser.getGame()) >= MIN_GAMES_DIFFERENCE;
    }

    private boolean isTieBreakWin(PlayerScore winner) {
        return winner.getTieBreakPoint().equals(POINTS_TO_WIN_TIEBREAK);
    }
}