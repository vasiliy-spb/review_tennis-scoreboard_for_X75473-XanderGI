<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="dto" scope="request" type="io.github.XanderGI.dto.MatchesPageDto"/>
<%--@elvariable id="pageNumber" type="java.lang.Integer"--%>
<%--@elvariable id="filterName" type="java.lang.String"--%>
<%--@elvariable id="error" type="java.lang.String"--%>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tennis Scoreboard | Finished Matches</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</head>

<body>
<header class="header">
    <section class="nav-header">
        <div class="brand">
            <div class="nav-toggle">
                <img src="${pageContext.request.contextPath}/images/menu.png" alt="Logo" class="logo">
            </div>
            <span class="logo-text">TennisScoreboard</span>
        </div>
        <div>
            <nav class="nav-links">
                <a class="nav-link" href="${pageContext.request.contextPath}/">Home</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/matches">Matches</a>
            </nav>
        </div>
    </section>
</header>
<main>
    <div class="container">
        <h1>Matches</h1>
        <form class="input-container" method="get" action="${pageContext.request.contextPath}/matches">
            <input class="input-filter" aria-label="Filter matches by player name" placeholder="Filter by name" type="text" name="filter_by_player_name"
                   value="<c:out value="${filterName}"/>"/>
            <div class="filter-buttons-group">
                <button type="submit" class="btn-filter">Find</button>
                <c:if test="${not empty filterName}">
                    <a class="btn-filter" href="${pageContext.request.contextPath}/matches">Reset Filter</a>
                </c:if>
            </div>
        </form>
        <c:if test="${not empty error}">
            <p style="color: red;"><c:out value="${error}"/></p>
        </c:if>
        <c:choose>
            <c:when test="${not empty dto.matches}">
                <div class="table-responsive">
                    <table class="table-matches">
                        <tr>
                            <th>Player One</th>
                            <th>Player Two</th>
                            <th>Winner</th>
                        </tr>
                        <c:forEach var="match" items="${dto.matches}">
                            <tr>
                                <td><c:out value="${match.playerOneName}"/></td>
                                <td><c:out value="${match.playerTwoName}"/></td>
                                <td>
                                    <span class="winner-name-td"><c:out value="${match.winnerName}"/></span>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>

                <!-- Цикл от 1 до totalPages отображает сразу все существующие страницы.
                Лучше сделать окно пагинации ограниченным текущей страницей +-2 вокруг неё -->

                <!-- Если текст внутри двойных кавычек нужно заключить ещё в одни кавычки —
                стоит использовать одинарные, иначе первая двойная кавычка считается закрывающей.
                Сейчас так:
                    <a class="prev" href="<c:out value="${prevPageUrl}"/>"> < </a>
                Лучше так:
                    <a class="prev" href="<c:out value='${prevPageUrl}'/>"> < </a>

                Аналогично и в других местах.
                 -->
                <div class="pagination">
                    <c:if test="${pageNumber > 1}">
                        <c:url value="/matches" var="prevPageUrl">
                            <c:param name="page" value="${pageNumber - 1}"/>
                            <c:if test="${not empty filterName}">
                                <c:param name="filter_by_player_name" value="${filterName}"/>
                            </c:if>
                        </c:url>
                        <a class="prev" href="<c:out value="${prevPageUrl}"/>"> < </a>
                    </c:if>


                    <c:forEach var="i" begin="1" end="${dto.totalPages}">
                        <c:url value="/matches" var="pageUrl">
                            <c:param name="page" value="${i}"/>
                            <c:if test="${not empty filterName}">
                                <c:param name="filter_by_player_name" value="${filterName}"/>
                            </c:if>
                        </c:url>
                        <a class="num-page ${i == pageNumber ? 'current' : ''}"
                           href="<c:out value="${pageUrl}"/>">${i}</a>
                    </c:forEach>

                    <c:if test="${pageNumber < dto.totalPages}">
                        <c:url value="/matches" var="nextPageUrl">
                            <c:param name="page" value="${pageNumber + 1}"/>
                            <c:if test="${not empty filterName}">
                                <c:param name="filter_by_player_name" value="${filterName}"/>
                            </c:if>
                        </c:url>
                        <a class="next" href="<c:out value="${nextPageUrl}"/>"> > </a>
                    </c:if>
                </div>
            </c:when>
            <c:otherwise>
                <p style="color: red;">No matches found</p>
            </c:otherwise>
        </c:choose>
    </div>
</main>
<footer>
    <div class="footer">
        <p>&copy; Tennis Scoreboard, project from <a href="https://zhukovsd.github.io/java-backend-learning-course/">zhukovsd/java-backend-learning-course</a>
            roadmap.</p>
    </div>
</footer>
</body>
</html>
