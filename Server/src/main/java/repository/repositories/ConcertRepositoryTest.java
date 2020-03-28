package repository.repositories;

import model.Concert;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;
import java.time.LocalTime;

public class ConcertRepositoryTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();


    @org.junit.Test(expected=Exception.class)
    public void add_InvalidTickets() throws Exception {
        ConcertRepository concertRepository = new ConcertRepository();
        Concert concertInvalid = new Concert("Artist", "Stage", LocalDate.now(), LocalTime.now(), -2, -10);
        concertRepository.add(concertInvalid);
        expectedEx.expectMessage("Invalid tickets");
    }

    @org.junit.Test(expected=Exception.class)
    public void add_InvalidArtist() throws Exception {
        ConcertRepository concertRepository = new ConcertRepository();
        Concert concertInvalid = new Concert("", "Stage", LocalDate.now(), LocalTime.now(), 1000, 100);
        concertRepository.add(concertInvalid);
        expectedEx.expectMessage("Artist name invalid");
    }

    @org.junit.Test(expected=Exception.class)
    public void add_InvalidDate() throws Exception {
        ConcertRepository concertRepository = new ConcertRepository();
        Concert concertInvalid = new Concert("", "Stage", LocalDate.of(1999, 1, 1), LocalTime.now(), 1000, 100);
        concertRepository.add(concertInvalid);
        expectedEx.expectMessage("Invalid date");
    }

    @org.junit.Test
    public void add_Valid() throws Exception {
        ConcertRepository concertRepository = new ConcertRepository();
        Concert concertValid = new Concert(100, "Ariana Grande", 100, 100, "Location", LocalDate.now(), LocalTime.now());
        concertRepository.add(concertValid);
//        Concert checkIfRight = concertRepository.findOne(100);
//        assertThat(checkIfRight.getArtist(), is("Ariana Grande"));
        //should delete concert with id 100
    }



}