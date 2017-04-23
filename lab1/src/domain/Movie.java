
/**
 * Created by Petean Mihai on 3/6/2017.
 */
public class Movie extends BaseEntity<Integer>{
    private String name;
    private int rating;
    private int yearOfRelease;
    private boolean rented;
    private static int globalID = 0;

    public Rental getContract() {
        return contract;
    }

    public void setContract(Rental contract) {
        this.contract = contract;
    }

    private Rental contract = null;
    public static int getGlobalID(){
        return ++globalID;
    }
    @Override
    public String toString() {
        return name + ";" + rating + ";" + yearOfRelease;
    }

    public Movie(String name, int rating, int yearOfRelease) {
        this.name = name;
        this.rating = rating;
        this.yearOfRelease = yearOfRelease;
        this.setId(-1); //because we don't care about it
    }

    public Movie(String name, int rating, int yearOfRelease,int id) {
        this.name = name;
        this.rating = rating;
        this.yearOfRelease = yearOfRelease;
        this.rented = false;
        this.setId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getYearOfRelease() {
        return yearOfRelease;
    }

    public void setYearOfRelease(int yearOfRelease) {
        this.yearOfRelease = yearOfRelease;
    }

    public boolean isRented() {
        return rented;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }
}
