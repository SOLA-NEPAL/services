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
import java.util.Date;
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

/**
 * Entity representing the party.party table.
 */
@Table(name = "party", schema = "party")
@DefaultSorter(sortString = "name, last_name")
public class Party extends AbstractVersionedEntity {

    public static final String TYPE_CODE_NON_NATURAL_PERSON = "nonNaturalPerson";
    public static final String TYPE_CODE_NATURAL_PERSON = "naturalPerson";
    public static final String QUERY_WHERE_BYTYPECODE = "type_code = #{partyTypeCode} and office_code=#{" + AbstractReadOnlyEntity.PARAM_OFFICE_CODE + "}";
    public static final String QUERY_WHERE_LODGING_AGENTS = "party.id in (select party_id from party.party_role where party.party_role.type_code = 'lodgingAgent') "
            + "AND office_code=#{" + AbstractReadOnlyEntity.PARAM_OFFICE_CODE + "}";
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "fathers_name")
    private String fathersName;
    @Column(name = "fathers_last_name")
    private String fathersLastName;
    @Column(name = "alias")
    private String alias;
    @Column(name = "ext_id")
    private String extId;
    @Column(name = "id_number")
    private String idNumber;
    @Column(name = "email")
    private String email;
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "phone")
    private String phone;
    @Column(name = "fax")
    private String fax;
    @Column(name = "address_id")
    private String addressId;
    @Column(name = "id_type_code")
    private String idTypeCode;
    @Column(name = "preferred_communication_code")
    private String preferredCommunicationCode;
    @Column(name = "type_code")
    private String typeCode;
    @Column(name = "gender_code")
    private String genderCode;
    @ExternalEJB(ejbLocalClass = AddressEJBLocal.class, loadMethod = "getAddress", saveMethod = "saveAddress")
    @ChildEntity(childIdField = "addressId")
    private Address address;
    @ChildEntityList(parentIdField = "partyId")
    private List<PartyRole> roleList;
    @Column(name = "party.is_rightholder(id) AS is_rightholder", insertable = false, updatable = false)
    private boolean rightHolder;
    //additional fields
    @Column(name = "grandfather_name")
    private String grandfatherName;
    @Column(name = "grandfather_last_name")
    private String grandFatherLastName;
    @Column(name = "date_of_birth")
    private Date birthDate;
    @Column(name = "remarks")
    private String rmks;
    @Column(name = "id_provider_office_code")
    private String issuingOfficeCode;
    @Column(name = "id_issue_date")
    private Date idIssueDate;
    //additional fields for image storage.
    @Column(name="photo_id")
    private String photoId;
    @Column(name="left_finger_id")
    private String leftFingerId;
    @Column(name="right_finger_id")
    private String rightFingerId;
    @Column(name="signature_id")
    private String signatureId;
    
    @ExternalEJB(ejbLocalClass=DigitalArchiveEJBLocal.class,
            loadMethod = "getDocument",
            saveMethod= "saveDocument")
    @ChildEntity(childIdField="photoId")
    private Document photoDoc;
    
    @ExternalEJB(ejbLocalClass=DigitalArchiveEJBLocal.class,
            loadMethod = "getDocument",
            saveMethod= "saveDocument")
    @ChildEntity(childIdField="leftFingerId")
    private Document leftFingerDoc;
    
    @ExternalEJB(ejbLocalClass=DigitalArchiveEJBLocal.class,
            loadMethod = "getDocument",
            saveMethod= "saveDocument")
    @ChildEntity(childIdField="rightFingerId")
    private Document rightFingerDoc;
    
    @ExternalEJB(ejbLocalClass=DigitalArchiveEJBLocal.class,
            loadMethod = "getDocument",
            saveMethod= "saveDocument")
    @ChildEntity(childIdField="signatureId")
    private Document signatureDoc;

    private String officeCode;
    
    public Document getLeftFingerDoc() {
        return leftFingerDoc;
    }

    public void setLeftFingerDoc(Document leftFingerDoc) {
        this.leftFingerDoc = leftFingerDoc;
        if (this.leftFingerDoc!=null){
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
        if (this.photoDoc!=null){
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
        if (this.rightFingerDoc!=null){
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
        if (this.signatureDoc!=null){
            this.setSignatureId(signatureDoc.getId());
        }
    }

    public String getSignatureId() {
        return signatureId;
    }

    public void setSignatureId(String signatureId) {
        this.signatureId = signatureId;
    }
    
    public Date getBirthDate(){
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getGrandFatherLastName() {
        return grandFatherLastName;
    }

    public void setGrandFatherLastName(String grandFatherLastName) {
        this.grandFatherLastName = grandFatherLastName;
    }

    public String getGrandfatherName() {
        return grandfatherName;
    }

    public void setGrandfatherName(String grandfatherName) {
        this.grandfatherName = grandfatherName;
    }

    public Date getIdIssueDate() {
        return idIssueDate;
    }

    public void setIdIssueDate(Date idIssueDate) {
        this.idIssueDate = idIssueDate;
    }

    public String getIssuingOfficeCode() {
        return issuingOfficeCode;
    }

    public void setIssuingOfficeCode(String issuingOfficeCode) {
        this.issuingOfficeCode = issuingOfficeCode;
    }

    public String getRmks() {
        return rmks;
    }

    public void setRmks(String rmks) {
        this.rmks = rmks;
    }

    public Party() {
        super();
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

    public String getFathersLastName() {
        return fathersLastName;
    }

    public void setFathersLastName(String fathersLastName) {
        this.fathersLastName = fathersLastName;
    }

    public String getFathersName() {
        return fathersName;
    }

    public void setFathersName(String fathersName) {
        this.fathersName = fathersName;
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
