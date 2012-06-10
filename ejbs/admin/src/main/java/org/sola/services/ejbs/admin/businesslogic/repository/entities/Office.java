package org.sola.services.ejbs.admin.businesslogic.repository.entities;

import javax.persistence.Column;
import javax.persistence.Table;
import org.sola.services.common.repository.entities.AbstractCodeEntity;

@Table(name = "office", schema = "system")
public class Office extends AbstractCodeEntity {

    public static final String WHERE_BY_USERNAME = "code in ("
            + "SELECT d.office_code FROM system.department d inner join system.appuser u "
            + "on d.code = u.department_code where u.username = #{" + User.PARAM_USERNAME + "}) ";
    
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
