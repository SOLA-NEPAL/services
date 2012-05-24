/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.administrative.repository.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.entities.AbstractVersionedEntity;

/**
 *
 * @author KumarKhadka
 */
@Table(name = "moth", schema = "system")
public class Moth extends AbstractVersionedEntity {

    public static final String VDC_PARAM = "vdcSid";
    public static final String MOTH_LUJ_PARAM = "mothLuj";
    public static final String GET_BY_VDC_AND_MOTHLUJ = "vdc_sid=#{" + VDC_PARAM + "} and moth_luj=#{" + MOTH_LUJ_PARAM + "}";
    @Id
    @Column(name = "moth_sid")
    private String mothSid;
    @Column(name = "mothluj_no")
    private String mothlujNumber;
    @Column(name = "vdc_sid")
    private String vdcSid;
    @Column(name = "ward_no")
    private int wardNo;
    @Column(name = "moth_luj")
    private String mothLuj;
    @Column(name = "financialyear")
    private int financialYear;
    @Column(name = "lmocd")
    private int lmocd;

    public int getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(int financialYear) {
        this.financialYear = financialYear;
    }

    public int getLmocd() {
        return lmocd;
    }

    public void setLmocd(int lmocd) {
        this.lmocd = lmocd;
    }

    public String getMothLuj() {
        return mothLuj;
    }

    public void setMothLuj(String mothLuj) {
        this.mothLuj = mothLuj;
    }

    public String getMothSid() {
        mothSid = mothSid == null ? generateId() : mothSid;
        return mothSid;
    }

    public void setMothSid(String mothSid) {
        this.mothSid = mothSid;
    }

    public String getMothlujNumber() {
        return mothlujNumber;
    }

    public void setMothlujNumber(String mothlujNumber) {
        this.mothlujNumber = mothlujNumber;
    }

    public String getVdcSid() {
        return vdcSid;
    }

    public void setVdcSid(String vdcSid) {
        this.vdcSid = vdcSid;
    }

    public int getWardNo() {
        return wardNo;
    }

    public void setWardNo(int wardNo) {
        this.wardNo = wardNo;
    }
}
