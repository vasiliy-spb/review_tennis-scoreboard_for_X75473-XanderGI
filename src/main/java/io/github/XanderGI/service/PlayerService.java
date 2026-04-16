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
    private final PlayerRepository playerRepository;
    private final TransactionRunner transactionRunner;

    public Player getOrCreatePlayer(String name) {
        try {
            return transactionRunner.execute(() -> playerRepository.findByName(name)
                    .orElseGet(() -> playerRepository.save(new Player(null, name))));
        } catch (RuntimeException e) {
            if (isConstraintViolation(e)) {
                log.warn("A race condition has occurred! Player: {}", name);

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