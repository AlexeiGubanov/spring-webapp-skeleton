package org.swas.aspect;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.swas.service.Result;

/**
 * Реализация AroundAdvice в старом формате (JDK proxy),
 * т.к. большинство методов используют декларативную работу с транзакциями, которая в свою очередь
 * также построена на JDK proxy.
 * Данный Advice должен срабатывать ПОСЛЕ работы TransactionInterceptor,
 * в случае если метод выкинул исключение, то данный Advice перехватывает его и возвращает new Result(exception).
 * В конфиге должно быть описано, что данный Advice только для методов возвращающих Result.
 * <bean id="serviceAspect" class="com.yowo.aspect.UnthrowableResultMethodAdvice" />
 * <bean id="advisor" class="org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor">
 * <property name="advice" ref="serviceAspect" />
 * <property name="expression" value="execution(com.yowo.service.Result com.yowo.service.*.*(..))" />
 * </bean>
 * <bean class="org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator" />
 *
 * @author Alexei.Gubanov@gmail.com
 *         Date: 08.12.11
 */
public class UnthrowableResultMethodAdvice implements Ordered, MethodInterceptor {

    private int order = Integer.MAX_VALUE;

    private static final Logger log = LoggerFactory.getLogger(UnthrowableResultMethodAdvice.class);

    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        } catch (Throwable t) {
            StringBuilder sb = new StringBuilder();
            sb.append("Error during execution ")
                    .append(invocation.getMethod().getDeclaringClass())
                    .append(".")
                    .append(invocation.getMethod().getName())
                    .append(": ");
            log.error(sb.toString(), t);
            return new Result(t);
        }
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }
}
