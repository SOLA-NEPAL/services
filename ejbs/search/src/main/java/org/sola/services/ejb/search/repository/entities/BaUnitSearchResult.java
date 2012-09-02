/**
 * ******************************************************************************************
 * Copyright (C) 2012 - Food and Agriculture Organization of the United Nations (FAO).
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice,this list
 *       of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice,this list
 *       of conditions and the following disclaimer in the documentation and/or other
 *       materials provided with the distribution.
 *    3. Neither the name of FAO nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,STRICT LIABILITY,OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
package org.sola.services.ejb.search.repository.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import org.sola.services.common.repository.entities.AbstractEntity;

public class BaUnitSearchResult extends AbstractEntity {

    public static final String SEARCH_PARAM_OFFICE_CODE = "officeCode";
    public static final String SEARCH_PARAM_BA_UNIT_ID = "baUnitId";
    
    public static final String SELECT_PART = 
            "SELECT b.id, b.name, b.name_firstpart, b.name_lastpart, b.status_code, b.office_code, b.fy_code, "
            + "(SELECT string_agg(COALESCE(p.name, '') || ' ' || COALESCE(p.last_name, ''), '::::') "
            + "FROM administrative.rrr rrr INNER JOIN (administrative.party_for_rrr pr "
            + "INNER JOIN party.party p ON pr.party_id = p.id) ON rrr.id = pr.rrr_id "
            + "WHERE rrr.status_code = 'current' AND rrr.ba_unit_id = b.id) AS rightholders, "
            + "l.id as loc_id, m.id as moth_id, l.pana_no as pana_no, m.mothluj_no as moth_no, a.ward_no, "
            + "a.vdc_code, co.parcel_no, msh.map_number, co.map_sheet_id "
            + "FROM (administrative.ba_unit b LEFT JOIN (administrative.rrr r inner join "
            + "(administrative.loc l INNER JOIN administrative.moth m on l.moth_id=m.id) on r.loc_id=l.id) "
            + "ON b.id = r.ba_unit_id and r.status_code='current') LEFT JOIN "
            + "((cadastre.cadastre_object co LEFT JOIN cadastre.map_sheet msh on co.map_sheet_id=msh.id) "
            + "LEFT JOIN (address.address a INNER JOIN address.vdc vdc on a.vdc_code = vdc.code) "
            + "on co.address_id = a.id) on b.cadastre_object_id = co.id ";
    
//    (SELECT string_agg(COALESCE(p.name, '') || ' ' || COALESCE(p.last_name, ''), '::::') "
//            + "FROM administrative.rrr rrr INNER JOIN (administrative.party_for_rrr pr "
//            + "INNER JOIN party.party p ON pr.party_id = p.id) ON rrr.id = pr.rrr_id "
//            + "WHERE rrr.status_code = 'current' AND "
//            + "(POSITION(LOWER(#{ownerName}) IN LOWER(COALESCE(p.name, ''))) > 0 OR "
//            + "POSITION(LOWER(#{ownerName}) IN LOWER(COALESCE(p.last_name, ''))) > 0) AND rrr.ba_unit_id = b.id)"
    
    public static final String SEARCH_QUERY = SELECT_PART
            + "WHERE POSITION(LOWER(#{nameFirstPart}) IN LOWER(COALESCE(b.name_firstpart, ''))) > 0 "
            + "AND POSITION(LOWER(#{nameLastPart}) IN LOWER(COALESCE(b.name_lastpart, ''))) > 0 "
            + "AND b.office_code = #{" + SEARCH_PARAM_OFFICE_CODE + "} AND b.status_code != 'pending' "
            + "ORDER BY b.name_firstpart, b.name_lastpart "
            + "LIMIT 101";
    
    public static final String SEARCH_BY_ID_QUERY = SELECT_PART
            + "WHERE b.id = #{" + SEARCH_PARAM_BA_UNIT_ID + "} AND b.office_code = #{" + SEARCH_PARAM_OFFICE_CODE + "}";
        
    @Id
    @Column
    private String id;
    @Column
    private String name;
    @Column(name = "name_firstpart")
    private String nameFirstPart;
    @Column(name = "name_lastpart")
    private String nameLastPart;
    @Column(name = "status_code")
    private String statusCode;
    @Column
    private String rightholders;
    @Column(name="office_code", updatable=false)
    private String officeCode;
    @Column(name="fy_code")
    private String fiscalYearCode;
    @Column(name = "loc_id")
    private boolean locId;
    @Column(name = "moth_id")
    private boolean mothId;
    @Column(name = "pana_no")
    private boolean panaNo;
    @Column(name = "moth_no")
    private boolean mothNo;
    @Column(name="ward_no")
    private String wardNo;
    @Column(name="vdc_code")
    private String vdcCode;
    @Column(name="parcel_no")
    private String parcelNo;
    @Column(name="map_number")
    private String mapNumber;
    @Column(name="map_sheet_id")
    private String mapSheetId;
    
    public BaUnitSearchResult() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getRightholders() {
        return rightholders;
    }

    public void setRightholders(String rightholders) {
        this.rightholders = rightholders;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
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

    public boolean isLocId() {
        return locId;
    }

    public void setLocId(boolean locId) {
        this.locId = locId;
    }

    public boolean isMothId() {
        return mothId;
    }

    public void setMothId(boolean mothId) {
        this.mothId = mothId;
    }

    public boolean isMothNo() {
        return mothNo;
    }

    public void setMothNo(boolean mothNo) {
        this.mothNo = mothNo;
    }

    public boolean isPanaNo() {
        return panaNo;
    }

    public void setPanaNo(boolean panaNo) {
        this.panaNo = panaNo;
    }

    public String getMapNumber() {
        return mapNumber;
    }

    public void setMapNumber(String mapNumber) {
        this.mapNumber = mapNumber;
    }

    public String getMapSheetId() {
        return mapSheetId;
    }

    public void setMapSheetId(String mapSheetId) {
        this.mapSheetId = mapSheetId;
    }

    public String getParcelNo() {
        return parcelNo;
    }

    public void setParcelNo(String parcelNo) {
        this.parcelNo = parcelNo;
    }

    public String getVdcCode() {
        return vdcCode;
    }

    public void setVdcCode(String vdcCode) {
        this.vdcCode = vdcCode;
    }

    public String getWardNo() {
        return wardNo;
    }

    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
    }
}
