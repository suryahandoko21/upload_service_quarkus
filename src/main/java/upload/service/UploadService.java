package upload.service;

import com.opencsv.exceptions.CsvException;
import upload.dto.UploadRequest;

import java.io.IOException;

public interface UploadService {
    void uploadFile(UploadRequest request) throws IOException, CsvException;
}
