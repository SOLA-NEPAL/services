package org.sola.services.ejb.address.repository.entities;

import javax.persistence.Column;
import javax.persistence.Table;
import org.sola.services.common.repository.entities.AbstractCodeEntity;

@Table(name = "district", schema = "address")
public class District extends AbstractCodeEntity {

    @Column(name = "zone_code")
    private int zoneCode;

    public District() {
        super();
    }

    public int getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(int zoneCode) {
        this.zoneCode = zoneCode;
    }
}
