/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.party.repository.entities;

import javax.persistence.Table;
import org.sola.services.common.repository.DefaultSorter;
import org.sola.services.common.repository.entities.AbstractCodeEntity;

/**
 *
 * @author Kumar
 */
@Table(name = "id_office_type", schema = "party")
@DefaultSorter(sortString = "display_value")
public class IdOfficeType extends AbstractCodeEntity {

    public IdOfficeType() {
        super();
    }
}
