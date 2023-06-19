package com.eduard.spring1;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyTestController {
	AtomicInteger errorCounterSimulator = new AtomicInteger(0);
	AtomicInteger maxNumberOfFailures = new AtomicInteger(0);

	@GetMapping("/testme")
	public ResponseEntity<String> testEndpoint() {
		errorCounterSimulator.incrementAndGet();
		if (errorCounterSimulator.get() <= maxNumberOfFailures.get()) {
			throw new RuntimeException("error counter is " + errorCounterSimulator);
		}
		return ResponseEntity.ok("Test OK");
	}

	@PostMapping("/reset/{maxFailures}")
	public ResponseEntity<String> resetEndpoint(@PathVariable("maxFailures") int maxFailures) {
		maxNumberOfFailures.getAndSet(maxFailures);
		errorCounterSimulator.getAndSet(0);
		return ResponseEntity.ok("Reset OK");
	}
}