/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.party.repository.entities;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.ChildEntity;
import org.sola.services.common.repository.ChildEntityList;
import org.sola.services.common.repository.DefaultSorter;
import org.sola.services.common.repository.ExternalEJB;
import org.sola.services.common.repository.entities.AbstractVersionedEntity;
import org.sola.services.ejb.address.businesslogic.AddressEJBLocal;
import org.sola.services.ejb.address.repository.entities.Address;

/**
 *
 * @author KumarKhadka
 */
@Table(name = "party", schema = "party")
@DefaultSorter(sortString = "name, last_name")
public class Party2 extends AbstractVersionedEntity {

    public static final String TYPE_CODE_NON_NATURAL_PERSON = "nonNaturalPerson";
    public static final String TYPE_CODE_NATURAL_PERSON = "naturalPerson";
    public static final String QUERY_WHERE_BYTYPECODE = "type_code = #{partyTypeCode}";
    public static final String QUERY_WHERE_LODGING_AGENTS = "party.id in (select party_id from party.party_role where party.party_role.type_code = 'lodgingAgent')";
    
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "ext_id")
    private String extId;
    @Column(name = "type_code")
    private String typeCode;
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
    @Column(name = "grandfather_name")
    private String grandFatherName;
    @Column(name = "grandfather_last_name")
    private String grandFatherLastName;
    @Column(name = "ward_no")
    private int wardNo;
    @Column(name = "street")
    private String street;
    @Column(name = "date_of_birth")
    private Date dateOfBirth;
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "vdc_code")
    private int vdcCode;
    @Column(name = "dist_code")
    private int distCode;
    @Column(name = "gender_code")
    private String genderCode;
    @Column(name = "address_id")
    private String addressId;
    @Column(name = "id_type_code")
    private String idTypeCode;
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
    @Column(name = "preferred_communication_code")
    private String preferredCommunicationCode;
    @ExternalEJB(ejbLocalClass = AddressEJBLocal.class,
    loadMethod = "getAddress", saveMethod = "saveAddress")
    @ChildEntity(childIdField = "addressId")
    private Address address;
    @ChildEntityList(parentIdField = "partyId")
    private List<PartyRole> roleList;
    
    @Column(name = "party.is_rightholder(id) AS is_rightholder", insertable = false, updatable = false)
    private boolean rightHolder;

    public Party2(){
        super();
    }
    
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPreferredCommunicationCode() {
        return preferredCommunicationCode;
    }

    public void setPreferredCommunicationCode(String preferredCommunicationCode) {
        this.preferredCommunicationCode = preferredCommunicationCode;
    }

    public boolean isRightHolder() {
        return rightHolder;
    }

    public void setRightHolder(boolean rightHolder) {
        this.rightHolder = rightHolder;
    }

    public List<PartyRole> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<PartyRole> roleList) {
        this.roleList = roleList;
    }
       
    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getDistCode() {
        return distCode;
    }

    public void setDistCode(int distCode) {
        this.distCode = distCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    public String getGrandFatherLastName() {
        return grandFatherLastName;
    }

    public void setGrandFatherLastName(String grandFatherLastName) {
        this.grandFatherLastName = grandFatherLastName;
    }

    public String getGrandFatherName() {
        return grandFatherName;
    }

    public void setGrandFatherName(String grandFatherName) {
        this.grandFatherName = grandFatherName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getIdTypeCode() {
        return idTypeCode;
    }

    public void setIdTypeCode(String idTypeCode) {
        this.idTypeCode = idTypeCode;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getVdcCode() {
        return vdcCode;
    }

    public void setVdcCode(int vdcCode) {
        this.vdcCode = vdcCode;
    }

    public int getWardNo() {
        return wardNo;
    }

    public void setWardNo(int wardNo) {
        this.wardNo = wardNo;
    }

    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

   

    public String getId() {
        id = id == null ? generateId() : id;
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
