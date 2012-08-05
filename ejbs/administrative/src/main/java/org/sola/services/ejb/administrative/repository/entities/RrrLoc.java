package org.sola.services.ejb.administrative.repository.entities;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Id;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;
import org.sola.services.ejb.party.repository.entities.Party;
import org.sola.services.ejb.source.repository.entities.Source;

public class RrrLoc extends AbstractReadOnlyEntity {

    public static final String PARAM_LOC_ID = "locId";
    public static final String PARAM_STATUS = "status";
    public static final String SELECT_GET_SOURCE_IDS = "select id from administrative.get_loc_source_ids(#{"
            + PARAM_LOC_ID +"}, #{"+ PARAM_STATUS +"})";
    public static final String SELECT_GET_PARTY_IDS = "select id from administrative.get_loc_party_ids(#{"
            + PARAM_LOC_ID +"}, #{"+ PARAM_STATUS +"})";
    public static final String SELECT_RRR_LOCS = "select loc_id, type_code, registration_date, status_code, " 
            + "administrative.get_rrrloc_notation(loc_id, status_code) as notation_text from administrative.get_loc_rrrs(#{"
            + PARAM_LOC_ID +"}, #{"+ PARAM_OFFICE_CODE +"})";
    
    @Id
    @Column(name = "loc_id")
    private String locId;
    @Column(name = "type_code")
    private String typeCode;
    @Column(name = "registration_date")
    private Date registrationDate;
    @Column(name = "notation_text")
    private String notationText;
    @Column(name = "status_code")
    private String statusCode;
    
    
    private List<Source> sourceList;
    private List<Party> rightHolderList;

    public RrrLoc() {
        super();
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getNotationText() {
        return notationText;
    }

    public void setNotationText(String notationText) {
        this.notationText = notationText;
    }

    public List<Party> getRightHolderList() {
        return rightHolderList;
    }

    public void setRightHolderList(List<Party> rightHolderList) {
        this.rightHolderList = rightHolderList;
    }

    public List<Source> getSourceList() {
        return sourceList;
    }

    public void setSourceList(List<Source> sourceList) {
        this.sourceList = sourceList;
    }
}
