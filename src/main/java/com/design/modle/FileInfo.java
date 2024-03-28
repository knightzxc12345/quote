package com.design.modle;

import lombok.Data;

import java.io.InputStream;

@Data
public class FileInfo {

    public InputStream inputStream;

    public long contentLength;

    public String contentType;

    public FileInfo(InputStream inputStream, long contentLength, String contentType) {
        this.inputStream = inputStream;
        this.contentLength = contentLength;
        this.contentType = contentType;
    }

}