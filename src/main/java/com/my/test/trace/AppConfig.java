package com.my.test.trace;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.sleuth.instrument.web.TraceWebFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ServletComponentScan
public class AppConfig {



}
