package com.angel.orchestrator_service.endpoints;

import com.angel.orchestrator_service.pojo.ReportRequest;
import com.angel.orchestrator_service.pojo.ReportResponse;
import com.angel.orchestrator_service.services.api.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/data")
public class DataController {

    private DataService dataService;

    @Autowired
    public DataController(DataService dataService){
        this.dataService = dataService;
    }

    @PostMapping(value = "/getTotal", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*",allowedHeaders = {"Authorization", "Cache-Control", "Content-Type","Access-Control-Allow-Origin"})
    public ResponseEntity<ReportResponse> getTotal(@RequestBody ReportRequest reportRequest) {
        ReportResponse response = this.dataService.getTotal(reportRequest);
        return ResponseEntity.ok(response);
    }
}
