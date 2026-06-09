package io.github.XanderGI.service;

import io.github.XanderGI.entity.Player;
import io.github.XanderGI.infrastructure.transaction.TransactionRunner;
import io.github.XanderGI.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;

@Slf4j
@RequiredArgsConstructor
public class PlayerService {

    // TODO: Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)

    private final PlayerRepository playerRepository;
    private final TransactionRunner transactionRunner;

    // Транзакция должна оборачивать всю бизнес-операцию создания матча (то есть метод create в OngoingMatchesService)
    public Player getOrCreatePlayer(String name) {
        try {
            return transactionRunner.execute(() -> playerRepository.findByName(name)
                    .orElseGet(() -> playerRepository.save(new Player(null, name))));

        // Стоит ловить кастомные исключения, в которые заворачиваются иcключения, специфичные для DAO слоя
            // (например, DataAccessException), а все остальные пропускать дальше к глобальному обработчику.
        } catch (RuntimeException e) {
            if (isConstraintViolation(e)) {

                // ConstraintViolationException не всегда означает нарушение уникальности имени
                log.warn("A race condition has occurred! Player: {}", name);

                // Просто поиск игрока можно выполнять без транзакции
                return transactionRunner.execute(() -> playerRepository.findByName(name))
                        .orElseThrow(() -> new IllegalStateException("Player must exist after constraint violation: " + name));
            }

            throw e;
        }
    }

    private boolean isConstraintViolation(Exception e) {
        Throwable currentCause = e;

        while (currentCause != null) {
            if (currentCause instanceof ConstraintViolationException) {
                return true;
            }
            currentCause = currentCause.getCause();
        }

        return false;
    }
}