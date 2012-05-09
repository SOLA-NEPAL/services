/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejbs.admin.businesslogic.repository.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.entities.AbstractEntity;

/**
 *
 * @author KumarKhadka
 */
@Table(name="nepal_user_additional_info",schema="system")
public class UserNepalModification extends AbstractEntity{
    @Id
    @Column(name="user_id")
    private String userId;
    @Column(name="district_code")
    private String districtCode;
    @Column(name="lmo_code")
    private String lmoCode;
    
    
   public UserNepalModification(){
        super();
    }
    
   public UserNepalModification(String userId, String districtCode,String lmoCode){
        super();
        this.userId=userId;
        this.districtCode=districtCode;
        this.lmoCode=lmoCode;
    }
    

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getLmoCode() {
        return lmoCode;
    }

    public void setLmoCode(String lmoCode) {
        this.lmoCode = lmoCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    
    
}
