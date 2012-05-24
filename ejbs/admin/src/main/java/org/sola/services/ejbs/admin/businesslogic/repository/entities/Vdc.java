/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejbs.admin.businesslogic.repository.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.entities.AbstractCodeEntity;

/**
 *
 * @author KumarKhadka
 */
@Table(name = "vdc", schema = "system")
public class Vdc extends  AbstractCodeEntity {   
    public static final String VDC_NAME_PARAM = "displayValue";
    public static final String VDC_CODE_PARAM = "vdcCode";
    public static final String GET_BY_VDC_NAME = "display_value=#{" + VDC_NAME_PARAM + "}";
    public static final String GET_BY_VDC_CODE = "code=#{" + VDC_CODE_PARAM + "}";
    
    @Id
    @Column(name="code")
    private String vdcCode;    
    @Column(name = "district_code")
    private String districtCode;   

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getVdcCode() {
        return vdcCode;
    }

    public void setVdcCode(String vdcCode) {
        this.vdcCode = vdcCode;
    }
    
    
}
