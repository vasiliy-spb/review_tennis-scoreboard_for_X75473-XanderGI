package io.github.XanderGI.servlet;

import io.github.XanderGI.exception.InvalidMatchException;
import io.github.XanderGI.exception.MatchNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public abstract class BaseServlet extends HttpServlet {

    // TODO: Сервлет отправляет сообщение из исключения (`e.getMessage()`) напрямую пользователю.
        // Сообщения об ошибках из исключений могут содержать технические детали, которые не предназначены
        // для конечного пользователя и могут представлять угрозу безопасности. Например, сообщение может быть
        // `"No entity found for query 'SELECT ...'"` или `"Validation failed for field 'internalFieldName'"`,
        // что раскрывает структуру БД или внутренние имена полей.
        //
        // Лучше никогда не отправлять необработанное сообщение из исключения на клиент.
        // Вместо этого можно использовать заранее определённые, безопасные сообщения или коды ошибок.
        // Само исключение при этом нужно логировать для разработчиков.
        //
        // Это повысит безопасность приложения и улучшит пользовательский опыт при возникновении ошибок.
        //
        // В этом проекте допустимо сделать это для ошибок валидации.

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

            // TODO: Не стоит отправлять сообщение из исключения (e.getMessage()) напрямую во View
            handleError(req, resp, VIEW_SERVER_ERROR, e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            log.error("Failed to process request: {}", e.getMessage(), e);
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