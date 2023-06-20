package com.eduard.spring1;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class ClientMainApp {

	public static void main(String[] args) {
		System.out.println("Starting");

		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

		MyRestClient myRestClient = context.getBean(MyRestClient.class);

		 myRestClient.initializeTestErrorSimulator("4"); //Server status is OK
		 //myRestClient.initializeTestErrorSimulator("5"); //Server status is KO

		boolean status = myRestClient.makeApiCall();

		System.out.println("Server status is " + (status ? "OK" : "KO"));
	}
}
