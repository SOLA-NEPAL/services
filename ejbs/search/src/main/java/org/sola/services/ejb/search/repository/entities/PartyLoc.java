package org.sola.services.ejb.search.repository.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

public class PartyLoc extends AbstractReadOnlyEntity {

    public static final String PARAM_LANG = "lang";
    public static final String PARAM_LOC_ID = "locId";
    public static final String PARAM_STATUS = "statusCode";
//    public static final String QUERY_GET_BY_LOC =
//            "SELECT DISTINCT p.id, p.name, p.last_name, p.fathers_name, p.grandfather_name, p.id_number, p.id_issue_date, "
//            + "(select body from document.document where id = p.photo_id) as photo, "
//            + "(select body from document.document where id = p.left_finger_id) as left_fingerprint, "
//            + "(select body from document.document where id = p.right_finger_id) as right_fingerprint, "
//            + "a.vdc_code, get_translation(vdc.display_value, #{" + PARAM_LANG + "}) AS vdc_name, a.ward_no, a.street, "
//            + "get_translation(o.display_value, #{" + PARAM_LANG + "}) AS office_name "
//            + "FROM (party.party p LEFT JOIN (address.address a INNER JOIN address.vdc vdc ON a.vdc_code=vdc.code) ON p.address_id=a.id) INNER JOIN "
//            + "(administrative.party_for_rrr pr INNER JOIN "
//            + "(administrative.rrr r LEFT JOIN system.office o ON r.office_code=o.code) ON pr.rrr_id=r.id) ON p.id=pr.party_id "
//            + "WHERE r.status_code=#{" + PARAM_STATUS + "} AND r.loc_id=#{" + PARAM_LOC_ID + "}";
    
        public static final String QUERY_GET_BY_LOC =
            "SELECT DISTINCT p.id, p.name, p.last_name, p.fathers_name, p.grandfather_name, p.id_number, p.id_issue_date, "
            + "(select body from document.document where id = p.photo_id) as photo, "
            + "(select body from document.document where id = p.left_finger_id) as left_fingerprint, "
            + "(select body from document.document where id = p.right_finger_id) as right_fingerprint, "
            + "a.vdc_code, get_translation(vdc.display_value, #{" + PARAM_LANG + "}) AS vdc_name, a.ward_no, a.street, "
            + "get_translation(o.display_value, #{" + PARAM_LANG + "}) AS office_name "
            + "FROM (party.party p LEFT JOIN (address.address a INNER JOIN address.vdc vdc ON a.vdc_code=vdc.code) ON p.address_id=a.id) INNER JOIN "
            + "(administrative.party_for_rrr pr INNER JOIN "
            + "(administrative.rrr r LEFT JOIN system.office o ON r.office_code=o.code) ON pr.rrr_id=r.id) ON p.id=pr.party_id "
            + "WHERE ((#{" + PARAM_STATUS + "}='pending' AND r.status_code='pending' AND r.nr NOT IN "
            + "(SELECT nr FROM administrative.rrr WHERE ba_unit_id = r.ba_unit_id "
            + "AND r.loc_id =#{" + PARAM_LOC_ID + "} AND status_code='current' AND nr = r.nr)) "
            + "OR (#{" + PARAM_STATUS + "}='pending' AND r.status_code='current' AND r.nr NOT IN (SELECT nr FROM administrative.rrr "
            + "WHERE ba_unit_id = r.ba_unit_id AND r.loc_id =#{" + PARAM_LOC_ID + "} AND status_code='pending' AND nr = r.nr)) "
            + "OR (#{" + PARAM_STATUS + "}= 'current' AND r.status_code='current')) "             
            + "AND r.loc_id  =#{" + PARAM_LOC_ID + "}";
            

    @Id
    @Column
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "last_name")
    private String lastName;
    @Column(name="fathers_name")
    private String fatherName;
    @Column(name="grandfather_name")
    private String grandfatherName;
    @Column(name="id_issue_date")
    private Integer idIssueDate;
    @Column(name="id_number")
    private String idNumber;
    @Column(name = "photo")
    private byte[] photo;
    @Column(name = "left_fingerprint")
    private byte[] leftFingerprint;
    @Column(name = "right_fingerprint")
    private byte[] rightFingerprint;
    @Column(name = "vdc_code")
    private String vdcCode;
    @Column(name = "vdc_name")
    private String vdcName;
    @Column(name = "ward_no")
    private String wardNo;
    @Column(name = "street")
    private String street;
    @Column(name="office_name")
    private String officeName;
    
    public PartyLoc() {
        super();
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getGrandfatherName() {
        return grandfatherName;
    }

    public void setGrandfatherName(String grandfatherName) {
        this.grandfatherName = grandfatherName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIdIssueDate() {
        return idIssueDate;
    }

    public void setIdIssueDate(Integer idIssueDate) {
        this.idIssueDate = idIssueDate;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public byte[] getLeftFingerprint() {
        return leftFingerprint;
    }

    public void setLeftFingerprint(byte[] leftFingerprint) {
        this.leftFingerprint = leftFingerprint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public byte[] getRightFingerprint() {
        return rightFingerprint;
    }

    public void setRightFingerprint(byte[] rightFingerprint) {
        this.rightFingerprint = rightFingerprint;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

    public String getWardNo() {
        return wardNo;
    }

    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }
}
