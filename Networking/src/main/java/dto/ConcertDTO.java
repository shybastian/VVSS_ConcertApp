package dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class ConcertDTO implements Serializable {
    private int id, totalTickets, soldTickets;
    private String location, artist;
    private LocalDate date;
    private LocalTime time;

    public ConcertDTO(int id, int totalTickets, int soldTickets, String location, String artist, LocalDate date, LocalTime time) {
        this.id = id;
        this.totalTickets = totalTickets;
        this.soldTickets = soldTickets;
        this.location = location;
        this.artist = artist;
        this.date = date;
        this.time = time;
    }

    @Override
    public String toString() {
        return "ConcertDTO{" +
                "id=" + id +
                ", totalTickets=" + totalTickets +
                ", soldTickets=" + soldTickets +
                ", location='" + location + '\'' +
                ", artist='" + artist + '\'' +
                ", date=" + date +
                ", time=" + time +
                '}';
    }

    public int getId() {
        return id;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public int getSoldTickets() {
        return soldTickets;
    }

    public String getLocation() {
        return location;
    }

    public String getArtist() {
        return artist;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }
}
