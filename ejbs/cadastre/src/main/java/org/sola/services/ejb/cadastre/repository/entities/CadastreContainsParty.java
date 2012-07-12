/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.cadastre.repository.entities;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 *
 * @author KumarKhadka
 */
@Table(name = "cadastre_contains_party", schema = "cadastre")
public class CadastreContainsParty {

    @Column(name = "partyid")
    private String partyId;
    @Column(name = "cadastre_objectid")
    private String cadastreObjectId;

    public String getCadastreObjectId() {
        return cadastreObjectId;
    }

    public void setCadastreObjectId(String cadastreObjectId) {
        this.cadastreObjectId = cadastreObjectId;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }
}
