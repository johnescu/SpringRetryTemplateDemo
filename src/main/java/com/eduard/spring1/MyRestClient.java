package com.eduard.spring1;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;


@Service
public class MyRestClient {

	private final RestTemplate restTemplate;
	private final RetryTemplate retryTemplate;

	public MyRestClient(RestTemplate restTemplate, RetryTemplate retryTemplate) {
		this.restTemplate = restTemplate;
		this.retryTemplate = retryTemplate;
	}


	public void initializeTestErrorSimulator(String maxFailures) {

		restTemplate.postForObject("http://127.0.0.1:8080/reset/" + maxFailures, null, String.class);
	}

	public boolean makeApiCall() {
		RetryCallback<String, HttpServerErrorException> retryCallback = new RetryCallback<String, HttpServerErrorException>() {
			@Override
			public String doWithRetry(RetryContext context) throws HttpServerErrorException {
//				String url = "https://webhook.site/2c19a2d5-6bf3-40be-8f70-857a5be0ef51";
				String url = "http://127.0.0.1:8080/testme";

				System.out.println("API call to " + url);
				return restTemplate.getForObject(url, String.class);
			}
		};

		try {
			String result = retryTemplate.execute(retryCallback);
			System.out.println("API response: " + result);
			return true;
		} catch (HttpServerErrorException e) {
			System.out.println("API exception:" + e.getMessage());
			return false;
		}
	}
}
