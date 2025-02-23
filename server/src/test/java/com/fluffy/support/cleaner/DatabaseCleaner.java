package com.fluffy.support.cleaner;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Component
@ActiveProfiles("test")
public class DatabaseCleaner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseCleaner.class);
    @Autowired
    private RedisTemplate<?, ?> redisTemplate;

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void clear() {
        em.clear();
        truncate();
        clearCache();
    }

    private void truncate() {
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY = 0").executeUpdate();
        getTruncateQueries().forEach(query -> em.createNativeQuery(query).executeUpdate());
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY = 1").executeUpdate();

        log.info("[DatabaseCleaner] All tables are truncated.");
    }

    @SuppressWarnings("unchecked")
    private List<String> getTruncateQueries() {
        String sql = """
                SELECT Concat('TRUNCATE TABLE ', TABLE_NAME, ' RESTART IDENTITY', ';') AS q
                FROM INFORMATION_SCHEMA.TABLES
                WHERE TABLE_SCHEMA = 'PUBLIC'
                """;

        return em.createNativeQuery(sql).getResultList();
    }

    private void clearCache() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory())
                .getConnection()
                .serverCommands()
                .flushDb();

        log.info("[DatabaseCleaner] Redis cache is cleared.");
    }
}
