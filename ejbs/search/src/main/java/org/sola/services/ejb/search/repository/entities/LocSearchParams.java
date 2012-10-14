package org.sola.services.ejb.search.repository.entities;

import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

public class LocSearchParams extends AbstractReadOnlyEntity {
    private String baUnitId;
    private String partyId;
    private String locId;
    
    public LocSearchParams(){
        super();
    }

    public String getBaUnitId() {
        return baUnitId;
    }

    public void setBaUnitId(String baUnitId) {
        this.baUnitId = baUnitId;
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }
}
