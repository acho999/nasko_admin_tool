package com.angel.orchestrator_service.endpoints;

import com.angel.orchestrator_service.pojo.ReportRequest;
import com.angel.orchestrator_service.pojo.ReportResponse;
import com.angel.orchestrator_service.services.api.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
public class DataController {

    private DataService dataService;

    @Autowired
    public DataController(DataService dataService){
        this.dataService = dataService;
    }

    @GetMapping(value = "/getTotal")
    public ResponseEntity<ReportResponse> getTotal(@RequestBody ReportRequest reportRequest) {
        ReportResponse response = this.dataService.getTotal(reportRequest);
        return ResponseEntity.ok(response);
    }
}
