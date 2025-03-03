package com.fluffy.support.cleaner;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PostgresDatabaseCleaner implements DataCleaner {

    private static final Logger log = LoggerFactory.getLogger(PostgresDatabaseCleaner.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void clear() {
        em.clear();
        truncate();
    }

    private void truncate() {
        em.createNativeQuery("SET CONSTRAINTS ALL DEFERRED").executeUpdate();
        getTruncateQueries().forEach(query -> em.createNativeQuery(query).executeUpdate());
        em.createNativeQuery("SET CONSTRAINTS ALL IMMEDIATE").executeUpdate();

        log.info("[PostgresDatabaseCleaner] All tables are truncated.");
    }

    @SuppressWarnings("unchecked")
    private List<String> getTruncateQueries() {
        String sql = """
                SELECT 'TRUNCATE TABLE ' || tablename || ' RESTART IDENTITY CASCADE;'
                FROM pg_tables
                WHERE schemaname = 'public'
                """;

        return em.createNativeQuery(sql).getResultList();
    }
}