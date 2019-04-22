package com.person.demo;

import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;
import io.prometheus.client.spring.boot.EnableSpringBootMetricsCollector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RequestMapping;
import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;

@SpringBootApplication
public class DemoApplication extends SpringBootServletInitializer {

    //static final Counter requests = Counter.build().name("requests_total").help("Total number of requests.").register();

    //static final Histogram requestLatency = Histogram.build().name("requests_latency_seconds").help("Request latency in seconds.").register();

    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder) {
        return builder.sources(DemoApplication.class);
    }


//    @RequestMapping("/count")
//    String home()
//    {
//        requests.inc();
//        Histogram.Timer requestTimer = requestLatency.startTimer();
//        try
//        {
//            requests.inc();
//            return requests.toString();
//        }
//        finally
//        {
//            // Stop the histogram timer
//            requestTimer.observeDuration();
//        }
//    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
