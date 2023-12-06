package com.angel.orchestrator_service.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {
    private String name;
    private Date startDate;
    private Date endDate;
    private BigDecimal total;
    private BigDecimal gpuTtotal;
    private BigDecimal hddTotal;
}
