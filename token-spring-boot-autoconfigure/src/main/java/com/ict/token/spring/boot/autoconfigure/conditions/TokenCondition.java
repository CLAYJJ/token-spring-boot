package com.ict.token.spring.boot.autoconfigure.conditions;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
/*
 * token配置条件：配置了token.includes就满足条件，否则不满足
 * @author Dante
 */
public class TokenCondition extends SpringBootCondition {
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        if (environment.containsProperty("token.includes[0]"))
            return ConditionOutcome.match();
        return ConditionOutcome.noMatch("token.includes未配置");
    }
}
