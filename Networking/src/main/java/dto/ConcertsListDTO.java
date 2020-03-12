package dto;

import model.Concert;

import java.io.Serializable;
import java.util.List;

public class ConcertsListDTO implements Serializable {
    private List<Concert> concertList;

    public ConcertsListDTO(List<Concert> concertList) {
        this.concertList = concertList;
    }

    @Override
    public String toString() {
        return "ConcertsListDTO{" +
                "concertList=" + concertList +
                '}';
    }

    public List<Concert> getConcertList() {
        return concertList;
    }
}
