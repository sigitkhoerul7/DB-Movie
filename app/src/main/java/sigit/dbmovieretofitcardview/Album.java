package sigit.dbmovieretofitcardview;

/**
 * Created by sigit on 31/07/17.
 */

public class Album {
    public String name;
    private int numOfSong;
    private int thumbnail;

    public Album(){

    }
    public Album(String name, int numOfSong, int thumbnail){
        this.name = name;
        this.numOfSong = numOfSong;
        this.thumbnail = thumbnail;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public int getNumOfSong(){
        return numOfSong;
    }
    public void setNumOfSong(int numOfSong){
        this.numOfSong = numOfSong;
    }
    public int getThumbnail(){
        return thumbnail;
    }
    public void setThumbnail(int thumbnail){
        this.thumbnail = thumbnail;
    }
}
