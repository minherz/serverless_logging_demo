package com.google.example.helloworld;

import java.util.Collections;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.cloud.logging.LogEntry;
import com.google.cloud.logging.Logging;
import com.google.cloud.logging.LoggingOptions;
import com.google.cloud.logging.Payload.JsonPayload;
import com.google.cloud.logging.Payload.StringPayload;
import com.google.cloud.logging.Severity;

@SpringBootApplication
public class HelloWorldApplication {
	private static Logging googleLog;
	private static final String LOG_NAME = "helloworld-service";
	private static Map<String, String> NO_LABELS = Collections.emptyMap();

	public static void main(String[] args) {
		String projectId = System.getenv("GOOGLE_PROJECT_ID");
		LoggingOptions options = LoggingOptions.newBuilder().setProjectId(projectId).build();
		googleLog = options.getService();

		HelloWorldApplication.Log(Severity.INFO, "helloworld is launched", null);

		SpringApplication.run(HelloworldApplication.class, args);
	}

	public static void Log(Severity severity, String payload, Map<String, String> labels) {
		if (labels == null) {
			labels = NO_LABELS;
		}
		LogEntry entry = LogEntry.newBuilder(StringPayload.of(payload))
				.setSeverity(severity)
				.setLogName(LOG_NAME)
				.setLabels(labels)
				.build();
		googleLog.write(Collections.singleton(entry));
	}

	public static void Log(Severity severity, Map<String, ?> payload, Map<String, String> labels) {
		if (labels == null) {
			labels = NO_LABELS;
		}
		LogEntry entry = LogEntry.newBuilder(JsonPayload.of(payload))
				.setSeverity(severity)
				.setLogName(LOG_NAME)
				.setLabels(labels)
				.build();
		googleLog.write(Collections.singleton(entry));
	}
}
