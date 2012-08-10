package org.sola.services.ejb.administrative.repository.entities;

import javax.persistence.Table;
import org.sola.services.common.repository.DefaultSorter;
import org.sola.services.common.repository.entities.AbstractCodeEntity;
/**
 * Entity representing the administrative.restriction_reason code table
 */
@Table(name = "restriction_reason", schema = "administrative")
@DefaultSorter(sortString="display_value")
public class RestrictionReason extends AbstractCodeEntity {
    public RestrictionReason() {
        super();
    }
}
