package org.sola.services.ejb.search.repository.entities;

import java.util.List;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

public class LocDetails extends AbstractReadOnlyEntity {
    private List<PartyLoc> parties;
    private List<RrrLocDetails> rrrs;
    
    public LocDetails(){
        super();
    }

    public List<PartyLoc> getParties() {
        return parties;
    }

    public void setParties(List<PartyLoc> parties) {
        this.parties = parties;
    }

    public List<RrrLocDetails> getRrrs() {
        return rrrs;
    }

    public void setRrrs(List<RrrLocDetails> rrrs) {
        this.rrrs = rrrs;
    }
}
