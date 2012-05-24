package org.sola.services.ejbs.admin.businesslogic.repository.entities;

import javax.persistence.Column;
import javax.persistence.Table;
import org.sola.services.common.repository.DefaultSorter;
import org.sola.services.common.repository.entities.AbstractCodeEntity;

@Table(name="department", schema="system")
@DefaultSorter(sortString="name")
public class Department extends AbstractCodeEntity {
    public static final String PARAM_OFFICE_CODE = "officeCode";
    public static final String WHERE_BY_OFFICE_CODE = "office_code = #{" + PARAM_OFFICE_CODE + "}";

    @Column(name="office_code")
    private String officeCode;

    public Department(){
        super();
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }
}
