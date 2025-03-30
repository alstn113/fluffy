package com.fluffy.support.cleaner

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class PostgresDatabaseCleaner(
    @PersistenceContext private val em: EntityManager,
) : DataCleaner {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(PostgresDatabaseCleaner::class.java)
    }

    @Transactional
    override fun clear() {
        em.clear()
        truncate()
    }

    private fun truncate() {
        em.createNativeQuery("SET CONSTRAINTS ALL DEFERRED").executeUpdate()
        getTruncateQueries().forEach { query -> em.createNativeQuery(query).executeUpdate() }
        em.createNativeQuery("SET CONSTRAINTS ALL IMMEDIATE").executeUpdate()

        log.info("[PostgresDatabaseCleaner] All tables are truncated.")
    }

    private fun getTruncateQueries(): List<String> {
        val sql = """
                SELECT 'TRUNCATE TABLE ' || tablename || ' RESTART IDENTITY CASCADE;'
                FROM pg_tables
                WHERE schemaname = 'public'
                """.trimIndent()

        return em.createNativeQuery(sql).resultList.mapNotNull { it as? String }
    }
}