# PublicFeeds

Spring Boot app.\
Build with "mvn package" then run with "java -jar /path/to/file.jar"\

Default port is 5050.\
Swagger is available on "/swagger-ui/index.html" path\

### Endpoints:
- Feeds 
GET "/feed/current"\
get current live feed items, can have query parameters to narrow items shown, but data fetched from the live feed is the same 20 items\
parameter:\
1. q : search query string, only show results match it
2. minDate : minimum date for the results, format YYYY-MM-DD
3. maxDate : maximum date for the results, format YYYY-MM-DD
4. pageSize : number of results shown per requests, data fetched is maximum 20 items though
5. pageNumber : selected page to be shown, must not be greater than page available

GET "/feed/current/save"\
get current live feed items, same as above but save the result items to server db\

GET "/feed/query/tags"\
get current live feed items with certain media tags, can have query parameters to narrow items shown, but data fetched is the same 20 items\
parameter:\
1. tags : list of tags to be searched for
2. anyTags : whether results can match any of the tags or must match all of them
3. q : search query string, only show results match it
4. minDate : minimum date for the results, format YYYY-MM-DD
5. maxDate : maximum date for the results, format YYYY-MM-DD
6. pageSize : number of results shown per requests, data fetched is maximum 20 items though
7. pageNumber : selected page to be shown, must not be greater than page available

GET "/feed/query/tags/save"\
get current live feed items with certain media tags, same as above but save the result items to server db\
parameter:\
1. tags : list of tags to be searched for
2. anyTags : whether results can match any of the tags or must match all of them

GET "/feed/query/ids"\
get current live feed items from authors with id given, can have query parameters to narrow items shown, but data fetched is the same 20 items\
parameter:\
1. ids : list of author ids which feed items to be retrieved
3. q : search query string, only show results match it
4. minDate : minimum date for the results, format YYYY-MM-DD
5. maxDate : maximum date for the results, format YYYY-MM-DD
6. pageSize : number of results shown per requests, data fetched is maximum 20 items though
7. pageNumber : selected page to be shown, must not be greater than page available

GET "/feed/query/ids/save"\
get current live feed items from authors with id given,  same as above but save the result items to server db\
parameter:\
1. ids : list of author ids which feed items to be retrieved

GET "/feed/save"\
save feed items previously viewed\
parameter:\
1. itemIds : list of item ids to be saved


- Saved Item
GET "/saved"\
get all saved feed items\

DELETE "/saved"\
delete all saved feed items\

GET "/saved/{id}"\
get a saved feed item\
parameter:\
1. id : id of saved items, if not found returns 404 status code

DELETE "/saved/{id}"\
delete a saved feed item\
1. id : id of saved items, no error if not found

GET "/saved/count" : GET
returns numbers of saved items in format {"count" : numberOfItems}

GET "/saved/clear"\
delete all saved feed items, alias for DELETE "/saved" endpoint\

tba...\

