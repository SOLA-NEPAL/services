 <<<<<<< HEAD
=======
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
>>>>>>> dateTestBranch
package org.sola.services.ejbs.admin.businesslogic.repository.entities;

import javax.persistence.Column;
import javax.persistence.Table;
import org.sola.services.common.repository.entities.AbstractCodeEntity;

@Table(name = "office", schema = "system")
public class Office extends AbstractCodeEntity {

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
