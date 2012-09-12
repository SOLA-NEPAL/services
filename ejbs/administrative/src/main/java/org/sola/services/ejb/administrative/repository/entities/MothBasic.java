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
@Table(name = "moth", schema = "administrative")
public class MothBasic extends AbstractVersionedEntity {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "mothluj_no")
    private String mothlujNumber;
    @Column(name = "vdc_code")
    private String vdcCode;
    @Column(name = "moth_luj")
    private String mothLuj;
    @Column(name = "office_code")
    private String officeCode;
    @Column(name="fy_code", updatable=false)
    private String fiscalYearCode;
    @Column(name="ward_no")
    private String wardNumber;
    
    public MothBasic(){
        super();
    }

    public String getFiscalYearCode() {
        return fiscalYearCode;
    }

    public void setFiscalYearCode(String fiscalYearCode) {
        this.fiscalYearCode = fiscalYearCode;
    }

    public String getWardNumber() {
        return wardNumber;
    }

    public void setWardNumber(String wardNumber) {
        this.wardNumber = wardNumber;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public String getId() {
        id = id == null ? generateId() : id;
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMothLuj() {
        return mothLuj;
    }

    public void setMothLuj(String mothLuj) {
        this.mothLuj = mothLuj;
    }

    public String getMothlujNumber() {
        return mothlujNumber;
    }

    public void setMothlujNumber(String mothlujNumber) {
        this.mothlujNumber = mothlujNumber;
    }

    public String getVdcCode() {
        return vdcCode;
    }

    public void setVdcCode(String vdcCode) {
        this.vdcCode = vdcCode;
    }
}
