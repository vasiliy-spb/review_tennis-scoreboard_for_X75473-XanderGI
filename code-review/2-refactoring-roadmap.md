# Роадмап рефакторинга по файлам

Это упорядоченный список файлов, которые следует исправлять в соответствии с замечаниями в комментариях. Рекомендую двигаться последовательно.

Файлы, не указанные в списке, можно исправлять в любом порядке.

### Шаг 1: Сущности

- `/entity/Player.java`
- `/entity/Match.java`

### Шаг 2: Слой доступа к данным

- `/repository/impl/HibernatePlayerRepository.java`
- `/repository/MatchRepository.java`
- `/repository/impl/HibernateMatchRepository.java`
- `/repository/impl/InMemoryOngoingMatchRepository.java`

### Шаг 3: Доменные модели

- `/model/Point.java`
- `/model/PlayerScore.java`
- `/model/MatchScore.java`

### Шаг 4: DTO

- `/dto/MatchDto.java`
- `/dto/MatchScoreDto.java`
- `/dto/MatchesPageDto.java`
- `/dto/PlayerItemDto.java`

### Шаг 5: Сервисный слой

- `/service/PlayerService.java`
- `/service/OngoingMatchesService.java`
- `/service/FinishedMatchesPersistenceService.java`
- `/service/MatchFacadeService.java`
- `/service/MatchScoreCalculationService.java`

### Шаг 6: Утилиты и вспомогательные классы

- `/infrastructure/transaction/TransactionRunner.java`
- `/util/HibernateUtil.java`
- `/util/ValidationUtil.java`
- `/util/SearchUtil.java`

### Шаг 7: Сервлеты

- `/listener/ContextListener.java`
- `/servlet/BaseServlet.java`
- `/servlet/NewMatchServlet.java`
- `/servlet/MatchScoreServlet.java`
- `/servlet/MatchesServlet.java`

### Шаг 8: Тесты и JSP

- `src/test/java/io/github/XanderGI/model/MatchScoreTest.java`
- `src/test/java/io/github/XanderGI/service/MatchScoreCalculationServiceTest.java`
- `src/main/webapp/WEB-INF/match-score.jsp`
- `src/main/webapp/WEB-INF/matches.jsp`
