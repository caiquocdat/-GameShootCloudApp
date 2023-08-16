package com.caiquocdat.shootclouds.model;

public class ScoreModel {
    private long id; // ID dùng để định danh trong database
    private int point;
    private int rank; // Thứ tự xếp hạng

    // Constructors
    public ScoreModel() {}

    public ScoreModel(int rank, int point) {
        this.rank = rank;
        this.point = point;
    }

    public ScoreModel(long id, int rank, int point) {
        this.id = id;
        this.rank = rank;
        this.point = point;
    }

    // Getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "Score{" +
                "id=" + id +
                ", rank=" + rank +
                ", point=" + point +
                '}';
    }
}
