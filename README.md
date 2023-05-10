# PokerTDS application

This project is a simple version of online-casino. Customers have the opportunity to play European roulette and Texas
Hold'em.

## Database

Application use PostgreSQL database. For start the application you need Postgres server (jdbc:postgresql://localhost:
5432) with created database 'PokerAppDB'. Database contains nine tables.

* Table _users_ - contains application users data, that is directly related to the application;
* Table _users_data_ - contains personal users data, which is necessary due to the commercial nature of the application;
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

## Available endpoints for users

* http://localhost:8080/signup - POST method, registration
* http://localhost:8080/login - POST method, login

* http://localhost:8080/user - GET method, current user information
* http://localhost:8080/user/change/email - PUT method, change user's email
* http://localhost:8080/user/change/password - PUT method, change user's password
* http://localhost:8080/user - DELETE method, delete current user's account

* http://localhost:8080/photos - GET method, show all user's photos
* http://localhost:8080/photos/upload - POST method, upload photos
* http://localhost:8080/photo/{photoId} - GET method, show photo by ID
* http://localhost:8080/photo/{photoId}/download - GET method, download photo by ID
* http://localhost:8080/photo/{photoId}/delete - DELETE method, delete photo by ID
* http://localhost:8080/photos/download - POST method, download selected photos
* http://localhost:8080/photos - DELETE method, delete selected photos

* http://localhost:8080/albums - GET method, show all user's albums
* http://localhost:8080/album - POST method, create album
* http://localhost:8080/album/{albumId} - GET method, show album by ID
* http://localhost:8080/album/{albumId} - PUT method, edit album by ID
* http://localhost:8080/album/{albumId}/download - GET method, download album by ID
* http://localhost:8080/album/{albumId} - DELETE method, delete album by ID

* http://localhost:8080/subscription - GET method, show user's subscription 

* http://localhost:8080/plans - GET method, show available plans

## Available endpoints for admins

* http://localhost:8080/admin/signup - POST method, registration admins

* http://localhost:8080/admin/users - GET method, show all user's
* http://localhost:8080/admin/user/{userId} - GET method, show user by ID
* http://localhost:8080/admin/user - POST method, create user by ID
* http://localhost:8080/admin/user - PUT method, edit user by ID
* http://localhost:8080/admin/user/{userId} - DELETE method, delete user by ID
* http://localhost:8080/admin/authority/{authorityId}

* http://localhost:8080/admin/authorities - GET method, show all authorities
* http://localhost:8080/admin/authority/{authorityId} - GET method, show authority by ID
* http://localhost:8080/admin/authority - POST method, create authority
* http://localhost:8080/admin/authority - PUT method, edit authority
* http://localhost:8080/admin/authority/{authorityId} - DELETE method, delete authority by ID

* http://localhost:8080/admin/plans - GET method, show all plans 
* http://localhost:8080/admin/plan/{planId} - GET method, show plan by ID
* http://localhost:8080/admin/plan - POST method, create plan
* http://localhost:8080/admin/plan - PUT method, edit plan
* http://localhost:8080/admin/plan/{planId} - DELETE method, delete plan by ID

* http://localhost:8080/admin/subscriptions - GET method, show all subscriptions
* http://localhost:8080/admin/subscription/{subscriptionId} - GET method, show subscription by ID
* http://localhost:8080/admin/subscription - POST method, create subscription
* http://localhost:8080/admin/subscription - PUT method, edit subscription
* http://localhost:8080/admin/subscription/{subscriptionId} - DELETE method, delete subscription by ID
