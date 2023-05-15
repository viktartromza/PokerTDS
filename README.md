# PokerTDS application

This project is a simple version of online-casino. Customers have the opportunity to play European roulette and Texas
Hold'em.
By default, application have an admin account with username 'admin' and password 'admin'.

## Database

Application use PostgreSQL database. For start the application you need Postgres server(jdbc:postgresql:
//localhost:5432) with created database 'PokerAppDB'. Database contains nine tables.

* Table _users_ - contains application users data, that is directly related to the application;
* Table _users_data_ - contains personal users' data, which is necessary due to the commercial nature of the application;
* Table _wallets_ - contains information about users money balances;
* Table _games_ - contains common information about games, that are finished or ongoing;
* Table _users_games_ - link table between users and games;
* Table _roulette_games_ - contains information about roulette-games, that are finished or ongoing;
* Table _bets_roulette_ - contains information about bets in roulette-games;
* Table _texasholdem_games_ - contains information about Texas Hold'em games, that are finished or ongoing;
* Table _bets_poker_ - contains information about bets in poker-games.

## Roulette rules

The application implements a simplified version of European roulette. The player can bet on any of the 37 pockets from
0 to 36 (Straight Bet). The payout is 35:1. Another type of bet offered is ODD/EVEN. The payout is 1:1, also known as 
even money. Zero-pocket neither applies to even nor odd.

### Automatically finishing of roulette games

The application implements automatic completion mechanism for games that have not been updated for more than 1,5 hour.
The user is notified via email from `onlinecasinotds@gmail.com` half an hour before the finishing.


## Poker rules

The application implements only one type of poker - Texas Hold'em.
To transfer information between the frontend and backend about cards, the following order is used:
Card is a 2 character string with the first character representing the rank
(one of `A`, `K`, `Q`, `J`, `T`, `9`, `8`, `7`, `6`, `5`, `4`, `3`, `2`) and the second character representing
the suit (one of `h`, `d`, `c`, `s`). Jokers are not used.

A value of a Texas Hold'em hand is the best possible value out of all possible subsets of
5 cards from the 7 cards which are formed by 5 board cards and 2 hand cards.
See [Texas Hold'em rules](https://en.wikipedia.org/wiki/Texas_hold_%27em).

### Hand Value

The hand values for 5 cards are as follows (in descending order - from strongest to weakest):

* `Straight Flush` - a `Straight` (see below) which is also a `Flush` (see below)
* `Four of a kind` - Four cards of the same rank
* `Full House` - a combination of `Three of a kind` and a `Pair`
* `Flush` - 5 cards of the same suit
* `Straight` - a sequence of 5 cards of consecutive rank (note an exception - `A` can both precede `2` and follow `K`)
* `Three of a kind` - three cards with the same rank
* `Two pairs` - two `Pair`-s
* `Pair` - two cards of the same rank
* `High card` - the "fallback" in case no other hand value rule applies

In case of ties the ranks of the cards forming the combinations decide the highest value.

In case of further ties, the ranks of the remaining cards decide the highest value.

All suits are considered equal in strength.

When comparing `Full House`-s, the `Three of a kind` rank comparison is more important than the `Pair` rank
comparison, for example, `QQQ88 > 999KK`, `KKK77 > QQQJJ` and `KKK77 > KKK66`.

When comparing `Straight`-s, the `A2345` `Straight` is the weakest one and the `TJQKA` one the strongest one,
for example, `23456 > A2345` and `TJQKA > 9TJQK`.

### Automatically finishing of Texas Hold'em

The application implements automatic completion mechanism for games that have not been updated for more than 8 minutes.
The user is notified via email from `onlinecasinotds@gmail.com` 3 minutes before the finishing.

## Available endpoints for users

* http://localhost:8080/auth - POST method, login(authentication)

* http://localhost:8080/users/info - GET method, current user information
* http://localhost:8080/users/scores - GET method, list of usernames with id and score, except ADMINs
* http://localhost:8080/users/{id} - GET method, info about username and score of user with given id
* http://localhost:8080/users/registration - POST method, new user registration
* http://localhost:8080/users/update - PUT method, update current user data
* http://localhost:8080/user - DELETE method, delete current user's account

* http://localhost:8080/games - GET method, show games belong to selected user
* http://localhost:8080/games/info - GET method, info about all games of current user
* http://localhost:8080/games/info/{id} - GET method, info about selected game

* http://localhost:8080/games/roulette - POST method, create new roulette game for current user
* http://localhost:8080/games/roulette - PUT method, make bet for current roulette game and return result
* http://localhost:8080/games/roulette/finish/{id} - PUT method, finish current roulette game (leaving the table)

* http://localhost:8080/games/poker/texas - POST method, create new texas hold'em for current user
* http://localhost:8080/games/poker/texas - PUT method, adding a bet for current texas hold'em, return info about current state and casino decision

* http://localhost:8080/wallets/info - GET method, show user's wallet 
* http://localhost:8080/wallets - POST method, create wallet for current user

## Available endpoints for admins

* http://localhost:8080/admin/users - GET method, show all user's
* http://localhost:8080/admin/delusers - GET method, show all deleted user's
* http://localhost:8080/admin/wallets - PUT method, withdraw or refill wallet of selected user
* http://localhost:8080/admin/users/{id} - DELETE method, delete selected user's account

