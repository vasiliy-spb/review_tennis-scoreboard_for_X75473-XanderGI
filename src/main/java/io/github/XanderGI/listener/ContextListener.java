package io.github.XanderGI.listener;

import io.github.XanderGI.config.AppConfig;
import io.github.XanderGI.infrastructure.transaction.TransactionRunner;
import io.github.XanderGI.mapper.MatchMapper;
import io.github.XanderGI.repository.MatchRepository;
import io.github.XanderGI.repository.OngoingMatchRepository;
import io.github.XanderGI.repository.PlayerRepository;
import io.github.XanderGI.repository.impl.HibernateMatchRepository;
import io.github.XanderGI.repository.impl.HibernatePlayerRepository;
import io.github.XanderGI.repository.impl.InMemoryOngoingMatchRepository;
import io.github.XanderGI.service.*;
import io.github.XanderGI.util.HibernateUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@WebListener
public class ContextListener implements ServletContextListener {
    public static final String ONGOING_MATCHES_SERVICE = "ongoingMatchesService";
    public static final String FINISHED_MATCHES_SERVICE = "finishedMatchesService";
    public static final String MATCH_FACADE_SERVICE = "matchFacadeService";
    public static final String MATCH_MAPPER = "matchMapper";
    public static final int CLEANUP_INITIAL_DELAY = 5;
    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            AppConfig config = AppConfig.loadFromEnvironment();

            Flyway.configure()
                    .dataSource(config.dbUrl(), config.dbUsername(), config.dbPassword())
                    .load().
                    migrate();

            HibernateUtil.init(config);


            OngoingMatchRepository ongoingMatchRepository = new InMemoryOngoingMatchRepository();
            PlayerRepository playerRepository = new HibernatePlayerRepository();
            MatchRepository matchRepository = new HibernateMatchRepository();
            TransactionRunner transactionRunner = new TransactionRunner();

            MatchMapper matchMapper = MatchMapper.INSTANCE;

            PlayerService playerService = new PlayerService(playerRepository, transactionRunner);
            OngoingMatchesService ongoingMatchesService = new OngoingMatchesService(ongoingMatchRepository, playerService);
            FinishedMatchesPersistenceService finishedMatchesService = new FinishedMatchesPersistenceService(matchRepository, transactionRunner, matchMapper);
            MatchScoreCalculationService calculationMatchService = new MatchScoreCalculationService();
            MatchFacadeService matchFacadeService = new MatchFacadeService(ongoingMatchesService, calculationMatchService, finishedMatchesService);

            scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleWithFixedDelay(() -> ongoingMatchRepository.removeStaleMatches(config.staleMatchLifetimeMinutes()),
                    CLEANUP_INITIAL_DELAY,
                    config.cleanupPeriodMinutes(),
                    TimeUnit.MINUTES);

            sce.getServletContext().setAttribute(ONGOING_MATCHES_SERVICE, ongoingMatchesService);
            sce.getServletContext().setAttribute(FINISHED_MATCHES_SERVICE, finishedMatchesService);
            sce.getServletContext().setAttribute(MATCH_FACADE_SERVICE, matchFacadeService);
            sce.getServletContext().setAttribute(MATCH_MAPPER, matchMapper);

            log.info("Application context initialized");
        } catch (Exception e) {
            throw new RuntimeException("Application initialization failed", e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) {
            scheduler.shutdownNow();
        }

        HibernateUtil.close();
    }
}