package io.github.XanderGI.servlet;

import io.github.XanderGI.exception.InvalidMatchException;
import io.github.XanderGI.exception.MatchNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public abstract class BaseServlet extends HttpServlet {
    private static final String ERROR_ATTRIBUTE = "error";

    protected abstract String getErrorPath();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            super.service(req, resp);
        } catch (InvalidMatchException | IllegalArgumentException e) {
            handleError(req, resp, getErrorPath(), e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        } catch (MatchNotFoundException e) {
            handleError(req, resp, getErrorPath(), e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
        }

    }

    private void handleError(HttpServletRequest req, HttpServletResponse resp, String path, String errorMessage, int status) throws IOException, ServletException {
        req.setAttribute(ERROR_ATTRIBUTE, errorMessage);
        resp.setStatus(status);
        req.getRequestDispatcher(path).forward(req, resp);
    }
}