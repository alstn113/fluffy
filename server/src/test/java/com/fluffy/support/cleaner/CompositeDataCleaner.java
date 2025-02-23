package com.fluffy.support.cleaner;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@Component
@ActiveProfiles("test")
public class CompositeDataCleaner implements DataCleaner {

    private final List<DataCleaner> cleaners;

    public CompositeDataCleaner(List<DataCleaner> cleaners) {
        this.cleaners = cleaners;
    }

    @Override
    public void clear() {
        cleaners.forEach(DataCleaner::clear);
    }
}
