package org.sola.services.ejbs.admin.businesslogic.repository.entities;

import javax.persistence.Column;
import javax.persistence.Table;
import org.sola.services.common.repository.entities.AbstractCodeEntity;

@Table(name = "office", schema = "system")
public class Office extends AbstractCodeEntity {
    public static final String PARAM_DISTRICT_CODE = "districtCode";
    public static final String WHERE_BY_DISTRICT_CODE = "district_code = #{" + PARAM_DISTRICT_CODE + "}";
    
    @Column(name = "district_code")
    private String districtCode;

    public Office() {
        super();
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }
}
