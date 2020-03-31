package service;

import model.Transactions;
import repository.repositories.TransactionRepository;
import repository.validators.TransactionRepoValidator;

public class TransactionService {
    private TransactionRepository transactionRepository;
    private TransactionRepoValidator transactionRepoValidator;

    public TransactionService(TransactionRepository transactionRepository, TransactionRepoValidator transactionRepoValidator) {
        this.transactionRepository = transactionRepository;
        this.transactionRepoValidator = transactionRepoValidator;
    }

    public TransactionRepository getTransactionRepository() {
        return transactionRepository;
    }

    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public TransactionRepoValidator getTransactionRepoValidator() {
        return transactionRepoValidator;
    }

    public void setTransactionRepoValidator(TransactionRepoValidator transactionRepoValidator) {
        this.transactionRepoValidator = transactionRepoValidator;
    }

    public void addTransaction(Transactions transaction) throws Exception {
        transactionRepository.add(transaction);
    }

    public boolean validateTransaction(Transactions transaction)
    {
        return transactionRepoValidator.canContinueTransaction(transaction);
    }
}
