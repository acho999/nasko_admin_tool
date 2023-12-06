package com.angel.orchestrator_service.pojo;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Credentials {

    private String username;
    private String password;

}
