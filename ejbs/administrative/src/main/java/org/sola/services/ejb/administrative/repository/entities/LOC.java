/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.administrative.repository.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.ChildEntity;
import org.sola.services.common.repository.entities.AbstractVersionedEntity;

/**
 *
 * @author KumarKhadka
 */
@Table(name = "land_owner_certificate", schema = "Administrative")
public class LOC extends AbstractVersionedEntity {

    @Id
    @Column(name = "loc_sid")
    private String locSid;
    @Column(name = "moth_sid")
    private String mothSid;
    @Column(name = "pana_no")
    private int panaNo;
    @Column(name = "tmp_pana_no")
    private int tmpPanaNo;
    @Column(name = "property_type")
    private int propertyType;
    @Column(name = "oshp_type")
    private int oshpType;
    @Column(name = "transaction_no")
    private int transactionNo;
    @ChildEntity(childIdField = "locSid")
    private BaUnit baUnit;

    public BaUnit getBaUnit() {
        return baUnit;
    }

    public void setBaUnit(BaUnit baUnit) {
        this.baUnit = baUnit;
    }

    public String getLocSid() {
        locSid = locSid == null ? generateId() : locSid;
        return locSid;
    }

    public void setLocSid(String locSid) {
        this.locSid = locSid;
    }

    public String getMothSid() {
        return mothSid;
    }

    public void setMothSid(String mothSid) {
        this.mothSid = mothSid;
    }

    public int getOshpType() {
        return oshpType;
    }

    public void setOshpType(int oshpType) {
        this.oshpType = oshpType;
    }

    public int getPanaNo() {
        return panaNo;
    }

    public void setPanaNo(int panaNo) {
        this.panaNo = panaNo;
    }

    public int getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(int propertyType) {
        this.propertyType = propertyType;
    }

    public int getTmpPanaNo() {
        return tmpPanaNo;
    }

    public void setTmpPanaNo(int tmpPanaNo) {
        this.tmpPanaNo = tmpPanaNo;
    }

    public int getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(int transactionNo) {
        this.transactionNo = transactionNo;
    }
}
