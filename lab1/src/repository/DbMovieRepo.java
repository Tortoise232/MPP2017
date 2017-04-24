import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by Petean Mihai on 3/27/2017.
 */
public class DbMovieRepo implements TRepository<Integer, Movie>{
    Connection connection;
    private static int maxID = 0;
    public DbMovieRepo() throws SQLException {
         this.connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sdiProject",
                 "postgres",
                 "teslaCoil");
        PreparedStatement myState = this.connection.prepareStatement("SELECT MAX(id) FROM movies");
        ResultSet myResult = myState.executeQuery();
        myResult.next();
        this.maxID = myResult.getInt(1);
     }

    @Override
    public Optional<Movie> findOne(Integer integer) {
        try {
            PreparedStatement myState = this.connection.prepareStatement("SELECT * FROM movies WHERE id = " + integer);
            ResultSet myResult = myState.executeQuery();
            myResult.next();
            return Optional.of(new Movie(myResult.getString("name"), myResult.getInt("rating"), myResult.getInt("yearofrelease"), myResult.getInt("id")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Movie> findAll() {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            PreparedStatement myState = this.connection.prepareStatement("SELECT * FROM movies");
            ResultSet myResult = myState.executeQuery();
            while(myResult.next())
            {
                movies.add(new Movie(myResult.getString("name"), myResult.getInt("rating"), myResult.getInt("yearofrelease"), myResult.getInt("id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }


    @Override
    public Optional<Movie> save(Movie entity) {
        try {
            PreparedStatement myState = this.connection.prepareStatement("INSERT INTO movies VALUES(" + ++this.maxID + "," + entity.getYearOfRelease() + "," + entity.getRating() + ",'" + entity.getName() + "')");
            myState.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Movie> delete(Integer integer) {

        try {
            PreparedStatement myState = this.connection.prepareStatement("DELETE FROM movies WHERE id = " + integer);
            myState.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Movie> update(Movie entity) {
        try {
            PreparedStatement myState = this.connection.prepareStatement("UPDATE movies SET name = " + entity.getName() + ", rating = " + entity.getRating() + ", yearofrelease = " + entity.getYearOfRelease() + "WHERE id = " + entity.getId());
            myState.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }
}
