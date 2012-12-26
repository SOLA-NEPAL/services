/**
 * ******************************************************************************************
 * Copyright (C) 2012 - Food and Agriculture Organization of the United Nations
 * (FAO). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,this
 * list of conditions and the following disclaimer. 2. Redistributions in binary
 * form must reproduce the above copyright notice,this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. 3. Neither the name of FAO nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT,STRICT LIABILITY,OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.cadastre.repository.entities;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.LocalInfo;
import org.sola.services.common.repository.AccessFunctions;
import org.sola.services.common.repository.ChildEntity;
import org.sola.services.common.repository.ExternalEJB;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;
import org.sola.services.common.repository.entities.AbstractVersionedEntity;
import org.sola.services.ejb.address.businesslogic.AddressEJBLocal;
import org.sola.services.ejb.address.repository.entities.Address;

/**
 *
 * @author soladev
 */
@Table(name = "cadastre_object", schema = "cadastre")
public class CadastreObject extends AbstractVersionedEntity {
    private static final String QUERY_WHERE_BY_OFFICE = "(office_code=#{"
            + AbstractReadOnlyEntity.PARAM_OFFICE_CODE + "} OR office_code IS NULL)";
    public static final String QUERY_WHERE_SEARCHBYPARTS = "status_code= 'current' and "
            + "compare_strings(#{search_string}, name_firstpart || ' ' || name_lastpart) "
            + "AND " + QUERY_WHERE_BY_OFFICE;
    public static final String QUERY_WHERE_SEARCHBY_EXACT_PARTS = "status_code= 'current' and "
            + "name_firstpart=#{firstpart} and name_lastpart=#{lastpart}"
            + " and " + QUERY_WHERE_BY_OFFICE;
    public static final String QUERY_WHERE_SEARCHBYPARTS_PENDING = "status_code= 'pending' and "
            + "compare_strings(#{search_string}, name_firstpart || ' ' || name_lastpart) "
            + "AND " + QUERY_WHERE_BY_OFFICE;    
    public static final String QUERY_WHERE_SEARCHBYPOINT = "status_code= 'current' and "
            + "ST_Intersects(geom_polygon, SetSRID(ST_Point(#{x}, #{y}), #{srid})) "
            + "AND " + QUERY_WHERE_BY_OFFICE;
    public static final String QUERY_WHERE_SEARCHBYBAUNIT = "status_code= 'current' and id in "
            + " (select cadastre_object_id from administrative.ba_unit "
            + "where id = #{ba_unit_id}) AND " + QUERY_WHERE_BY_OFFICE;
    public static final String QUERY_WHERE_SEARCHBYSERVICE = "status_code= 'current' "
            + "and transaction_id in "
            + " (select id from transaction.transaction where from_service_id = #{service_id}) "
            + "AND " + QUERY_WHERE_BY_OFFICE;
    public static final String QUERY_WHERE_SEARCHBYTRANSACTION =
            "transaction_id = #{transaction_id} AND " + QUERY_WHERE_BY_OFFICE;
    public static final String QUERY_WHERE_SEARCHBYGEOM = "status_code= 'current' and "
            + "ST_DWithin(geom_polygon, get_geometry_with_srid(#{geom}), "
            + "system.get_setting('map-tolerance')::double precision) AND " + QUERY_WHERE_BY_OFFICE;
    //By Kabindra
    //--------------------------------------------------------------------------
    public static final String QUERY_WHERE_SEARCHBY_STRING_INTERSECTION = "status_code= 'current' and "
            + "st_intersects(geom_polygon, setsrid(st_geomfromtext(#{geom}), #{srid})) AND " + QUERY_WHERE_BY_OFFICE;
    public static final String QUERY_WHERE_SEARCHBY_BYTE_INTERSECTION = "status_code= 'current' and "
            + "st_intersects(geom_polygon, setsrid(st_geomfromewkb(#{geom}" + "::geometry)" + ", #{srid})) "
            + "AND " + QUERY_WHERE_BY_OFFICE;
    //--------------------------------------------------------------------------
    public static final String MAP_SHEET_CODE_PARAM = "mapSheetCode";
    public static final String GET_CADASTRE_BY_MAPSHEET_CODE = "map_sheet_id=#{" + MAP_SHEET_CODE_PARAM + "} "
            + "AND " + QUERY_WHERE_BY_OFFICE;
    public static final String VDC_PARAM = "vdc";
    public static final String WARD_NO_PARAM = "wardno";
    public static final String GET_BY_VDC_AND_WARD_NO = "vdc=#{" + VDC_PARAM + "} and wardno=#{" + WARD_NO_PARAM + "} "
            + "AND " + QUERY_WHERE_BY_OFFICE;
    public static final String PARCEL_NO_PARAM = "parcelno";
    public static final String GET_BY_VDC_AND_WARD_NO_PARCEL_NO = "vdc=#{" + VDC_PARAM + "} and wardno=#{" + WARD_NO_PARAM + "} and parcel_no=#{"
            + PARCEL_NO_PARAM + "} AND " + QUERY_WHERE_BY_OFFICE;
    public static final String GET_BY_MAPSHEET_AND_PARCELNO = "map_sheet_id=#{" + MAP_SHEET_CODE_PARAM
            + "} and parcel_no=#{" + PARCEL_NO_PARAM + "} AND " + QUERY_WHERE_BY_OFFICE;
    //full query by Kabindra
    private static final String QUERY_WHERE_BY_OFFICE1 = "(p.office_code=#{"
            + AbstractReadOnlyEntity.PARAM_OFFICE_CODE + "} OR office_code IS NULL)";
    public static final String GET_BY_ADMIN_BOUNDARY_SELECT_PART =
            "p.id, p.type_code, p.map_sheet_id, p.fy_code, p.approval_datetime, p.historic_datetime,"
            + "p.name_firstpart, p.name_lastpart, p.status_code, p.transaction_id, p.dataset_id, "
            + "st_asewkb(p.geom_polygon) as geom_polygon, p.parcel_no, p.parcel_note, p.land_type_code, p.land_use_code, p.land_class_code,p.address_id,"
            + "p.rowversion, p.change_user, p.rowidentifier, p.office_code";
    public static final String GET_BY_ADMIN_BOUNDARY_FROM_PART =
            "cadastre.cadastre_object as p,"
            //+ "cadastre.spatial_unit_address as pa,"
            + "address.address as a";
    public static final String GET_BY_ADMIN_BOUNDARY_WHERE_PART =
            " p.id=pa.spatial_unit_id and "
            + " pa.address_id=a.id and "
            + " a.vdc_code=#{" + VDC_PARAM
            + "} and a.ward_no=#{" + WARD_NO_PARAM
            + "} and p.parcel_no=#{" + PARCEL_NO_PARAM + "} "
            + "and " + QUERY_WHERE_BY_OFFICE1;
    //list ward numbers.
    public static final String GET_BY_WARD_IN_VDC =
            " select distinct a.ward_no "
            + " from cadastre.cadastre_object as p,"
            + "address.address as a"
            + " where p.address_id=a.id and "
            + " a.vdc_code=#{" + VDC_PARAM + "} "
            + "and " + QUERY_WHERE_BY_OFFICE1;
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "type_code")
    private String typeCode;
    @Column(name = "map_sheet_id")
    private String mapSheetId;
    @Column(name = "map_sheet_id2")
    private String mapSheetId2;
    @Column(name = "map_sheet_id3")
    private String mapSheetId3;
    @Column(name = "map_sheet_id4")
    private String mapSheetId4;
    @Column(name = "building_unit_type_code")
    private String buildingUnitTypeCode;
    @Column(name = "approval_datetime")
    private Date approvalDatetime;
    @Column(name = "historic_datetime")
    private Date historicDatetime;
   // @Column(name = "name_firstpart", updatable = false)
     @Column(name = "name_firstpart", updatable = true)
    private String nameFirstPart;
    //@Column(name = "name_lastpart", updatable = false)
     @Column(name = "name_lastpart", updatable = true)
    private String nameLastPart;
    @Column(name = "status_code", insertable = false, updatable = false)
    private String statusCode;
    @Column(name = "transaction_id", updatable = false)
    private String transactionId;
    @Column(name = "geom_polygon")
    @AccessFunctions(onSelect = "st_asewkb(geom_polygon)",
    onChange = "get_geometry_with_srid(#{geomPolygon})")
    private byte[] geomPolygon;
    @Column(name = "parcel_no")
    private String parcelno;
    @Column(name = "official_area")
    private BigDecimal officialArea;
    @Column(name = "area_unit_type_code")
    private String areaUnitTypeCode;
    @Column(name = "parcel_note")
    private String parcelNote;
    @Column(name = "land_type_code")
    private String landTypeCode;
    @Column(name = "land_use_code")
    private String landUseCode;
    @Column(name = "land_class_code")
    private String landClassCode;
    @Column(name = "address_id")
    private String addressId;
    @ExternalEJB(ejbLocalClass = AddressEJBLocal.class, loadMethod = "getAddress", saveMethod = "saveAddress")
    @ChildEntity(childIdField = "addressId")
    private Address address;
    @ChildEntity(childIdField = "mapSheetId", readOnly = true)
    private MapSheet mapSheet;
    @ChildEntity(childIdField = "mapSheetId2", readOnly = true)
    private MapSheet mapSheet2;
    @ChildEntity(childIdField = "mapSheetId3", readOnly = true)
    private MapSheet mapSheet3;
    @ChildEntity(childIdField = "mapSheetId4", readOnly = true)
    private MapSheet mapSheet4;
    @Column(name = "office_code", updatable = false)
    private String officeCode;
    @Column(name = "fy_code", updatable = false)
    private String fiscalYearCode;
    @Column(name="dataset_id")
    private String datasetId;
    
    public String getMapSheetId() {
        return mapSheetId;
    }

    public void setMapSheetId(String mapSheetId) {
        this.mapSheetId = mapSheetId;
    }

    public String getParcelNote() {
        return parcelNote;
    }

    public void setParcelNote(String parcelNote) {
        this.parcelNote = parcelNote;
    }

    public String getParcelno() {
        return parcelno;
    }

    public void setParcelno(String parcelno) {
        this.parcelno = parcelno;
    }

    public CadastreObject() {
        super();
    }

    public String getId() {
        id = id == null ? generateId() : id;
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Date getApprovalDatetime() {
        return approvalDatetime;
    }

    public void setApprovalDatetime(Date approvalDatetime) {
        this.approvalDatetime = approvalDatetime;
    }

    public byte[] getGeomPolygon() {
        return geomPolygon;
    }

    public void setGeomPolygon(byte[] geomPolygon) {
        this.geomPolygon = geomPolygon;
    }

    public Date getHistoricDatetime() {
        return historicDatetime;
    }

    public void setHistoricDatetime(Date historicDatetime) {
        this.historicDatetime = historicDatetime;
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
        if (address != null) {
            this.setAddressId(address.getId());
        }
    }

    public String getLandClassCode() {
        return landClassCode;
    }

    public void setLandClassCode(String landClassCode) {
        this.landClassCode = landClassCode;
    }

    public String getLandUseCode() {
        return landUseCode;
    }

    public void setLandUseCode(String landUseCode) {
        this.landUseCode = landUseCode;
    }

    public String getLandTypeCode() {
        return landTypeCode;
    }

    public void setLandTypeCode(String landTypeCode) {
        this.landTypeCode = landTypeCode;
    }

    public MapSheet getMapSheet() {
        return mapSheet;
    }

    public void setMapSheet(MapSheet mapSheet) {
        this.mapSheet = mapSheet;
        if (mapSheet != null) {
            this.setMapSheetId(mapSheet.getId());
        }
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public String getFiscalYearCode() {
        return fiscalYearCode;
    }

    public void setFiscalYearCode(String fiscalYearCode) {
        this.fiscalYearCode = fiscalYearCode;
    }

    public String getMapSheetId2() {
        return mapSheetId2;
    }

    public void setMapSheetId2(String mapSheetId2) {
        this.mapSheetId2 = mapSheetId2;
    }

    public String getMapSheetId3() {
        return mapSheetId3;
    }

    public void setMapSheetId3(String mapSheetId3) {
        this.mapSheetId3 = mapSheetId3;
    }

    public String getMapSheetId4() {
        return mapSheetId4;
    }

    public void setMapSheetId4(String mapSheetId4) {
        this.mapSheetId4 = mapSheetId4;
    }

    public MapSheet getMapSheet2() {
        return mapSheet2;
    }

    public void setMapSheet2(MapSheet mapSheet2) {
        this.mapSheet2 = mapSheet2;
    }

    public MapSheet getMapSheet3() {
        return mapSheet3;
    }

    public void setMapSheet3(MapSheet mapSheet3) {
        this.mapSheet3 = mapSheet3;
    }

    public MapSheet getMapSheet4() {
        return mapSheet4;
    }

    public void setMapSheet4(MapSheet mapSheet4) {
        this.mapSheet4 = mapSheet4;
    }

    public String getBuildingUnitTypeCode() {
        return buildingUnitTypeCode;
    }

    public void setBuildingUnitTypeCode(String buildingUnitTypeCode) {
        this.buildingUnitTypeCode = buildingUnitTypeCode;
    }

    public BigDecimal getOfficialArea() {
        return officialArea;
    }

    public void setOfficialArea(BigDecimal officialArea) {
        this.officialArea = officialArea;
    }

    public String getAreaUnitTypeCode() {
        return areaUnitTypeCode;
    }

    public void setAreaUnitTypeCode(String areaUnitTypeCode) {
        this.areaUnitTypeCode = areaUnitTypeCode;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    @Override
    public void preSave() {
        if (this.isNew() && this.getTransactionId() == null) {
            setTransactionId(LocalInfo.getTransactionId());
        }
        if (isNew()) {
            // Check first/last part names
            String firstPart = getNameFirstPart() == null ? "" : getNameFirstPart();
            String lastPart = getNameLastPart() == null ? "" : getNameLastPart();
            if (!firstPart.equals(generateNameFirstPart())) {
                setNameFirstPart(generateNameFirstPart());
            }
            if (!lastPart.equals(generateNameLastPart())) {
                setNameLastPart(generateNameLastPart());
            }
        }
        super.preSave();
    }

    public String generateNameFirstPart() {
        if (getAddress() == null || getAddress().getVdcCode() == null
                || getAddress().getWardNo() == null
                || getMapSheet().getMapNumber() == null) {
            return null;
        }
        return getAddress().getVdcCode() + "-" + getAddress().getWardNo()
                + "-" + getMapSheet().getMapNumber();
    }

    public String generateNameLastPart() {
        if (getParcelno() == null) {
            return null;
        }
        return getParcelno();
    }
}
