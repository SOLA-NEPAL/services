package org.sola.services.ejb.administrative.repository.entities;

import javax.persistence.Table;
import org.sola.services.common.repository.DefaultSorter;
import org.sola.services.common.repository.entities.AbstractCodeEntity;

@Table(name = "owner_type", schema = "administrative")
@DefaultSorter(sortString = "display_value")
public class OwnerType extends AbstractCodeEntity {
    public OwnerType() {
        super();
    }
}
