package org.sola.services.ejb.administrative.repository.entities;

import javax.persistence.Table;
import org.sola.services.common.repository.DefaultSorter;
import org.sola.services.common.repository.entities.AbstractCodeEntity;

/**
 *
 * @author KumarKhadka
 */
@Table(name = "share_type", schema = "administrative")
@DefaultSorter(sortString = "display_value")
public class OwnershipType extends AbstractCodeEntity {
    public OwnershipType() {
        super();
    }
}
