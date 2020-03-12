package service.beanConfigs;

import org.springframework.context.annotation.Bean;
import repository.repositories.TransactionRepository;
import repository.validators.TransactionRepoValidator;
import service.TransactionService;

public class TransactionServiceConfig {

    @Bean(name="transactionRepository")
    public TransactionRepository createTransactionRepository()
    {
        return new TransactionRepository();
    }

    @Bean(name="transactionRepositoryValidator")
    public TransactionRepoValidator createTransactionRepositoryValidator()
    {
        return new TransactionRepoValidator();
    }

    @Bean(name="transactionService")
    public TransactionService createTransactionService()
    {
        TransactionService transactionService =
                new TransactionService(createTransactionRepository(), createTransactionRepositoryValidator());
        return transactionService;
    }
}
