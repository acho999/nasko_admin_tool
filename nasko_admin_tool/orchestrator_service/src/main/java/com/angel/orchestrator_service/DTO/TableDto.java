package com.angel.orchestrator_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TableDto implements Serializable {


    private String userId;
    private String tableName;//some keyword or user id
    private String customerKey;
    private Map<String, String> columnNamesAndDataTypes;

}
