package org.sola.services.ejb.search.repository.entities;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

public class RestrictionInfo extends AbstractReadOnlyEntity {

    public static final String PARAM_LANG = "languageCode";
    public static final String PARAM_REF_NUM = "refNum";
    public static final String PARAM_REF_DATE = "refDate";
    
    public static final String QUERY_SELECT = ""
            + "SELECT DISTINCT ba.id AS ba_unit_id, co.area_unit_type_code, "
            + "get_translation(vdc.display_value, #{" + PARAM_LANG + "}) AS vdc_name, "
            + "a.ward_no, co.parcel_no, m.map_number, co.official_area "
            + "FROM administrative.ba_unit ba "
            + "  INNER JOIN ((cadastre.cadastre_object co "
            + "    LEFT JOIN (address.address a "
            + "      LEFT JOIN address.vdc vdc ON a.vdc_code = vdc.code) "
            + "    ON co.address_id = a.id) "
            + "    LEFT JOIN cadastre.map_sheet m "
            + "    ON co.map_sheet_id = m.id) "
            + "  ON ba.cadastre_object_id = co.id "
            + "WHERE ba.id IN (SELECT r.ba_unit_id "
            + "  FROM administrative.rrr r "
            + "    INNER JOIN (administrative.source_describes_rrr sr "
            + "      INNER JOIN source.source s ON sr.source_id = s.id) "
            + "    ON r.id = sr.rrr_id "
            + "  WHERE r.type_code = 'simpleRestriction' AND r.status_code = 'current' "
            + "    AND r.office_code = #{" + PARAM_OFFICE_CODE + "} "
            + "    AND s.reference_nr = #{" + PARAM_REF_NUM + "} "
            + "    AND s.recordation = #{" + PARAM_REF_DATE + "})";

    @Column(name = "ba_unit_id")
    private String baUnitId;
    @Column(name = "vdc_name")
    private String vdcName;
    @Column(name = "ward_no")
    private String wardNumber;
    @Column(name = "parcel_no")
    private String parcelNumber;
    @Column(name = "map_number")
    private String mapNumber;
    @Column(name = "area_unit_type_code")
    private String areaUnitTypeCode;
    @Column(name = "official_area")
    private BigDecimal officialArea;
    
    private List<RestrictionRrr> rrrs;
    private List<Owner> owners;
    
    public RestrictionInfo() {
        super();
    }

    public String getBaUnitId() {
        return baUnitId;
    }

    public void setBaUnitId(String baUnitId) {
        this.baUnitId = baUnitId;
    }

    public String getAreaUnitTypeCode() {
        return areaUnitTypeCode;
    }

    public void setAreaUnitTypeCode(String areaUnitTypeCode) {
        this.areaUnitTypeCode = areaUnitTypeCode;
    }

    public String getMapNumber() {
        return mapNumber;
    }

    public void setMapNumber(String mapNumber) {
        this.mapNumber = mapNumber;
    }

    public BigDecimal getOfficialArea() {
        return officialArea;
    }

    public void setOfficialArea(BigDecimal officialArea) {
        this.officialArea = officialArea;
    }

    public String getParcelNumber() {
        return parcelNumber;
    }

    public void setParcelNumber(String parcelNumber) {
        this.parcelNumber = parcelNumber;
    }

    public String getVdcName() {
        return vdcName;
    }

    public void setVdcName(String vdcName) {
        this.vdcName = vdcName;
    }

    public String getWardNumber() {
        return wardNumber;
    }

    public void setWardNumber(String wardNumber) {
        this.wardNumber = wardNumber;
    }

    public List<Owner> getOwners() {
        return owners;
    }

    public void setOwners(List<Owner> owners) {
        this.owners = owners;
    }

    public List<RestrictionRrr> getRrrs() {
        return rrrs;
    }

    public void setRrrs(List<RestrictionRrr> rrrs) {
        this.rrrs = rrrs;
    }
}
