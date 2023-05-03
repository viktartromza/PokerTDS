# PokerTDS application

This project is a simple version of online-casino. Customers have the opportunity to play European roulette and Texas
Hold'em. By default, application
have an admin account with username 'root' and password 'root'.

## Database

Application use PostgreSQL database. For start the application you need Postgres server (jdbc:postgresql://localhost:
5432/storage) with created database 'storage'. Database contains eight tables.

* Table _users_ - contains information about application users;
* Table _authorities_ - contains authorities and roles available for application users;
* Table _users_authorities_ - link table between users and authorities;
* Table _plans_ - contains plans available users (for the future application scale);
* Table _subscriptions_ - contains information about users plan, issue and expiry date (for the future application
  scale); Now upon registration user get a free plan unit 2050-01-01;
* Table _photos_ - contains information about uploaded users photos;
* Table _albums_ - contains information about created users albums;
* Table _albums_photos_ - link table between albums and photos.

## Storage

  Application use MinIO object storage for store files. All MinIO properties store in storage.properties. For start the
  application you need MinIO server (localhost:9000), access and secret key for application, created buckets for photos
  and thumbnails.

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