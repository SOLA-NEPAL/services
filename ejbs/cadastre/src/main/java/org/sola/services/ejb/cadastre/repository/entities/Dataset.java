package org.sola.services.ejb.cadastre.repository.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.entities.AbstractEntity;

@Table(schema="cadastre", name="dataset")
public class Dataset extends AbstractEntity {
    public static final String PARAM_USERNAME = "userName";
    public static final String WHERE_BY_OFFICE_CODE = "office_code=#{ " + PARAM_OFFICE_CODE + "}";
    public static final String WHERE_BY_USERNAME = "vdc_code IN "
            + "(SELECT uvdc.vdc_code FROM system.vdc_appuser uvdc "
            + "INNER JOIN system.appuser u ON uvdc.appuser_id = u.id WHERE u.username = #{ " + PARAM_USERNAME + "})";
    
    @Id
    @Column
    private String id;
    @Column
    private String name;
    @Column
    private int srid;
    @Column(name="office_code")
    private String officeCode;
    @Column(name="vdc_code")
    private String vdcCode;
    
    public Dataset(){
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public int getSrid() {
        return srid;
    }

    public void setSrid(int srid) {
        this.srid = srid;
    }

    public String getVdcCode() {
        return vdcCode;
    }

    public void setVdcCode(String vdcCode) {
        this.vdcCode = vdcCode;
    }
}
