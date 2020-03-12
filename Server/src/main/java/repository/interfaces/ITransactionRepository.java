package repository.interfaces;

import model.Transactions;

public interface ITransactionRepository extends IRepository<Transactions, Integer> {
    Transactions findOne(int idTransaction);
}
