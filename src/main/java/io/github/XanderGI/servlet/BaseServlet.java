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
    private static final String VIEW_SERVER_ERROR = "server-error";
    private static final String VIEW_PATH_TEMPLATE = "/WEB-INF/%s.jsp";

    protected abstract String getErrorPath();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            super.service(req, resp);
        } catch (InvalidMatchException | IllegalArgumentException e) {
            handleError(req, resp, getErrorPath(), e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        } catch (MatchNotFoundException e) {
            handleError(req, resp, getErrorPath(), e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            handleError(req, resp, VIEW_SERVER_ERROR, e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    private void handleError(HttpServletRequest req, HttpServletResponse resp, String viewName, String errorMessage, int status) throws IOException, ServletException {
        req.setAttribute(ERROR_ATTRIBUTE, errorMessage);
        resp.setStatus(status);
        renderView(req, resp, viewName);
    }

    protected void renderView(HttpServletRequest req, HttpServletResponse resp, String viewName) throws ServletException, IOException {
        String path = VIEW_PATH_TEMPLATE.formatted(viewName);
        req.getRequestDispatcher(path).forward(req, resp);
    }
}