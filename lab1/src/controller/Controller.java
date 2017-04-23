import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.StreamSupport;

/**
 * Created by Petean Mihai on 3/7/2017.
 */
public class Controller {
    private TRepository<Integer,Client> clientRepo;
    private TRepository<Integer,Movie> movieRepo;
    private TRepository<Integer, Rental> rentalRepo;

    public Controller() throws SQLException {
        this.clientRepo = new InMemoryRepository<>();
        this.movieRepo = new InMemoryRepository<>();
        this.rentalRepo = new InMemoryRepository<>();
    }

    public Controller(boolean a) throws SQLException {
        this.clientRepo = new DbClientRepo();
        this.movieRepo = new DbMovieRepo();
        this.rentalRepo = new DbRentalRepo();
    }

    public Controller(String clientFile, String movieFile, String rentalFile) throws SQLException {
        try {
            this.clientRepo = new FileRepository<>(clientFile, new ClientProcessor());
            this.movieRepo = new FileRepository<>(movieFile, new MovieProcessor());
            this.rentalRepo = new FileRepository<>(rentalFile, new RentalProcessor());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public Controller(String movieFile){
        this.clientRepo = new XMLRepository<>("clients.xml", new ClientProcessor());
        this.movieRepo = new XMLRepository<>(movieFile, new MovieProcessor());
        this.rentalRepo = new XMLRepository<>("rentals.xml", new RentalProcessor());
    }

    public Controller(TRepository<Integer,Client> clientRepo, TRepository<Integer,Movie> movieRepo) {
        this.clientRepo = clientRepo;
        this.movieRepo = movieRepo;
    }

    public TRepository<Integer,Client> getClientRepo() {
        return clientRepo;
    }

    public void setClientRepo(TRepository<Integer,Client> clientRepo) {
        this.clientRepo = clientRepo;
    }

    public TRepository<Integer, Movie> getMovieRepo() {
        return movieRepo;
    }

    public void setMovieRepo(TRepository<Integer, Movie> movieRepo) {
        this.movieRepo = movieRepo;
    }


    /**
     * Adds a movie to the movieRepo
     * @param  movie
     */
    public void addMovie(Movie movie) throws MovieException {
        MovieValidator.validate(movie);
        this.movieRepo.save(movie);
    }

    public void addRental(String movie, String client) throws RentalException {
        int movieID = this.findMovie(movie).getId();
        int clientID = this.findClient(client).getId();
        Rental myRental = new Rental(movieID,clientID,Rental.getGlobalID());
        addRental(myRental);
    }

    /**
     * Adds a rental to the clientRepo
     * @param  rental
     */
    public void addRental(Rental rental) throws RentalException {
        // TODO: 21-Mar-17 Update Client'ss rented movies list, update Movie status to 'rented'
        RentalValidator.validate(rental);
        this.rentalRepo.save(rental); }

    /**
     * Adds a client to the clientRepo
     * @param  client
     */
    public void addClient(Client client) throws ClientException {
        ClientValidator.validate(client);
        this.clientRepo.save(client);
    }

    /**
     * Finds the client with the given name
     * @param  clientName
     * @return an {@code Optional} - null if there is no entity with the given name, otherwise return the client with that name.
     *
     */
    public Client findClient(String clientName){
        return StreamSupport.stream(getClients().spliterator() , false)
                .filter(c -> c.getName().equals(clientName))
                .findFirst()
                .get();
    }
    /**
     * Finds the movie with the given name
     * @param  movieName
     * @return an {@code Optional} - null if there is no entity with the given name, otherwise return the movie with that name.
     *
     */
    public Movie findMovie(String movieName){
        return StreamSupport.stream(getMovies().spliterator(), false)
                .filter(m -> m.getName().equals(movieName))
                .findFirst()
                .get();
    }


    /**
     * Removes the movie with the given name
     * @param  movieName
     * @return an {@code Optional} - null if there is no entity with the given name, otherwise delete the movie with that name.
     *
     */
    public void removeMovie(String movieName) {
        final Movie[] result = new Movie[1];
        this.movieRepo.findAll().forEach((Movie item)-> {if(item.getName().equals(movieName)) result[0] = item;});
        this.movieRepo.delete(result[0].getId());
    }


    /**
     * Removes the rental contract with the given movie name and client name
     * @param  movieName
     * @param clientName
     * @return an {@code Optional} - null if there is no entity with the given name, otherwise delete the rental contract with the given names.
     *
     */
    public void removeRental(String movieName, String  clientName){
        this.rentalRepo.findAll().forEach((Rental rental) -> {
           boolean movieInRepo  = (this.movieRepo.findOne(rental.getMovie()).get().getName().equals(movieName)) ? true : false;
           boolean clientInRepo = (this.clientRepo.findOne(rental.getClient()).get().getName().equals(clientName)) ? true : false;
           if(movieInRepo & clientInRepo)
               this.rentalRepo.delete(rental.getId());
        });
    }

    /**
     * Removes the client with the given name
     * @param  clientName
     * @return an {@code Optional} - null if there is no client with the given name, otherwise delete the client with that name.
     *
     */
    public void removeClient(String clientName) {
        final Client[] result = new Client[1];
        this.clientRepo.findAll().forEach((Client item)-> {if(item.getName().equals(clientName)) result[0] = item;});
        this.clientRepo.delete(result[0].getId());
    }


    /**
     * Returns an iterable list of movies stored in the repo
     * @return an {@code Optional} - null if there is no movie in the repo, otherwise return an iterable of movies.
     *
     */
    public Iterable<Movie> getMovies(){
        return movieRepo.findAll();
    }


    /**
     * Returns an iterable list of clients stored in the repo
     * @return an {@code Optional} - null if there is no client in the repo, otherwise return an iterable of clients.
     *
     */
    public Iterable<Client> getClients(){
        return clientRepo.findAll();
    }


    /**
     * Returns an iterable list of rental contracts stored in the repo
     * @return an {@code Optional} - null if there is no contract in the repo, otherwise return an iterable of contracts.
     *
     */
    public Iterable<Rental> getRentals(){ return rentalRepo.findAll(); }


}
