# Tic Tac Toe API for Adsquare

## Endpoints

### Start new game
```shell
curl -X POST http://localhost:8080/game
```

### Play a round
```shell
curl --request POST \
     --header "Content-Type: application/json"
     --url 'http://localhost:8080/game/{game_id}/play'
     --d '{"player": "X|O", "row": 0, "column": 0}'
```

### Get the game state
```shell
curl -X GET http://localhost:8080/game/{game_id}
```

### Responses

All endpoints return the latest state of the game with the following schema.

```json
{
  "id": "UUID",
  "state": "PLAYING|WIN|FINISHED",
  "winner": "X|O", // This will only appear if the game state is "WIN"
  "board": {
    "boardSize": 3, // The board size will always be 3, but the code is prepared for different board sizes.
    "grid": [
      {
        "row": [0-2],
        "column": [0-2],
        "player": "X|O|EMPTY"
      }
    ]
  }
}
```

## How to run

From the command line:

````shell
gradle :bootRun 
````
it will start the REST API in the port `8080`.

If you use `IntelliJ`, just run the `TicTacToeApplication`


## Tech stack

- Java 17
- Spring Boot 3.0.5
- Database H2 (Embedded)
- JUnit 5

### Database choice

The embedded H2 database was chosen to speed up the development and not to depend on any external databases avoid the creation of docker-compose files.

The data is stored in the filesystem under the `data` folder in the project's root folder.

The `Hibernate` configuration is set to create/update the schema automatically from the entities. A better solution would be to use a proper migration library like `Flyway` to manage the changes in the schema.

In a real world, we would have a cluster of database server to be more reliable and avoid data corruption/loss. 

