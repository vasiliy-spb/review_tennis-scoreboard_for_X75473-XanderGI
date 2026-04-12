package io.github.XanderGI.listener;

import io.github.XanderGI.infrastructure.transaction.TransactionRunner;
import io.github.XanderGI.repository.MatchRepository;
import io.github.XanderGI.repository.OngoingMatchRepository;
import io.github.XanderGI.repository.PlayerRepository;
import io.github.XanderGI.repository.impl.HibernateMatchRepository;
import io.github.XanderGI.repository.impl.HibernatePlayerRepository;
import io.github.XanderGI.repository.impl.InMemoryOngoingMatchRepository;
import io.github.XanderGI.service.FinishedMatchesPersistenceService;
import io.github.XanderGI.service.MatchFacadeService;
import io.github.XanderGI.service.MatchScoreCalculationService;
import io.github.XanderGI.service.OngoingMatchesService;
import io.github.XanderGI.util.HibernateUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {
    public static final String ONGOING_MATCHES_SERVICE = "ongoingMatchesService";
    public static final String FINISHED_MATCHES_SERVICE = "finishedMatchesService";
    public static final String MATCH_FACADE_SERVICE = "matchFacadeService";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        HibernateUtil.init();

        OngoingMatchRepository ongoingMatchRepository = new InMemoryOngoingMatchRepository();
        PlayerRepository playerRepository = new HibernatePlayerRepository();
        MatchRepository matchRepository = new HibernateMatchRepository();
        TransactionRunner transactionRunner = new TransactionRunner();

        OngoingMatchesService ongoingMatchesService = new OngoingMatchesService(ongoingMatchRepository, playerRepository, transactionRunner);
        FinishedMatchesPersistenceService finishedMatchesService = new FinishedMatchesPersistenceService(matchRepository, transactionRunner);
        MatchScoreCalculationService calculationMatchService = new MatchScoreCalculationService();
        MatchFacadeService matchFacadeService = new MatchFacadeService(ongoingMatchesService, calculationMatchService, finishedMatchesService);

        sce.getServletContext().setAttribute(ONGOING_MATCHES_SERVICE, ongoingMatchesService);
        sce.getServletContext().setAttribute(FINISHED_MATCHES_SERVICE, finishedMatchesService);
        sce.getServletContext().setAttribute(MATCH_FACADE_SERVICE, matchFacadeService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateUtil.close();
    }
}