package org.sola.services.ejb.search.repository.entities;

import javax.persistence.Column;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

public class RestrictionRrr extends AbstractReadOnlyEntity {

    public static final String PARAM_BA_UNIT_ID = "baUnitId";
    public static final String PARAM_REF_NUM = "refNum";
    public static final String PARAM_REF_DATE = "refDate";
    
    public static final String QUERY_SELECT = ""
            + "SELECT DISTINCT r.id, r.sn, r,registration_number, r.registration_date, "
            + "  r.bundle_number, r.bundle_page_no, r.restriction_office_name, "
            + "  r.restriction_office_address, s.reference_nr, s.recordation, n.notation_text "
            + "FROM (administrative.rrr r "
            + "  INNER JOIN (administrative.source_describes_rrr sr "
            + "    INNER JOIN source.source s "
            + "    ON sr.source_id = s.id) "
            + "  ON r.id = sr.rrr_id) "
            + "  LEFT JOIN administrative.notation n "
            + "  ON r.id = n.rrr_id "
            + "WHERE r.type_code = 'simpleRestriction'"
            + "  AND r.status_code = 'current' "
            + "  AND r.ba_unit_id = #{" + PARAM_BA_UNIT_ID + "} "
            + "  AND s.reference_nr = #{" + PARAM_REF_NUM + "}"
            + "  AND s.recordation = #{" + PARAM_REF_DATE + "}";

    @Column(name="id")
    private String id;
    @Column(name="sn")
    private String sn;
    @Column(name="registration_number")
    private String regNumber;
    @Column(name="registration_date")
    private Integer regDate;
    @Column(name="bundle_number")
    private String bundleNumber;
    @Column(name="bundle_page_no")
    private String bundlePageNumber;
    @Column(name="restriction_office_name")
    private String restrictionOfficeName;
    @Column(name="restriction_office_address")
    private String restrictionOfficeAddress;
    @Column(name="reference_nr")
    private String refNumber;
    @Column(name="recordation")
    private Integer refDate;
    @Column(name="notation_text")
    private String remarks;
    
    public RestrictionRrr() {
        super();
    }

    public String getBundleNumber() {
        return bundleNumber;
    }

    public void setBundleNumber(String bundleNumber) {
        this.bundleNumber = bundleNumber;
    }

    public String getBundlePageNumber() {
        return bundlePageNumber;
    }

    public void setBundlePageNumber(String bundlePageNumber) {
        this.bundlePageNumber = bundlePageNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRefDate() {
        return refDate;
    }

    public void setRefDate(Integer refDate) {
        this.refDate = refDate;
    }

    public String getRefNumber() {
        return refNumber;
    }

    public void setRefNumber(String refNumber) {
        this.refNumber = refNumber;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRestrictionOfficeAddress() {
        return restrictionOfficeAddress;
    }

    public void setRestrictionOfficeAddress(String restrictionOfficeAddress) {
        this.restrictionOfficeAddress = restrictionOfficeAddress;
    }

    public String getRestrictionOfficeName() {
        return restrictionOfficeName;
    }

    public void setRestrictionOfficeName(String restrictionOfficeName) {
        this.restrictionOfficeName = restrictionOfficeName;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
}
