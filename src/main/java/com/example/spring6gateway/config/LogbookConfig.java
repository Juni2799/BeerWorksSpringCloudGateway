package com.example.spring6gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.HttpLogFormatter;
import org.zalando.logbook.Sink;
import org.zalando.logbook.json.JsonHttpLogFormatter;
import org.zalando.logbook.logstash.LogstashLogbackSink;

// The intent of below Config class is to use the dependencies of LogStash
// and generate a more proper compilation of the logs in JSON output.
@Configuration
public class LogbookConfig {
    @Bean
    public Sink LogbookLogStash(){
        HttpLogFormatter formatter = new JsonHttpLogFormatter();
        LogstashLogbackSink sink = new LogstashLogbackSink(formatter);
        return sink;
    }
}
