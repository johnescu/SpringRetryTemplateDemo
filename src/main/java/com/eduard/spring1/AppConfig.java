package com.eduard.spring1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableRetry
@ComponentScan
public class AppConfig {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public RetryTemplate retryTemplate() {
		RetryTemplate retryTemplate = new RetryTemplate();

		FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
		backOffPolicy.setBackOffPeriod(5000L); // 5-second interval between retries
		retryTemplate.setBackOffPolicy(backOffPolicy);

		RetryPolicy retryPolicy = new SimpleRetryPolicy(5) {
			@Override
			public boolean canRetry(RetryContext context) {
				boolean rez = super.canRetry(context);
				Throwable lastThrowable = context.getLastThrowable();
				if (lastThrowable instanceof HttpServerErrorException) {
					HttpStatus statusCode = ((HttpServerErrorException) lastThrowable).getStatusCode();
					return rez && (statusCode == HttpStatus.SERVICE_UNAVAILABLE);  // Retry on HTTP status code 503
				}
				return rez;
			}
		};
		retryTemplate.setRetryPolicy(retryPolicy);
		return retryTemplate;
	}

}
