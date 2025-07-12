package com.spendscan.spendscan.feature.manage_transaction.domain.usecase

import com.spendscan.spendscan.core.domain.repository.TransactionRepository
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(transactionId: Long) = transactionRepository.deleteTransaction(transactionId)
}