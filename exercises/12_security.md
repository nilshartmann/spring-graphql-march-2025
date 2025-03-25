# Exercise: Security

## Preparation

* Please set the property `demo-token-generator.enable` to `true` in the `application.properties` file
* When you restart the application, two tokens will be printed to the console—one for a user with the role `ROLE_ADMIN` and one with `ROLE_USER`
* When sending queries from GraphiQL, you can include the token in the `Header` tab to execute queries as either of the two users (or leave it out for anonymous access)

## Steps

* The `createComment` mutation should only be allowed for users with the `ROLE_USER` role
* A story should only be returned if either:
    * it does **not** have the `PublicationState` set to `draft`
    * **or** the user has the `ROLE_ADMIN` role
    * (Admins can see everything; other users can only see stories that are in `review` or `published`)

## Documentation

* Spring Security `Authentication` object: https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html#servlet-authentication-authentication
* `@PreAuthorize` for checking permissions before method calls: https://docs.spring.io/spring-security/reference/servlet/authorization/method-security.html#use-preauthorize
* `@PostAuthorize` for authorizing method return values: https://docs.spring.io/spring-security/reference/servlet/authorization/method-security.html#use-postauthorize
* `@PostFilter` for filtering returned lists based on authorization: https://docs.spring.io/spring-security/reference/servlet/authorization/method-security.html#use-postfilter

* Background: "Spring Expression Language (SpEL)": https://docs.spring.io/spring-framework/reference/core/expressions.html
