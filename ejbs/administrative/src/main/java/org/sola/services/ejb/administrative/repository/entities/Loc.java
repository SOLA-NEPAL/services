package org.sola.services.ejb.administrative.repository.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.entities.AbstractVersionedEntity;

/**
 *
 * @author KumarKhadka
 */
@Table(name = "loc", schema = "administrative")
public class Loc extends AbstractVersionedEntity {

    public static final String PANA_NO_PARAM = "panaNo";
    public static final String MOTH_ID_PARAM = "mothId";
    public static final String GET_BY_MOTH_ID_AND_PANA_NO = "pana_no=#{" + PANA_NO_PARAM + "} and moth_id=#{" + MOTH_ID_PARAM + "}";
    public static final String GET_BY_MOTH_ID = "moth_id=#{" + MOTH_ID_PARAM + "}";
    
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "moth_id")
    private String mothId;
    @Column(name = "pana_no")
    private int panaNo;
    @Column(name = "tmp_pana_no")
    private int tmpPanaNo;
<<<<<<< HEAD
=======
    @Column(name = "status_code", updatable = false)
    private String statusCode;   
    @ChildEntityList(parentIdField = "locId")
    private List<BaUnit> baUnits;
>>>>>>> dateTestbranch
    @Column(name = "office_code")
    private String officeCode;

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

    public String getMothId() {
        return mothId;
    }

    public void setMothId(String mothId) {
        this.mothId = mothId;
    }

    public int getPanaNo() {
        return panaNo;
    }

    public void setPanaNo(int panaNo) {
        this.panaNo = panaNo;
    }

    public int getTmpPanaNo() {
        return tmpPanaNo;
    }

    public void setTmpPanaNo(int tmpPanaNo) {
        this.tmpPanaNo = tmpPanaNo;
    }
<<<<<<< HEAD
=======

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
    
>>>>>>> dateTestbranch
}
