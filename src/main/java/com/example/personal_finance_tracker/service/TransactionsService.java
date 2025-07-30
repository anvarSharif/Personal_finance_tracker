package com.example.personal_finance_tracker.service;

import com.example.personal_finance_tracker.dto.TransactionDTO;
import com.example.personal_finance_tracker.dto.TransactionResponseDTO;
import com.example.personal_finance_tracker.entity.TransactionEntity;
import com.example.personal_finance_tracker.entity.UserEntity;
import com.example.personal_finance_tracker.entity.enums.TransactionType;
import com.example.personal_finance_tracker.repo.TransactionEntityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionsService {
    private final TransactionEntityRepository transactionEntityRepository;

    public TransactionsService(TransactionEntityRepository transactionEntityRepository) {
        this.transactionEntityRepository = transactionEntityRepository;
    }

    public TransactionResponseDTO saveTransaction(TransactionDTO transactionDTO, UserEntity currentUser) {
        TransactionEntity transaction = TransactionEntity.builder()
                .amount(transactionDTO.getAmount())
                .type(TransactionType.valueOf(transactionDTO.getType().toUpperCase()))
                .transactionDate(transactionDTO.getTransactionDate())
                .userId(currentUser.getId())
                .category(transactionDTO.getCategory())
                .description(transactionDTO.getDescription())
                .build();
        transactionEntityRepository.save(transaction);
        return toTransactionResponseDTO(transaction);
    }


    public List<TransactionResponseDTO> getTransactionsForUser(String month, UserEntity currentUser) {
        YearMonth yearMonth = YearMonth.parse(month);
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();
        List<TransactionEntity> transactionEntityList = transactionEntityRepository.findByUserIdAndTransactionDateBetween(currentUser.getId(), start, end);
        List<TransactionResponseDTO> transactionDTOList = new ArrayList<>();
        for (TransactionEntity transactionEntity : transactionEntityList) {
            transactionDTOList.add(toTransactionResponseDTO(transactionEntity));
        }
        return transactionDTOList;
    }


    private TransactionResponseDTO toTransactionResponseDTO(TransactionEntity transaction) {
        return TransactionResponseDTO.builder()
                .id(transaction.getId())
                .userId(transaction.getUserId())
                .amount(transaction.getAmount())
                .type(transaction.getType().name())
                .transactionDate(transaction.getTransactionDate())
                .userId(transaction.getUserId())
                .category(transaction.getCategory())
                .description(transaction.getDescription())
                .build();
    }
}
