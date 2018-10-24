package com.my.test.trace;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class HomeController {

    @Resource
    TestService testService;

    @GetMapping("")
    public String index() {
        testService.test();
        return "I'm OK!";
    }
}
