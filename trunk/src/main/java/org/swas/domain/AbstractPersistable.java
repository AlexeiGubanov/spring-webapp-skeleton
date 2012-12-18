package org.swas.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Common implementation for {@code Persistable} interface.
 * Contains common attributes, equality and hashCode logic overridings (relative to ID attribute).
 *
 * @author Alexei.Gubanov@gmail.com
 *         Date: 09.11.11
 */
@MappedSuperclass
public abstract class AbstractPersistable<ID extends Serializable> implements Persistable<ID> {

    public abstract void setId(ID id);

    @Version
    private int version;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn = new Date();


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractPersistable that = (AbstractPersistable) o;
        return !(getId() != null ? !getId().equals(that.getId()) : that.getId() != null);
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }


    public boolean isNew() {
        return this.getId() == null;
    }

    @Transient
    private ID fantomId;

    /**
     * <p/>
     * If not null - this object is a 'fantom' with id equals fantomId.
     * 'Fantom' means that object is not saved (new).
     */
    public ID getFantomId() {
        return fantomId;
    }


    public void setFantomId(ID fantomId) {
        this.fantomId = fantomId;
    }

    /**
     * <p/>
     * Return true if object is fantom.
     */
    public boolean isFantom() {
        return fantomId != null;
    }

    /**
     * <p/>
     * Return to fantom state, if possible
     */
    public void rollbackToFantom() {
        if (this.fantomId != null && this.getId() == null)
            this.setId(fantomId);
    }

    protected void copyTo(AbstractPersistable<ID> target) {
        target.setId(this.getId());
        target.setCreatedOn(this.getCreatedOn());
        target.setFantomId(this.getFantomId());
        target.setLastUpdate(this.getLastUpdate());
        target.setVersion(this.getVersion());
    }
}
