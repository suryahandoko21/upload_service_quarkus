package upload.controller;

import com.opencsv.exceptions.CsvException;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.MultipartForm;
import upload.dto.UploadRequest;
import upload.service.UploadService;

import java.io.IOException;

@Path("/v1/upload")
public class UploadController {

    private final UploadService uploadService;

    @Inject
    public UploadController(UploadService uploadService){
        this.uploadService = uploadService;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@MultipartForm UploadRequest request) throws IOException, CsvException {
        uploadService.uploadFile(request);
        return Response.ok().build();
    }
}
