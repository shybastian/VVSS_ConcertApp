package server;

import model.Concert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import service.ConcertService;
import services.ApplicationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServerImplTest {

    @InjectMocks
    private ServerImplementation server;

    @Mock
    private ConcertService concertService;

    @Test(expected = ApplicationException.class)
    public void filterConcertPastException() throws ApplicationException {
        server.filterConcertsByGivenDate(LocalDate.of(2012, 4, 12));
    }

    @Test(expected = ApplicationException.class)
    public void filterConcertFutureException() throws ApplicationException {
        server.filterConcertsByGivenDate(LocalDate.of(2029, 4, 12));
    }

    @Test
    public void filterConcert() throws ApplicationException {
        List<Concert> concerts = new ArrayList<>();
        concerts.add(new Concert(null, null,
                LocalDate.of(2020, 4, 7), null, 5, 3));
        concerts.add(new Concert(null, null,
                LocalDate.of(2012, 4, 1), null, 5, 3));
        concerts.add(new Concert(null, null,
                LocalDate.of(2020, 4, 7), null, 5, 3));

        when(concertService.findAll()).thenReturn(concerts);

        List<Concert> filteredConcerts = server.filterConcertsByGivenDate(LocalDate.of(2020, 4, 7));

        assertEquals(2, filteredConcerts.size());
        assertEquals(LocalDate.of(2020, 4, 7), filteredConcerts.get(1).getDate());
    }
}