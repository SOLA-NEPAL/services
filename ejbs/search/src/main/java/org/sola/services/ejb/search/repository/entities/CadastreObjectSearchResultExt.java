package org.sola.services.ejb.search.repository.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

@Entity
@Table(name = "cadastre_object", schema = "cadastre")
public class CadastreObjectSearchResultExt extends AbstractReadOnlyEntity {

    public static final String MAP_SHEET_CODE_PARAM = "mapSheetCode";
    public static final String VDC_PARAM = "vdcCode";
    public static final String WARD_NO_PARAM = "wardNo";
    public static final String PARCEL_NO_PARAM = "parcelNo";
    public static final String LANGUAGE_CODE_PARAM = "languageCode";
    public static final String PARCEL_SEARCH_QUERY =
            "SELECT DISTINCT pcl.id, pcl.map_sheet_id, pcl.map_sheet_id2, pcl.map_sheet_id3, pcl.map_sheet_id4, "
            + "pcl.parcel_no, pcl.name_firstpart, pcl.name_lastpart, m1.map_number, m2.map_number as map_number2, "
            + "m3.map_number as map_number3, m4.map_number as map_number4, ad.ward_no, ad.vdc_code, "
            + "get_translation(vdc.display_value, #{" + LANGUAGE_CODE_PARAM + "}) as vdc_name "
            + "FROM ((((cadastre.cadastre_object as pcl LEFT JOIN cadastre.map_sheet m1 ON pcl.map_sheet_id = m1.id) "
            + "LEFT JOIN cadastre.map_sheet m2 ON pcl.map_sheet_id2 = m2.id) "
            + "LEFT JOIN cadastre.map_sheet m3 ON pcl.map_sheet_id3 = m3.id) "
            + "LEFT JOIN cadastre.map_sheet m4 ON pcl.map_sheet_id4 = m4.id) "
            + "LEFT JOIN (address.address as ad INNER JOIN address.vdc vdc ON ad.vdc_code = vdc.code) "
            + "ON pcl.address_id = ad.id "
            + "WHERE (COALESCE(ad.vdc_code, '') = #{" + VDC_PARAM + "} OR #{" + VDC_PARAM + "}='') "
            + "AND (COALESCE(pcl.map_sheet_id, '') = #{" + MAP_SHEET_CODE_PARAM + "} OR "
            + "COALESCE(pcl.map_sheet_id2, '') = #{" + MAP_SHEET_CODE_PARAM + "} OR "
            + "COALESCE(pcl.map_sheet_id3, '') = #{" + MAP_SHEET_CODE_PARAM + "} OR "
            + "COALESCE(pcl.map_sheet_id4, '') = #{" + MAP_SHEET_CODE_PARAM + "} OR "
            + "#{" + MAP_SHEET_CODE_PARAM + "}='') AND pcl.status_code in ('pending', 'current') "
            + "AND (COALESCE(ad.ward_no, '') = #{" + WARD_NO_PARAM + "} OR #{" + WARD_NO_PARAM + "}='') "
            + "AND (COALESCE(pcl.parcel_no, '') = #{" + PARCEL_NO_PARAM + "} OR #{" + PARCEL_NO_PARAM + "}='') "
            + "AND (COALESCE(pcl.office_code, '') = #{" + PARAM_OFFICE_CODE + "} OR #{" + PARAM_OFFICE_CODE + "}='') "
            + "LIMIT 101";
    
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "name_firstpart")
    private String firstName;
    @Column(name = "name_lastpart")
    private String lastName;
    @Column(name = "parcel_no")
    private String parcelno;
    @Column(name = "map_sheet_id")
    private String mapsheetId;
    @Column(name = "map_sheet_id2")
    private String mapsheetId2;
    @Column(name = "map_sheet_id3")
    private String mapsheetId3;
    @Column(name = "map_sheet_id4")
    private String mapsheetId4;
    @Column(name = "map_number")
    private String mapsheetNumber;
    @Column(name = "map_number2")
    private String mapsheetNumber2;
    @Column(name = "map_number3")
    private String mapsheetNumber3;
    @Column(name = "map_number4")
    private String mapsheetNumber4;
    @Column(name = "ward_no")
    private String wardNumber;
    @Column(name = "vdc_code")
    private String vdcCode;
    @Column(name = "vdc_name")
    private String vdcName;

    public CadastreObjectSearchResultExt() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMapsheetId() {
        return mapsheetId;
    }

    public void setMapsheetId(String mapsheetId) {
        this.mapsheetId = mapsheetId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getParcelno() {
        return parcelno;
    }

    public void setParcelno(String parcelno) {
        this.parcelno = parcelno;
    }


    public String getMapsheetId2() {
        return mapsheetId2;
    }

    public void setMapsheetId2(String mapsheetId2) {
        this.mapsheetId2 = mapsheetId2;
    }

    public String getMapsheetId3() {
        return mapsheetId3;
    }

    public void setMapsheetId3(String mapsheetId3) {
        this.mapsheetId3 = mapsheetId3;
    }

    public String getMapsheetId4() {
        return mapsheetId4;
    }

    public void setMapsheetId4(String mapsheetId4) {
        this.mapsheetId4 = mapsheetId4;
    }

    public String getMapsheetNumber() {
        return mapsheetNumber;
    }

    public void setMapsheetNumber(String mapsheetNumber) {
        this.mapsheetNumber = mapsheetNumber;
    }

    public String getMapsheetNumber2() {
        return mapsheetNumber2;
    }

    public void setMapsheetNumber2(String mapsheetNumber2) {
        this.mapsheetNumber2 = mapsheetNumber2;
    }

    public String getMapsheetNumber3() {
        return mapsheetNumber3;
    }

    public void setMapsheetNumber3(String mapsheetNumber3) {
        this.mapsheetNumber3 = mapsheetNumber3;
    }

    public String getMapsheetNumber4() {
        return mapsheetNumber4;
    }

    public void setMapsheetNumber4(String mapsheetNumber4) {
        this.mapsheetNumber4 = mapsheetNumber4;
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
}
