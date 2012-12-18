package org.swas.aspect;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;

import java.util.Arrays;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 08.12.11
 */
public class LoggableServiceMethodAdvice implements Ordered, MethodInterceptor {

    private int order = Integer.MAX_VALUE;

    private static final Logger log = LoggerFactory.getLogger(LoggableServiceMethodAdvice.class);

    public void setOrder(int order) {
        this.order = order;
    }

    public Object invoke(MethodInvocation invocation) throws Throwable {
        StringBuilder sb = new StringBuilder();
        sb.append("Calling ")
                .append(invocation.getMethod().getDeclaringClass())
                .append(".")
                .append(invocation.getMethod().getName())
                .append("(")
                .append(Arrays.toString(invocation.getArguments()))
                .append(") = ");
        Object r = invocation.proceed();
        sb.append(r);
        log.info(sb.toString());
        return r;
    }

    public int getOrder() {
        return order;
    }
}


