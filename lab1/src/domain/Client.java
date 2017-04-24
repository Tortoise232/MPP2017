import java.util.ArrayList;

/**
 * Created by Petean Mihai on 3/6/2017.
 */
public class Client extends BaseEntity<Integer>{
    private ArrayList<Rental> contracts;
    private String name;
    static int globalID = 0;

    static int getGlobalID(){
        return ++globalID;
    }

    public ArrayList<Rental> getContracts() {
        return contracts;
    }

    public void setContracts(ArrayList<Rental> contracts) {
        this.contracts = contracts;
    }

    public Client() {
        this.contracts = new ArrayList<Rental>();
    }

    public Client(String name){
        this.name = name;
        this.contracts = new ArrayList<Rental>();
    }

    public Client(ArrayList<Rental> contracts, String name, int id) {
        this.contracts = contracts;
        this.name = name;
        this.setId(id);
    }

    @Override
    public String toString() {
        return name;
    }

    public Client(String name, int id){
        this.contracts = new ArrayList<Rental>();
        this.name = name;
        this.setId(id);
    }
    public ArrayList<Rental> getRentedMovies() {
        return contracts;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
