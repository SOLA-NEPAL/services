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
package org.sola.services.ejb.search.repository.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import org.sola.services.common.repository.entities.AbstractEntity;

public class BaUnitSearchResult extends AbstractEntity {

    public static final String SEARCH_PARAM_OFFICE_CODE = "officeCode";
    public static final String SEARCH_PARAM_BA_UNIT_ID = "baUnitId";
    public static final String SELECT_PART =
            "SELECT DISTINCT b.id, b.name, b.name_firstpart, b.name_lastpart, b.status_code, b.office_code, b.fy_code, "
            + "(SELECT string_agg(COALESCE(p.name, '') || ' ' || COALESCE(p.last_name, ''), '::::') "
            + "FROM administrative.rrr rrr INNER JOIN (administrative.party_for_rrr pr "
            + "INNER JOIN party.party p ON pr.party_id = p.id) ON rrr.id = pr.rrr_id "
            + "WHERE rrr.status_code = 'current' AND rrr.ba_unit_id = b.id) AS rightholders, "
            + "l.id as loc_id, m.id as moth_id, l.pana_no as pana_no, m.mothluj_no as moth_no, a.ward_no, "
            + "a.vdc_code, co.parcel_no, msh.map_number, co.map_sheet_id, '' as action "
            + "FROM (administrative.ba_unit b LEFT JOIN (administrative.rrr r inner join "
            + "(administrative.loc l INNER JOIN administrative.moth m on l.moth_id=m.id) on r.loc_id=l.id) "
            + "ON b.id = r.ba_unit_id and r.status_code='current') LEFT JOIN "
            + "((cadastre.cadastre_object co LEFT JOIN cadastre.map_sheet msh on co.map_sheet_id=msh.id) "
            + "LEFT JOIN (address.address a INNER JOIN address.vdc vdc on a.vdc_code = vdc.code) "
            + "on co.address_id = a.id) on b.cadastre_object_id = co.id ";
    
    public static final String PARAM_TRANSACTION_ID = "transactionId";
    public static final String QUERY_WITH_ACTION =
            "SELECT m.*, (CASE WHEN m.status_code='pending' THEN 'new' ELSE "
            + "(CASE WHEN (SELECT COUNT(1)>0 FROM administrative.ba_unit_target bt WHERE bt.ba_unit_id = m.id AND bt.transaction_id=#{" + PARAM_TRANSACTION_ID + "}) THEN 'terminate' ELSE "
            + "(CASE WHEN (SELECT COUNT(1)>0 FROM administrative.required_relationship_baunit br WHERE br.from_ba_unit_id = m.id AND br.transaction_id=#{" + PARAM_TRANSACTION_ID + "}) THEN 'split' ELSE "
            + "(CASE WHEN (SELECT COUNT(1)>0 FROM administrative.rrr rrr WHERE rrr.ba_unit_id = m.id AND rrr.status_code = 'pending' AND rrr.transaction_id=#{" + PARAM_TRANSACTION_ID + "}) THEN 'change' "
            + "ELSE '' END) END) END) END) as action "
            + "FROM"
            + "("
            + "SELECT DISTINCT b.id, b.name, b.name_firstpart, b.name_lastpart, b.status_code, b.office_code, b.fy_code, "
            + "(SELECT string_agg(COALESCE(p.name, '') || ' ' || COALESCE(p.last_name, ''), '::::') "
            + "FROM administrative.rrr rrr INNER JOIN (administrative.party_for_rrr pr "
            + "INNER JOIN party.party p ON pr.party_id = p.id) ON rrr.id = pr.rrr_id "
            + "WHERE rrr.status_code = 'current' AND rrr.ba_unit_id = b.id) AS rightholders, "
            + "(CASE WHEN r.status_code IS NULL THEN l2.id ELSE l.id END) as loc_id, "
            + "(CASE WHEN r.status_code IS NULL THEN m2.id ELSE m.id END) as moth_id, "
            + "(CASE WHEN r.status_code IS NULL THEN l2.pana_no ELSE l.pana_no END) as pana_no, "
            + "(CASE WHEN r.status_code IS NULL THEN m2.mothluj_no ELSE m.mothluj_no END) as moth_no, a.ward_no, "
            + "a.vdc_code, co.parcel_no, msh.map_number, co.map_sheet_id "
            + "FROM ((administrative.ba_unit b LEFT JOIN (administrative.rrr r inner join "
            + "(administrative.loc l INNER JOIN administrative.moth m on l.moth_id=m.id) on r.loc_id=l.id) "
            + "ON b.id = r.ba_unit_id and r.status_code='current') LEFT JOIN (administrative.rrr r2 inner join "
            + "(administrative.loc l2 INNER JOIN administrative.moth m2 on l2.moth_id=m2.id) on r2.loc_id=l2.id) "
            + "ON b.id = r2.ba_unit_id and r2.status_code='pending') LEFT JOIN "
            + "((cadastre.cadastre_object co LEFT JOIN cadastre.map_sheet msh on co.map_sheet_id=msh.id) "
            + "LEFT JOIN (address.address a INNER JOIN address.vdc vdc on a.vdc_code = vdc.code) "
            + "on co.address_id = a.id) on b.cadastre_object_id = co.id "
            + "WHERE b.id IN (SELECT id FROM administrative.ba_unit bu WHERE bu.transaction_id=#{" + PARAM_TRANSACTION_ID + "} UNION "
            + "SELECT but.ba_unit_id as id FROM administrative.ba_unit_target but WHERE but.transaction_id=#{" + PARAM_TRANSACTION_ID + "} UNION "
            + "SELECT bur.from_ba_unit_id as id FROM administrative.required_relationship_baunit bur WHERE bur.transaction_id=#{" + PARAM_TRANSACTION_ID + "} UNION "
            + "SELECT rr.ba_unit_id as id FROM administrative.rrr rr WHERE rr.transaction_id=#{" + PARAM_TRANSACTION_ID + "}) "
            + "AND b.office_code = #{" + SEARCH_PARAM_OFFICE_CODE + "}"
            + ") m "
            + "ORDER BY action";
    
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
    @Column(name = "office_code", updatable = false)
    private String officeCode;
    @Column(name = "fy_code")
    private String fiscalYearCode;
    @Column(name = "loc_id")
    private String locId;
    @Column(name = "moth_id")
    private String mothId;
    @Column(name = "pana_no")
    private String panaNo;
    @Column(name = "moth_no")
    private String mothNo;
    @Column(name = "ward_no")
    private String wardNo;
    @Column(name = "vdc_code")
    private String vdcCode;
    @Column(name = "parcel_no")
    private String parcelNo;
    @Column(name = "map_number")
    private String mapNumber;
    @Column(name = "map_sheet_id")
    private String mapSheetId;
    @Column(name = "action")
    private String action;

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

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getMothId() {
        return mothId;
    }

    public void setMothId(String mothId) {
        this.mothId = mothId;
    }

    public String getMothNo() {
        return mothNo;
    }

    public void setMothNo(String mothNo) {
        this.mothNo = mothNo;
    }

    public String getPanaNo() {
        return panaNo;
    }

    public void setPanaNo(String panaNo) {
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

}
