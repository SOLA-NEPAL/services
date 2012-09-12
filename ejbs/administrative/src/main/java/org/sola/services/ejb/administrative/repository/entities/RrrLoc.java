package org.sola.services.ejb.administrative.repository.entities;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Id;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;
import org.sola.services.ejb.party.repository.entities.Party;

public class RrrLoc extends AbstractReadOnlyEntity {

    public static final String PARAM_LOC_ID = "locId";
    public static final String PARAM_STATUS = "status";
    public static final String SELECT_GET_PARTY_IDS = "select id from administrative.get_loc_party_ids(#{"
            + PARAM_LOC_ID +"}, #{"+ PARAM_STATUS +"})";
    public static final String SELECT_RRR_LOCS = "select loc_id, type_code, owner_type_code, ownership_type_code, status_code " 
            + "from administrative.get_loc_rrrs(#{" + PARAM_LOC_ID +"}, #{"+ PARAM_OFFICE_CODE +"})";
    
    @Id
    @Column(name = "loc_id")
    private String locId;
    @Column(name = "type_code")
    private String typeCode;
    @Column(name = "owner_type_code")
    private String ownerTypeCode;
    @Column(name = "ownership_type_code")
    private String ownershipTypeCode;
    @Column(name = "status_code")
    private String statusCode;
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

    public String getOwnerTypeCode() {
        return ownerTypeCode;
    }

    public void setOwnerTypeCode(String ownerTypeCode) {
        this.ownerTypeCode = ownerTypeCode;
    }

    public String getOwnershipTypeCode() {
        return ownershipTypeCode;
    }

    public void setOwnershipTypeCode(String ownershipTypeCode) {
        this.ownershipTypeCode = ownershipTypeCode;
    }

    public List<Party> getRightHolderList() {
        return rightHolderList;
    }

    public void setRightHolderList(List<Party> rightHolderList) {
        this.rightHolderList = rightHolderList;
    }
}
