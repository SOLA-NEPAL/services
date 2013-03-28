/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.party.repository.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.entities.AbstractEntity;

/**
 *
 * @author Kumar
 */
@Table(name = "category_for_party", schema = "party")
public class CategoryForParty extends AbstractEntity {

    public CategoryForParty() {
        super();
    }
    @Id
    @Column(name = "category_id")
    private String categoryId;
    @Id
    @Column(name = "party_id")
    private String partyId;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }
}
