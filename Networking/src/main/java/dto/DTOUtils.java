package dto;

import model.Concert;
import model.Transactions;
import model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DTOUtils {

    public static User getUserFromDTO(UserDTO userDTO)
    {
        User user = new User(userDTO.getUsername(), userDTO.getPassword());
        return user;
    }

    public static UserDTO getDTOFromUser(User user)
    {
        UserDTO userDTO = new UserDTO(user.getUsername(), user.getPassword());
        return userDTO;
    }

    public static List<Concert> getListFromDTO(ConcertsListDTO concertsListDTO)
    {
        List<Concert> concertList = new ArrayList<>(concertsListDTO.getConcertList());
        return concertList;
    }

    public static ConcertsListDTO getDTOFromList(List<Concert> concertList)
    {
        ConcertsListDTO concertsListDTO = new ConcertsListDTO(concertList);
        return concertsListDTO;
    }

    public static DateDTO getDTOfromDate(LocalDate date)
    {
        DateDTO dateDTO = new DateDTO(date);
        return dateDTO;
    }

    public static LocalDate getDatefromDTO(DateDTO dateDTO)
    {
        LocalDate date = LocalDate.parse(dateDTO.getDate().toString());
        return date;
    }

    public static TransactionDTO getDTOFromTransaction(Transactions transaction)
    {
        TransactionDTO transactionDTO = new TransactionDTO(transaction.getIdConcert(), transaction.getBoughtTickets(),
                transaction.getSellerUsername(), transaction.getBuyerName());
        return transactionDTO;
    }

    public static Transactions getTransactionsFromDTO(TransactionDTO transactionDTO)
    {
        Transactions transactions = new Transactions(transactionDTO.getConcertId(), transactionDTO.getBoughtTickets(),
                transactionDTO.getSellerUsername(), transactionDTO.getBuyerName());
        return transactions;
    }


}
