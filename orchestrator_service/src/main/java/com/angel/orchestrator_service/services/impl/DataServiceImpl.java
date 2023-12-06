package com.angel.orchestrator_service.services.impl;

import com.angel.orchestrator_service.entities.ReportRow;
import com.angel.orchestrator_service.pojo.ReportRequest;
import com.angel.orchestrator_service.pojo.ReportResponse;
import com.angel.orchestrator_service.repositories.ReportRepository;
import com.angel.orchestrator_service.services.api.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataServiceImpl implements DataService {

    private final ReportRepository repository;

    @Autowired
    public DataServiceImpl(ReportRepository repository){
        this.repository = repository;
    }

    @Override
    public ReportResponse getTotal(ReportRequest reportRequest){

        List<ReportRow> rows = this.repository.getReportRowsByName(reportRequest.getName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        List<ReportRow> floats = rows.stream().filter(
            x -> sdf.format(x.getDate()).compareTo(sdf.format(reportRequest.getStartDate())) >= 0
                 && sdf.format(x.getDate()).compareTo(sdf.format(reportRequest.getEndDate())) <= 0)
            .collect(Collectors.toList());

        BigDecimal total =  floats.stream().map(ReportRow::getHostTotalEarnings)
            .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal hddTotal =  floats.stream().map(ReportRow::getHostDiskEarnings)
            .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal gpuTotal =  floats.stream().map(ReportRow::getHostGpuEarnings)
            .reduce(BigDecimal.valueOf(0), BigDecimal::add);

        return ReportResponse.builder()
            .total(total)
            .hddTotal(hddTotal)
            .gpuTtotal(gpuTotal)
            .name(reportRequest.getName())
            .startDate(reportRequest.getStartDate())
            .endDate(reportRequest.getEndDate())
            .build();
    }

}
