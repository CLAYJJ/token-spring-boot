package com.ict.token.spring.boot.autoconfigure.conditions;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
/**
 * token配置条件：配置了token.enable = true和token.includes就满足条件，否则不满足
 * @author Dante
 */
public class TokenCondition extends SpringBootCondition {

    private final String TOKEN_ENABLE = "token.enable";
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        if (Boolean.TRUE.toString().equals(environment.getProperty(TOKEN_ENABLE)))
                return ConditionOutcome.match();
        return ConditionOutcome.noMatch(TOKEN_ENABLE+":未配置或者配置为false");

    }


}
