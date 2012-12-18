package org.swas.aspect;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.swas.service.Result;

/**
 * ���������� AroundAdvice � ������ ������� (JDK proxy),
 * �.�. ����������� ������� ���������� ������������� ������ � ������������, ������� � ���� �������
 * ����� ��������� �� JDK proxy.
 * ������ Advice ������ ����������� ����� ������ TransactionInterceptor,
 * � ������ ���� ����� ������� ����������, �� ������ Advice ������������� ��� � ���������� new Result(exception).
 * � ������� ������ ���� �������, ��� ������ Advice ������ ��� ������� ������������ Result.
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
