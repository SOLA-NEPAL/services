/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.administrative.repository.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.ChildEntityList;
import org.sola.services.common.repository.entities.AbstractVersionedEntity;

/**
 *
 * @author KumarKhadka
 */
@Table(name = "land_owner_certificate", schema = "administrative")
public class Loc extends AbstractVersionedEntity {

    public static final String PANA_NO_PARAM = "panaNo";
    public static final String MOTH_ID_PARAM = "mothId";
    public static final String GET_BY_MOTH_ID_AND_PANA_NO = "pana_no=#{" + PANA_NO_PARAM + "} and moth_id=#{" + MOTH_ID_PARAM + "}";
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "moth_id")
    private String mothId;
    @Column(name = "pana_no")
    private int panaNo;
    @Column(name = "tmp_pana_no")
    private int tmpPanaNo;
    @Column(name = "property_type")
    private int propertyType;
    @Column(name = "oshp_type")
    private int oshpType;
    @Column(name = "transaction_id")
    private String transactionId;
    @ChildEntityList(parentIdField = "locId")
    private List<BaUnit> baUnits;
    @Column(name = "office_code")
    private String officeCode;

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

   
    public List<BaUnit> getBaUnits() {
        baUnits = baUnits == null ? new ArrayList<BaUnit>() : baUnits;
        return baUnits;
    }

    public void setBaUnits(List<BaUnit> baUnits) {
        this.baUnits = baUnits;
    }

    public String getId() {
        id = id == null ? generateId() : id;
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMothId() {
        return mothId;
    }

    public void setMothId(String mothId) {
        this.mothId = mothId;
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
