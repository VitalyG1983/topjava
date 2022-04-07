package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private static final Comparator<User> USER_NAME_COMPARATOR = Comparator.comparing(User::getName).thenComparing(User::getEmail);
    private final Map<Integer, User> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public boolean delete(int id) {
        log.info("User deleted by id={}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
            log.info("User saved with id={}", user.getId());
            return user;
        }
        User u = repository.computeIfPresent(user.getId(), (id, oldUser) -> user);
        if (u != null) {
            log.info("User updated with id={}", user.getId());
        } else {
            log.info("User not updated with id={}", user.getId());
        }
        return u;
    }

    @Override
    public User get(int id) {
        log.info("User gotten by id={}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("Users getAll()");
        List<User> users = new ArrayList<>(repository.values());
        users.sort(USER_NAME_COMPARATOR);
        return users;
    }

    @Override
    public User getByEmail(String email) {
        User u = email != null
                ? repository.values().stream().filter(user ->
                equalsIgnoreCase(user.getEmail(), email)).findFirst().orElse(null)
                : null;
        if (u != null) {
            log.info("User getByEmail with id={}, email={} ", u.getId(), email);
        } else {
            log.info("User not getByEmail with email={}", email);
        }
        return u;
    }
}