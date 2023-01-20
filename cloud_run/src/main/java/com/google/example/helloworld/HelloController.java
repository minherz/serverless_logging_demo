package com.google.example.helloworld;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.logging.Severity;

@RestController
public class HelloController {
    static final long ONE_SECOND_IN_MS = 1000;
    static Map<String, String> CONTROLLER_LABELS = Map.of("class", "HelloController", "key2", "value2");

    @GetMapping("/")
    public String index() {
        HelloWorldApplication.Log(
                Severity.INFO, Map.of("method", "index", "action", "start"), CONTROLLER_LABELS);
        emulatingWork();
        HelloWorldApplication.Log(
                Severity.INFO, Map.of("method", "index", "action", "end"), CONTROLLER_LABELS);
        return "Greetings, World! Hello from Cloud Run!";
    }

    private void emulatingWork() {
        HelloWorldApplication.Log(
                Severity.INFO, Map.of("method", "emulatingWork", "action", "start"), CONTROLLER_LABELS);
        try {
            Thread.sleep(ONE_SECOND_IN_MS);
        } catch (InterruptedException ex) {
            // ignore interrupts
            HelloWorldApplication.Log(Severity.WARNING, "sleep was interrupted", CONTROLLER_LABELS);
        }
        HelloWorldApplication.Log(
                Severity.INFO, Map.of("method", "emulatingWork", "action", "end"), CONTROLLER_LABELS);
    }
}
