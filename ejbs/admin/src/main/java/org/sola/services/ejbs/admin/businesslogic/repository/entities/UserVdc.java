package org.sola.services.ejbs.admin.businesslogic.repository.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.DefaultSorter;
import org.sola.services.common.repository.entities.AbstractEntity;

@Table(schema="system", name="vdc_appuser")
@DefaultSorter(sortString="vdc_code")
public class UserVdc extends AbstractEntity {
    @Id
    @Column
    private String id;
    @Column(name="vdc_code")
    private String vdcCode;
    @Column(name="ward_no")
    private String wardNumber;
    @Column(name="appuser_id")
    private String userId;
    
    public UserVdc(){
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVdcCode() {
        return vdcCode;
    }

    public void setVdcCode(String vdcCode) {
        this.vdcCode = vdcCode;
    }

    public String getWardNumber() {
        return wardNumber;
    }

    public void setWardNumber(String wardNumber) {
        this.wardNumber = wardNumber;
    }
}
