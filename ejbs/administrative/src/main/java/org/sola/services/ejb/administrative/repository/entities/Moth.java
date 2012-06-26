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
import org.sola.services.common.repository.ChildEntity;
import org.sola.services.common.repository.ChildEntityList;
import org.sola.services.common.repository.ExternalEJB;
import org.sola.services.common.repository.entities.AbstractVersionedEntity;
import org.sola.services.ejbs.admin.businesslogic.AdminEJBLocal;
import org.sola.services.ejbs.admin.businesslogic.repository.entities.Vdc;

/**
 *
 * @author KumarKhadka
 */
@Table(name = "moth", schema = "administrative")
public class Moth extends AbstractVersionedEntity {

    public static final String VDC_PARAM = "vdcCode";
    public static final String MOTH_LUJ_PARAM = "mothLuj";
    public static final String MOTH_LUJ_NUMBER_PARAM = "mothlujNumber";
    public static final String GET_BY_VDC_AND_MOTHLUJ = "vdc_code=#{" + VDC_PARAM + "} and moth_luj=#{" + MOTH_LUJ_PARAM + "}";
    public static final String GET_BY_VDC_MOTHLUJ_AND_MOTHLUJ_NUMBER = "vdc_code=#{" + VDC_PARAM + "} and moth_luj=#{" + MOTH_LUJ_PARAM + "} and mothluj_no=#{" + MOTH_LUJ_NUMBER_PARAM + "}";
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "mothluj_no")
    private String mothlujNumber;
    @Column(name = "vdc_code")
    private String vdcCode;
    @Column(name = "moth_luj")
    private String mothLuj;
    @Column(name = "financial_year")
    private String financialYear;
    @Column(name = "office_code")
    private String officeCode;
    @Column(name = "transaction_id")
    private String transactionId;
    @ExternalEJB(ejbLocalClass = AdminEJBLocal.class, loadMethod = "getVdcByCode")
    @ChildEntity(childIdField = "vdcCode")
    private Vdc vdc;
    // @ExternalEJB(ejbLocalClass = AdministrativeEJBLocal.class, loadMethod = "getLOC")
    @ChildEntityList(parentIdField = "mothId")
    private List<Loc> locList;

    public List<Loc> getLocList() {
        locList = locList == null ? new ArrayList<Loc>() : locList;
        return locList;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

    public String getFinancialYear() {
        return financialYear;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public void setLocList(List<Loc> locList) {
        this.locList = locList;
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

    public Vdc getVdc() {
        return vdc;
    }

    public void setVdc(Vdc vdc) {
        this.vdc = vdc;
        if (vdc != null) {
            this.setVdcCode(vdc.getCode());
        }
    }

    public String getVdcCode() {
        return vdcCode;
    }

    public void setVdcCode(String vdcCode) {
        this.vdcCode = vdcCode;
    }
}
