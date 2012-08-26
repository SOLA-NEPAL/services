package org.sola.services.ejbs.admin.businesslogic.repository.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Table;
import org.sola.services.common.repository.entities.AbstractCodeEntity;

@Table(name="financial_year", schema="system")
public class FiscalYear extends AbstractCodeEntity {
    public static final String WHERE_GET_BY_CURRENT = "current='t'";
    
    @Column
    private boolean current;
    @Column(name="start_date")
    private Date startDate;
    @Column(name="end_date")
    private Date endDate;
    
    public FiscalYear(){
        super();
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
