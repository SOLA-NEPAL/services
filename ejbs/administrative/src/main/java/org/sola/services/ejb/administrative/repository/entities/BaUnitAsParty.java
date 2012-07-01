/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.administrative.repository.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.entities.AbstractEntity;


/**
 *
 * @author KumarKhadka
 */
@Table(schema = "administrative", name = "ba_unit_as_party")
public class BaUnitAsParty extends AbstractEntity {
    public static final String PARTY_ID_PARAM = "partyId";
    public static final String GET_BY_PARTY_ID = "party_id=#{" + PARTY_ID_PARAM + "}";
    
    @Id
    @Column(name = "ba_unit_id")
    private String baUnitId;
    @Id
    @Column(name = "party_id")
    private String partyId;

    public String getBaUnitId() {
        return baUnitId;
    }

    public void setBaUnitId(String baUnitId) {
        this.baUnitId = baUnitId;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }
}
