import java.util.Optional;

/**
 * Created by Petean Mihai on 3/14/2017.
 */
public interface TRepository<ID, T extends BaseEntity<ID>> {
        /**
         * Find the entity with the given {@code id}.
         *
         * @param id
         *            must be not null.
         * @return an {@code Optional} encapsulating the entity with the given id.
         * @throws IllegalArgumentException
         *             if the given id is null.
         */
        Optional<T> findOne(ID id);

        /**
         *
         * @return all entities.
         */
        Iterable<T> findAll();

        /**
         * Saves the given entity.
         *
         * @param entity
         *            must not be null.
         * @return an {@code Optional} - null if the entity was saved otherwise (e.g. id already exists) returns the entity.
         * @throws IllegalArgumentException
         *             if the given entity is null.
        */
        Optional<T> save(T entity);

        /**
         * Removes the entity with the given id.
         *
         * @param id
         *            must not be null.
         * @return an {@code Optional} - null if there is no entity with the given id, otherwise the removed entity.
         * @throws IllegalArgumentException
         *             if the given id is null.
         */
        Optional<T> delete(ID id);

        /**
         * Updates the given entity.
         *
         * @param entity
         *            must not be null.
         * @return an {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist) returns the
         *         entity.
         * @throws IllegalArgumentException
         *             if the given entity is null.
         */
        Optional<T> update(T entity);
    }
