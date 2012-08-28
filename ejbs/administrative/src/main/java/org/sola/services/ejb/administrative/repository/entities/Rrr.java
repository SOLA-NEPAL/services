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
package org.sola.services.ejb.administrative.repository.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.LocalInfo;
import org.sola.services.common.StatusConstants;
import org.sola.services.common.repository.ChildEntity;
import org.sola.services.common.repository.ChildEntityList;
import org.sola.services.common.repository.ExternalEJB;
import org.sola.services.common.repository.RepositoryUtility;
import org.sola.services.common.repository.entities.AbstractVersionedEntity;
import org.sola.services.ejb.party.businesslogic.PartyEJBLocal;
import org.sola.services.ejb.party.repository.entities.Party;
import org.sola.services.ejb.source.businesslogic.SourceEJBLocal;
import org.sola.services.ejb.source.repository.entities.Source;
import org.sola.services.ejb.system.br.Result;
import org.sola.services.ejb.system.businesslogic.SystemEJBLocal;
import org.sola.services.ejb.transaction.businesslogic.TransactionEJBLocal;
import org.sola.services.ejb.transaction.repository.entities.Transaction;
import org.sola.services.ejb.transaction.repository.entities.TransactionStatusType;

/**
 * Entity representing administrative.rrr table.
 *
 * @author soladev
 */
@Table(name = "rrr", schema = "administrative")
public class Rrr extends AbstractVersionedEntity {

    public static final String QUERY_PARAMETER_TRANSACTIONID = "transactionId";
    public static final String QUERY_WHERE_BYTRANSACTIONID = "transaction_id = "
            + "#{" + QUERY_PARAMETER_TRANSACTIONID + "}";
    public static final String QUERY_WHERE_BY_LOCID = "loc_id = "
            + "#{" + RrrLoc.PARAM_LOC_ID + "} and (status_code = '" + StatusConstants.PENDING
            + "' or status_code = '" + StatusConstants.CURRENT + "')";
    public static final String ORDER_BY_BAUNIT_ID = "ba_unit_id";
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "ba_unit_id")
    private String baUnitId;
    @Column(name = "nr")
    private String nr;
    @Column(name = "registration_number")
    private String registrationNumber;
    @Column(name = "sn")
    private String sn;
    @Column(name = "type_code")
    private String typeCode;
    @Column(name = "status_code", insertable = false, updatable = false)
    private String statusCode;
    @Column(name = "is_primary")
    private boolean primary;
    @Column(name = "registration_date")
    private Date registrationDate;
    @Column(name = "transaction_id", updatable = false)
    private String transactionId;
    @Column(name = "expiration_date")
    private Date expirationDate;
    @Column(name = "mortgage_amount")
    private BigDecimal mortgageAmount;
    @Column(name = "mortgage_interest_rate")
    private BigDecimal mortgageInterestRate;
    @Column(name = "mortgage_ranking")
    private Integer mortgageRanking;
    @Column(name = "mortgage_type_code")
    private String mortgageTypeCode;
    @Column(name = "loc_id")
    private String locId;
    @Column(name = "office_code", updatable = false)
    private String officeCode;
    @Column(name = "fy_code", updatable = false)
    private String fiscalYearCode;
    @Column(name = "is_terminating")
    private boolean terminating;
    // Child entity fields
    @ChildEntity(insertBeforeParent = false, parentIdField = "rrrId")
    private BaUnitNotation notation;
    @ExternalEJB(ejbLocalClass = SourceEJBLocal.class,
    loadMethod = "getSources", saveMethod = "saveSource")
    @ChildEntityList(parentIdField = "rrrId", childIdField = "sourceId",
    manyToManyClass = SourceDescribesRrr.class)
    private List<Source> sourceList;
    @ExternalEJB(ejbLocalClass = PartyEJBLocal.class, loadMethod = "getParties")
    @ChildEntityList(parentIdField = "rrrId", childIdField = "partyId",
    manyToManyClass = PartyForRrr.class, readOnly = true)
    private List<Party> rightHolderList;
    @ChildEntity(childIdField = "locId", insertBeforeParent = true)
    private LocWithMoth loc;
    @Column(name = "restriction_reason_code")
    private String restrictionReasonCode;
    @Column(name = "restriction_office_name")
    private String restrictionOfficeName;
    @Column(name = "restriction_release_office_name")
    private String restrictionReleaseOfficeName;
    @Column(name = "restriction_office_address")
    private String restrictionOfficeAddress;
    @Column(name = "restriction_release_reason_code")
    private String restrictionReleaseReasonCode;
    @Column(name = "tenancy_type_code")
    private String tenancyTypeCode;
    @Column(name = "owner_type_code")
    private String ownerTypeCode;
//    @Column(name="ownership_type_code")    
//    private String ownerShipTypeCode;    
    @Column(name = "share_type_code")//should remove
    private String shareTypeCode;
    @Column(name = "bundle_number")
    private String bundleNumber;
    @Column(name = "bundle_page_no")
    private String bundlePageNo;
    @Column(name = "valuation_amount")
    private double valuationAmount;
    @Column(name = "tax_amount")
    private double taxAmount;
    // Other fields
    private Boolean locked = null;

    public Rrr() {
        super();
    }

    private String generateRrrNumber() {
        String result = "";
        SystemEJBLocal systemEJB = RepositoryUtility.tryGetEJB(SystemEJBLocal.class);
        if (systemEJB != null) {
            Result newNumberResult = systemEJB.checkRuleGetResultSingle("generate-rrr-nr", null);
            if (newNumberResult != null && newNumberResult.getValue() != null) {
                result = newNumberResult.getValue().toString();
            }
        }
        return result;
    }

    private Transaction getTransaction() {
        Transaction result = null;
        TransactionEJBLocal transactionEJB = RepositoryUtility.tryGetEJB(TransactionEJBLocal.class);
        if (transactionEJB != null) {
            result = transactionEJB.getTransactionById(getTransactionId(), Transaction.class);
        }
        return result;
    }

    public String getId() {
        id = id == null ? generateId() : id;
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBaUnitId() {
        return baUnitId;
    }

    public void setBaUnitId(String baUnitId) {
        this.baUnitId = baUnitId;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public BigDecimal getMortgageAmount() {
        return mortgageAmount;
    }

    public void setMortgageAmount(BigDecimal mortgageAmount) {
        this.mortgageAmount = mortgageAmount;
    }

    public BigDecimal getMortgageInterestRate() {
        return mortgageInterestRate;
    }

    public void setMortgageInterestRate(BigDecimal mortgageInterestRate) {
        this.mortgageInterestRate = mortgageInterestRate;
    }

    public Integer getMortgageRanking() {
        return mortgageRanking;
    }

    public void setMortgageRanking(Integer mortgageRanking) {
        this.mortgageRanking = mortgageRanking;
    }

    public String getMortgageTypeCode() {
        return mortgageTypeCode;
    }

    public void setMortgageTypeCode(String mortgageTypeCode) {
        this.mortgageTypeCode = mortgageTypeCode;
    }

    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
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

    public BaUnitNotation getNotation() {
        return notation;
    }

    public void setNotation(BaUnitNotation notation) {
        this.notation = notation;
    }

    public List<Source> getSourceList() {
        return sourceList;
    }

    public void setSourceList(List<Source> sourceList) {
        this.sourceList = sourceList;
    }

    public List<Party> getRightHolderList() {
        return rightHolderList;
    }

    public void setRightHolderList(List<Party> rightHolderList) {
        this.rightHolderList = rightHolderList;
    }

    public Boolean isLocked() {
        if (locked == null) {
            locked = false;
            Transaction transaction = getTransaction();
            if (transaction != null
                    && transaction.getStatusCode().equals(TransactionStatusType.COMPLETED)) {
                locked = true;
            }
        }
        return locked;
    }

    public LocWithMoth getLoc() {
        return loc;
    }

    public void setLoc(LocWithMoth loc) {
        this.loc = loc;
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
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

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public boolean isTerminating() {
        return terminating;
    }

    public void setTerminating(boolean terminating) {
        this.terminating = terminating;
    }

    public String getRestrictionReasonCode() {
        return restrictionReasonCode;
    }

    public String getOwnerTypeCode() {
        return ownerTypeCode;
    }

    public void setOwnerTypeCode(String ownerTypeCode) {
        this.ownerTypeCode = ownerTypeCode;
    }

    public String getShareTypeCode() {
        return shareTypeCode;
    }

    public void setShareTypeCode(String shareTypeCode) {
        this.shareTypeCode = shareTypeCode;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public double getValuationAmount() {
        return valuationAmount;
    }

    public void setValuationAmount(double valuationAmount) {
        this.valuationAmount = valuationAmount;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    @Override
    public void preSave() {
        if (this.isNew()) {
            setTransactionId(LocalInfo.getTransactionId());
        }

        if (isNew() && getNr() == null) {
            // Assign a generated number to the Rrr if it is not currently set.
            setNr(generateRrrNumber());
        }
        super.preSave();
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getRestrictionOfficeName() {
        return restrictionOfficeName;
    }

    public void setRestrictionOfficeName(String restrictionOfficeName) {
        this.restrictionOfficeName = restrictionOfficeName;
    }

    public String getRestrictionReleaseOfficeName() {
        return restrictionReleaseOfficeName;
    }

    public void setRestrictionReleaseOfficeName(String restrictionReleaseOfficeName) {
        this.restrictionReleaseOfficeName = restrictionReleaseOfficeName;
    }

    public String getRestrictionOfficeAddress() {
        return restrictionOfficeAddress;
    }

    public void setRestrictionOfficeAddress(String restrictionOfficeAddress) {
        this.restrictionOfficeAddress = restrictionOfficeAddress;
    }

    public String getRestrictionReleaseReasonCode() {
        return restrictionReleaseReasonCode;
    }

    public void setRestrictionReleaseReasonCode(String restrictionReleaseReasonCode) {
        this.restrictionReleaseReasonCode = restrictionReleaseReasonCode;
    }

    public String getTenancyTypeCode() {
        return tenancyTypeCode;
    }

    public void setTenancyTypeCode(String tenancyTypeCode) {
        this.tenancyTypeCode = tenancyTypeCode;
    }

    public String getBundleNumber() {
        return bundleNumber;
    }

    public void setBundleNumber(String bundleNumber) {
        this.bundleNumber = bundleNumber;
    }

    public String getBundlePageNo() {
        return bundlePageNo;
    }

    public void setBundlePageNo(String bundlePageNo) {
        this.bundlePageNo = bundlePageNo;
    }
}
