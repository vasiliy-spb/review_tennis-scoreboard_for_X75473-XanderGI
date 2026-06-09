package io.github.XanderGI.service;

import io.github.XanderGI.model.MatchScore;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MatchScoreCalculationService {

    // Класс не добавляет никакой дополнительной логики к запуску начисления очка
        // и служит простой обёрткой над вызовом метода модели.
        // В текущем виде он является избыточным слоем абстракции, который усложняет код, не принося никакой пользы.

    public void addPoint(MatchScore matchScore, Integer playerId) {
        matchScore.pointWonBy(playerId);
    }
}