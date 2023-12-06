package com.angel.orchestrator_service.repositories;

import com.angel.orchestrator_service.entities.ReportRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<ReportRow, String> {
   List<ReportRow> getReportRowsByName (String name);
}
