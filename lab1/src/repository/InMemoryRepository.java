
import sun.security.validator.ValidatorException;

import javax.xml.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Petean Mihai on 3/6/2017.
 */

    public class InMemoryRepository<ID, T extends BaseEntity<ID>> implements TRepository<ID, T> {

        private Map<ID, T> entities;

        public InMemoryRepository() {
            entities = new HashMap<>();
        }

        @Override
        public Optional<T> findOne(ID id) {
            if (id == null) {
                throw new IllegalArgumentException("id must not be null");
            }
            return Optional.ofNullable(entities.get(id));
        }

        @Override
        public Iterable<T> findAll() {
            Set<T> allEntities = entities.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toSet());
            return allEntities;
        }

        @Override
        public Optional<T> save(T entity){
            if (entity == null) {
                throw new IllegalArgumentException("id must not be null");
            }
            return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
        }

        @Override
        public Optional<T> delete(ID id) {
            if (id == null) {
                throw new IllegalArgumentException("id must not be null");
            }
            return Optional.ofNullable(entities.remove(id));
        }

        @Override
        public Optional<T> update(T entity) {
            if (entity == null) {
                throw new IllegalArgumentException("entity must not be null");
            }
            return Optional.ofNullable(entities.computeIfPresent(entity.getId(), (k, v) -> entity));
        }
    }
