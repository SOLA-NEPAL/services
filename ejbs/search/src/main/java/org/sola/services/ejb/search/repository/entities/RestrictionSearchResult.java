package org.sola.services.ejb.search.repository.entities;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Id;
import org.sola.services.common.repository.entities.AbstractEntity;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

public class RestrictionSearchResult extends AbstractReadOnlyEntity {

    public static final String PARAM_LANG = "languageCode";
    public static final String PARAM_VDC_CODE = "vdcCode";
    public static final String PARAM_MAP_SHEET_ID = "mapSheetId";
    public static final String PARAM_WARD_NO = "wardNo";
    public static final String PARAM_PARCEL_NO = "parcelNo";
    public static final String PARAM_BUNDLE_NO = "bundleNo";
    public static final String PARAM_BUNDLE_PAGE_NO = "bundlePageNo";
    public static final String PARAM_RESTR_REASON_CODE = "restrictionReasonCode";
    public static final String PARAM_RESTR_OFFICE = "restrictionOffice";
    public static final String PARAM_REFERENCE_NO = "referenceNo";
    public static final String PARAM_SOURCE_TYPE_CODE = "sourceTypeCode";
    public static final String PARAM_SERIAL_NO = "serialNo";
    public static final String PARAM_OWNER_NAME = "ownerName";
    public static final String PARAM_OWNER_LAST_NAME = "ownerLastName";
    public static final String PARAM_REG_NUMBER = "regNumber";
    public static final String PARAM_REG_DATE_FROM = "regDateFrom";
    public static final String PARAM_REG_DATE_TO = "regDateTo";
    public static final String PARAM_REF_DATE_FROM = "refDateFrom";
    public static final String PARAM_REF_DATE_TO = "refDateTo";
    public static final String PARAM_PRICE_FROM = "priceFrom";
    public static final String PARAM_PRICE_TO = "priceTo";
    
    public static final String QUERY_SELECT =
            "SELECT DISTINCT ba.id, get_translation(vdc.display_value, #{" + PARAM_LANG + "}) AS vdc_name, m.map_number, "
            + "a.ward_no, co.parcel_no, r.bundle_number, r.bundle_page_no, r.registration_number, "
            + "r.registration_date, r.restriction_reason_code, r.restriction_office_name, r.sn, "
            + "s.type_code, s.reference_nr, s.recordation, r.mortgage_amount, n.notation_text, r.status_code, "
            + "(SELECT string_agg(COALESCE(p.name, '') || "
            + "(CASE WHEN p.last_name IS NULL OR p.last_name='' THEN '' ELSE ' ' END) || COALESCE(p.last_name, '')"
            + "|| (CASE WHEN p.address_id IS NULL OR p.address_id='' THEN '' ELSE ' (' END) "
            + "|| (SELECT get_translation(v.display_value, #{" + PARAM_LANG + "}) "
            + "|| (CASE WHEN a1.ward_no IS NULL OR a1.ward_no='' THEN '' ELSE '-' END) "
            + "    || COALESCE(a1.ward_no, '') || (CASE WHEN a1.street IS NULL OR a1.street='' THEN '' ELSE ', ' END) "
            + "     || COALESCE(a1.street, '') "
            + "     FROM address.address a1 LEFT JOIN address.vdc v ON a1.vdc_code=v.code WHERE a1.id=p.address_id) "
            + " || (CASE WHEN p.address_id IS NULL OR p.address_id='' THEN '' ELSE ')' END), '; ') "
            + " FROM (administrative.rrr r2 INNER JOIN administrative.rrr_type rt ON r2.type_code=rt.code) "
            + " LEFT JOIN (administrative.party_for_rrr pr INNER JOIN party.party p ON pr.party_id = p.id) ON r2.id = pr.rrr_id "
            + " WHERE r2.ba_unit_id = ba.id AND rt.rrr_group_type_code='ownership' AND r2.status_code='current') AS owners "
            + "FROM (administrative.ba_unit ba "
            + "  LEFT JOIN ((cadastre.cadastre_object co "
            + "    LEFT JOIN (address.address a "
            + "      LEFT JOIN address.vdc vdc "
            + "      ON a.vdc_code=vdc.code) "
            + "    ON co.address_id=a.id)"
            + "    LEFT JOIN cadastre.map_sheet m "
            + "    ON co.map_sheet_id = m.id) "
            + "  ON ba.cadastre_object_id = co.id) "
            + "  INNER JOIN ((administrative.rrr r "
            + "    LEFT JOIN administrative.notation n "
            + "    ON r.id=n.rrr_id) "
            + "    LEFT JOIN (administrative.source_describes_rrr sr "
            + "      LEFT JOIN source.source s "
            + "      ON sr.source_id=s.id) "
            + "    ON r.id=sr.rrr_id) "
            + "  ON ba.id = r.ba_unit_id "
            + "WHERE r.status_code='current' "
            + "AND (COALESCE(a.vdc_code, '') = #{" + PARAM_VDC_CODE + "} OR #{" + PARAM_VDC_CODE + "}='') "
            + "AND (COALESCE(co.map_sheet_id, '') = #{" + PARAM_MAP_SHEET_ID + "} OR #{" + PARAM_MAP_SHEET_ID + "}='') "
            + "AND (LOWER(COALESCE(a.ward_no, '')) = LOWER(#{" + PARAM_WARD_NO + "}) OR #{" + PARAM_WARD_NO + "}='') "
            + "AND (LOWER(COALESCE(co.parcel_no, '')) = LOWER(#{" + PARAM_PARCEL_NO + "}) OR #{" + PARAM_PARCEL_NO + "}='') "
            + "AND (LOWER(COALESCE(r.bundle_number, '')) = LOWER(#{" + PARAM_BUNDLE_NO + "}) OR #{" + PARAM_BUNDLE_NO + "}='') "
            + "AND (LOWER(COALESCE(r.bundle_page_no, '')) = LOWER(#{" + PARAM_BUNDLE_PAGE_NO + "}) OR #{" + PARAM_BUNDLE_PAGE_NO + "}='') "
            + "AND (COALESCE(r.restriction_reason_code, '') = #{" + PARAM_RESTR_REASON_CODE + "} OR #{" + PARAM_RESTR_REASON_CODE + "}='') "
            + "AND (POSITION(LOWER(#{" + PARAM_RESTR_OFFICE + "}) IN LOWER(COALESCE(r.restriction_office_name, ''))) > 0) "
            + "AND (POSITION(LOWER(#{" + PARAM_REFERENCE_NO + "}) IN LOWER(COALESCE(s.reference_nr, ''))) > 0) "
            + "AND (COALESCE(s.type_code, '') = #{" + PARAM_SOURCE_TYPE_CODE + "} OR #{" + PARAM_SOURCE_TYPE_CODE + "}='') "
            + "AND (POSITION(LOWER(#{" + PARAM_SERIAL_NO + "}) IN LOWER(COALESCE(r.sn, ''))) > 0) "
            + "AND ((#{" + PARAM_OWNER_NAME + "}='' AND #{" + PARAM_OWNER_LAST_NAME + "}='') OR r.id IN (SELECT r3.id FROM administrative.rrr r3 "
            + "  INNER JOIN (administrative.party_for_rrr pr2 INNER JOIN party.party p2 ON pr2.party_id=p2.id) ON r3.id=pr2.rrr_id "
            + "  WHERE r3.status_code='current' AND r3.office_code = #{" + AbstractEntity.PARAM_OFFICE_CODE + "} AND r3.type_code='simpleRestriction' AND "
            + "  (POSITION(LOWER(#{" + PARAM_OWNER_NAME + "}) IN LOWER(COALESCE(p2.name, ''))) > 0) "
            + "  AND (POSITION(LOWER(#{" + PARAM_OWNER_LAST_NAME + "}) IN LOWER(COALESCE(p2.last_name, ''))) > 0))) "
            + "AND (COALESCE(r.registration_number, '') = #{" + PARAM_REG_NUMBER + "} OR #{" + PARAM_REG_NUMBER + "}='') "
            + "AND ((#{" + PARAM_REG_DATE_FROM + "}=0 AND #{" + PARAM_REG_DATE_TO + "}=99999999) OR r.registration_date BETWEEN #{" + PARAM_REG_DATE_FROM + "} AND #{" + PARAM_REG_DATE_TO + "}) "
            + "AND ((#{" + PARAM_REF_DATE_FROM + "}=0 AND #{" + PARAM_REF_DATE_TO + "}=99999999) OR s.recordation BETWEEN #{" + PARAM_REF_DATE_FROM + "} AND #{" + PARAM_REF_DATE_TO + "}) "
            + "AND ((#{" + PARAM_PRICE_FROM + "}=0 AND #{" + PARAM_PRICE_TO + "}=99999999999999999999) OR r.mortgage_amount BETWEEN #{" + PARAM_PRICE_FROM + "} AND #{" + PARAM_PRICE_TO + "}) "
            + "AND r.office_code = #{" + AbstractEntity.PARAM_OFFICE_CODE + "} AND r.type_code='simpleRestriction' "
            + "LIMIT 101";

    @Id
    @Column
    private String id;
    @Column(name="vdc_name")
    private String vdcName;
    @Column(name="map_number")
    private String mapNumber;
    @Column(name="ward_no")
    private String wardNo;
    @Column(name="parcel_no")
    private String parcelNo;
    @Column(name="bundle_number")
    private String bundleNumber;
    @Column(name="bundle_page_no")
    private String bundlePageNo;
    @Column(name="registration_number")
    private String registrationNumber;
    @Column(name="registration_date")
    private Integer registrationDate;
    @Column(name="restriction_reason_code")
    private String restrictionReasonCode;
    @Column(name="restriction_office_name")
    private String restrictionOfficeName;
    @Column(name="sn")
    private String serialNumber;
    @Column(name="type_code")
    private String sourceTypeCode;
    @Column(name="reference_nr")
    private String referenceNr;
    @Column(name="recordation")
    private Integer referenceDate;
    @Column(name="mortgage_amount")
    private BigDecimal price;
    @Column(name="notation_text")
    private String notationText;
    @Column(name="status_code")
    private String statusCode;
    @Column(name="owners")
    private String owners;
    
    public RestrictionSearchResult() {
        super();
    }

    public String getBundleNumber() {
        return bundleNumber;
    }

    public void setBundleNumber(String bundleNumber) {
        this.bundleNumber = bundleNumber;
    }

    public String getBundlePageNo() {
        return bundlePageNo;
    }

    public void setBundlePageNo(String bundlePageNo) {
        this.bundlePageNo = bundlePageNo;
    }

    public String getId() {
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

    public String getNotationText() {
        return notationText;
    }

    public void setNotationText(String notationText) {
        this.notationText = notationText;
    }

    public String getOwners() {
        return owners;
    }

    public void setOwners(String owners) {
        this.owners = owners;
    }

    public String getParcelNo() {
        return parcelNo;
    }

    public void setParcelNo(String parcelNo) {
        this.parcelNo = parcelNo;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getReferenceDate() {
        return referenceDate;
    }

    public void setReferenceDate(Integer referenceDate) {
        this.referenceDate = referenceDate;
    }

    public String getReferenceNr() {
        return referenceNr;
    }

    public void setReferenceNr(String referenceNr) {
        this.referenceNr = referenceNr;
    }

    public Integer getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Integer registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getRestrictionOfficeName() {
        return restrictionOfficeName;
    }

    public void setRestrictionOfficeName(String restrictionOfficeName) {
        this.restrictionOfficeName = restrictionOfficeName;
    }

    public String getRestrictionReasonCode() {
        return restrictionReasonCode;
    }

    public void setRestrictionReasonCode(String restrictionReasonCode) {
        this.restrictionReasonCode = restrictionReasonCode;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSourceTypeCode() {
        return sourceTypeCode;
    }

    public void setSourceTypeCode(String sourceTypeCode) {
        this.sourceTypeCode = sourceTypeCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getVdcName() {
        return vdcName;
    }

    public void setVdcName(String vdcName) {
        this.vdcName = vdcName;
    }

    public String getWardNo() {
        return wardNo;
    }

    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
    }
}
