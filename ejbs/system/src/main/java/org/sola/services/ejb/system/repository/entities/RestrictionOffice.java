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
@Table(name = "restriction_office", schema = "administrative")
@DefaultSorter(sortString = "display_value")
public class RestrictionOffice extends AbstractCodeEntity {

    public static final String CANCEL = "cancel";

    public RestrictionOffice() {
        super();
    }
}
