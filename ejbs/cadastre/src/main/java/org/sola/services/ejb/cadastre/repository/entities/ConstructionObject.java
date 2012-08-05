/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.cadastre.repository.entities;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.AccessFunctions;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;
import org.sola.services.common.repository.entities.AbstractVersionedEntity;

/**
 *
 * @author ShresthaKabin
 */
@Table(name = "construction", schema = "cadastre")
public class ConstructionObject extends AbstractVersionedEntity {
    
    private static final String QUERY_WHERE_BY_OFFICE = "(p.office_code=#{"
            + AbstractReadOnlyEntity.PARAM_OFFICE_CODE + "} OR p.office_code IS NULL)";
     
    public static final String MAP_SHEET_CODE_PARAM = "mapSheetCode";
    public static final String GET_CADASTRE_BY_MAPSHEET_CODE = "p.map_sheet_id=#{" + MAP_SHEET_CODE_PARAM + "} "
            + "AND " + QUERY_WHERE_BY_OFFICE;
    
    public static final String GET_BY_SELECT_PART =
            "c.cid,st_asewkb(c.geom_polygon) as geom_polygon,c.id,c.constype,c.area";
    public static final String GET_BY_FROM_PART =
            "cadastre.cadastre_object as p,"
            + "cadastre.construction as c";
    public static final String GET_BY_WHERE_PART =
            " p.id=c.id and " + GET_CADASTRE_BY_MAPSHEET_CODE;  
    
    @Id
    @Column(name = "cid")
    private int cid;
    
    @Column(name = "geom_polygon")
    @AccessFunctions(onSelect = "st_asewkb(geom_polygon)")
    private byte[] geomPolygon;
    
    @Column(name = "id")
    private String id;
    
    @Column(name = "constype")
    private int constype;
    
    @Column(name = "area")
    private BigDecimal area;

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getConstype() {
        return constype;
    }

    public void setConstype(int constype) {
        this.constype = constype;
    }

    public byte[] getGeomPolygon() {
        return geomPolygon;
    }

    public void setGeomPolygon(byte[] geomPolygon) {
        this.geomPolygon = geomPolygon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
