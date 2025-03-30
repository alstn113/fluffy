package com.fluffy.global.storage

import com.fluffy.global.storage.response.PresignedUrlResponse

interface StorageClient {

    fun getPresignedUrl(filePath: String): PresignedUrlResponse

    fun delete(filePath: String)
}