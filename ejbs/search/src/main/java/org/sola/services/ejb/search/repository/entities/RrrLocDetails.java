/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.search.repository.entities;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Id;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

/**
 *
 * @author Solovov
 */
public class RrrLocDetails extends AbstractReadOnlyEntity {

    public static final String PARAM_LANG = "lang";
    public static final String PARAM_LOC_ID = "locId";
    public static final String QUERY_SELECT = "SELECT DISTINCT l.id, b.name_firstpart, b.name_lastpart, "
            + "(SELECT string_agg(c.parcel_no, ',') FROM cadastre.cadastre_object c WHERE c.id IN "
            + "(SELECT cadastre_object_id FROM cadastre.cadastre_object_target ct INNER JOIN cadastre.cadastre_object c1 "
            + "ON ct.transaction_id=c1.transaction_id WHERE c1.id=b.cadastre_object_id)) AS old_parcels, "
            + "(SELECT string_agg(COALESCE(name, '') || ' ' || COALESCE(last_name, ''), ',') FROM party.party p INNER JOIN "
            + "(administrative.party_for_rrr pr INNER JOIN administrative.rrr r1 ON pr.rrr_id=r1.id) ON p.id=pr.party_id "
            + "WHERE r1.status_code='current' AND r1.type_code='tenancy' AND r1.ba_unit_id=b.id) AS tenants, get_translation(o.display_value, #{" + PARAM_LANG + "}) AS office_name, "
            + "a.vdc_code, get_translation(vdc.display_value, #{" + PARAM_LANG + "}) AS vdc_name, a.ward_no, m.map_number, co.parcel_no, "
            + "co.land_use_code, r.ownership_type_code, r.owner_type_code, r.registration_number, r.registration_date, "
            + "(SELECT get_translation(rt.display_value, #{" + PARAM_LANG + "}) "
            + " FROM transaction.transaction tr INNER JOIN (application.service ser INNER JOIN application.request_type rt ON ser.request_type_code = rt.code) "
            + "   ON tr.from_service_id = ser.id WHERE tr.id=r.transaction_id) AS request_name,"
            + "co.land_type_code, co.land_class_code, "
            + "co.official_area, co.area_unit_type_code, n.notation_text, l.pana_no, moth.mothluj_no "
            + "FROM (administrative.ba_unit b "
            + "INNER JOIN ((cadastre.cadastre_object co "
            + "  LEFT JOIN (address.address a "
            + "     LEFT JOIN address.vdc vdc ON a.vdc_code=vdc.code) "
            + "  ON co.address_id=a.id) "
            + "  LEFT JOIN cadastre.map_sheet m ON co.map_sheet_id=m.id) "
            + "ON b.cadastre_object_id = co.id) "
            + "INNER JOIN (((administrative.rrr r LEFT JOIN system.office o ON r.office_code=o.code) "
            + "LEFT JOIN administrative.notation n ON r.id=n.rrr_id) "
            + "  INNER JOIN (administrative.loc l INNER JOIN administrative.moth moth ON l.moth_id=moth.id) "
            + "ON r.loc_id=l.id) "
            + "ON b.id=r.ba_unit_id "
            + "WHERE r.status_code = 'current' AND r.loc_id  =#{" + PARAM_LOC_ID + "}";
    @Id
    @Column
    private String id;
    @Column(name = "name_firstpart")
    private String nameFirstPart;
    @Column(name = "name_lastpart")
    private String nameLastPart;
    @Column(name = "old_parcels")
    private String oldParcels;
    @Column(name = "tenants")
    private String tenants;
    @Column(name = "vdc_code")
    private String vdcCode;
    @Column(name = "vdc_name")
    private String vdcName;
    @Column(name = "ward_no")
    private String wardNumber;
    @Column(name = "map_number")
    private String mapNumber;
    @Column(name = "parcel_no")
    private String parcelNumber;
    @Column(name = "land_use_code")
    private String landUseCode;
    @Column(name = "ownership_type_code")
    private String ownershipTypeCode;
    @Column(name = "owner_type_code")
    private String ownerTypeCode;
    @Column(name = "land_type_code")
    private String landTypeCode;
    @Column(name = "land_class_code")
    private String landClassCode;
    @Column(name = "official_area")
    private BigDecimal officialArea;
    @Column(name = "area_unit_type_code")
    private String areaUnitTypeCode;
    @Column(name = "notation_text")
    private String notationText;
    @Column(name = "pana_no")
    private String panaNumber;
    @Column(name = "mothluj_no")
    private String mothNumber;
    @Column(name = "office_name")
    private String officeName;
    @Column(name = "registration_number")
    private String regNumber;
    @Column(name = "registration_date")
    private Integer regDate;
    @Column(name = "request_name")
    private String requestName;

    public RrrLocDetails() {
        super();
    }

    public String getAreaUnitTypeCode() {
        return areaUnitTypeCode;
    }

    public void setAreaUnitTypeCode(String areaUnitTypeCode) {
        this.areaUnitTypeCode = areaUnitTypeCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLandClassCode() {
        return landClassCode;
    }

    public void setLandClassCode(String landClassCode) {
        this.landClassCode = landClassCode;
    }

    public String getLandTypeCode() {
        return landTypeCode;
    }

    public void setLandTypeCode(String landTypeCode) {
        this.landTypeCode = landTypeCode;
    }

    public String getLandUseCode() {
        return landUseCode;
    }

    public void setLandUseCode(String landUseCode) {
        this.landUseCode = landUseCode;
    }

    public String getMapNumber() {
        return mapNumber;
    }

    public void setMapNumber(String mapNumber) {
        this.mapNumber = mapNumber;
    }

    public String getMothNumber() {
        return mothNumber;
    }

    public void setMothNumber(String mothNumber) {
        this.mothNumber = mothNumber;
    }

    public String getNameFirstPart() {
        return nameFirstPart;
    }

    public void setNameFirstPart(String nameFirstPart) {
        this.nameFirstPart = nameFirstPart;
    }

    public String getNameLastPart() {
        return nameLastPart;
    }

    public void setNameLastPart(String nameLastPart) {
        this.nameLastPart = nameLastPart;
    }

    public String getNotationText() {
        return notationText;
    }

    public void setNotationText(String notationText) {
        this.notationText = notationText;
    }

    public BigDecimal getOfficialArea() {
        return officialArea;
    }

    public void setOfficialArea(BigDecimal officialArea) {
        this.officialArea = officialArea;
    }

    public String getOldParcels() {
        return oldParcels;
    }

    public void setOldParcels(String oldParcels) {
        this.oldParcels = oldParcels;
    }

    public String getOwnerTypeCode() {
        return ownerTypeCode;
    }

    public void setOwnerTypeCode(String ownerTypeCode) {
        this.ownerTypeCode = ownerTypeCode;
    }

    public String getOwnershipTypeCode() {
        return ownershipTypeCode;
    }

    public void setOwnershipTypeCode(String ownershipTypeCode) {
        this.ownershipTypeCode = ownershipTypeCode;
    }

    public String getPanaNumber() {
        return panaNumber;
    }

    public void setPanaNumber(String panaNumber) {
        this.panaNumber = panaNumber;
    }

    public String getParcelNumber() {
        return parcelNumber;
    }

    public void setParcelNumber(String parcelNumber) {
        this.parcelNumber = parcelNumber;
    }

    public String getTenants() {
        return tenants;
    }

    public void setTenants(String tenants) {
        this.tenants = tenants;
    }

    public String getVdcCode() {
        return vdcCode;
    }

    public void setVdcCode(String vdcCode) {
        this.vdcCode = vdcCode;
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

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public Integer getRegDate() {
        return regDate;
    }

    public void setRegDate(Integer regDate) {
        this.regDate = regDate;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }
}
