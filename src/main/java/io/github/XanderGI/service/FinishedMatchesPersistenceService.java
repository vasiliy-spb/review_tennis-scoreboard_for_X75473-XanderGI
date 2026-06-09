package io.github.XanderGI.service;

import io.github.XanderGI.dto.MatchDto;
import io.github.XanderGI.dto.MatchesPageDto;
import io.github.XanderGI.exception.MatchNotFinishedException;
import io.github.XanderGI.exception.MatchPersistenceException;
import io.github.XanderGI.exception.MatchRepositoryException;
import io.github.XanderGI.infrastructure.transaction.TransactionRunner;
import io.github.XanderGI.mapper.MatchMapper;
import io.github.XanderGI.model.MatchScore;
import io.github.XanderGI.repository.MatchRepository;
import io.github.XanderGI.util.SearchUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class FinishedMatchesPersistenceService {

    // TODO: Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)

    // Размер страницы по умолчанию более уместно хранить в сервлете, так как в идеале он должен приходить с фронтенда.
        // А сервис должен принимать это значение в качестве аргумента в методы.
    private static final int PAGE_SIZE = 5;
    private final MatchRepository matchRepository;
    private final TransactionRunner transactionRunner;
    private final MatchMapper mapper;

    public void save(MatchScore matchScore) {
        if (!matchScore.isMatchOver()) {
            throw new MatchNotFinishedException("Match not finished for finishMatch");
        }

        try {
            transactionRunner.execute(() -> matchRepository.save(matchScore));

            String firstPlayerName = matchScore.getPlayerOne().getName();
            String secondPlayerName = matchScore.getPlayerTwo().getName();
            log.info("Match successfully saved into DB: {} vs {}", firstPlayerName, secondPlayerName);

        // Стоит ловить кастомные исключения, в которые заворачиваются иcключения, специфичные для DAO слоя
            // (например, DataAccessException), а все остальные пропускать дальше к глобальному обработчику.
        } catch (RuntimeException e) {
            throw new MatchPersistenceException("Couldn't finishMatch match", e);
        }
    }

    public MatchesPageDto getMatchesPage(int page, String filterName) {
        if (page < 1) {
            throw new IllegalArgumentException("Page must be positive");
        }

        try {
            int offset = calculateOffset(page);

            return transactionRunner.execute(() -> {
                        List<String> tokens = SearchUtil.tokenize(filterName);

                        // Можно добавить в маппер метод, который принимает список Entity и возвращает список DTO
                        List<MatchDto> matches = matchRepository.findMatches(offset, PAGE_SIZE, tokens).stream()
                                .map(mapper::toMatchDto)
                                .toList();

                        // Лучше использовать примитивный тип long
                        Long countOfMatches = matchRepository.countMatchesByTokens(tokens);
                        int totalPages = calculateTotalPages(countOfMatches);

                        return new MatchesPageDto(
                                matches,
                                totalPages
                        );
                    }
            );

        // Стоит ловить кастомные исключения, в которые заворачиваются иcключения, специфичные для DAO слоя
            // (например, DataAccessException), а все остальные пропускать дальше к глобальному обработчику.
        } catch (RuntimeException e) {
            String message = String.format("Couldn't get matches for page %d with filter '%s'", page, filterName);
            throw new MatchRepositoryException(message, e);
        }
    }

    private int calculateOffset(int page) {
        return (page - 1) * PAGE_SIZE;
    }

    // Лучше принимать примитивный тип long, чтобы преобразование во время вычисления не могло выбросить NullPointerException
    private int calculateTotalPages(Long countOfMatches) {
        return (int) Math.ceil((double) countOfMatches / PAGE_SIZE);
    }
}