package org.sola.services.ejb.administrative.repository.entities;

import javax.persistence.Table;
import org.sola.services.common.repository.DefaultSorter;
import org.sola.services.common.repository.entities.AbstractCodeEntity;

/**
 *
 * @author KumarKhadka
 */
@Table(name = "tenant_type", schema = "administrative")
@DefaultSorter(sortString = "display_value")
public class TenantType extends AbstractCodeEntity {
    public TenantType() {
        super();
    }
}
