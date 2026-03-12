package io.github.XanderGI.listener;

import io.github.XanderGI.repository.OngoingMatchRepository;
import io.github.XanderGI.repository.PlayerRepository;
import io.github.XanderGI.service.FinishedMatchesPersistenceService;
import io.github.XanderGI.service.MatchScoreCalculationService;
import io.github.XanderGI.service.OngoingMatchesService;
import io.github.XanderGI.utils.HibernateUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        HibernateUtil.init();

        OngoingMatchRepository ongoingMatchRepository = new OngoingMatchRepository();
        PlayerRepository playerRepository = new PlayerRepository();

        OngoingMatchesService ongoingMatchesService = new OngoingMatchesService(ongoingMatchRepository, playerRepository);
        FinishedMatchesPersistenceService finishedMatchesService = new FinishedMatchesPersistenceService();
        MatchScoreCalculationService calculationMatchService = new MatchScoreCalculationService();

        sce.getServletContext().setAttribute("ongoingMatchesService", ongoingMatchesService);
        sce.getServletContext().setAttribute("finishedMatchesService", finishedMatchesService);
        sce.getServletContext().setAttribute("calculationMatchService", calculationMatchService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateUtil.close();
    }
}