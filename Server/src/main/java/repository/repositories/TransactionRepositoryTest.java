package repository.repositories;

import model.Transactions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TransactionRepositoryTest {
    TransactionRepository repository;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        this.repository = new TransactionRepository();
    }

    @Test
    public void add() throws Exception {
//        add_ECTickets();
//        add_invalidBuyer();
//        add_invalidSeller();
//        add_invalidTickets();
    }

    @Test(expected = Exception.class)
    public void add_invalidTickets() throws Exception{
        Transactions transaction = new Transactions(1, -1, "Sebi", "Sebi");
        repository.add(transaction);
        expectedEx.expectMessage("Bought Tickets is invalid");
    }

    @Test
    public void add_ECTickets() throws Exception {
        Transactions transaction = new Transactions(1, 0, "Sebi", "Sebi");
        repository.add(transaction);
        expectedEx.expectMessage("");
    }

    @Test(expected = Exception.class)
    public void add_invalidSeller() throws Exception {
        Transactions transaction = new Transactions(1, 1, "", "Sebi");
        repository.add(transaction);
        expectedEx.expectMessage("Seller Name invalid");
    }

    @Test(expected = Exception.class)
    public void add_invalidBuyer() throws Exception {
        Transactions transaction = new Transactions(1, 1, "Sebi", "");
        repository.add(transaction);
        expectedEx.expectMessage("Buyer Name invalid");
    }
}