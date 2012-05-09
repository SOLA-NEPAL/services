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
@Table(name="vdcs",schema="nep_system")
public class Vdc extends  AbstractEntity{
    
//    public static final String VDC_PARAM="vdcName";
//    public static final String GET_BY_VDC="vdc_name=#{"+VDC_PARAM+"}";
    @Id
    @Column(name="code")
    private int vdcCode;
    @Column(name="vdc_name")
    private String vdcName;
    @Column(name="district_code")
    private int districtCode;

    public int getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(int districtCode) {
        this.districtCode = districtCode;
    }

    public int getVdcCode() {
        return vdcCode;
    }

    public void setVdcCode(int vdcCode) {
        this.vdcCode = vdcCode;
    }

    public String getVdcName() {
        return vdcName;
    }

    public void setVdcName(String vdcName) {
        this.vdcName = vdcName;
    }
    
    @Override
    public String toString()
    {
        return vdcName;
    }
    
}
