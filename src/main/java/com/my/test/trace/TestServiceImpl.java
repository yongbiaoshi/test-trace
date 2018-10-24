package com.my.test.trace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestServiceImpl implements TestService {
    @Override
    public void test() {
        log.info("测试方法-test");
    }
}
