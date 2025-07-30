package com.example.personal_finance_tracker.repo;

import com.example.personal_finance_tracker.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface TransactionEntityRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findByUserIdAndTransactionDateBetween(Long userId, LocalDate start, LocalDate end);
}