package com.playtomic.tests.wallet.model.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import java.math.BigDecimal;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@OptimisticLocking
public class WalletEntity {
    @Id
    @GeneratedValue
    Long id;

    BigDecimal balance;

    //Version used for locking optimistic strategy, avoids concurrent updates on entity
    @Version
    Integer version;
}
