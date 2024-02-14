package com.carrepairservice.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class CarServiceMapperTest {

    private CarServiceMapper carServiceMapper;

    @BeforeEach
    public void setUp() {
        carServiceMapper = new CarServiceMapperImpl();
    }
}
