/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.search.repository.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

/**
 *
 * @author Kumar
 */
@Entity
@Table(name = "cadastre_object", schema = "cadastre")
public class ParcelSearchResult extends AbstractReadOnlyEntity {

    public static final String MAP_SHEET_CODE_PARAM = "mapSheetCode";
    public static final String VDC_PARAM = "vdcCode";
    public static final String WARD_NO_PARAM = "wardNo";
    public static final String PARCEL_NO_PARAM = "parcelNo";
    public static final String PARCEL_SEARCH_QUERY = "Select distinct pcl.id,pcl.map_sheet_id,pcl.parcel_no,pcl.name_firstpart,pcl.name_lastpart "
            + "from cadastre.cadastre_object as pcl left join address.address as ad on pcl.addressid=ad.id "
            + "WHERE (ad.vdc_code = #{" + VDC_PARAM + "} OR #{" + VDC_PARAM + "}='') "
           // + "And (mps.map_sheet_id = #{" + MAP_SHEET_CODE_PARAM + "} OR #{" + MAP_SHEET_CODE_PARAM + "}='') "
            + "And (COALESCE(ad.ward_no, '') = #{" + WARD_NO_PARAM + "} OR #{" + WARD_NO_PARAM + "}='') "
            + "And (COALESCE(pcl.parcel_no, 0) = #{" + PARCEL_NO_PARAM + "} OR #{" + PARCEL_NO_PARAM + "}=0) "
            + "LIMIT 101";
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "map_sheet_id")
    private String mapsheetId;
    @Column(name = "name_firstpart")
    private String firstName;
    @Column(name = "name_lastpart")
    private String lastName;
    @Column(name = "parcel_no")
    private int parcelno;

    public ParcelSearchResult() {
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

    public int getParcelno() {
        return parcelno;
    }

    public void setParcelno(int parcelno) {
        this.parcelno = parcelno;
    }
}
