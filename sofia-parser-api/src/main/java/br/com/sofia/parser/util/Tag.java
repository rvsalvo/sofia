package br.com.sofia.parser.util;


public class Tag {
    
    private String title;
    private String artist;
    private String album;
    private String year;
    private String comment;
    private byte genre;
    
    public String getTitle() {
    
        return title;
    }
    
    public void setTitle( String title ) {
    
        this.title = title;
    }
    
    public String getArtist() {
    
        return artist;
    }
    
    public void setArtist( String artist ) {
    
        this.artist = artist;
    }
    
    public String getAlbum() {
    
        return album;
    }
    
    public void setAlbum( String album ) {
    
        this.album = album;
    }
    
    public String getYear() {
    
        return year;
    }
    
    public void setYear( String year ) {
    
        this.year = year;
    }
    
    public String getComment() {
    
        return comment;
    }
    
    public void setComment( String comment ) {
    
        this.comment = comment;
    }
    
    public byte getGenre() {
    
        return genre;
    }
    
    public void setGenre( byte genre ) {
    
        this.genre = genre;
    }
    
}
