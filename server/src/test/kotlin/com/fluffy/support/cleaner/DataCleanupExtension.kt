package com.fluffy.support.cleaner

import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.test.context.junit.jupiter.SpringExtension

class DataCleanupExtension : AfterEachCallback {

    override fun afterEach(context: ExtensionContext) {
        val dataCleaner = getDataCleaner(context)
        dataCleaner.clear()
    }

    private fun getDataCleaner(context: ExtensionContext): CompositeDataCleaner {
        return SpringExtension.getApplicationContext(context)
            .getBean(CompositeDataCleaner::class.java)
    }
}
