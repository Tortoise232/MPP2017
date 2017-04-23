/**
 * Created by teo on 11-Mar-17.
 */



public class Rental extends BaseEntity<Integer>{


    @Override
    public String toString() {
        return movie + ";" + client;
    }

    private int movie;
    private int client;
    static int globalID = 0;
    //this might be ok, dunno if it's better to just use the client id or movie name
    //UPDATE: it's definitely not ok. i hate my life

    static int getGlobalID(){
        return ++globalID;
    }

    public Rental(int movie, int client, int rentId) {
        this.movie = movie;
        this.client = client;
        this.setId(rentId);
    }

    public int getMovie() {
        return movie;
    }

    public void setMovie(int movie) {
        this.movie = movie;
    }

    public int getClient() {
        return client;
    }

    public void setClient(int client) {
        this.client = client;
    }
}
