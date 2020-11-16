# PublicFeeds

Spring Boot app. 
Build with "mvn package" then run with "java -jar /path/to/file.jar"

Default port is 5050.
Swagger is available on "/swagger-ui/index.html" path

Endpoints:
-Feeds 
-> "/feed/current" : GET
-> "/feed/current/save" : GET
-> "/feed/query/tags" : GET
-> "/feed/query/tags/save" : GET
-> "/feed/query/ids" : GET
-> "/feed/query/ids/save" : GET
-> "/feed/save" : GET

-Saved Item
-> "/saved" : GET
-> "/saved" : DELETE
-> "/saved/{id}" : GET
-> "/saved/{id}" : DELETE
-> "/saved/count" : GET
-> "/saved/clear" : GET

tba...
