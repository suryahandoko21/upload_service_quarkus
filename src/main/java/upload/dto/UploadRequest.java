package upload.dto;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

public class UploadRequest {
    @RestForm
    public FileUpload file;
}
