/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.administrative.repository.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.ChildEntity;
import org.sola.services.common.repository.ExternalEJB;
import org.sola.services.common.repository.entities.AbstractVersionedEntity;
import org.sola.services.ejbs.admin.businesslogic.AdminEJBLocal;
import org.sola.services.ejb.address.repository.entities.Vdc;

/**
 *
 * @author KumarKhadka
 */
@Table(name = "moth", schema = "administrative")
public class MothBasic extends AbstractVersionedEntity {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "mothluj_no")
    private String mothlujNumber;
    @Column(name = "vdc_code")
    private String vdcCode;
    @Column(name = "moth_luj")
    private String mothLuj;
    @Column(name = "office_code")
    private String officeCode;
    @Column(name="creation_date", insertable=false, updatable=false)
    private Date creationDate;
    @ExternalEJB(ejbLocalClass = AdminEJBLocal.class, loadMethod = "getVdcByCode")
    @ChildEntity(childIdField = "vdcCode")
    private Vdc vdc;
    
    public MothBasic(){
        super();
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public String getId() {
        id = id == null ? generateId() : id;
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMothLuj() {
        return mothLuj;
    }

    public void setMothLuj(String mothLuj) {
        this.mothLuj = mothLuj;
    }

    public String getMothlujNumber() {
        return mothlujNumber;
    }

    public void setMothlujNumber(String mothlujNumber) {
        this.mothlujNumber = mothlujNumber;
    }

    public Vdc getVdc() {
        return vdc;
    }

    public void setVdc(Vdc vdc) {
        this.vdc = vdc;
        if (vdc != null) {
            this.setVdcCode(vdc.getCode());
        }
    }

    public String getVdcCode() {
        return vdcCode;
    }

    public void setVdcCode(String vdcCode) {
        this.vdcCode = vdcCode;
    }
}
