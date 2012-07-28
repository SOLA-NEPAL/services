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
    public static final String LOC_ID_PARAM = "locId";
    public static final String TMP_PANA_NO_PARAM = "tmpPanaNo";
    public static final String MOTH_ID_PARAM = "mothId";
    public static final String GET_BY_MOTH_ID_AND_PANA_NO = "(pana_no=#{"
            + PANA_NO_PARAM + "} OR tmp_pana_no=#{" + TMP_PANA_NO_PARAM
            + "}) and moth_id=#{" + MOTH_ID_PARAM
            + "} and office_code=#{" + PARAM_OFFICE_CODE + "}";
    public static final String GET_BY_MOTH_ID = "moth_id=#{" + MOTH_ID_PARAM + "}"
            + " and office_code=#{" + PARAM_OFFICE_CODE + "}";
    public static final String GET_BY_ID = "id=#{" + LOC_ID_PARAM + "}"
            + " and office_code=#{" + PARAM_OFFICE_CODE + "}";
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "moth_id")
    private String mothId;
    @Column(name = "pana_no")
    private String panaNo;
    @Column(name = "tmp_pana_no")
    private String tmpPanaNo;
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

    public String getPanaNo() {
        return panaNo;
    }

    public void setPanaNo(String panaNo) {
        this.panaNo = panaNo;
    }

    public String getTmpPanaNo() {
        return tmpPanaNo;
    }

    public void setTmpPanaNo(String tmpPanaNo) {
        this.tmpPanaNo = tmpPanaNo;
    }
}