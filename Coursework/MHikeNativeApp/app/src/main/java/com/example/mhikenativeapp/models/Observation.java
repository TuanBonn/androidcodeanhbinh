package com.example.mhikenativeapp.models; // Đảm bảo đúng tên package

/**
 * Data Model cho một Quan sát (Observation) [cite: 660-661].
 * Quan trọng: Phải có 'hikeId' để biết nó thuộc về chuyến đi nào.
 */
public class Observation {

    private long id;
    private String observation; // Required [cite: 663]
    private String time;        // Required [cite: 664]
    private String comments;    // Optional [cite: 665]
    private long hikeId;        // Foreign Key (Khóa ngoại) để liên kết với bảng Hikes

    // Constructors
    public Observation() {
        // Constructor rỗng
    }

    public Observation(long id, String observation, String time, String comments, long hikeId) {
        this.id = id;
        this.observation = observation;
        this.time = time;
        this.comments = comments;
        this.hikeId = hikeId;
    }

    // Getters and Setters (Tự generate hoặc copy)

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public long getHikeId() {
        return hikeId;
    }

    public void setHikeId(long hikeId) {
        this.hikeId = hikeId;
    }
}