# TopPosts:Digitally Remastered

TopPosts is a data driven Java web app that uses Spring, Hibernate, and PostgreSQL to searches the web each night and find the most popular posts. The logic to determine what is popular and what isn't is based on data from popular social networks.

To build the project:

```gradlew cleaneclipse eclipse```

On the live server, the application uses PostgreSQl, but for local development, the project uses an embedded H2 database.

To start the project locally:

```gradlew bootrun```

The application  is scheduled to run a job nightly to get the top posts. To populate the database manually outside of the schedule, use jconsole to connect to the running application and execute the process with the following MBean:

com.mcnavish.topposts.scheduler -> ProcessFeedsTask -> procesFeedsTask -> Operations -> forceProcessFeeds()


You are now able to go to http://localhost:8080 and view the user facing application.

You can also view the live application at:  http://topposts-mcnavishapps.rhcloud.com/
