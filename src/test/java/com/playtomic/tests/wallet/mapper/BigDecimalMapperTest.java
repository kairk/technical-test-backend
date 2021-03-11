package com.playtomic.tests.wallet.mapper;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;

public class BigDecimalMapperTest {
    private final BigDecimalMapper mapper = new BigDecimalMapper();

    @Test
    public void respositoryToService_OK() {
        //Given
        String amount = "10.54126";
        BigDecimal expected = new BigDecimal("10.54126");
        //When
        Optional<BigDecimal> result = mapper.stringToBigDecimal(amount);

        //Then
        assertTrue(result.isPresent());
        assertEquals(0, expected.compareTo(result.get()));
    }

    @Test
    public void respositoryToService_KO() {
        //Given
        String amount = "error";
        //When
        Optional<BigDecimal> result = mapper.stringToBigDecimal(amount);

        //Then
        assertFalse(result.isPresent());
    }
}
