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

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import org.sola.services.common.repository.entities.AbstractEntity;

public class BaUnitSearchResult extends AbstractEntity {

    public static final String PARAM_BA_UNIT_ID = "baUnitId";
    public static final String PARAM_VDC_CODE = "vdcCode";
    public static final String PARAM_WARD = "ward";
    public static final String PARAM_MAPSHEET_CODE = "mapSheetCode";
    public static final String PARAM_PARCEL_NO = "parcelNo";
    public static final String PARAM_MOTH = "moth";
    public static final String PARAM_LOC = "loc";
    public static final String PARAM_RIGHTHOLDER_ID = "rightHolderId";
    public static final String PARAM_APROVAL_DATE_FROM = "fromDate";
    public static final String PARAM_APROVAL_DATE_TO = "toDate";
    public static final String PARAM_UPTO_DATE = "upToDate";
    public static final String PARAM_FROM = "from";
    public static final String PARAM_FROM_FISCAL_YEAR = "fromFiscalYear";
    public static final String PARAM_TO_FISCAL_YEAR = "toFiscalYear";
    public static final String PARAM_GENDER_CODE = "genderCode";
    public static final String PARAM_HANDICAPPED = "handicapped";
    public static final String PARAM_DEPRIVED = "deprived";
    public static final String PARAM_MARTYRS = "martyrs";
    public static final String PARAM_BAUNIT_HISTORIC_ID = "historicBaunitId";
    public static final String SELECT_PART =
            "SELECT DISTINCT b.id, b.name, b.cadastre_object_id, b.name_firstpart, b.name_lastpart, b.status_code, b.office_code, b.fy_code, "
            + "(SELECT string_agg(COALESCE(p.name, '') || ' ' || COALESCE(p.last_name, ''), '::::') "
            + "FROM administrative.rrr rrr INNER JOIN (administrative.party_for_rrr pr "
            + "INNER JOIN party.party p ON pr.party_id = p.id) ON rrr.id = pr.rrr_id "
            + "WHERE rrr.status_code = 'current' AND rrr.ba_unit_id = b.id) AS rightholders, "
            + "l.id as loc_id, m.id as moth_id, l.pana_no as pana_no, m.mothluj_no as moth_no, a.ward_no, "
            + "a.vdc_code, co.parcel_no, msh.map_number, co.map_sheet_id, co.approval_datetime, '' as action "
            + "FROM (administrative.ba_unit b LEFT JOIN ((administrative.rrr r "
            + "LEFT JOIN administrative.party_for_rrr pr ON r.id = pr.rrr_id and r.status_code='current') inner join "
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
            + "SELECT DISTINCT b.id, b.name, b.cadastre_object_id, b.name_firstpart, b.name_lastpart, b.status_code, b.office_code, b.fy_code, "
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
            + "AND b.office_code = #{" + PARAM_OFFICE_CODE + "}"
            + ") m "
            + "ORDER BY action";
    public static final String SEARCH_QUERY = SELECT_PART
            + "WHERE (COALESCE(a.vdc_code, '') = #{" + PARAM_VDC_CODE + "} OR #{" + PARAM_VDC_CODE + "}='') "
            //+ "AND (CASE WHEN #{" + PARAM_APROVAL_DATE_FROM + "}=date('0001-02-01 00:00:00.0') AND #{" + PARAM_APROVAL_DATE_TO + "}=date('2500-02-01 00:00:00.0') THEN co.approval_datetime is null else co.approval_datetime BETWEEN #{" + PARAM_APROVAL_DATE_FROM + "} AND #{" + PARAM_APROVAL_DATE_TO + "} END) "
            + "AND co.approval_datetime BETWEEN #{" + PARAM_APROVAL_DATE_FROM + "} AND #{" + PARAM_APROVAL_DATE_TO + "} "
            + "AND (COALESCE(a.ward_no, '') = #{" + PARAM_WARD + "} OR #{" + PARAM_WARD + "}='') "
            + "AND (COALESCE(co.map_sheet_id, '') = #{" + PARAM_MAPSHEET_CODE + "} OR "
            + "COALESCE(co.map_sheet_id2, '') = #{" + PARAM_MAPSHEET_CODE + "} OR "
            + "COALESCE(co.map_sheet_id3, '') = #{" + PARAM_MAPSHEET_CODE + "} OR "
            + "COALESCE(co.map_sheet_id4, '') = #{" + PARAM_MAPSHEET_CODE + "} OR "
            + "#{" + PARAM_MAPSHEET_CODE + "}='') AND (co.status_code = 'current' OR COALESCE(co.status_code, '') = '') "
            + "AND (COALESCE(co.parcel_no, '') = #{" + PARAM_PARCEL_NO + "} OR #{" + PARAM_PARCEL_NO + "}='') "
            + "AND (COALESCE(m.mothluj_no, '') = #{" + PARAM_MOTH + "} OR #{" + PARAM_MOTH + "}='') "
            + "AND (COALESCE(l.pana_no, '') = #{" + PARAM_LOC + "} OR #{" + PARAM_LOC + "}='') "
            + "AND (COALESCE(pr.party_id, '') = #{" + PARAM_RIGHTHOLDER_ID + "} OR #{" + PARAM_RIGHTHOLDER_ID + "}='') "
            + "AND b.office_code = #{" + PARAM_OFFICE_CODE + "} AND b.status_code != 'pending' "
            + "ORDER BY a.vdc_code "
            + "LIMIT 101";
    public static final String SEARCH_QUERY1 = SELECT_PART
            + "WHERE (COALESCE(a.vdc_code, '') = #{" + PARAM_VDC_CODE + "} OR #{" + PARAM_VDC_CODE + "}='') "
            + "AND (COALESCE(a.ward_no, '') = #{" + PARAM_WARD + "} OR #{" + PARAM_WARD + "}='') "
            + "AND (COALESCE(co.map_sheet_id, '') = #{" + PARAM_MAPSHEET_CODE + "} OR "
            + "COALESCE(co.map_sheet_id2, '') = #{" + PARAM_MAPSHEET_CODE + "} OR "
            + "COALESCE(co.map_sheet_id3, '') = #{" + PARAM_MAPSHEET_CODE + "} OR "
            + "COALESCE(co.map_sheet_id4, '') = #{" + PARAM_MAPSHEET_CODE + "} OR "
            + "#{" + PARAM_MAPSHEET_CODE + "}='') AND (co.status_code = 'current' OR COALESCE(co.status_code, '') = '') "
            + "AND (COALESCE(co.parcel_no, '') = #{" + PARAM_PARCEL_NO + "} OR #{" + PARAM_PARCEL_NO + "}='') "
            + "AND (COALESCE(m.mothluj_no, '') = #{" + PARAM_MOTH + "} OR #{" + PARAM_MOTH + "}='') "
            + "AND (COALESCE(l.pana_no, '') = #{" + PARAM_LOC + "} OR #{" + PARAM_LOC + "}='') "
            + "AND (COALESCE(pr.party_id, '') = #{" + PARAM_RIGHTHOLDER_ID + "} OR #{" + PARAM_RIGHTHOLDER_ID + "}='') "
            + "AND b.office_code = #{" + PARAM_OFFICE_CODE + "} AND b.status_code != 'pending' "
            + "ORDER BY a.vdc_code "
            + "LIMIT 101";
    public static final String SEARCH_BY_ID_QUERY = SELECT_PART
            + "WHERE b.id = #{" + PARAM_BA_UNIT_ID + "} AND b.office_code = #{" + PARAM_OFFICE_CODE + "}";
    public static final String SELECT_PART_NEW = "SELECT DISTINCT b.id, b.name, b.cadastre_object_id, b.name_firstpart, "
            + "b.name_lastpart, b.status_code, b.office_code, b.fy_code, "
            + "(SELECT string_agg(COALESCE(pc.display_value, ''), '::::' ) "
            + "FROM administrative.rrr rrr INNER JOIN (administrative.party_for_rrr pr "
            + "INNER JOIN party.party p ON pr.party_id = p.id inner join party.category_for_party cp on cp.party_id=p.id "
            + "inner join party.party_categories pc on pc.code=cp.category_id) ON rrr.id = pr.rrr_id "
            + "WHERE rrr.status_code = 'current' AND rrr.ba_unit_id = b.id "
            + "AND (COALESCE(p.gender_code, '') = #{" + PARAM_GENDER_CODE + "} OR #{" + PARAM_GENDER_CODE + "}='') "
            + "AND ((COALESCE(pc.code, '') = #{" + PARAM_HANDICAPPED + "} OR #{" + PARAM_HANDICAPPED + "}='') "
            + "OR (COALESCE(pc.code, '') = #{" + PARAM_DEPRIVED + "} OR #{" + PARAM_DEPRIVED + "}='')"
            + "OR (COALESCE(pc.code, '') = #{" + PARAM_MARTYRS + "} OR #{" + PARAM_MARTYRS + "}=''))) AS categories, "
            + "l.id as loc_id, m.id as moth_id, l.pana_no as pana_no, m.mothluj_no as moth_no, a.ward_no, "
            + "a.vdc_code, co.parcel_no, msh.map_number, co.map_sheet_id, co.approval_datetime "
            + "FROM (administrative.ba_unit b inner JOIN ((administrative.rrr r "
            + "inner JOIN administrative.party_for_rrr pr ON r.id = pr.rrr_id "
            + "INNER JOIN party.party p ON pr.party_id = p.id inner join party.category_for_party cp on cp.party_id=p.id "
            + "inner join party.party_categories pc on pc.code=cp.category_id and r.status_code='current' "
            + "AND (COALESCE(p.gender_code, '') = #{" + PARAM_GENDER_CODE + "} OR #{" + PARAM_GENDER_CODE + "}='') "
            + "AND ((COALESCE(pc.code, '') = #{" + PARAM_HANDICAPPED + "} OR #{" + PARAM_HANDICAPPED + "}='') "
            + "OR (COALESCE(pc.code, '') = #{" + PARAM_DEPRIVED + "} OR #{" + PARAM_DEPRIVED + "}='') "
            + "OR (COALESCE(pc.code, '') = #{" + PARAM_MARTYRS + "} OR #{" + PARAM_MARTYRS + "}=''))) inner join "
            + "(administrative.loc l INNER JOIN administrative.moth m on l.moth_id=m.id) on r.loc_id=l.id) "
            + "ON b.id = r.ba_unit_id and r.status_code='current') inner JOIN "
            + "((cadastre.cadastre_object co inner JOIN cadastre.map_sheet msh on co.map_sheet_id=msh.id) "
            + "inner JOIN (address.address a INNER JOIN address.vdc vdc on a.vdc_code = vdc.code) "
            + "on co.address_id = a.id) on b.cadastre_object_id = co.id "
            + "where b.status_code='current' AND r.office_code = #{" + PARAM_OFFICE_CODE + "} ";
    public static final String SEARCH_QUERY_REGISTRATION_FROM_TO = SELECT_PART_NEW
            + " and (co.approval_datetime BETWEEN #{" + PARAM_APROVAL_DATE_FROM + "} AND #{" + PARAM_APROVAL_DATE_TO + "}) "
            + " AND (COALESCE(p.gender_code, '') = #{" + PARAM_GENDER_CODE + "} OR #{" + PARAM_GENDER_CODE + "}='') ";
    public static final String SEARCH_QUERY_REGISTRATION_UP_TO = SELECT_PART_NEW
            + " and  co.approval_datetime <=#{" + PARAM_UPTO_DATE + "} ";
    public static final String SEARCH_QUERY_REGISTRATION_FROM = SELECT_PART_NEW
            + " AND (co.approval_datetime BETWEEN #{" + PARAM_FROM + "} AND now()) ";
    public static final String SEARCH_QUERY_REGISTRATION_FISCAL_YEAR = SELECT_PART_NEW
            + " AND co.approval_datetime >= #{" + PARAM_FROM_FISCAL_YEAR + "} AND co.approval_datetime < #{" + PARAM_TO_FISCAL_YEAR + "} ";
    public static final String SELECT_PART_WITH_TRANSACTION = SELECT_PART
            + " INNER JOIN transaction.transaction t on b.transaction_id=t.id ";
    public static final String SEARCH_QUERY_TRANSACTION_FROM_TO = SELECT_PART_WITH_TRANSACTION
            + " WHERE (t.approval_datetime BETWEEN #{" + PARAM_APROVAL_DATE_FROM + "} AND #{" + PARAM_APROVAL_DATE_TO + "}) "
            + "AND r.office_code = #{" + PARAM_OFFICE_CODE + "}";
    public static final String SEARCH_QUERY_TRANSACTION_UP_TO = SELECT_PART_WITH_TRANSACTION
            + " WHERE  t.approval_datetime <=#{" + PARAM_UPTO_DATE + "} "
            + "AND r.office_code = #{" + PARAM_OFFICE_CODE + "}";
    public static final String SEARCH_QUERY_TRANSACTION_FROM = SELECT_PART_WITH_TRANSACTION
            + " WHERE (t.approval_datetime BETWEEN #{" + PARAM_FROM + "} AND now()) "
            + "AND r.office_code = #{" + PARAM_OFFICE_CODE + "}";
    public static final String SEARCH_QUERY_TRANSACTION_FISCAL_YEAR = SELECT_PART_WITH_TRANSACTION
            + " WHERE t.approval_datetime >= #{" + PARAM_FROM_FISCAL_YEAR + "} AND t.approval_datetime < #{" + PARAM_TO_FISCAL_YEAR + "} "
            + "AND r.office_code = #{" + PARAM_OFFICE_CODE + "}";
    public static final String SEARCH_QUERY_PARCEL_FROM_TO = SELECT_PART
            + " WHERE (co.approval_datetime BETWEEN #{" + PARAM_APROVAL_DATE_FROM + "} AND #{" + PARAM_APROVAL_DATE_TO + "}) "
            + "AND r.office_code = #{" + PARAM_OFFICE_CODE + "}";
    public static final String SEARCH_QUERY_PARCEL_UP_TO = SELECT_PART
            + " WHERE  co.approval_datetime <=#{" + PARAM_UPTO_DATE + "} "
            + "AND r.office_code = #{" + PARAM_OFFICE_CODE + "}";
    public static final String SEARCH_QUERY_PARCEL_FROM = SELECT_PART
            + " WHERE (co.approval_datetime BETWEEN #{" + PARAM_FROM + "} AND now()) "
            + "AND r.office_code = #{" + PARAM_OFFICE_CODE + "}";
    public static final String SEARCH_QUERY_PARCEL_FISCAL_YEAR = SELECT_PART
            + " WHERE co.approval_datetime >= #{" + PARAM_FROM_FISCAL_YEAR + "} AND co.approval_datetime < #{" + PARAM_TO_FISCAL_YEAR + "} "
            + "AND r.office_code = #{" + PARAM_OFFICE_CODE + "}";
    public static final String SELECT_PART_PARCEL_SPLIT = "SELECT distinct b.id, b.name, "
            + "(SELECT string_agg(bu1.name_lastpart, '::::') "
            + "FROM administrative.required_relationship_baunit rrb1 "
            + "INNER JOIN administrative.ba_unit bu1 on bu1.id= rrb1.to_ba_unit_id "
            + "WHERE rrb1.from_ba_unit_id =rrb.from_ba_unit_id) as new_parcel_no, "
            + "b.cadastre_object_id, b.name_firstpart, b.name_lastpart, b.status_code, b.office_code, b.fy_code, "
            + "l.pana_no,m.mothluj_no, m.moth_luj,a.ward_no,a.vdc_code, co.parcel_no, msh.map_number, co.map_sheet_id,co.approval_datetime "
            + "FROM (((administrative.ba_unit b INNER JOIN (administrative.rrr r inner join "
            + "(administrative.loc l INNER JOIN administrative.moth m on l.moth_id=m.id) on r.loc_id=l.id) "
            + "ON b.id = r.ba_unit_id) INNER JOIN((cadastre.cadastre_object co INNER JOIN cadastre.map_sheet msh on co.map_sheet_id=msh.id) "
            + "INNER JOIN(address.address a INNER JOIN address.vdc vdc on a.vdc_code = vdc.code)on co.address_id = a.id) "
            + "on b.cadastre_object_id = co.id) INNER JOIN administrative.required_relationship_baunit rrb "
            + "on b.id = rrb.from_ba_unit_id) "
            + "WHERE b.status_code='historic' and rrb.relation_code='split' and r.status_code='previous' AND r.office_code = #{" + PARAM_OFFICE_CODE + "} ";
    public static final String SEARCH_QUERY_PARCEL_SPLIT_FROM_TO = SELECT_PART_PARCEL_SPLIT
            + " AND co.approval_datetime BETWEEN #{" + PARAM_APROVAL_DATE_FROM + "} AND #{" + PARAM_APROVAL_DATE_TO + "} ";
    public static final String SEARCH_QUERY_PARCEL_SPLIT_FROM = SELECT_PART_PARCEL_SPLIT
            + " AND co.approval_datetime BETWEEN #{" + PARAM_FROM + "} AND now() ";
    public static final String SEARCH_QUERY_PARCEL_SPLIT_UP_TO = SELECT_PART_PARCEL_SPLIT
            + " AND co.approval_datetime <=#{" + PARAM_UPTO_DATE + "} ";
    public static final String SEARCH_QUERY_PARCEL_SPLIT_FISCAL_YEAR = SELECT_PART_PARCEL_SPLIT
            + " AND co.approval_datetime >= #{" + PARAM_FROM_FISCAL_YEAR + "} AND co.approval_datetime < #{" + PARAM_TO_FISCAL_YEAR + "} ";
    public static final String SERCH_QUERY_USING_HISTORIC_ID = "SELECT distinct b.id, b.name, "
            + "b.cadastre_object_id, b.name_firstpart, b.name_lastpart, b.status_code, b.office_code, b.fy_code, "
            + "l.id as loc_id, m.id as moth_id, l.pana_no as pana_no, m.mothluj_no as moth_no, a.ward_no, "
            + "a.vdc_code, co.parcel_no, msh.map_number, co.map_sheet_id,co.approval_datetime "
            + "FROM (administrative.ba_unit b INNER JOIN (administrative.rrr r inner join "
            + "(administrative.loc l INNER JOIN administrative.moth m on l.moth_id=m.id) on r.loc_id=l.id) "
            + "ON b.id = r.ba_unit_id) INNER JOIN((cadastre.cadastre_object co INNER JOIN cadastre.map_sheet msh on co.map_sheet_id=msh.id) "
            + "INNER JOIN(address.address a INNER JOIN address.vdc vdc on a.vdc_code = vdc.code)on co.address_id = a.id) "
            + "on b.cadastre_object_id = co.id "
            + "WHERE b.id in(SELECT rrb.to_ba_unit_id FROM administrative.required_relationship_baunit rrb "
            + "WHERE rrb.from_ba_unit_id = #{" + PARAM_BAUNIT_HISTORIC_ID + "} AND r.office_code = #{" + PARAM_OFFICE_CODE + "}) ";
    //+ "INNER JOIN administrative.required_relationship_baunit rrb on b.id = rrb.to_ba_unit_id "
    //+ "WHERE rrb.from_ba_unit_id = #{" + PARAM_BAUNIT_HISTORIC_ID + "} AND r.office_code = #{" + PARAM_OFFICE_CODE + "} ";
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
    @Column(name = "cadastre_object_id")
    private String cadastreObjectId;
    @Column(name = "parcel_no")
    private String parcelNo;
    @Column(name = "map_number")
    private String mapNumber;
    @Column(name = "map_sheet_id")
    private String mapSheetId;
    @Column(name = "action")
    private String action;
    @Column(name = "approval_datetime")
    private Date approvalDateTime;
    @Column(name = "categories")
    private String categories;
    @Column(name = "new_parcel_no")
    private String newParcelNo;

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

    public String getCadastreObjectId() {
        return cadastreObjectId;
    }

    public void setCadastreObjectId(String cadastreObjectId) {
        this.cadastreObjectId = cadastreObjectId;
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

    public Date getApprovalDateTime() {
        return approvalDateTime;
    }

    public void setApprovalDateTime(Date approvalDateTime) {
        this.approvalDateTime = approvalDateTime;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getNewParcelNo() {
        return newParcelNo;
    }

    public void setNewParcelNo(String newParcelNo) {
        this.newParcelNo = newParcelNo;
    }
}
