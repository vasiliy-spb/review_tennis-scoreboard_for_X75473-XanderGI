<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="match" scope="request" type="io.github.XanderGI.dto.MatchScoreDto"/>
<jsp:useBean id="uuid" scope="request" type="java.util.UUID"/>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tennis Scoreboard | Match Score</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;700&display=swap" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Roboto+Mono:wght@300&display=swap" rel="stylesheet">
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
    <c:choose>
        <c:when test="${match.matchOver}">
            <div class="container">
                <h1>Match finished! <c:out value="${match.winnerName}"/>  wins!</h1>
                <section class="score">
                    <div class="table-responsive">
                        <table class="table">
                            <thead class="result">
                            <tr>
                                <th class="table-text">PLAYER</th>
                                <th class="table-text">SETS</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr class="player1">
                                <td class="table-text"><c:out value="${match.playerOne.name}"/></td>
                                <td class="table-text"><c:out value="${match.playerOne.sets}"/></td>
                            </tr>
                            <tr class="player2">
                                <td class="table-text"><c:out value="${match.playerTwo.name}"/></td>
                                <td class="table-text"><c:out value="${match.playerTwo.sets}"/></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </section>
                <a href="${pageContext.request.contextPath}/" class="home-btn">HOME</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="container">
                <h1>Current match</h1>
                <div class="current-match-image"></div>
                <section class="score">
                    <div class="table-responsive">
                        <table class="table">
                            <thead class="result">
                            <tr>
                                <th class="table-text">Player</th>
                                <th class="table-text">Sets</th>
                                <th class="table-text">Games</th>
                                <th class="table-text">Points</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr class="player1">
                                <td class="table-text"><c:out value="${match.playerOne.name}"/></td>
                                <td class="table-text"><c:out value="${match.playerOne.sets}"/></td>
                                <td class="table-text"><c:out value="${match.playerOne.games}"/></td>
                                <td class="table-text"><c:out value="${match.playerOne.displayPoints}"/></td>
                                <td class="table-text">
                                    <form method="post" action="${pageContext.request.contextPath}/match-score?uuid=${uuid}">
                                        <input type="hidden" name="uuid" value="<c:out value="${uuid}"/>">
                                        <input type="hidden" name="playerId" value="<c:out value="${match.playerOne.id}"/>">
                                        <input class="score-btn" type="submit" value="Score">
                                    </form>
                                </td>
                            </tr>
                            <tr class="player2">
                                <td class="table-text"><c:out value="${match.playerTwo.name}"/></td>
                                <td class="table-text"><c:out value="${match.playerTwo.sets}"/></td>
                                <td class="table-text"><c:out value="${match.playerTwo.games}"/></td>
                                <td class="table-text"><c:out value="${match.playerTwo.displayPoints}"/></td>
                                <td class="table-text">
                                    <form method="post" action="${pageContext.request.contextPath}/match-score?uuid=${uuid}">
                                        <input type="hidden" name="uuid" value="<c:out value="${uuid}"/>">
                                        <input type="hidden" name="playerId" value="<c:out value="${match.playerTwo.id}"/>">
                                        <input class="score-btn" type="submit" value="Score">
                                    </form>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </section>
            </div>
        </c:otherwise>
    </c:choose>
</main>
<footer>
    <div class="footer">
        <p>&copy; Tennis Scoreboard, project from <a href="https://zhukovsd.github.io/java-backend-learning-course/">zhukovsd/java-backend-learning-course</a>
            roadmap.</p>
    </div>
</footer>
</body>
</html>