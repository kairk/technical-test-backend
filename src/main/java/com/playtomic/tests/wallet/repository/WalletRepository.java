package com.playtomic.tests.wallet.repository;

import com.playtomic.tests.wallet.model.repository.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface WalletRepository extends JpaRepository<WalletEntity, Long> {
}
