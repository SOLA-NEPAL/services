package org.sola.services.ejbs.admin.businesslogic.repository.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.entities.AbstractCodeEntity;

@Table(name="districts",schema="nep_system")
public class District extends AbstractCodeEntity{
    @Id
    @Column(name="code")
    private int districtCode;
    @Column(name="district_name")
    private String districtName;

    @Column(name="zone_code")
    private int zoneCode;
    
    public District(){
        super();
    }
    
    public int getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(int zoneCode) {
        this.zoneCode = zoneCode;
    }
}
