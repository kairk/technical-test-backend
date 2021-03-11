package com.playtomic.tests.wallet.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@Slf4j
public class BigDecimalMapper {

    public Optional<BigDecimal> stringToBigDecimal(String string) {
        Optional<BigDecimal> result = Optional.empty();

        try {
            result = Optional.of(new BigDecimal(string));
        } catch (NumberFormatException ex) {
            log.error("Couldn't parse amount: " + string + " to BigDecimal");
        }

        return result;
    }
}
