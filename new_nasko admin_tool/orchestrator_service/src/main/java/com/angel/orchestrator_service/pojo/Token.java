package com.angel.orchestrator_service.pojo;

import com.angel.orchestrator_service.ConstantsAndEnums.TokenType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    public String token;
    public TokenType tokenType = TokenType.Bearer;
    public String tokenName;
    public boolean expired;
}
