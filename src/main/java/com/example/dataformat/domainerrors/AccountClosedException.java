package com.example.dataformat.domainerrors;

public class AccountClosedException extends RuntimeException {
    private final String accountId;

    public AccountClosedException(String accountId) {
        super("Account " + accountId + " is closed and cannot be used for transfers");
        this.accountId = accountId;
    }
    public String getAccountId() {
        return accountId;
    }

}
