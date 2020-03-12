package repository.interfaces;

import model.Concert;

import java.time.LocalDate;

public interface IConcertRepository extends IRepository<Concert,Integer> {
    Iterable<Concert> filterConcerts(LocalDate date);

}
