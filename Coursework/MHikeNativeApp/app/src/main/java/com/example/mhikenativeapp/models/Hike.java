package com.example.mhikenativeapp.models; // Đảm bảo đúng tên package

/**
 * Data Model cho một chuyến đi (Hike).
 * ĐÃ NÂNG CẤP với 2 trường tự định nghĩa: weather và trailCondition.
 */
public class Hike {

    private long id;
    private String name;
    private String location;
    private String date;
    private String parkingAvailable;
    private String length;
    private String difficulty;
    private String description;

    // YÊU CẦU MỚI: Thêm 2 trường tự định nghĩa
    private String weather;
    private String trailCondition;

    // Constructors
    public Hike() {
        // Constructor rỗng
    }

    // Constructor đầy đủ (đã cập nhật)
    public Hike(long id, String name, String location, String date, String parkingAvailable, String length, String difficulty, String description, String weather, String trailCondition) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.parkingAvailable = parkingAvailable;
        this.length = length;
        this.difficulty = difficulty;
        this.description = description;
        this.weather = weather; // MỚI
        this.trailCondition = trailCondition; // MỚI
    }

    // --- Getters and Setters ---
    // (Bao gồm cả 2 trường mới ở cuối)

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getParkingAvailable() {
        return parkingAvailable;
    }

    public void setParkingAvailable(String parkingAvailable) {
        this.parkingAvailable = parkingAvailable;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // --- Getters/Setters MỚI ---

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTrailCondition() {
        return trailCondition;
    }

    public void setTrailCondition(String trailCondition) {
        this.trailCondition = trailCondition;
    }
}