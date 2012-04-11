/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejbs.admin.businesslogic.repository.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.DefaultSorter;
import org.sola.services.common.repository.entities.AbstractEntity;

/**
 *
 * @author KumarKhadka
 */
@Table(name = "np_calendar", schema = "nep_system")
@DefaultSorter(sortString = "nep_year,nep_month")
public class NepaliMonth extends AbstractEntity{
    @Id
    @Column(name = "nep_year")
    private int nepYear;
    @Id
    @Column(name = "nep_month")
    private int nepMonth;
    @Column(name = "dayss")
    private int days;
    
    public static final String YEAR_PARAM="nepYear";
    public static final String MONTH_PARAM="nepMonth";
    public static final String GET_BY_YEAR="nep_year=#{"+YEAR_PARAM+"}";
    public static final String GET_BY_YEAR_AND_MONTH="nep_year=#{"+YEAR_PARAM+"} and nep_month=#{" + MONTH_PARAM + "}";

    public int getDays() {
        return days;
    }

    public void setDays(int daays) {
        this.days = daays;
    }

    public int getNepMonth() {
        return nepMonth;
    }

    public void setNepMonth(int nepMonth) {
        this.nepMonth = nepMonth;
    }

    public int getNepYear() {
        return nepYear;
    }

    public void setNepYear(int nepYear) {
        this.nepYear = nepYear;
    }
}
