package com.playtomic.tests.wallet.model.repository;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WalletEntity {
    @Id
    @GeneratedValue
    Long id;
    BigDecimal balance;
}
