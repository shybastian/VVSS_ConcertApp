package model;

import java.io.Serializable;

public class Transactions implements Serializable {
    private int id, idConcert, boughtTickets;
    private String sellerUsername, buyerName;

    public Transactions(int idConcert, int boughtTickets, String sellerUsername, String buyerName) {
        this.idConcert = idConcert;
        this.boughtTickets = boughtTickets;
        this.sellerUsername = sellerUsername;
        this.buyerName = buyerName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdConcert() {
        return idConcert;
    }

    public void setIdConcert(int idConcert) {
        this.idConcert = idConcert;
    }

    public int getBoughtTickets() {
        return boughtTickets;
    }

    public void setBoughtTickets(int boughtTickets) {
        this.boughtTickets = boughtTickets;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    @Override
    public String toString() {
        return "Transactions{" +
                "id=" + id +
                ", idConcert=" + idConcert +
                ", boughtTickets=" + boughtTickets +
                ", sellerUsername='" + sellerUsername + '\'' +
                ", buyerName='" + buyerName + '\'' +
                '}';
    }
}
