package com.yet.spring.core.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class StatisticsAspect {
    private Map<Class<?>, Integer> counter = new HashMap<>();

    @Pointcut("execution(* *.logEvent(..))")
    private void allLogEventMethods() {

    }

    @AfterReturning("allLogEventMethods()")
    public void count(JoinPoint joinPoint) {
        Class<?> clazz = joinPoint.getTarget().getClass();
        counter.merge(clazz, 1, (oldV, newV) -> ++oldV);
    }

    public Map<Class<?>, Integer> getCounter() {
        return Collections.unmodifiableMap(counter);
    }
}
