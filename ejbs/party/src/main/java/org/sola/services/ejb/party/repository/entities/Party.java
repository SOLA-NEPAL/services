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
package org.sola.services.ejb.party.repository.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.ChildEntity;
import org.sola.services.common.repository.ChildEntityList;
import org.sola.services.common.repository.DefaultSorter;
import org.sola.services.common.repository.ExternalEJB;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;
import org.sola.services.common.repository.entities.AbstractVersionedEntity;
import org.sola.services.digitalarchive.businesslogic.DigitalArchiveEJBLocal;
import org.sola.services.digitalarchive.repository.entities.Document;
import org.sola.services.ejb.address.businesslogic.AddressEJBLocal;
import org.sola.services.ejb.address.repository.entities.Address;
import org.sola.services.ejb.party.businesslogic.PartyEJBLocal;

/**
 * Entity representing the party.party table.
 */
@Table(name = "party", schema = "party")
@DefaultSorter(sortString = "name, last_name")
public class Party extends AbstractVersionedEntity {

    public static final String QUERY_WHERE_BYTYPECODE = "type_code = #{partyTypeCode} and office_code=#{" + AbstractReadOnlyEntity.PARAM_OFFICE_CODE + "}";
    public static final String QUERY_WHERE_LODGING_AGENTS = "party.id in (select party_id from party.party_role where party.party_role.type_code = 'lodgingAgent') "
            + "AND office_code=#{" + AbstractReadOnlyEntity.PARAM_OFFICE_CODE + "}";
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "is_child")
    private boolean child;
    @Column(name = "parent_id")
    private String parentId;
    @ChildEntity(childIdField = "parentId", readOnly = true)
    Party parent;
    @Column(name = "ext_id")
    private String extId;
    @Column(name = "type_code")
    private String typeCode;
    //@AccessFunctions(onSelect = "party.get_party_name(parent_id) as parent_name")
    // private String parentName;    
    @Column(name = "name")
    private String name;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "father_type_code")
    private String fatherTypeCode;
    @Column(name = "fathers_name")
    private String fatherName;
    @Column(name = "grandfather_type_code")
    private String grandfatherTypeCode;
    @Column(name = "grandfather_name")
    private String grandfatherName;
    @Column(name = "alias")
    private String alias;
    @Column(name = "gender_code")
    private String genderCode;
    @Column(name = "address_id")
    private String addressId;
    @Column(name = "id_type_code")
    private String idTypeCode;
    @Column(name = "id_number")
    private String idNumber;
    @Column(name = "id_issue_date")
    private Integer idIssueDate;
    @Column(name = "id_office_type_code")
    private String idOfficeTypeCode;
    @Column(name = "id_office_district_code")
    private String idOfficeDistrictCode;
    @Column(name = "email")
    private String email;
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "phone")
    private String phone;
    @Column(name = "fax")
    private String fax;
    @Column(name = "preferred_communication_code")
    private String preferredCommunicationCode;
    @Column(name = "date_of_birth")
    private Integer birthDate;
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "office_code")
    private String officeCode;
    @Column(name = "photo_id")
    private String photoId;
    @Column(name = "left_finger_id")
    private String leftFingerId;
    @Column(name = "right_finger_id")
    private String rightFingerId;
    @Column(name = "signature_id")
    private String signatureId;
    @ExternalEJB(ejbLocalClass = AddressEJBLocal.class, loadMethod = "getAddress", saveMethod = "saveAddress")
    @ChildEntity(childIdField = "addressId")
    private Address address;
    @ChildEntityList(parentIdField = "partyId")
    private List<PartyRole> roleList;
    @Column(name = "party.is_rightholder(id) AS is_rightholder", insertable = false, updatable = false)
    private boolean rightHolder;
    @ExternalEJB(ejbLocalClass = DigitalArchiveEJBLocal.class,
    loadMethod = "getDocument",
    saveMethod = "saveDocument")
    @ChildEntity(childIdField = "photoId")
    private Document photoDoc;
    @ExternalEJB(ejbLocalClass = DigitalArchiveEJBLocal.class,
    loadMethod = "getDocument",
    saveMethod = "saveDocument")
    @ChildEntity(childIdField = "leftFingerId")
    private Document leftFingerDoc;
    @ExternalEJB(ejbLocalClass = DigitalArchiveEJBLocal.class,
    loadMethod = "getDocument",
    saveMethod = "saveDocument")
    @ChildEntity(childIdField = "rightFingerId")
    private Document rightFingerDoc;
    @ExternalEJB(ejbLocalClass = DigitalArchiveEJBLocal.class,
    loadMethod = "getDocument",
    saveMethod = "saveDocument")
    @ChildEntity(childIdField = "signatureId")
    private Document signatureDoc;
    @ExternalEJB(ejbLocalClass = PartyEJBLocal.class, loadMethod = "getPartyCategorys")
    @ChildEntityList(parentIdField = "partyId", childIdField = "categoryId",
    manyToManyClass = CategoryForParty.class, readOnly = true)
    private List<PartyCategory> categoryList;
    private boolean handicapped;
    private boolean deprived;
    private boolean martyr;

    public Party() {
        super();
    }

    public List<PartyCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<PartyCategory> categoryList) {
        this.categoryList = categoryList;
    }

    public Party getParent() {
        return parent;
    }

    public void setParent(Party parent) {
        this.parent = parent;
    }

    public Document getLeftFingerDoc() {
        return leftFingerDoc;
    }

    public void setLeftFingerDoc(Document leftFingerDoc) {
        this.leftFingerDoc = leftFingerDoc;
        if (this.leftFingerDoc != null) {
            this.setLeftFingerId(leftFingerDoc.getId());
        }
    }

    public String getLeftFingerId() {
        return leftFingerId;
    }

    public void setLeftFingerId(String leftFingerId) {
        this.leftFingerId = leftFingerId;
    }

    public Document getPhotoDoc() {
        return photoDoc;
    }

    public void setPhotoDoc(Document photoDoc) {
        this.photoDoc = photoDoc;
        if (this.photoDoc != null) {
            this.setPhotoId(photoDoc.getId());
        }
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public Document getRightFingerDoc() {
        return rightFingerDoc;
    }

    public void setRightFingerDoc(Document rightFingerDoc) {
        this.rightFingerDoc = rightFingerDoc;
        if (this.rightFingerDoc != null) {
            this.setRightFingerId(rightFingerDoc.getId());
        }
    }

    public String getRightFingerId() {
        return rightFingerId;
    }

    public void setRightFingerId(String rightFingerId) {
        this.rightFingerId = rightFingerId;
    }

    public Document getSignatureDoc() {
        return signatureDoc;
    }

    public void setSignatureDoc(Document signatureDoc) {
        this.signatureDoc = signatureDoc;
        if (this.signatureDoc != null) {
            this.setSignatureId(signatureDoc.getId());
        }
    }

    public String getSignatureId() {
        return signatureId;
    }

    public void setSignatureId(String signatureId) {
        this.signatureId = signatureId;
    }

    public Integer getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Integer birthDate) {
        this.birthDate = birthDate;
    }

    public String getGrandfatherName() {
        return grandfatherName;
    }

    public void setGrandfatherName(String grandfatherName) {
        this.grandfatherName = grandfatherName;
    }

    public Integer getIdIssueDate() {
        return idIssueDate;
    }

    public void setIdIssueDate(Integer idIssueDate) {
        this.idIssueDate = idIssueDate;
    }

    public String getId() {
        id = id == null ? generateId() : id;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public boolean isChild() {
        return child;
    }

    public void setChild(boolean child) {
        this.child = child;
    }

    public String getFatherTypeCode() {
        return fatherTypeCode;
    }

    public void setFatherTypeCode(String fatherTypeCode) {
        this.fatherTypeCode = fatherTypeCode;
    }

    public String getGrandfatherTypeCode() {
        return grandfatherTypeCode;
    }

    public void setGrandfatherTypeCode(String grandfatherTypeCode) {
        this.grandfatherTypeCode = grandfatherTypeCode;
    }

    public String getIdOfficeDistrictCode() {
        return idOfficeDistrictCode;
    }

    public void setIdOfficeDistrictCode(String idOfficeDistrictCode) {
        this.idOfficeDistrictCode = idOfficeDistrictCode;
    }

    public String getIdOfficeTypeCode() {
        return idOfficeTypeCode;
    }

    public void setIdOfficeTypeCode(String idOfficeTypeCode) {
        this.idOfficeTypeCode = idOfficeTypeCode;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

//    public String getParentName() {
//        return parentName;
//    }
//
//    public void setParentName(String parentName) {
//        this.parentName = parentName;
//    }
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPreferredCommunicationCode() {
        return preferredCommunicationCode;
    }

    public void setPreferredCommunicationCode(String preferredCommunicationCode) {
        this.preferredCommunicationCode = preferredCommunicationCode;
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

    public List<PartyRole> getRoleList() {
        roleList = roleList == null ? new ArrayList<PartyRole>() : roleList;
        return roleList;
    }

    public void setRoleList(List<PartyRole> roleList) {
        this.roleList = roleList;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    public String getIdTypeCode() {
        return idTypeCode;
    }

    public void setIdTypeCode(String idTypeCode) {
        this.idTypeCode = idTypeCode;
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
}
