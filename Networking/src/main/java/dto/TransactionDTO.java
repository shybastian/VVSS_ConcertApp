package dto;

import java.io.Serializable;

public class TransactionDTO implements Serializable {
    private int concertId, boughtTickets;
    private String sellerUsername,buyerName;

    public TransactionDTO(int concertId, int boughtTickets, String sellerUsername, String buyerName) {
        this.concertId = concertId;
        this.boughtTickets = boughtTickets;
        this.sellerUsername = sellerUsername;
        this.buyerName = buyerName;
    }

    @Override
    public String toString() {
        return "TransactionDTO{" +
                "concertId=" + concertId +
                ", boughtTickets=" + boughtTickets +
                ", sellerUsername='" + sellerUsername + '\'' +
                ", buyerName='" + buyerName + '\'' +
                '}';
    }

    public int getConcertId() {
        return concertId;
    }

    public int getBoughtTickets() {
        return boughtTickets;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public String getBuyerName() {
        return buyerName;
    }
}
