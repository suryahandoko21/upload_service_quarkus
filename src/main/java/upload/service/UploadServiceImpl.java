package upload.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import upload.dto.UploadRequest;
import upload.producer.UserProducer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import static java.nio.file.Files.newInputStream;

@ApplicationScoped
public class UploadServiceImpl implements UploadService {
    private static final Logger logger = LoggerFactory.getLogger(UploadServiceImpl.class);

    private final UserProducer userProducer;

    @Inject
    public UploadServiceImpl(
            UserProducer userProducer
    ) {
        this.userProducer = userProducer;
    }

    public void uploadFile(UploadRequest request) throws IOException, CsvException {
        var file = request.file;
        var extension = file.fileName().substring(file.fileName().lastIndexOf(".") + 1);
        if ("xls".equals(extension) || "xlsx".equals(extension)) {
            readExcelFile(file);
        } else if ("csv".equals(extension)) {
            readCsvFile(file);
        } else {
            logger.info("Unsupported file type");
        }
    }

    private void readExcelFile(FileUpload fileUpload) throws IOException {
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(newInputStream(fileUpload.filePath()));
            for(Sheet sheet : workbook) {
                for(Row row : sheet) {
                   this.sendCreateUser(row.getCell(1).getStringCellValue(), row.getCell(0).getStringCellValue());
                }
            }
        } catch (Exception e) {
           logger.info("Error reading excel file");
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }

    }

    private void sendCreateUser(String name, String email) {
        JsonObject data = new JsonObject();
        data.put("name", name);
        data.put("email", email);
        data.put("password", "123456");
        data.put("role", "USER");
        userProducer.sendUserCreated(data.toString());
    }

    private void readCsvFile(FileUpload fileUpload) throws IOException, CsvException {
        Reader reader = new InputStreamReader(newInputStream(fileUpload.filePath()));
        // Parse CSV data
        CSVReader csvReader = new CSVReaderBuilder(reader).build();
        List<String[]> rows = csvReader.readAll();
        for(String[] row : rows) {
            var data = row[0].split(";");
            this.sendCreateUser(data[1], data[0]);
        }
    }
}
