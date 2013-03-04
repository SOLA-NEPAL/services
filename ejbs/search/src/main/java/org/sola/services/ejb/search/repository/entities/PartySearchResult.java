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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

@Entity
@Table(name = "party", schema = "party")
public class PartySearchResult extends AbstractReadOnlyEntity {

    public static final String PARAM_ROLE_TYPE_CODE = "roleTypeCode";
    public static final String PARAM_TYPE_CODE = "typeCode";
    public static final String PARAM_NAME = "name";
    public static final String PARAM_LAST_NAME = "lastName";
    public static final String PARAM_FATHER_NAME = "fatherName";
    public static final String PARAM_GRANDFATHER_NAME = "grandFatherName";
    public static final String PARAM_ID_ISSUE_DATE = "idIssueDate";
    public static final String PARAM_ID_NUMBER = "idNumber";
    public static final String PARAM_DISTRICT_CODE = "districtCode";
    public static final String PARAM_VDC_CODE = "vdcCode";
    public static final String PARAM_WARD = "ward";
    public static final String PARAM_STREET = "street";
    public static final String PARAM_LANG = "lang";
    public static final String PARAM_UPTO_DATE = "upToDate";
    public static final String PARAM_FROM_DATE = "frmDate";
    public static final String PARAM_TO_DATE = "toDate";
    public static final String PARAM_FROM = "from";
    public static final String PARAM_FROM_FISCAL_YEAR = "fromFiscalYear";
    public static final String PARAM_TO_FISCAL_YEAR = "toFiscalYear";
    public static final String SEARCH_QUERY =
            "SELECT distinct p.id, p.name, p.last_name, p.ext_id, p.type_code, p.office_code, p.is_child, "
            + "p.id_office_type_code, p.id_issue_date, p.id_number, p.father_type_code, "
            + "p.fathers_name, p.grandfather_type_code, p.parent_id, p.grandfather_name, p.gender_code, "
            + "a.vdc_code, get_translation(vdc.display_value, #{" + PARAM_LANG + "}) AS vdc_name, a.ward_no, a.street, "
            + "(SELECT CASE (SELECT COUNT(1) FROM administrative.party_for_rrr ap "
            + "WHERE ap.party_id = p.id) WHEN 0 THEN false ELSE true END) AS is_rightholder "
            + "FROM (party.party p LEFT JOIN party.party_role pr ON p.id = pr.party_id) "
            + "LEFT JOIN (address.address a LEFT JOIN address.vdc vdc ON a.vdc_code=vdc.code) ON p.address_id = a.id "
            + "WHERE (#{" + PARAM_NAME + "} = '' OR POSITION(LOWER(#{" + PARAM_NAME + "}) IN LOWER(COALESCE(p.name, ''))) > 0) "
            + "AND (#{" + PARAM_LAST_NAME + "} = '' OR POSITION(LOWER(#{" + PARAM_LAST_NAME + "}) IN LOWER(COALESCE(p.last_name, ''))) > 0) "
            + "AND (#{" + PARAM_FATHER_NAME + "} = '' OR POSITION(LOWER(#{" + PARAM_FATHER_NAME + "}) IN LOWER(COALESCE(p.fathers_name, ''))) > 0) "
            + "AND (#{" + PARAM_GRANDFATHER_NAME + "} = '' OR POSITION(LOWER(#{" + PARAM_GRANDFATHER_NAME + "}) IN LOWER(COALESCE(p.grandfather_name, ''))) > 0) "
            + "AND (#{" + PARAM_ID_ISSUE_DATE + "} = 0 OR #{" + PARAM_ID_ISSUE_DATE + "} = COALESCE(p.id_issue_date, 0)) "
            + "AND (#{" + PARAM_ID_NUMBER + "} = '' OR POSITION(LOWER(#{" + PARAM_ID_NUMBER + "}) IN LOWER(COALESCE(p.id_number, ''))) > 0) "
            + "AND (#{" + PARAM_DISTRICT_CODE + "} = '' OR #{" + PARAM_DISTRICT_CODE + "} = COALESCE(vdc.district_code, '')) "
            + "AND (#{" + PARAM_VDC_CODE + "} = '' OR #{" + PARAM_VDC_CODE + "} = COALESCE(vdc.code, '')) "
            + "AND (#{" + PARAM_WARD + "} = '' OR LOWER(#{" + PARAM_WARD + "}) = LOWER(COALESCE(a.ward_no, ''))) "
            + "AND (#{" + PARAM_STREET + "} = '' OR POSITION(LOWER(#{" + PARAM_STREET + "}) IN LOWER(COALESCE(a.street, ''))) > 0) "
            + "AND (#{" + PARAM_TYPE_CODE + "} = '' OR LOWER(p.type_code) = LOWER(#{" + PARAM_TYPE_CODE + "})) "
            + "AND (#{" + PARAM_ROLE_TYPE_CODE + "} = '' OR LOWER(pr.type_code) = LOWER(#{" + PARAM_ROLE_TYPE_CODE + "})) "
            + "ORDER BY p.name, p.last_name "
            + "LIMIT 101";
    public static final String SEARCH_QUERY_UPTO_DATE =
            "SELECT distinct p.id, p.name, p.last_name, p.ext_id, p.type_code, p.office_code, "
            + "p.is_child, p.id_office_type_code, p.id_issue_date, p.id_number, p.father_type_code, "
            + "p.fathers_name, p.grandfather_type_code, p.parent_id, p.grandfather_name, p.gender_code, a.vdc_code, "
            + "get_translation(vdc.display_value, #{" + PARAM_LANG + "}), a.ward_no, a.street,t.approval_datetime "
            + "FROM ((party.party p inner JOIN administrative.party_for_rrr pr ON p.id = pr.party_id)"
            + "inner JOIN (address.address a LEFT JOIN address.vdc vdc ON a.vdc_code=vdc.code) ON p.address_id = a.id)"
            + "inner join(administrative.rrr r inner join transaction.transaction t on r.transaction_id=t.id)"
            + "on r.id=pr.rrr_id where r.type_code='ownership'"
            + "and  t.approval_datetime <=#{" + PARAM_UPTO_DATE + "}";
    public static final String SEARCH_QUERY_FROM_AND_TO_DATE =
            "SELECT distinct p.id, p.name, p.last_name, p.ext_id, p.type_code, p.office_code, "
            + "p.is_child, p.id_office_type_code, p.id_issue_date, p.id_number, p.father_type_code, "
            + "p.fathers_name, p.grandfather_type_code, p.parent_id, p.grandfather_name, p.gender_code, a.vdc_code, "
            + "get_translation(vdc.display_value, #{" + PARAM_LANG + "}), a.ward_no, a.street,t.approval_datetime  "
            + "FROM ((party.party p inner JOIN administrative.party_for_rrr pr ON p.id = pr.party_id) "
            + "INNER JOIN (address.address a LEFT JOIN address.vdc vdc ON a.vdc_code=vdc.code) ON p.address_id = a.id) "
            + "INNER join(administrative.rrr r inner join transaction.transaction t on r.transaction_id=t.id) "
            + "ON r.id=pr.rrr_id where r.type_code='ownership' "
            + "AND (t.approval_datetime BETWEEN #{" + PARAM_FROM_DATE + "} AND #{" + PARAM_TO_DATE + "}) "
            + "AND r.office_code = #{" + PARAM_OFFICE_CODE + "}";
    public static final String SEARCH_QUERY_FROM_DATE =
            "SELECT distinct p.id, p.name, p.last_name, p.ext_id, p.type_code, p.office_code, "
            + "p.is_child, p.id_office_type_code, p.id_issue_date, p.id_number, p.father_type_code, "
            + "p.fathers_name, p.grandfather_type_code, p.parent_id, p.grandfather_name, p.gender_code, a.vdc_code, "
            + "get_translation(vdc.display_value, #{" + PARAM_LANG + "}), a.ward_no, a.street,t.approval_datetime  "
            + "FROM ((party.party p inner JOIN administrative.party_for_rrr pr ON p.id = pr.party_id) "
            + "INNER JOIN (address.address a LEFT JOIN address.vdc vdc ON a.vdc_code=vdc.code) ON p.address_id = a.id) "
            + "INNER join(administrative.rrr r inner join transaction.transaction t on r.transaction_id=t.id) "
            + "ON r.id=pr.rrr_id where r.type_code='ownership' "
            + "AND (t.approval_datetime BETWEEN #{" + PARAM_FROM + "} AND now()) "
            + "AND r.office_code = #{" + PARAM_OFFICE_CODE + "}";
    public static final String SEARCH_QUERY_FISCAL_YEAR =
            "SELECT distinct p.id, p.name, p.last_name, p.ext_id, p.type_code, p.office_code, "
            + "p.is_child, p.id_office_type_code, p.id_issue_date, p.id_number, p.father_type_code, "
            + "p.fathers_name, p.grandfather_type_code, p.parent_id, p.grandfather_name, p.gender_code, a.vdc_code, "
            + "get_translation(vdc.display_value, #{" + PARAM_LANG + "}), a.ward_no, a.street,t.approval_datetime  "
            + "FROM ((party.party p inner JOIN administrative.party_for_rrr pr ON p.id = pr.party_id) "
            + "INNER JOIN (address.address a LEFT JOIN address.vdc vdc ON a.vdc_code=vdc.code) ON p.address_id = a.id) "
            + "INNER join(administrative.rrr r inner join transaction.transaction t on r.transaction_id=t.id) "
            + "ON r.id=pr.rrr_id where r.type_code='ownership' "
            + "AND t.approval_datetime >= #{" + PARAM_FROM_FISCAL_YEAR + "} AND t.approval_datetime < #{" + PARAM_TO_FISCAL_YEAR + "} "
            + "AND r.office_code = #{" + PARAM_OFFICE_CODE + "}";
    @Id
    @Column
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "ext_id")
    private String extId;
    @Column(name = "type_code")
    private String typeCode;
    @Column(name = "is_rightholder")
    private boolean rightHolder;
    @Column(name = "office_code", updatable = false)
    private String officeCode;
    @Column(name = "is_child")
    private boolean child;
    @Column(name = "id_office_type_code")
    private String idOfficeTypeCode;
    @Column(name = "id_issue_date")
    private Integer idIssueDate;
    @Column(name = "id_number")
    private String idNumber;
    @Column(name = "father_type_code")
    private String fatherTypeCode;
    @Column(name = "fathers_name")
    private String fatherName;
    @Column(name = "grandfather_type_code")
    private String grandfatherTypeCode;
    @Column(name = "grandfather_name")
    private String grandfatherName;
    @Column(name = "gender_code")
    private String genderCode;
    @Column(name = "vdc_code")
    private String vdcCode;
    @Column(name = "vdc_name")
    private String vdcName;
    @Column(name = "ward_no")
    private String wardNo;
    @Column(name = "street")
    private String street;
    @Column(name = "parent_id")
    private String parentId;

    public PartySearchResult() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public boolean isRightHolder() {
        return rightHolder;
    }

    public void setRightHolder(boolean rightHolder) {
        this.rightHolder = rightHolder;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public boolean isChild() {
        return child;
    }

    public void setChild(boolean child) {
        this.child = child;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getFatherTypeCode() {
        return fatherTypeCode;
    }

    public void setFatherTypeCode(String fatherTypeCode) {
        this.fatherTypeCode = fatherTypeCode;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    public String getGrandfatherName() {
        return grandfatherName;
    }

    public void setGrandfatherName(String grandfatherName) {
        this.grandfatherName = grandfatherName;
    }

    public String getGrandfatherTypeCode() {
        return grandfatherTypeCode;
    }

    public void setGrandfatherTypeCode(String grandfatherTypeCode) {
        this.grandfatherTypeCode = grandfatherTypeCode;
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

    public String getIdOfficeTypeCode() {
        return idOfficeTypeCode;
    }

    public void setIdOfficeTypeCode(String idOfficeTypeCode) {
        this.idOfficeTypeCode = idOfficeTypeCode;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
