package com.example.jobrunr.hikaricp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class HikariCPIntegrationTest {
    @Test
    public void givenConnection_thenFetchDbData() {
        List<Employee> employees = HikariCPDemo.fetchData();
        assertEquals(4, employees.size());
    }
}
