import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by Petean Mihai on 3/27/2017.
 */
public class DbRentalRepo implements TRepository<Integer, Rental> {
    Connection connection;
    private static int maxID;
    public DbRentalRepo() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sdiProject",
                "postgres",
                "teslaCoil");
        PreparedStatement getMax = this.connection.prepareStatement("SELECT Max(id) FROM rentals");
        ResultSet myResult = getMax.executeQuery();
        myResult.next();
        this.maxID =  myResult.getInt(1);
    }

    @Override
    public Optional<Rental> findOne(Integer integer) {
        try {
            PreparedStatement myState = this.connection.prepareStatement("SELECT * FROM rentals WHERE id = " + integer);
            ResultSet myResult = myState.executeQuery();
            myResult.next();
            return Optional.of(new Rental(myResult.getInt("movieid"), myResult.getInt("clientid"), myResult.getInt("id")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Rental> findAll() {
        ArrayList<Rental> rentals = new ArrayList<>();
        try {
            PreparedStatement myState = this.connection.prepareStatement("SELECT * FROM rentals");
            ResultSet myResult = myState.executeQuery();
            while(myResult.next())
            {
                rentals.add(new Rental(myResult.getInt("movieid"), myResult.getInt("clientid"), myResult.getInt("id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rentals;
    }

    @Override
    public Optional<Rental> save(Rental entity) {
        try {
            PreparedStatement myState = this.connection.prepareStatement("INSERT INTO rentals VALUES(" + ++this.maxID + "," + entity.getMovie() + "," + entity.getClient() + ")");
            myState.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Rental> delete(Integer integer) {
        try {
            PreparedStatement myState = this.connection.prepareStatement("DELETE FROM rentals WHERE id = " + integer);
            myState.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Rental> update(Rental entity) {
        try {
            PreparedStatement myState = this.connection.prepareStatement("UPDATE rentals SET movieid = " + entity.getMovie() + ", clientid = " + entity.getClient()  + "WHERE id = " + entity.getId());
            myState.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
