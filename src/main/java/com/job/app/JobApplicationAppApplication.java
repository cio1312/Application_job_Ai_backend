package com.job.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JobApplicationAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobApplicationAppApplication.class, args);
	}

}

//jdbc:h2:mem:testdb
//http://localhost:8080/h2-console/login.jsp?jsessionid=e486320ea60c51e4d083b1598255350f


/*
 * JDBC URL: jdbc:h2:./data/testdb (path to your H2 database file) Username: sa
 * Password: password
 */

/*
 * https://myaccount.google.com/security Go to Google Account settings. Navigate
 * to Security and turn on Less secure app access (note that this is a security
 * risk).
 */