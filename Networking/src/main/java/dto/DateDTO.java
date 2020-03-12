package dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class DateDTO implements Serializable {
    private LocalDate date;

    public DateDTO(LocalDate date)
    {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "DateDTO{" +
                "date=" + date +
                '}';
    }
}
