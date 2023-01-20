package com.google.example.helloworld;

import java.io.BufferedWriter;
import java.util.Collections;
import java.util.Map;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.cloud.logging.LogEntry;
import com.google.cloud.logging.Logging;
import com.google.cloud.logging.LoggingOptions;
import com.google.cloud.logging.Payload.JsonPayload;
import com.google.cloud.logging.Payload.StringPayload;
import com.google.cloud.logging.Severity;
import com.google.cloud.logging.Synchronicity;

public class HelloWorldFunction implements HttpFunction {
  private static final String LOG_NAME = "helloworld-function";
  private static final long ONE_SECOND_IN_MS = 1000;
  private static Map<String, String> CONTROLLER_LABELS = Map.of("class", "HelloWorldFunction", "key2", "value2");

  public void service(final HttpRequest request, final HttpResponse response) throws Exception {
    final BufferedWriter writer = response.getWriter();

    try (Logging logging = LoggingOptions.getDefaultInstance().getService()) {
      logging.setWriteSynchronicity(Synchronicity.SYNC);
      Log(logging, Severity.INFO, Map.of("method", "service", "action", "start"), CONTROLLER_LABELS);
      emulatingWork(logging);
      Log(logging, Severity.INFO, Map.of("method", "service", "action", "end"), CONTROLLER_LABELS);
    }
    writer.write("Greetings, World! Hello from Cloud Functions!");
  }

  private void emulatingWork(Logging logging) {
    Log(logging, Severity.INFO, Map.of("method", "emulatingWork", "action", "start"), CONTROLLER_LABELS);
    try {
      Thread.sleep(ONE_SECOND_IN_MS);
    } catch (InterruptedException ex) {
      // ignore interrupts
      Log(logging, Severity.WARNING, "sleep was interrupted", CONTROLLER_LABELS);
    }
    Log(logging, Severity.INFO, Map.of("method", "emulatingWork", "action", "end"), CONTROLLER_LABELS);
  }

  private static void Log(Logging logging, Severity severity, String payload, Map<String, String> labels) {
    LogEntry entry = LogEntry.newBuilder(StringPayload.of(payload))
        .setSeverity(severity)
        .setLogName(LOG_NAME)
        .setLabels(labels)
        .build();
    logging.write(Collections.singleton(entry));
  }

  private static void Log(Logging logging, Severity severity, Map<String, ?> payload, Map<String, String> labels) {
    LogEntry entry = LogEntry.newBuilder(JsonPayload.of(payload))
        .setSeverity(severity)
        .setLogName(LOG_NAME)
        .setLabels(labels)
        .build();
    logging.write(Collections.singleton(entry));
  }
}
