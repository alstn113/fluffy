package com.fluffy.support.cleaner

import org.springframework.stereotype.Component
import org.springframework.test.context.ActiveProfiles

@Component
@ActiveProfiles("test")
class CompositeDataCleaner(
    private val cleaners: List<DataCleaner>
) : DataCleaner {

    override fun clear() {
        cleaners.forEach { it.clear() }
    }
}
