/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.party.repository.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.entities.AbstractEntity;


/**
 *
 * @author KumarKhadka
 */
@Table(name="districts",schema="nep_system")
public class District extends AbstractEntity{
    @Id
    @Column(name="code")
    private int districtCode;
    @Column(name="district_name")
    private String districtName;
    @Column(name="zone_code")
    private int zoneCode;
    
    public int getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(int districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public int getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(int zoneCode) {
        this.zoneCode = zoneCode;
    }
    
    
}
