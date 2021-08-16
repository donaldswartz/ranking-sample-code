# ranking-sample-code
A short sample of how esports rankings can be computed.

This code is inspired by prior work, and fits as a part of a larger project: an esports API containing matches, tournaments, and players. This calculator performs the function of computing players' records and then ranking them by a configurable set of rankings, based on what the particular esports program desires.

Most of the logic can be found in the calculator: https://github.com/donaldswartz/ranking-sample-code/blob/main/src/ds/ranking/RankingCalculator.java
Tests for this logic are found in the tests class: https://github.com/donaldswartz/ranking-sample-code/blob/main/test/ds/ranking/RankingCalculatorTests.java

# Glossary
**Match**: A competitive series of matches between two players. These frequently contain multiple games in a best-of-3, best-of-5, or other rule for determining the winner.
**Game**: A single game played of the video game itself.
**Score**: The score of the video game, if it has one. May be used as a tiebreaker.
**Record**: A record is an object which can be returned by the API showing the number of wins and losses for a player.
