package com.fluffy.global.storage;

import com.fluffy.global.storage.response.PresignedUrlResponse;

public interface StorageClient {

    PresignedUrlResponse getPresignedUrl(String filePath);

    void delete(String filePath);
}
