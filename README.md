# 5 in a Row!

The project consists of a Java server and client. 

 - The client is an interface for the user to create a new game and complete with a friend.
 - The server keeps track of the games state.

**N.B.** The server binds to port 8080 when starting up so verify this is free before starting the server.

#### Server swagger endpoint:
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Compile the server and client
Running this command in the client and server directories will execute the unit tests and package the project into executable jars.

`mvn package`

## Starting the server
After executing the package command the following can be executed against the compiled jar to start up the server. It is required to start the server before the clients.

`java -jar five-in-a-row-server-0.0.1-SNAPSHOT.jar`

## Starting the client(s)
After executing the package command on the client, this command can be executed from within the target directory in the client project. This will start a single session of the client.

`java -jar five-in-a-row-client-0.0.1-SNAPSHOT.jar`

multiple client sessions can be started from different terminal windows using this command.

## How to start a game
For each game of "5 in a row" there is required to be 2 players minimum. Where each player has a separate client running. It is possible though to have multiple games running against the one server.

Once the clients have started up the users will be presented with the following message:
> Do you have a Game ID and would like to join an existing game? (true or false)

This is the point where a user can decide to join an already existing game using an ID their friend has shared with them (Option: true) or alternatively start their own game (Option: false).

If the user decided to join an existing game they will be presented with the following prompt for them to enter the game ID their friend has sent them:
>Please Provide The Existing Game ID:

Regardless of whether the user selected to create a game or join an existing one, the next step will always be for them to enter their username:
>Please Provide Your Username:

There is a requirement that usernames must be unique to each game.

Next the user will be prompted to enter their colour choice:
> Please Provide Your Colour Choice (X or O):

Their colour choice is what will be displayed inside the "5 in a row" board. Similarly to usernames they must be unique to each game *however it is recommended that they are a single character for display purposes*.

**What happens if i made a mistake with one of the above questions?**
We are human and mistakes happen. If you make a mistake entering information in the above stages (e.g. a game ID that does not exist) the client will respond to errors from the server and prompt the user to submit the above information again after completing the menu:
> Could not join game. Message From the Server: No game with this ID exists

This will bring the user back to the start of the input messages allowing them to try again.

## How to play a game
If you have gotten this far, congratulations!!! You and your fiend are ready to play "5 in a row".

The game has the following rules:

 1. There must be 2 exactly players to a game.
 2. The first person to join the game will be the first to make a move.
 3. You must take turns making moves. Its not possible to make your next move until the other play has completed theirs.
 4. The first person to get 5 pieces (their colour) in a row (up, left, right or diagonally) will be crowned the winner.

Once a winner has been crowned their name will be displayed.