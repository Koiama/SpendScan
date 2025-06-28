package com.spendscan.core.domain.models

/**
 * Аккаунт банковского счёта
 */

data class AccountBrief(
    val id: Int,
    val name: String,
    val currency: String,
    val balance: String
)