package service.beanConfigs;

import org.springframework.context.annotation.Bean;
import repository.repositories.ConcertRepository;
import service.ConcertService;

public class ConcertServiceConfig {

    @Bean(name="concertRepository")
    public ConcertRepository createConcertRepository()
    {
        return new ConcertRepository();
    }

    @Bean(name="concertService")
    public ConcertService createConcertService()
    {
        ConcertService concertService = new ConcertService(createConcertRepository());
        return concertService;
    }
}
