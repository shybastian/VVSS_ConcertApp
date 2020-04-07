package server;

import model.Concert;
import service.ConcertService;
import services.ApplicationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServerImplementation {

    private ConcertService concertService;


    public List<Concert> filterConcertsByGivenDate(LocalDate date) throws ApplicationException {

        if (date.isBefore(LocalDate.now())) {
            throw new ApplicationException("You can't see concerts from the past!");
        } else if (date.isAfter(LocalDate.of(2023, 1, 1))) {
            throw new ApplicationException("The date is too far in the future!");
        } else {
            Iterable<Concert> allConcerts = concertService.findAll();
            List<Concert> filteredConcerts = new ArrayList<>();

            for(Concert concert : allConcerts) {
                if (concert.getDate().equals(date)) {
                    filteredConcerts.add(concert);
                }
            }

            return filteredConcerts;
        }
    }
}
