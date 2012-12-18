package org.swas.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Common persistable class with Long ID.
 * @author Alexei.Gubanov@gmail.com
 *         Date: 09.11.11
 */
@MappedSuperclass
public abstract class BasicPersistent extends AbstractPersistable<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
