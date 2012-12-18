package org.swas.util;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 10.11.11
 */
public class SpringBootStrapInvocator implements InitializingBean {


    private Runnable[] invocations;

    public Runnable[] getInvocations() {
        return invocations;
    }

    public void setInvocations(Runnable[] invocations) {
        this.invocations = invocations;
    }

    public void afterPropertiesSet() throws Exception {
        for (Runnable bi : invocations) {
            new Thread(bi).start();
        }
    }
}
