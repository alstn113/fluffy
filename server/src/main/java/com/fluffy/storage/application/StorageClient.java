package com.fluffy.storage.application;

import org.springframework.web.multipart.MultipartFile;

public interface StorageClient {

    String upload(MultipartFile file);

    void delete(String fileName);
}
