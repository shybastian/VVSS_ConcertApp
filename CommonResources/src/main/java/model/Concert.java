package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Concert implements Serializable {
    private int id, totalTickets, soldTickets;
    private String location, artist;
    private LocalDate date;
    private LocalTime time;

    public Concert(int id, String artist, int totalTickets, int soldTickets, String location, LocalDate date, LocalTime time) {
        this.id = id;
        this.artist = artist;
        this.totalTickets = totalTickets;
        this.soldTickets = soldTickets;
        this.location = location;
        this.date = date;
        this.time = time;
    }

    public Concert(String artist, String location, LocalDate date, LocalTime time, int totalTickets, int soldTickets) {
        this.artist = artist;
        this.totalTickets = totalTickets;
        this.soldTickets = soldTickets;
        this.location = location;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getSoldTickets() {
        return soldTickets;
    }

    public void setSoldTickets(int soldTickets) {
        this.soldTickets = soldTickets;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Concert{" +
                "id=" + id +
                ", totalTickets=" + totalTickets +
                ", soldTickets=" + soldTickets +
                ", location='" + location + '\'' +
                ", artist='" + artist + '\'' +
                ", date=" + date +
                ", time=" + time +
                '}';
    }
}
