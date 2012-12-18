package org.swas.domain;

import java.io.Serializable;

/**
 * Basic interface for persistable classes
 * @author Alexei.Gubanov@gmail.com
 *         Date: 08.12.11
 */
public interface Persistable<ID extends Serializable> extends Serializable{
    ID getId();
}
