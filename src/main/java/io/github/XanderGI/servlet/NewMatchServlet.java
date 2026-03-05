package io.github.XanderGI.servlet;

import io.github.XanderGI.exception.InvalidMatchException;
import io.github.XanderGI.repository.OngoingMatchRepository;
import io.github.XanderGI.repository.PlayerRepository;
import io.github.XanderGI.service.MatchService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;


@WebServlet("/new-match")
public class NewMatchServlet extends HttpServlet {
    private MatchService matchService;

    @Override
    public void init() {
        OngoingMatchRepository ongoingMatchRepository = new OngoingMatchRepository();
        PlayerRepository playerRepository = new PlayerRepository();
        matchService = new MatchService(ongoingMatchRepository, playerRepository);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/new-match.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nameOne = req.getParameter("nameOne");
        String nameTwo = req.getParameter("nameTwo");

        if (nameOne == null || nameOne.isBlank() || nameTwo == null || nameTwo.isBlank()) {
            req.setAttribute("error", "The names of the players cannot be empty");
            req.getRequestDispatcher("/new-match.jsp").forward(req, resp);
            return;
        }

        try {
            UUID uuid = matchService.create(nameOne, nameTwo);
            String site = "/match-score?uuid=";
            resp.sendRedirect(site + uuid);
        } catch (InvalidMatchException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/new-match.jsp").forward(req,resp);
        }

    }
}