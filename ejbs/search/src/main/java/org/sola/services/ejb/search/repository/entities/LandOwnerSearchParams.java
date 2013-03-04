/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.search.repository.entities;

import java.util.Date;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

/**
 *
 * @author Kumar
 */
public class LandOwnerSearchParams extends AbstractReadOnlyEntity {

    private Date fromDate;
    private Date toDate;
    private Date currentFiscalYear;
    private Date upToDate;
    private Date upToYear;

    public LandOwnerSearchParams() {
        super();
    }

    public Date getCurrentFiscalYear() {
        return currentFiscalYear;
    }

    public void setCurrentFiscalYear(Date currentFiscalYear) {
        this.currentFiscalYear = currentFiscalYear;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getUpToDate() {
        return upToDate;
    }

    public void setUpToDate(Date upToDate) {
        this.upToDate = upToDate;
    }

    public Date getUpToYear() {
        return upToYear;
    }

    public void setUpToYear(Date upToYear) {
        this.upToYear = upToYear;
    }
}
