package com.angel.orchestrator_service.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequest {
    private String name;
    private Date startDate;
    private Date endDate;
}
