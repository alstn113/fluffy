package com.fluffy.support.cleaner;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class DataClearExtension implements AfterEachCallback {

    @Override
    public void afterEach(ExtensionContext context) {
        CompositeDataCleaner dataCleaner = getDataCleaner(context);
        dataCleaner.clear();
    }

    private CompositeDataCleaner getDataCleaner(ExtensionContext context) {
        return SpringExtension.getApplicationContext(context)
                .getBean(CompositeDataCleaner.class);
    }
}
