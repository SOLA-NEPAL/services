/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.cadastre.repository.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.DefaultSorter;
import org.sola.services.common.repository.entities.AbstractEntity;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

/**
 *
 * @author KumarKhadka
 */
@Table(name = "map_sheet", schema = "cadastre")
@DefaultSorter(sortString = "map_number")
public class MapSheet extends AbstractEntity {

    public static final String MAPSHEET_TYPE_PARAM = "sheetType";    
    public static final String GET_BY_MAYSHEET_TYPE= "sheet_type=#{" + MAPSHEET_TYPE_PARAM + "}";
    //public static final String PARAM_OFFICE_CODE = "officeCode";
    public static final String WHERE_BY_OFFICE_CODE = "office_code = #{" + PARAM_OFFICE_CODE + "}";
    
     //map sheet listing
    public static final String VDC_PARAM = "vdc";
    public static final String WARD_NO_PARAM = "wardno";
    private static final String QUERY_WHERE_BY_OFFICE = "(p.office_code=#{"
            + AbstractReadOnlyEntity.PARAM_OFFICE_CODE + "} OR p.office_code IS NULL)";
    //ward based listing.
    public static final String GET_BY_WARD_SELECT_PART =
            "distinct m.id,m.map_number,m.sheet_type,m.office_code,m.srid ";
    public static final String GET_BY_WARD_FROM_PART =
            "cadastre.cadastre_object as p,"
            + "cadastre.spatial_unit_address as pa,"
            + "address.address as a,cadastre.map_sheet as m";
    public static final String GET_BY_WARD_WHERE_PART =
            " p.id=pa.spatial_unit_id and "
            + " pa.address_id=a.id and "
            + " a.vdc_code=#{" + VDC_PARAM + "} and "
            + " a.ward_no=#{" + WARD_NO_PARAM + "} and "
            + " p.map_sheet_id=m.map_number and "
            + QUERY_WHERE_BY_OFFICE;
    public static final String GET_BY_VDC_WHERE_PART =
            " p.id=pa.spatial_unit_id and "
            + " pa.address_id=a.id and "
            + " a.vdc_code=#{" + VDC_PARAM + "} and "
            + " p.map_sheet_id=m.map_number and "
            + QUERY_WHERE_BY_OFFICE;
    
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
