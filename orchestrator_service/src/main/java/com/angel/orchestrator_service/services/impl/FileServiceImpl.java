package com.angel.orchestrator_service.services.impl;

import com.angel.orchestrator_service.entities.ReportRow;
import com.angel.orchestrator_service.repositories.ReportRepository;
import com.angel.orchestrator_service.services.api.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    private final ReportRepository repository;
    private final Environment environment;

    @Autowired
    public FileServiceImpl(ReportRepository repository, Environment environment){
        this.repository = repository;
        this.environment = environment;
    }

    @Override
    public void saveDataFromFile(MultipartFile file) throws IOException {
        AtomicInteger counter = new AtomicInteger(0);
        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        String[] array = content.replaceAll("\"", "").split("\\r\\n");
        array[0] = "a,b,c,d,e";
        Map<Integer, String[]> map = Arrays.stream(array)
            .collect(Collectors.toMap(x -> counter.get(),
                                      x -> array[counter.getAndIncrement()].split(",")));
        map.remove(0);
        System.out.println(map);
        List<ReportRow> reportRows = map.values()
            .stream()
            .map(x -> ReportRow
                .builder()
                .name(x[0])
                .date(Date.valueOf(x[1].substring(0, 10)))
                .hostGpuEarnings(BigDecimal.valueOf(Double.parseDouble(x[2])))
                .hostDiskEarnings(BigDecimal.valueOf(Double.parseDouble(x[3])))
                .hostTotalEarnings(BigDecimal.valueOf(Double.parseDouble(x[4])))
                .build())
            .collect(Collectors.toList());
        this.saveFile(file);
        this.repository.saveAllAndFlush(reportRows);
    }

    @Override
    public void saveFile(MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                String uploadsDir = this.environment.getProperty("api.path");
                if (!new File(uploadsDir).exists()) {
                    new File(uploadsDir).mkdir();
                }
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDateTime now = LocalDateTime.now();
                System.out.println(dtf.format(now));
                String orgName = dtf.format(now) + "_" + file.getOriginalFilename();
                String filePath = uploadsDir + orgName;
                File dest = new File(filePath);
                file.transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }
}
