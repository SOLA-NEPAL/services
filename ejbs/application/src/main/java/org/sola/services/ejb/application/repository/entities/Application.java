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
package org.sola.services.ejb.application.repository.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.AccessFunctions;
import org.sola.services.common.repository.ChildEntity;
import org.sola.services.common.repository.ChildEntityList;
import org.sola.services.common.repository.ExternalEJB;
import org.sola.services.common.repository.RepositoryUtility;
import org.sola.services.common.repository.entities.AbstractVersionedEntity;
import org.sola.services.ejb.party.businesslogic.PartyEJBLocal;
import org.sola.services.ejb.party.repository.entities.Party;
import org.sola.services.ejb.search.businesslogic.SearchEJBLocal;
import org.sola.services.ejb.search.repository.entities.BaUnitSearchResult;
import org.sola.services.ejb.source.businesslogic.SourceEJBLocal;
import org.sola.services.ejb.source.repository.entities.Source;
import org.sola.services.ejb.system.br.Result;
import org.sola.services.ejb.system.businesslogic.SystemEJBLocal;

/**
 *
 * @author soladev
 */
@Table(name = "application", schema = "application")
public class Application extends AbstractVersionedEntity {

    public static final String ACTION_LODGED = "lodged";
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "nr")
    private String nr;
    @Column(name = "lodging_datetime")
    private Date lodgingDatetime;
    @Column(name = "expected_completion_date")
    private Date expectedCompletionDate;
    @Column(name = "assigned_datetime", insertable = true, updatable = false)
    private Date assignedDatetime;
    @Column(name = "assignee_id", insertable = true, updatable = false)
    private String assigneeId;
    @Column(name = "action_code", insertable = false, updatable = false)
    private String actionCode;
    @Column(name = "action_notes")
    private String actionNotes;
    @Column(name = "contact_person_id")
    private String contactPersonId;
    @Column(name = "agent_id")
    private String agentId;
    @Column(name = "status_code", insertable = false, updatable = false)
    private String statusCode = null;
    @Column(name = "services_fee")
    private BigDecimal servicesFee;
    @Column(name = "tax")
    private BigDecimal tax;
    @Column(name = "valuation_amount")
    private BigDecimal valuationAmount;
    @Column(name = "total_amount_paid")
    private BigDecimal totalAmountPaid;
    @Column(name = "fee_paid")
    private boolean feePaid;
    @Column(name = "receipt_number")
    private String receiptNumber;
    @Column(name = "receipt_date")
    private Date receiptDate;
    @Column(name = "payment_remarks")
    private String paymentRemarks;
    @Column(name = "office_code", updatable = false)
    private String officeCode;
    @Column(name = "fy_code", updatable = false)
    private String fiscalYearCode;
    @Column(name = "status_change_time", updatable = false, insertable = false)
    @AccessFunctions(onSelect = "application.f_get_application_status_change_date(id)")
    private Date statusChangeDate;
    @ExternalEJB(ejbLocalClass = PartyEJBLocal.class, loadMethod = "getParty")
    @ChildEntity(childIdField = "contactPersonId", readOnly = true)
    private Party contactPerson;
    @ExternalEJB(ejbLocalClass = PartyEJBLocal.class, loadMethod = "getParty")
    @ChildEntity(childIdField = "agentId", readOnly = true)
    private Party agent;
    @ChildEntityList(parentIdField = "applicationId")
    private List<Service> serviceList;
    @ExternalEJB(ejbLocalClass = SearchEJBLocal.class, loadMethod = "searchBaUnitsByIds")
    @ChildEntityList(parentIdField = "applicationId", childIdField = "baUnitId",
    manyToManyClass = ApplicationProperty.class, readOnly = true)
    private List<BaUnitSearchResult> propertyList;
    @ExternalEJB(ejbLocalClass = SourceEJBLocal.class,
    loadMethod = "getSources", saveMethod = "saveSource")
    @ChildEntityList(parentIdField = "applicationId", childIdField = "sourceId",
    manyToManyClass = ApplicationUsesSource.class)
    private List<Source> sourceList;

    public Application() {
        super();
    }

    private String generateApplicationNumber() {
        String result = "";
        SystemEJBLocal systemEJB = RepositoryUtility.tryGetEJB(SystemEJBLocal.class);
        if (systemEJB != null) {
            Result newNumberResult = systemEJB.checkRuleGetResultSingle("generate-application-nr", null);
            if (newNumberResult != null && newNumberResult.getValue() != null) {
                result = newNumberResult.getValue().toString();
            }
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

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getActionNotes() {
        return actionNotes;
    }

    public void setActionNotes(String actionNotes) {
        this.actionNotes = actionNotes;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public Date getAssignedDatetime() {
        return assignedDatetime;
    }

    public void setAssignedDatetime(Date assignedDatetime) {
        this.assignedDatetime = assignedDatetime;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getContactPersonId() {
        return contactPersonId;
    }

    public void setContactPersonId(String contactPersonId) {
        this.contactPersonId = contactPersonId;
    }

    public Date getExpectedCompletionDate() {
        return expectedCompletionDate;
    }

    public void setExpectedCompletionDate(Date expectedCompletionDate) {
        this.expectedCompletionDate = expectedCompletionDate;
    }

    public boolean isFeePaid() {
        return feePaid;
    }

    public void setFeePaid(boolean feePaid) {
        this.feePaid = feePaid;
    }

    public Date getLodgingDatetime() {
        return lodgingDatetime;
    }

    public void setLodgingDatetime(Date lodgingDatetime) {
        this.lodgingDatetime = lodgingDatetime;
    }

    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

    public BigDecimal getServicesFee() {
        return servicesFee;
    }

    public void setServicesFee(BigDecimal servicesFee) {
        this.servicesFee = servicesFee;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotalAmountPaid() {
        return totalAmountPaid;
    }

    public void setTotalAmountPaid(BigDecimal totalAmountPaid) {
        this.totalAmountPaid = totalAmountPaid;
    }

    public BigDecimal getValuationAmount() {
        return valuationAmount;
    }

    public void setValuationAmount(BigDecimal valuationAmount) {
        this.valuationAmount = valuationAmount;
    }

    public String getPaymentRemarks() {
        return paymentRemarks;
    }

    public void setPaymentRemarks(String paymentRemarks) {
        this.paymentRemarks = paymentRemarks;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public Date getStatusChangeDate() {
        return statusChangeDate;
    }

    public void setStatusChangeDate(Date statusChangeDate) {
        this.statusChangeDate = statusChangeDate;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public Party getAgent() {
        return agent;
    }

    public void setAgent(Party agent) {
        this.agent = agent;
        if (agent != null) {
            this.setAgentId(agent.getId());
        }
    }

    public Party getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(Party contactPerson) {
        this.contactPerson = contactPerson;
        if (contactPerson != null) {
            this.setContactPersonId(contactPerson.getId());
        }
    }

    public List<BaUnitSearchResult> getPropertyList() {
        propertyList = propertyList == null ? new ArrayList<BaUnitSearchResult>() : propertyList;
        return propertyList;
    }

    public void setPropertyList(List<BaUnitSearchResult> propertyList) {
        this.propertyList = propertyList;
    }

    public List<Service> getServiceList() {
        serviceList = serviceList == null ? new ArrayList<Service>() : serviceList;
        return serviceList;
    }

    public void setServiceList(List<Service> serviceList) {
        this.serviceList = serviceList;
    }

    public List<Source> getSourceList() {
        sourceList = sourceList == null ? new ArrayList<Source>() : sourceList;
        return sourceList;
    }

    public void setSourceList(List<Source> sourceList) {
        this.sourceList = sourceList;
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

    @Override
    public void preSave() {

        if (isNew() && getNr() == null) {
            setNr(generateApplicationNumber());
        }

        super.preSave();
    }
}
