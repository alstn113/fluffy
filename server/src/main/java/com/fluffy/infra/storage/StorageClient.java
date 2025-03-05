package com.fluffy.infra.storage;

public interface StorageClient {

    String getPresignedUrl(String fileName);

    void delete(String fileName);
}
