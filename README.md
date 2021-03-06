# Basic Back-end for blogging platform (Blog API services)

## About the project
One of home/job tasks I got while looking for my first job as a web developer;
Application made using <i><b>Java 8, Spring Boot, Spring Web, Spring Data JPA, Spring Security, H2-in-memory-database, Lombok</b></i>.
 
There is a login and registration functionality included;\
You can register new user, login with them using <b>Basic Auth</b>;\
Validation for registering new users, must provide valid email and password min length is 8 by default;

Anonymous can view all posts or by given id;\
Authenticated users can add new posts, view only their own posts, edit or delete them (CRUD);\
Validation for creating new posts, body must not be empty, title must have length of 7 by default;\
Basic authorization rules ensures that users only able to edit or delete their own posts; 

## How to use the application

To launch the application run this command (uses maven wrapper):
```
$ ./mvnw clean spring-boot:run
```
Or alternatively using your installed maven version:
```
$ mvn clean spring-boot:run
```
### Use <b><i>curl</i></b> to interact with application services
Please note application uses default Tomcat listening port: 8080, and in examples it run on localhost machine.

#### Get all blog posts by all user
```
curl --location --request GET 'http://localhost:8080/posts'
```

#### Get all own (currently authenticated) blog posts
```
curl --location --request GET 'http://localhost:8080/posts/user' \
--header 'Authorization: Basic dXNlckBtYXJ0eW5hcy5vcmc6cGFzc3dvcmQ='
```

#### Get all blog posts by given user email
```
curl --location --request GET 'http://localhost:8080/posts/user/user@martynas.org'
```

#### Get a post by given id
```
curl --location --request GET 'http://localhost:8080/posts/3'
```

#### Create new blog post
```
curl --location --request POST 'http://localhost:8080/posts' \
--header 'Authorization: Basic dXNlckBtYXJ0eW5hcy5vcmc6cGFzc3dvcmQ=' \
--form 'title=Post Title' \
--form 'body=Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.'
```

#### Replace a post by given id (only if one exists and is modified by same user post was created)
```
curl --location --request PUT 'http://localhost:8080/posts/3' \
--header 'Authorization: Basic dXNlckBtYXJ0eW5hcy5vcmc6cGFzc3dvcmQ=' \
--form 'title=Post Title' \
--form 'body=Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.'
```

#### Delete a post by given id (only if one exists and is deleted by same user post was created)
```
curl --location --request DELETE 'http://localhost:8080/posts/2' \
--header 'Authorization: Basic dXNlckBtYXJ0eW5hcy5vcmc6cGFzc3dvcmQ='
```