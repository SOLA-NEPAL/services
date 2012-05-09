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
 * @author KumarKhadka
 */
@Table(name="offices",schema="nep_system")
public class LMO extends AbstractEntity{
    @Id
    @Column(name="code")
    private int lmoCode;
    @Column(name="office_name")
    private String officeName;
    @Column(name="selected")
    private boolean selected;

    public int getLmoCode() {
        return lmoCode;
    }

    public void setLmoCode(int lmoCode) {
        this.lmoCode = lmoCode;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
}
