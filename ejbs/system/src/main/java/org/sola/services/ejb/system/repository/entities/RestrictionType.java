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
 * @author Dinesh
 */

/**
 * Entity representing the system.restriction_type code table
 */
@Table(name = "restriction_type", schema = "system")
@DefaultSorter(sortString="display_value")
public class RestrictionType extends AbstractCodeEntity {
    public static final String CANCEL = "cancel";

    public RestrictionType() {
        super();
    }
}
