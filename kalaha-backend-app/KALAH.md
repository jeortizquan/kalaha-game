FORMAT: 1A

# Kalah game API
This is a *Java RESTful Web Service* that runs a game of 6-stone Kalah.
Kalah is played on a board with two rows of 6 holes and two stores, called *kalahah*. The two players (North
and South) sit at each side of the board. We assume that South always starts the game. The game usually opens
with 4 counters in every hole, but other amounts (2, 3, 5, 6) are possible.
A move is made by selecting a nonempty hole at the player’s side of the board.
The counters are lifted from this hole and sown in anti-clockwise direction, starting with the next hole.
The player’s own kalahah is included in the sowing, but the opponent’s kalahah is skipped.
There are three possible outcomes of a move: (1) if the last counter is put into the player’s kalahah, the player
is allowed to move again (such a move is called a *Kalah-move*); (2) if the last counter is put in an empty hole on
the player’s side of the board, a capture takes place: all stones in the opposite opponent’s pit and the last stone
of the sowing are put into the player’s store and the turn is over; and (3) if the last counter is put anywhere else,
the turn is over directly. The game ends whenever a move leaves no counters on one player’s side, in which
case the other player captures all remaining counters. The player who collects the most counters is the winner.

# Kalah API Root [/]

This resource does not have any attributes.
 
+ Response 200 (text/plain)



### /games [POST]
You may create a new game using this action.

+ Response 201 (application/json)

+ Body 
````
    {
        "id": "2589",
        "url": "http://localhost:8080/games/2589",
    } 
````

### /games/{gameId}/pits/{pitId} [PUT]
You may move the stones in a game using this action.
+ Parameters
    + gameId (number) - ID of the game you want to play
    + pitId (number)  - ID of the pit you want to move the stones
    
+ Response 200 (application/json)
+ Body
````
    {
        "id": "2589",
        "url": "http://localhost:8080/games/2589",
        "turn": "SOUTH",
        "status": {
            "1": "6",
            "2": "6",
            "3": "0",
            "4": "7",
            "5": "7",
            "6": "7",
            "7": "1",
            "8": "7",
            "9": "7",
            "10": "6",
            "11": "6",
            "12": "6",
            "13": "6",
            "14": "0"
        }
    }
````

### /games/{gameId} [GET]
You can the data of a game using this action.
+ Parameters
    + gameId (number) - ID of the game you want to observe
    
+ Response 200 (application/json)
+ Body
````  
  {
       "id": "8529",
       "url": "http://localhost:8080/games/8529",
       "turn": "NORTH",
       "status": {
          "1": "6",
          "2": "6",
          "3": "0",
          "4": "7",
          "5": "7",
          "6": "7",
          "7": "1",
          "8": "7",
          "9": "7",
          "10": "6",
          "11": "6",
          "12": "6",
          "13": "6",
          "14": "0"
       }
  }
````

## /swagger-ui.html [GET]
You may use this to access to swagger UI 
+ Response 200 (text/html)

## /actuator/* [GET]
You may use this to access to monitoring tools (health, info, metrics)
 
+ Response 200 (application/json)