<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <link rel="stylesheet" href="css/style.css">

    <script src="js/app.js"></script>
</head>
<body>
<header class="header">
    <section class="nav-header">
        <div class="brand">
            <div class="nav-toggle">
                <img src="images/menu.png" alt="Logo" class="logo">
            </div>
            <span class="logo-text">TennisScoreboard</span>
        </div>
        <div>
            <nav class="nav-links">
                <a class="nav-link" href="/">Home</a>
                <a class="nav-link" href="/matches">Matches</a>
            </nav>
        </div>
    </section>
</header>
<main>
    <c:choose>
        <c:when test="${match.matchOver}">
            <div class="container">
                <h1>Match finished! ${match.winner.get().name} wins!</h1>
                <section class="score">
                    <table class="table">
                        <thead class="result">
                        <tr>
                            <th class="table-text">PLAYER</th>
                            <th class="table-text">SETS</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr class="player1">
                            <td class="table-text">${match.playerOne.name}</td>
                            <td class="table-text">${match.playerScoreOne.set}</td>
                        </tr>
                        <tr class="player2">
                            <td class="table-text">${match.playerTwo.name}</td>
                            <td class="table-text">${match.playerScoreTwo.set}</td>
                        </tr>
                        </tbody>
                    </table>
                </section>
                <a href="/" class="home-btn">HOME</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="container">
                <h1>Current match</h1>
                <div class="current-match-image"></div>
                <section class="score">
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
                            <td class="table-text">${match.playerOne.name}</td>
                            <td class="table-text">${match.playerScoreOne.set}</td>
                            <td class="table-text">${match.playerScoreOne.game}</td>
                            <td class="table-text">
                                <c:choose>
                                    <c:when test="${match.tieBreak}">
                                        ${match.playerScoreOne.tieBreakPoint}
                                    </c:when>
                                    <c:otherwise>
                                        ${match.playerScoreOne.point.value}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="table-text">
                                <form method="post" action="/match-score">
                                    <input type="hidden" name="uuid" value="${uuid}">
                                    <input type="hidden" name="playerId" value="${match.playerOne.id}">
                                    <input class="score-btn" type="submit" value="Score">
                                </form>
                            </td>
                        </tr>
                        <tr class="player2">
                            <td class="table-text">${match.playerTwo.name}</td>
                            <td class="table-text">${match.playerScoreTwo.set}</td>
                            <td class="table-text">${match.playerScoreTwo.game}</td>
                            <td class="table-text">
                                <c:choose>
                                    <c:when test="${match.tieBreak}">
                                        ${match.playerScoreTwo.tieBreakPoint}
                                    </c:when>
                                    <c:otherwise>
                                        ${match.playerScoreTwo.point.value}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="table-text">
                                <form method="post" action="/match-score">
                                    <input type="hidden" name="uuid" value="${uuid}">
                                    <input type="hidden" name="playerId" value="${match.playerTwo.id}">
                                    <input class="score-btn" type="submit" value="Score">
                                </form>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </section>
            </div>
        </c:otherwise>
    </c:choose>
</main>
<footer>
    <div class="footer">
        <p>&copy; Tennis Scoreboard, project from <a href="https://zhukovsd.github.io/java-backend-learning-course/">zhukovsd/java-backend-learning-course</a> roadmap.</p>
    </div>
</footer>
</body>
</html>
