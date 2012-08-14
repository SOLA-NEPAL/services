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

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.LocalInfo;
import org.sola.services.common.repository.AccessFunctions;
import org.sola.services.common.repository.ChildEntity;
import org.sola.services.common.repository.ChildEntityList;
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
    //<editor-fold defaultstate="collapsed" desc="By Kumar">
    //***********************************************************************************************************
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
    //***********************************************************************************************************
    //</editor-fold>   
    //full query by Kabindra
    private static final String QUERY_WHERE_BY_OFFICE1 = "(p.office_code=#{"
            + AbstractReadOnlyEntity.PARAM_OFFICE_CODE + "} OR office_code IS NULL)";
    public static final String GET_BY_ADMIN_BOUNDARY_SELECT_PART =
            "p.id, p.type_code, p.map_sheet_id, p.approval_datetime, p.historic_datetime,"
            + "p.source_reference, p.name_firstpart, p.name_lastpart, p.status_code, p.transaction_id,"
            + "st_asewkb(p.geom_polygon) as geom_polygon, p.parcel_no, p.parcel_note, p.parcel_typecode, p.land_usecode, p.land_classcode,p.addressid, p.guthi_namecode,"
            + "p.rowversion, p.change_user, p.rowidentifier, p.office_code";
    public static final String GET_BY_ADMIN_BOUNDARY_FROM_PART =
            "cadastre.cadastre_object as p,"
           + "cadastre.spatial_unit_address as pa,"
            + "address.address as a";
    public static final String GET_BY_ADMIN_BOUNDARY_WHERE_PART =
          " p.id=pa.spatial_unit_id and "            
            +" pa.address_id=a.id and " 
            +" a.vdc_code=#{" + VDC_PARAM
            + "} and a.ward_no=#{" + WARD_NO_PARAM
            + "} and p.parcel_no=#{" + PARCEL_NO_PARAM + "} "
            + "and " + QUERY_WHERE_BY_OFFICE1;
    //list ward numbers.
    public static final String GET_BY_WARD_IN_VDC =
            " select distinct a.ward_no "
            + " from cadastre.cadastre_object as p,"
            + "cadastre.spatial_unit_address as pa,"
            + "address.address as a"
            + " where p.id=pa.spatial_unit_id and "
            + " pa.address_id=a.id and "
            + " a.vdc_code=#{" + VDC_PARAM + "} "
            + "and " + QUERY_WHERE_BY_OFFICE1;
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "type_code")
    private String typeCode;
    @Column(name = "map_sheet_id")
    private String mapSheetCode;
    @Column(name = "approval_datetime")
    private Date approvalDatetime;
    @Column(name = "historic_datetime")
    private Date historicDatetime;
    @Column(name = "source_reference")
    private String sourceReference;
    @Column(name = "name_firstpart")
    private String nameFirstpart;
    @Column(name = "name_lastpart")
    private String nameLastpart;
    @Column(name = "status_code", insertable = false, updatable = false)
    private String statusCode;
    @Column(name = "transaction_id", updatable = false)
    private String transactionId;
    @Column(name = "geom_polygon")
    @AccessFunctions(onSelect = "st_asewkb(geom_polygon)",
    onChange = "get_geometry_with_srid(#{geomPolygon})")
    private byte[] geomPolygon;
    @ChildEntityList(parentIdField = "spatialUnitId")
    private List<SpatialValueArea> spatialValueAreaList;
    //Additional field required for SAEx application.
    @Column(name = "parcel_no")
    private int parcelno;
    @Column(name = "parcel_note")
    private String parcelNote;
    @Column(name = "parcel_typecode")
    private String parcelTypeCode;
    @Column(name = "land_usecode")
    private String landUseCode;
    @Column(name = "land_classcode")
    private String landClassCode;
    @Column(name = "guthi_namecode")
    private String guthiNameCode;
    @Column(name = "addressid")
    private String addressId;
    @ExternalEJB(ejbLocalClass = AddressEJBLocal.class, loadMethod = "getAddress", saveMethod = "saveAddress")
    @ChildEntity(childIdField = "addressId", readOnly = true)
    private Address address;
    @ChildEntity(childIdField = "mapSheetCode", readOnly = true)
    private MapSheet mapSheet;
    @Column(name = "office_code", updatable = false)
    private String officeCode;

    public String getMapSheetCode() {
        return mapSheetCode;
    }

    public void setMapSheetCode(String mapSheetCode) {
        this.mapSheetCode = mapSheetCode;
    }

    public String getParcelNote() {
        return parcelNote;
    }

    public void setParcelNote(String parcelNote) {
        this.parcelNote = parcelNote;
    }

    public int getParcelno() {
        return parcelno;
    }

    public void setParcelno(int parcelno) {
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

    public String getNameFirstpart() {
        return nameFirstpart;
    }

    public void setNameFirstpart(String nameFirstpart) {
        this.nameFirstpart = nameFirstpart;
    }

    public String getNameLastpart() {
        return nameLastpart;
    }

    public void setNameLastpart(String nameLastpart) {
        this.nameLastpart = nameLastpart;
    }

    public String getSourceReference() {
        return sourceReference;
    }

    public void setSourceReference(String sourceReference) {
        this.sourceReference = sourceReference;
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

    public String getGuthiNameCode() {
        return guthiNameCode;
    }

    public void setGuthiNameCode(String guthiNameCode) {
        this.guthiNameCode = guthiNameCode;
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

    public String getParcelTypeCode() {
        return parcelTypeCode;
    }

    public void setParcelTypeCode(String parcelTypeCode) {
        this.parcelTypeCode = parcelTypeCode;
    }

    public List<SpatialValueArea> getSpatialValueAreaList() {
        // Loaded eagerly by the CommonRepository
        return spatialValueAreaList;
    }

    public void setSpatialValueAreaList(List<SpatialValueArea> spatialValueAreaList) {
        this.spatialValueAreaList = spatialValueAreaList;
    }

    public MapSheet getMapSheet() {
        return mapSheet;
    }

    public void setMapSheet(MapSheet mapSheet) {
        this.mapSheet = mapSheet;
        if (mapSheet != null) {
            this.setMapSheetCode(mapSheet.getId());
        }
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    @Override
    public void preSave() {
        if (this.isNew() && this.getTransactionId() == null) {
            setTransactionId(LocalInfo.getTransactionId());
        }
        super.preSave();
    }
}
