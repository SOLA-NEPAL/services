/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.cadastre.repository.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.entities.AbstractEntity;

/**
 *
 * @author KumarKhadka
 */
@Table(name = "map_sheet", schema = "cadastre")
public class MapSheet extends AbstractEntity {

    public static final String MAPSHEET_TYPE_PARAM = "sheetType";    
    public static final String GET_BY_MAYSHEET_TYPE= "sheet_type=#{" + MAPSHEET_TYPE_PARAM + "}";
    //public static final String PARAM_OFFICE_CODE = "officeCode";
    public static final String WHERE_BY_OFFICE_CODE = "office_code = #{" + PARAM_OFFICE_CODE + "}";
    
    
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "map_number")
    private String mapNumber;
    @Column(name = "sheet_type")
    private int sheetType;
    @Column(name="office_code")
    private String officeCode;
    @Column (name="srid")
    private int srid;
            
//    @Column(name = "alpha_code")
//    private String alphaCode;

//    public String getAlphaCode() {
//        return alphaCode;
//    }
//
//    public void setAlphaCode(String alpha_code) {
//        this.alphaCode = alpha_code;
//    }

    public String getId() {
        id = id == null ? id = generateId() : id;
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMapNumber() {
        return mapNumber;
    }

    public void setMapNumber(String mapNumber) {
        this.mapNumber = mapNumber;
    }

    public int getSheetType() {
        return sheetType;
    }

    public void setSheetType(int sheetType) {
        this.sheetType = sheetType;
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
    
}
