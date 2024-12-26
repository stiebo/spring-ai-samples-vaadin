package dev.stiebo.app.dtos;

import org.springframework.core.io.InputStreamResource;

import java.io.InputStream;

public class UploadFileInputStreamResource extends InputStreamResource {
    private final String filename;

    public UploadFileInputStreamResource(InputStream inputStream, String filename) {
        super(inputStream);
        this.filename = filename;
    }

    @Override
    public String getFilename() {
        return this.filename;
    }
}
