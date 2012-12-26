package org.sola.services.ejb.administrative.repository.entities;

import java.util.Date;
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
    public static final String MOTH_TYPE_PARAM = "mothType";
    public static final String MOTH_ID_PARAM = "mothId";
    public static final String GET_BY_MOTH_ID_AND_PANA_NO = 
            "((pana_no=#{" + PANA_NO_PARAM + "} AND 'M' = #{" + MOTH_TYPE_PARAM + "}) OR "
            + "(tmp_pana_no=#{" + PANA_NO_PARAM + "}) AND 'L' = #{" + MOTH_TYPE_PARAM + "}) "
            + "and moth_id=#{" + MOTH_ID_PARAM + "} and office_code=#{" + PARAM_OFFICE_CODE + "}";
    public static final String GET_BY_MOTH_ID = "moth_id=#{" + MOTH_ID_PARAM + "}"
            + " and office_code=#{" + PARAM_OFFICE_CODE + "}";
    public static final String GET_BY_ID = "id=#{" + LOC_ID_PARAM + "}"
            + " and office_code=#{" + PARAM_OFFICE_CODE + "}";
    public static final String MothPage_Search_Query =
            "(#{" + PANA_NO_PARAM + "} = '' OR POSITION(LOWER(#{" + PANA_NO_PARAM + "}) IN LOWER(COALESCE(pana_no, ''))) > 0) "
            + "AND (#{" + MOTH_ID_PARAM + "} = '' OR POSITION(LOWER(#{" + MOTH_ID_PARAM + "}) IN LOWER(COALESCE(moth_id, ''))) > 0) "
            + "AND (COALESCE(office_code, '') = #{" + PARAM_OFFICE_CODE + "} OR #{" + PARAM_OFFICE_CODE + "}='') ";
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "moth_id")
    private String mothId;
    @Column(name = "pana_no")
    private String panaNo;
    @Column(name = "tmp_pana_no")
    private String tmpPanaNo;
    @Column(name = "office_code", updatable = false)
    private String officeCode;
    @Column(name = "creation_date", insertable = false, updatable = false)
    private Date creationDate;

    public Loc() {
        super();
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}