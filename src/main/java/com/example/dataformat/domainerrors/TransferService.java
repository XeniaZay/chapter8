package com.example.dataformat.domainerrors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
public class TransferService {

    private static final Logger log = LoggerFactory.getLogger(TransferService.class);

    private static final Map<String, Account> ACCOUNTS = Map.of(
            "ACC-1", new Account("ACC-1", new BigDecimal("1000.00"), "OPEN"),
            "ACC-2", new Account("ACC-2", new BigDecimal("500.00"), "OPEN"),
            "ACC-CLOSED", new Account("ACC-CLOSED", new BigDecimal("100.00"), "CLOSED"),
            "ACC-POOR", new Account("ACC-POOR", new BigDecimal("10.00"), "OPEN")
    );

    public TransferResponse transfer(TransferRequest request) {

        // счёт-источник закрыт → бизнес-ошибка 409
        Account from = ACCOUNTS.get(request.fromAccountId());
        if (from == null) {
            throw new AccountClosedException(request.fromAccountId());
        }
        if ("CLOSED".equals(from.status())) {
            throw new AccountClosedException(request.fromAccountId());
        }

        // получатель
        Account to = ACCOUNTS.get(request.toAccountId());
        if (to == null) {
            throw new AccountClosedException(request.toAccountId());
        }
        if ("CLOSED".equals(to.status())) {
            throw new AccountClosedException(request.toAccountId());
        }

        // недостаточно средств → бизнес-ошибка 422
        if (from.balance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException(
                    from.id(), from.balance(), request.amount());
        }

        String transferId = UUID.randomUUID().toString();

        log.info("Transfer completed transferId={} from={} to={} amount={}",
                transferId, from.id(), to.id(), request.amount());

        return new TransferResponse(
                transferId,
                from.id(),
                to.id(),
                request.amount(),
                "COMPLETED"
        );
    }

    private record Account(String id, BigDecimal balance, String status) {}
}
