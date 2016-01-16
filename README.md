# PlayRestAPIWithTests
Playframework 2.4 with generic RESTful API with Specs2 tests


## How to run?

OrientDB is used as dependency so download it from the website, install it and start the server.
You may need to update the admin credential in app/orientdb/Db.scala.

To use type
git clone https://github.com/pawank/Soriento.git
sbt update
sbt publishLocal

./activator run
visit http://127.0.0.1:9000

The play evolution will add a default user to be used for adding and viewing entries in local db

## How to debug the application?
1. Create a new Play 2 app in Edit Configuration
2. Provide name to the debug app
3. Run the newly created debug app
4. Open web browser at the port mentioned in url in the debug app settings