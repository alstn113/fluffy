package com.pass;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@Profile("local")
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        init();
    }

    private void init() {
    }
}
