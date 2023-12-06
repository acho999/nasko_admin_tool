package com.angel.orchestrator_service.services.api;

import com.angel.orchestrator_service.pojo.ReportRequest;
import com.angel.orchestrator_service.pojo.ReportResponse;

public interface DataService {

    ReportResponse getTotal(ReportRequest reportRequest);

}
