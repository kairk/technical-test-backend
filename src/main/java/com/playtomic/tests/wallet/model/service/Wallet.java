package com.playtomic.tests.wallet.model.service;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Wallet {
    Long id;
    BigDecimal balance;
}
