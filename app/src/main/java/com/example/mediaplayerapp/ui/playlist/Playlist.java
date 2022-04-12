package com.example.mediaplayerapp.ui.playlist;

public class Playlist {

    private int id;
    private int idResource;
    private String name;
    private String numbers;

    public Playlist(int id, int idResource, String name, String numbers) {
        this.id = id;
        this.idResource = idResource;
        this.name = name;
        this.numbers = numbers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdResource() {
        return idResource;
    }

    public void setIdResource(int idResource) {
        this.idResource = idResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }
}
