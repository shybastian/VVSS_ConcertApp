package service;

import model.Concert;
import repository.repositories.ConcertRepository;

import java.time.LocalDate;
import java.util.List;

public class ConcertService {
    private ConcertRepository concertRepository;

    public ConcertRepository getConcertRepository() {
        return concertRepository;
    }

    public void setConcertRepository(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    public ConcertService(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    public Iterable<Concert> filterConcerts(LocalDate date)
    {
        return concertRepository.filterConcerts(date);
    }

    public void updateConcert(int idConcert, int soldTickets)
    {
        concertRepository.update(idConcert, soldTickets);
    }

    public Iterable<Concert> findAll()
    {
        return concertRepository.findAll();
    }
}
