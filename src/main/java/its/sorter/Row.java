/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package its.sorter;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author mihailo-jankovic
 */
public class Row {

    private final SimpleStringProperty letter;
    private final SimpleStringProperty artist;
    private final SimpleStringProperty song;
    private final SimpleStringProperty type;

    public Row(String letter, String artist, String song, String type) {
        this.letter = new SimpleStringProperty(letter);
        this.artist = new SimpleStringProperty(artist);
        this.song = new SimpleStringProperty(song);
        this.type = new SimpleStringProperty(type);
    }

    public SimpleStringProperty letterProperty() {
        return letter;
    }

    public SimpleStringProperty artistProperty() {
        return artist;
    }

    public SimpleStringProperty songProperty() {
        return song;
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public String getLetter() {
        return letter.get();
    }

    public String getArtist() {
        return artist.get();
    }

    public String getSong() {
        return song.get();
    }

    public String getType() {
        return type.get();
    }
}
