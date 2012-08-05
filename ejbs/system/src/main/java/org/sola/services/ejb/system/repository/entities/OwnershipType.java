/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.system.repository.entities;

import javax.persistence.Table;
import org.sola.services.common.repository.DefaultSorter;
import org.sola.services.common.repository.entities.AbstractCodeEntity;

/**
 *
 * @author KumarKhadka
 */
@Table(name = "ownership_type", schema = "system")
@DefaultSorter(sortString = "display_value")
public class OwnershipType extends AbstractCodeEntity {

    public static final String CANCEL = "cancel";

    public OwnershipType() {
        super();
    }
}
