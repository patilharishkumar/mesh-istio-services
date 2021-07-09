package com.een.services.cmusers.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;

@RestController
@RequestMapping("/cmusers")
public class CmUsersController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmUsersController.class);

    @Autowired
    BuildProperties buildProperties;
    @Autowired
    RestTemplate restTemplate;
    @Value("${VERSION}")
    private String version;

    @GetMapping("/ping")
    public String ping() {
        LOGGER.info("Ping: name={}, version={}", buildProperties.getName(), version);
        String response = restTemplate.getForObject("http://apiv3-service:8080/users/ping", String.class);
        Tracer tracer = GlobalTracer.get();
        Tracer.SpanBuilder spanBuilder = tracer.buildSpan("CustomSpan")
                .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_SERVER);
 
        Span span = spanBuilder.start();
        Tags.COMPONENT.set(span, "CmUsersController");
        span.setTag("testtag", "test");
        span.finish();
        LOGGER.info("Calling: response={}", response);
        return "\nCamera Manager-service " + version + ". Calling... " + response;
    }

    @GetMapping("/ping-with-random-error")
    public ResponseEntity<String> pingWithRandomError() {
        LOGGER.info("Ping with random error: name={}, version={}", buildProperties.getName(), version);
        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity("http://apiv3-service:8080/users/ping-with-random-error", String.class);
        LOGGER.info("Calling: responseCode={}, response={}", responseEntity.getStatusCode(), responseEntity.getBody());
        return new ResponseEntity<>("I'm caller-service " + version + ". Calling... " + responseEntity.getBody(), responseEntity.getStatusCode());
    }

    @GetMapping("/ping-with-random-delay")
    public String pingWithRandomDelay() {
        LOGGER.info("Ping with random delay: name={}, version={}", buildProperties.getName(), version);
        String response = restTemplate.getForObject("http://apiv3-service:8080/users/ping-with-random-delay", String.class);
        LOGGER.info("Calling: response={}", response);
        return "I'm caller-service " + version + ". Calling... " + response;
    }

}
