# CODEWORDS GAME API

 - A fun JSON API game built with Spring Boot, where players guess hidden words.

## ENDPOINT GUIDES

### Game Start (Basic)
 - Endpoint - http://localhost:8080/game/

### Game Start (w/ User and Difficulty)
 - Endpoint - http://localhost:8080/game/
 - Request Body (JSON) :
   - {
     "user" : "test_user",
     "difficulty" : "easy"
     }
 - Difficulty Levels : easy (1pt), medium (2pts), hard (3pts)

### Take a Guess
 - Endpoint: http://localhost:8080/game/{gameId}/guess
 - Request Body (JSON) :
    - {
      "guess" : "g"
      }

### Forfeit Game
 - Endpoint: http://localhost:8080/game/{gameId}/forfeit

### Show Leaderboard
 - Endpoint: http://localhost:8080/game/leaderboard