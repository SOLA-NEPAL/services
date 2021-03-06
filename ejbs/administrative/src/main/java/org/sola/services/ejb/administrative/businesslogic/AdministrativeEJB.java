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
package org.sola.services.ejb.administrative.businesslogic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.sola.common.RolesConstants;
import org.sola.common.SOLAException;
import org.sola.common.mapping.MappingManager;
import org.sola.common.messaging.ServiceMessage;
import org.sola.services.common.EntityAction;
import org.sola.services.common.LocalInfo;
import org.sola.services.common.StatusConstants;
import org.sola.services.common.br.ValidationResult;
import org.sola.services.common.ejbs.AbstractEJB;
import org.sola.services.common.repository.CommonSqlProvider;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;
import org.sola.services.ejb.administrative.repository.entities.*;
import org.sola.services.ejb.cadastre.businesslogic.CadastreEJBLocal;
import org.sola.services.ejb.party.businesslogic.PartyEJBLocal;
import org.sola.services.ejb.party.repository.entities.Party;
import org.sola.services.ejb.search.businesslogic.SearchEJBLocal;
import org.sola.services.ejb.source.businesslogic.SourceEJBLocal;
import org.sola.services.ejb.system.businesslogic.SystemEJBLocal;
import org.sola.services.ejb.system.repository.entities.BrValidation;
import org.sola.services.ejb.transaction.businesslogic.TransactionEJBLocal;
import org.sola.services.ejb.transaction.repository.entities.RegistrationStatusType;
import org.sola.services.ejb.transaction.repository.entities.Transaction;
import org.sola.services.ejb.transaction.repository.entities.TransactionBasic;
import org.sola.services.ejbs.admin.businesslogic.AdminEJBLocal;

@Stateless
@EJB(name = "java:global/SOLA/AdministrativeEJBLocal", beanInterface = AdministrativeEJBLocal.class)
public class AdministrativeEJB extends AbstractEJB implements AdministrativeEJBLocal {

    @EJB
    private PartyEJBLocal partyEJB;
    @EJB
    private SourceEJBLocal sourceEJB;
    @EJB
    private SystemEJBLocal systemEJB;
    @EJB
    private TransactionEJBLocal transactionEJB;
    @EJB
    private CadastreEJBLocal cadastreEJB;
    @EJB
    private AdminEJBLocal adminEJB;
    @EJB
    private SearchEJBLocal searchEJB;

    @Override
    protected void postConstruct() {
        setEntityPackage(BaUnit.class.getPackage().getName());
    }

    @Override
    public List<ChangeStatusType> getChangeStatusTypes(String languageCode) {
        return getRepository().getCodeList(ChangeStatusType.class, languageCode);
    }

    @Override
    public List<BaUnitType> getBaUnitTypes(String languageCode) {
        return getRepository().getCodeList(BaUnitType.class, languageCode);
    }

    @Override
    public List<MortgageType> getMortgageTypes(String languageCode) {
        return getRepository().getCodeList(MortgageType.class, languageCode);
    }

    @Override
    public List<RrrGroupType> getRRRGroupTypes(String languageCode) {
        return getRepository().getCodeList(RrrGroupType.class, languageCode);
    }

    @Override
    public List<RrrType> getRRRTypes(String languageCode) {
        return getRepository().getCodeList(RrrType.class, languageCode);
    }

    @Override
    public BaUnit getBaUnitByCode(String nameFirstPart, String nameLastPart) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, BaUnit.QUERY_WHERE_BYPROPERTYCODE);
        params.put(BaUnit.QUERY_PARAMETER_FIRSTPART, nameFirstPart);
        params.put(BaUnit.QUERY_PARAMETER_LASTPART, nameLastPart);
        return getRepository().getEntity(BaUnit.class, params);
    }

    @Override
    @RolesAllowed(RolesConstants.ADMINISTRATIVE_BA_UNIT_SAVE)
    public BaUnit saveBaUnit(String serviceId, BaUnit baUnit) {
        if (baUnit == null) {
            return null;
        }
        TransactionBasic transaction =
                transactionEJB.getTransactionByServiceId(serviceId, true, TransactionBasic.class);
        LocalInfo.setTransactionId(transaction.getId());

        return saveBaUnit(baUnit);
    }

    @Override
    public BaUnit getBaUnitById(String id) {
        BaUnit result = null;
        if (id != null) {
            result = getRepository().getEntity(BaUnit.class, id);
        }
        return result;
    }

    @Override
    public List<ValidationResult> approveTransaction(
            String transactionId, String approvedStatus,
            boolean validateOnly, String languageCode) {
        LocalInfo.setTransactionId(transactionId);
        List<ValidationResult> validationResult = new ArrayList<ValidationResult>();

        //Change the status of BA Units that are involved in a transaction directly
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, BaUnit.QUERY_WHERE_BYTRANSACTIONID);
        params.put(BaUnit.QUERY_PARAMETER_TRANSACTIONID, transactionId);
        List<BaUnitStatusChanger> baUnitList =
                getRepository().getEntityList(BaUnitStatusChanger.class, params);

        for (BaUnitStatusChanger baUnit : baUnitList) {

            adminEJB.checkOfficeCode(baUnit.getOfficeCode());

            validationResult.addAll(this.validateBaUnit(baUnit, languageCode));
            if (systemEJB.validationSucceeded(validationResult) && !validateOnly) {
                if (baUnit.getStatusCode().equals(StatusConstants.PENDING)) {
                    baUnit.setStatusCode(StatusConstants.CURRENT);
                }
                getRepository().saveEntity(baUnit);
            }
        }

        // Get prior properties and mark them for termination
        if (!validateOnly) {
            params = new HashMap<String, Object>();
            params.put(CommonSqlProvider.PARAM_WHERE_PART, ParentBaUnitInfo.QUERY_WHERE_GET_FOR_TERMINATION);
            params.put(ParentBaUnitInfo.PARAM_TRANSACTION_ID, transactionId);
            List<ParentBaUnitInfo> parentBaUnits = getRepository().getEntityList(ParentBaUnitInfo.class, params);
            HashMap<String, String> relatedBaUnitIds = new HashMap<String, String>();
            
            if (parentBaUnits != null) {
                for (ParentBaUnitInfo parentBaUnit : parentBaUnits) {
                    if(!relatedBaUnitIds.containsKey(parentBaUnit.getRelatedBaUnitId())){
                        terminateBaUnitByTransactionId(parentBaUnit.getRelatedBaUnitId(), transactionId);
                    }
                    relatedBaUnitIds.put(parentBaUnit.getRelatedBaUnitId(), parentBaUnit.getRelatedBaUnitId());
                }
            }
        }

        // Gets BaUnits for termination and terminate underlaying RRRs
        params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, BaUnit.QUERY_WHERE_BY_TERMINATING);
        params.put(BaUnit.QUERY_PARAMETER_TRANSACTIONID, transactionId);
        List<BaUnit> terminatingBaUnits = getRepository().getEntityList(BaUnit.class, params);

        for (BaUnit baUnit : terminatingBaUnits) {
            if (baUnit.getStatusCode().equals(StatusConstants.PENDING) && !validateOnly) {
                throw new SOLAException(ServiceMessage.EJB_ADMINISTRATIVE_PENGIN_BA_UNIT_STATUS,
                        new String[]{baUnit.getNameFirstPart() + "/" + baUnit.getNameLastPart()});
            }

            adminEJB.checkOfficeCode(baUnit.getOfficeCode());
            BaUnitStatusChanger baUnitStatusChanger = new BaUnitStatusChanger();
            baUnitStatusChanger.setOfficeCode(baUnit.getOfficeCode());
            baUnitStatusChanger.setTransactionId(transactionId);
            baUnitStatusChanger.setId(baUnit.getId());
            baUnitStatusChanger.setRowId(baUnit.getRowId());
            baUnitStatusChanger.setRowVersion(baUnit.getRowVersion());
            baUnitStatusChanger.setChangeUser(baUnit.getChangeUser());

            validationResult.addAll(this.validateBaUnit(baUnitStatusChanger, languageCode));
            if (systemEJB.validationSucceeded(validationResult) && !validateOnly) {
                for (Rrr rrr : baUnit.getRrrList()) {
                    if (rrr.getStatusCode().equals(StatusConstants.PENDING)) {
                        throw new SOLAException(ServiceMessage.EJB_ADMINISTRATIVE_PENGIN_RRR_EXISTS,
                                new String[]{baUnit.getNameFirstPart() + "/" + baUnit.getNameLastPart()});
                    }
                    if (rrr.getStatusCode().equals(StatusConstants.CURRENT)) {
                        // Terminate current right by creating pending with terminating flag
                        Rrr pendingRrr = MappingManager.getMapper().map(rrr, Rrr.class);
                        pendingRrr.setTerminating(true);
                        pendingRrr.setId(null);
                        pendingRrr.setTransactionId(transactionId);
                        pendingRrr.setRowId(null);
                        pendingRrr.setRowVersion(0);
                        BaUnitNotation notation = new BaUnitNotation();
                        notation.setNotationText("Property termination");
                        pendingRrr.setNotation(notation);
                        getRepository().saveEntity(pendingRrr);
                    }
                }
                // Change BaUnit status to historic
                baUnitStatusChanger.setStatusCode(StatusConstants.HISTORIC);
                baUnitStatusChanger.setEntityAction(EntityAction.UPDATE);
                getRepository().saveEntity(baUnitStatusChanger);
            }
        }

        // Handle RRRs
        params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, Rrr.QUERY_WHERE_BYTRANSACTIONID);
        params.put(Rrr.QUERY_PARAMETER_TRANSACTIONID, transactionId);
        List<RrrStatusChanger> rrrStatusChangerList =
                getRepository().getEntityList(RrrStatusChanger.class, params);
        for (RrrStatusChanger rrr : rrrStatusChangerList) {

            adminEJB.checkOfficeCode(rrr.getOfficeCode());

            validationResult.addAll(this.validateRrr(rrr, languageCode));
            if (systemEJB.validationSucceeded(validationResult) && !validateOnly) {
                if (rrr.isTerminating()) {
                    rrr.setStatusCode(StatusConstants.HISTORIC);
                } else {
                    rrr.setStatusCode(StatusConstants.CURRENT);
                }
                getRepository().saveEntity(rrr);
            }
        }

        if (!validateOnly) {
            params = new HashMap<String, Object>();
            params.put(CommonSqlProvider.PARAM_WHERE_PART, BaUnitNotation.QUERY_WHERE_BYTRANSACTIONID);
            params.put(BaUnitNotation.QUERY_PARAMETER_TRANSACTIONID, transactionId);
            List<BaUnitNotationStatusChanger> baUnitNotationList =
                    getRepository().getEntityList(BaUnitNotationStatusChanger.class, params);
            for (BaUnitNotationStatusChanger baUnitNotation : baUnitNotationList) {
                baUnitNotation.setStatusCode(RegistrationStatusType.STATUS_CURRENT);
                getRepository().saveEntity(baUnitNotation);
            }
        }

        return validationResult;
    }

    /**
     * It runs the business rules for validating BAUnit.
     *
     * @param baUnit
     * @param languageCode
     * @return
     */
    private List<ValidationResult> validateBaUnit(
            BaUnitStatusChanger baUnit, String languageCode) {
        List<BrValidation> brValidationList = this.systemEJB.getBrForValidatingTransaction(
                "ba_unit", RegistrationStatusType.STATUS_CURRENT, null);
        HashMap<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("id", baUnit.getId());
        //Run the validation
        return this.systemEJB.checkRulesGetValidation(brValidationList, languageCode, params);
    }

    /**
     * It runs the business rules for validating Rrr.
     *
     * @param rrr
     * @param languageCode
     * @return
     */
    private List<ValidationResult> validateRrr(
            RrrStatusChanger rrr, String languageCode) {
        List<BrValidation> brValidationList = this.systemEJB.getBrForValidatingRrr(
                RegistrationStatusType.STATUS_CURRENT, rrr.getTypeCode());
        HashMap<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("id", rrr.getId());
        //Run the validation
        return this.systemEJB.checkRulesGetValidation(brValidationList, languageCode, params);
    }

    @Override
    public List<BaUnit> getBaUnitsByTransactionId(String transactionId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, BaUnit.QUERY_WHERE_BY_TRANSACTION_ID_EXTENDED);
        params.put(BaUnit.QUERY_PARAMETER_TRANSACTIONID, transactionId);
        return getRepository().getEntityList(BaUnit.class, params);
    }

    @Override
    public List<BaUnitRelType> getBaUnitRelTypes(String languageCode) {
        return getRepository().getCodeList(BaUnitRelType.class, languageCode);
    }

    @Override
    @RolesAllowed(RolesConstants.ADMINISTRATIVE_BA_UNIT_SAVE)
    public BaUnit terminateBaUnit(String baUnitId, String serviceId) {
        if (baUnitId == null || serviceId == null) {
            return null;
        }

        // Check transaction to exist and have pending status
        Transaction transaction = transactionEJB.getTransactionByServiceId(
                serviceId, true, Transaction.class);
        if (transaction == null || !transaction.getStatusCode().equals(RegistrationStatusType.STATUS_PENDING)) {
            return null;
        }

        //TODO: Put BR check to have only one pending transaction for the BaUnit and BaUnit to be with "current" status.
        //TODO: Check BR for service to have cancel action and empty Rrr field.
        terminateBaUnitByTransactionId(baUnitId, transaction.getId());
        return getBaUnitById(baUnitId);
    }

    private void terminateBaUnitByTransactionId(String baUnitId, String transactionId) {
        if (baUnitId == null || transactionId == null) {
            return;
        }
        BaUnitTarget baUnitTarget = new BaUnitTarget();
        baUnitTarget.setBaUnitId(baUnitId);
        baUnitTarget.setTransactionId(transactionId);
        getRepository().saveEntity(baUnitTarget);
    }

    @Override
    @RolesAllowed(RolesConstants.ADMINISTRATIVE_BA_UNIT_SAVE)
    public BaUnit cancelBaUnitTermination(String baUnitId) {
        if (baUnitId == null) {
            return null;
        }

        BaUnitBasic baUnit = getRepository().getEntity(BaUnitBasic.class, baUnitId);
        adminEJB.checkOfficeCode(baUnit.getOfficeCode());

        Map<String, Object> params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, BaUnitTarget.QUERY_WHERE_GET_BY_BAUNITID);
        params.put(BaUnitTarget.PARAM_BAUNIT_ID, baUnitId);

        List<BaUnitTarget> targets = getRepository().getEntityList(BaUnitTarget.class, params);

        if (targets != null || targets.size() > 0) {
            for (BaUnitTarget baUnitTarget : targets) {

                Transaction transaction = transactionEJB.getTransactionById(
                        baUnitTarget.getTransactionId(), Transaction.class);
                if (transaction != null
                        || transaction.getStatusCode().equals(RegistrationStatusType.STATUS_PENDING)) {
                    // DELETE peding 
                    baUnitTarget.setEntityAction(EntityAction.DELETE);
                    getRepository().saveEntity(baUnitTarget);
                }
            }
        }

        return getBaUnitById(baUnitId);
    }

    @Override
    @RolesAllowed(RolesConstants.ADMINISTRATIVE_BA_UNIT_SAVE)
    public Moth saveMoth(Moth moth) {
        if (moth.isNew()) {
            moth.setOfficeCode(adminEJB.getCurrentOfficeCode());
            moth.setFiscalYearCode(adminEJB.getCurrentFiscalYearCode());
        } else {
            adminEJB.checkOfficeCode(moth.getOfficeCode());
        }
        return getRepository().saveEntity(moth);
    }

    @Override
    public Moth getMoth(String id) {
        return getRepository().getEntityByOffice(Moth.class, id, adminEJB.getCurrentOfficeCode());
    }

    @Override
    public List<Moth> getMoths(String vdcCode, String mothLuj) {
        HashMap params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, Moth.GET_BY_VDC_AND_MOTHLUJ);
        params.put(Moth.VDC_PARAM, vdcCode);
        params.put(Moth.MOTH_LUJ_PARAM, mothLuj);
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        List<Moth> mth = getRepository().getEntityList(Moth.class, params);
        return mth;
    }

    @Override
    public Moth getMoth(String vdcCode, String mothLuj, String mothLujNumber) {
        HashMap params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, Moth.GET_BY_VDC_MOTHLUJ_AND_MOTHLUJ_NUMBER);
        params.put(Moth.VDC_PARAM, vdcCode);
        params.put(Moth.MOTH_LUJ_PARAM, mothLuj);
        params.put(Moth.MOTH_LUJ_NUMBER_PARAM, mothLujNumber);
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        return getRepository().getEntity(Moth.class, params);
    }

    @Override
    @RolesAllowed(RolesConstants.ADMINISTRATIVE_BA_UNIT_SAVE)
    public Loc saveLoc(Loc loc) {
        if (loc.isNew()) {
            if (getMoth(loc.getMothId()) == null) {
                throw new SOLAException(ServiceMessage.EXCEPTION_OBJECT_OUT_OF_OFFICE);
            }
            loc.setOfficeCode(adminEJB.getCurrentOfficeCode());
        } else {
            adminEJB.checkOfficeCode(loc.getOfficeCode());
        }
        return getRepository().saveEntity(loc);
    }

    @Override
    public Loc getLoc(String id) {
        return getRepository().getEntityByOffice(Loc.class, id, adminEJB.getCurrentOfficeCode());
    }

    @Override
    public LocWithMoth getLocWithMoth(String id) {
        return getRepository().getEntityByOffice(LocWithMoth.class, id, adminEJB.getCurrentOfficeCode());
    }

    @Override
    @RolesAllowed(RolesConstants.ADMINISTRATIVE_BA_UNIT_SAVE)
    public BaUnit saveBaUnit(BaUnit baUnit) {
        if(baUnit==null){
            return null;
        }
        
        // Check VDC access
        if(baUnit.getCadastreObject()!=null && baUnit.getCadastreObject().getAddress()!=null){
            adminEJB.checkVdcWardAccess(baUnit.getCadastreObject().getAddress().getVdcCode(), 
                    baUnit.getCadastreObject().getAddress().getWardNo(), true);
        }
        
        Object statusCode;

        
        if (baUnit.isNew()) {
            baUnit.setStatusCode(StatusConstants.PENDING);
            baUnit.setOfficeCode(adminEJB.getCurrentOfficeCode());
            baUnit.setFiscalYearCode(adminEJB.getCurrentFiscalYearCode());
            // Check cadastre object
            if (baUnit.getCadastreObject() != null && !baUnit.getCadastreObject().isNew()
                    && (baUnit.getCadastreObject().getEntityAction() == null)) {
                baUnit.getCadastreObject().setEntityAction(EntityAction.UPDATE);
            }
        } else {
            statusCode = baUnit.getOriginalValue("statusCode");
            if (statusCode != null) {
                if (!statusCode.toString().equals(StatusConstants.PENDING) && baUnit.isModified()) {
                    throw new SOLAException(ServiceMessage.EJB_ADMINISTRATIVE_BAUNIT_MODIFICATION_NOT_ALLOWED);
                }
            }
            adminEJB.checkOfficeCode(baUnit.getOfficeCode());
        }
        if (baUnit.getRrrList() != null) {
            for (Rrr rrr : baUnit.getRrrList()) {
                if (rrr.isNew()) {
                    rrr.setOfficeCode(adminEJB.getCurrentOfficeCode());
                    rrr.setFiscalYearCode(adminEJB.getCurrentFiscalYearCode());
                } else {
                    adminEJB.checkOfficeCode(rrr.getOfficeCode());
                    statusCode = rrr.getOriginalValue("statusCode");
                    if (statusCode != null) {
                        if (!statusCode.toString().equals(StatusConstants.PENDING) && rrr.isModified()) {
                            throw new SOLAException(ServiceMessage.EJB_ADMINISTRATIVE_BAUNIT_MODIFICATION_NOT_ALLOWED);
                        }
                    }
                }
            }
        }

        BaUnit newBaUnit = getBaUnitById(getRepository().saveEntity(baUnit).getId());
        if (synchronizeBaUnits(newBaUnit)) {
            // Retrieve new version of BaUnitBean
            return getBaUnitById(newBaUnit.getId());
        } else {
            return newBaUnit;
        }
    }

    /**
     * Propagates ownership values of the given BaUnit over other BaUnit,
     * belonging to the same LOC.
     */
    private boolean synchronizeBaUnits(BaUnit baUnit) {

        Rrr tmpRrr = null;
        boolean hasChanges = false;

        // Search for pending RRR
        for (Rrr rrr : baUnit.getRrrList()) {
            if (rrr.getStatusCode().equals(StatusConstants.PENDING)
                    && !rrr.isTerminating() && rrr.getLocId() != null) {
                tmpRrr = rrr;
                break;
            }
        }

        // Search for current RRR
        if (tmpRrr == null) {
            for (Rrr rrr : baUnit.getRrrList()) {
                if (rrr.getStatusCode().equals(StatusConstants.CURRENT) && rrr.getLocId() != null) {
                    tmpRrr = rrr;
                    break;
                }
            }
        }

        if (tmpRrr == null) {
            return false;
        }

        RrrLoc pendingRrrLoc = null;

        if (tmpRrr.getStatusCode().equals(StatusConstants.PENDING)) {
            // Use pending RRR from the provided BaUnit
            pendingRrrLoc = new RrrLoc();
            pendingRrrLoc.setLocId(tmpRrr.getLocId());
            pendingRrrLoc.setRightHolderList(tmpRrr.getRightHolderList());
            pendingRrrLoc.setStatusCode(tmpRrr.getStatusCode());
            pendingRrrLoc.setTypeCode(tmpRrr.getTypeCode());
            pendingRrrLoc.setOwnerTypeCode(tmpRrr.getOwnerTypeCode());
            pendingRrrLoc.setOwnershipTypeCode(tmpRrr.getOwnershipTypeCode());
        } else {
            // Pick up pending from the common LOC
            List<RrrLoc> rrrLocs = getRrrLocsById(tmpRrr.getLocId());
            for (RrrLoc rrrLoc : rrrLocs) {
                if (rrrLoc.getStatusCode().equals(StatusConstants.PENDING)) {
                    pendingRrrLoc = rrrLoc;
                    break;
                }
            }
        }

        // If no pending RRR on the provided BaUnit and no pending on the common LOC, than exit.
        if (pendingRrrLoc == null) {
            return false;
        }

        // Get RRRs by LOC for synchronizing
        List<Rrr> rrrs = getRrrsByLoc(pendingRrrLoc.getLocId());
        List<Rrr> newRrrs = new ArrayList<Rrr>();

        for (Rrr rrr : rrrs) {
            if (rrr.getStatusCode().equals(StatusConstants.PENDING) && !rrr.isTerminating()) {
                if (compareRrrAndLoc(rrr, pendingRrrLoc) == false) {
                    // Check transaction id to be the same as current
                    if (!rrr.getTransactionId().equals(LocalInfo.getTransactionId())) {
                        throw new SOLAException(ServiceMessage.EJB_ADMINISTRATIVE_RRR_MODIFIED_BY_ANOTHER_SERVICE);
                    }
                    // Update rrr if it is different from pending 
                    createUpdateRrrByRrrLoc(rrr, pendingRrrLoc);
                    hasChanges = true;
                }
                // Remove pending if it is equal to current 
                //(skip current RRR, to avoid disaapearing on the client form)
                //if (!rrr.getBaUnitId().equals(baUnit.getId())) {
                // Look for the current record
//                for (Rrr rrr2 : rrrs) {
//                    if (rrr2.getNr() != null && rrr.getNr() != null && !rrr2.getId().equals(rrr.getId())
//                            && rrr2.getNr().equals(rrr.getNr())
//                            && rrr2.getStatusCode().equals(StatusConstants.CURRENT)) {
//                        // Check if current and pending are equal, remove pending
//                        if (compareRrrs(rrr, rrr2)) {
//                            rrr.setEntityAction(EntityAction.DELETE);
//                            hasChanges = true;
//                        }
//                        break;
//                    }
//                }
                //}
            }

            if (rrr.getStatusCode().equals(StatusConstants.CURRENT)) {

                // Check if there is corresponding pending
                boolean isPending = false;
                for (Rrr rrr2 : rrrs) {
                    if (rrr2.getNr() != null && rrr.getNr() != null && !rrr2.getId().equals(rrr.getId())
                            && rrr2.getNr().equals(rrr.getNr())
                            && rrr2.getStatusCode().equals(StatusConstants.PENDING)) {
                        isPending = true;
                        break;
                    }
                }

                if (!isPending && compareRrrAndLoc(rrr, pendingRrrLoc) == false) {
                    // Create pending
                    Rrr newRrr = MappingManager.getMapper().map(rrr, Rrr.class);
                    newRrr.setId(null);
                    newRrr.setRowId(null);
                    newRrr.setRowVersion(0);
                    newRrr.setFiscalYearCode(adminEJB.getCurrentFiscalYearCode());

                    if(newRrr.getNotation()!=null){
                        newRrr.getNotation().setId(null);
                        newRrr.getNotation().setRowId(null);
                        newRrr.getNotation().setRowVersion(0);
                    }
                    
                    createUpdateRrrByRrrLoc(newRrr, pendingRrrLoc);
                    newRrrs.add(newRrr);
                    hasChanges = true;
                }
            }

        }

        rrrs.addAll(newRrrs);

        if (hasChanges) {
            for (Rrr rrr : rrrs) {
                getRepository().saveEntity(rrr);
            }
        }
        return hasChanges;
    }

    private Rrr createUpdateRrrByRrrLoc(Rrr rrr, RrrLoc rrrLoc) {
        if (rrrLoc == null) {
            return rrr;
        }

        if (rrr == null) {
            rrr = new Rrr();
            rrr.setLocId(rrrLoc.getLocId());
        }

        // Update rightholders
        if (rrrLoc.getRightHolderList() == null) {
            if (rrr.getRightHolderList() != null) {
                for (Party party : rrr.getRightHolderList()) {
                    party.setEntityAction(EntityAction.DISASSOCIATE);
                }
            }
        } else {
            if (rrr.getRightHolderList() == null) {
                rrr.setRightHolderList(new ArrayList<Party>());
            }
            // Remove if not in rrrLoc list
            for (Party party : rrr.getRightHolderList()) {
                boolean found = false;
                for (Party party2 : rrrLoc.getRightHolderList()) {
                    if (party.getId().equals(party2.getId())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    party.setEntityAction(EntityAction.DISASSOCIATE);
                }
            }
            // Add new parties on rrr from rrrLoc
            for (Party party : rrrLoc.getRightHolderList()) {
                boolean found = false;
                for (Party party2 : rrr.getRightHolderList()) {
                    if (party.getId().equals(party2.getId())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    rrr.getRightHolderList().add(party);
                }
            }
        }

        rrr.setStatusCode(rrrLoc.getStatusCode());
        rrr.setTypeCode(rrrLoc.getTypeCode());
        rrr.setOwnerTypeCode(rrrLoc.getOwnerTypeCode());
        rrr.setOwnershipTypeCode(rrrLoc.getOwnershipTypeCode());
        return rrr;
    }

    /**
     * Returns true if RRR and RrrLoc are equal.
     */
    private boolean compareRrrAndLoc(Rrr rrr, RrrLoc rrrLoc) {
        if (rrr == null || rrrLoc == null) {
            return false;
        }
        Rrr rrr2 = new Rrr();
        rrr2.setTypeCode(rrrLoc.getTypeCode());
        rrr2.setOwnerTypeCode(rrrLoc.getOwnerTypeCode());
        rrr2.setOwnershipTypeCode(rrrLoc.getOwnershipTypeCode());
        rrr2.setRightHolderList(rrrLoc.getRightHolderList());
        return compareRrrs(rrr, rrr2);
    }

    /**
     * Returns true if RRR and RrrLoc are equal.
     */
    private boolean compareRrrs(Rrr rrr, Rrr rrr2) {
        if (rrr == null || rrr2 == null) {
            return false;
        }

        if (!rrr2.getTypeCode().equals(rrr.getTypeCode())) {
            return false;
        }

        // Check owner type
        if ((rrr2.getOwnerTypeCode() != null && rrr.getOwnerTypeCode() == null)
                || (rrr2.getOwnerTypeCode() == null && rrr.getOwnerTypeCode() != null)) {
            return false;
        }

        if (rrr2.getOwnerTypeCode() != null && rrr.getOwnerTypeCode() != null
                && !rrr2.getOwnerTypeCode().equals(rrr.getOwnerTypeCode())) {
            return false;
        }

        // Check share type
        if ((rrr2.getOwnershipTypeCode() != null && rrr.getOwnershipTypeCode() == null)
                || (rrr2.getOwnershipTypeCode() == null && rrr.getOwnershipTypeCode() != null)) {
            return false;
        }

        if (rrr2.getOwnershipTypeCode() != null && rrr.getOwnershipTypeCode() != null
                && !rrr2.getOwnershipTypeCode().equals(rrr.getOwnershipTypeCode())) {
            return false;
        }

        // Check rightholders
        if ((rrr.getRightHolderList() == null && rrr2.getRightHolderList() != null)
                || (rrr.getRightHolderList() != null && rrr2.getRightHolderList() == null)) {
            return false;
        }
        if (rrr.getRightHolderList() != null && rrr2.getRightHolderList() != null) {
            if (rrr.getRightHolderList().size() != rrr2.getRightHolderList().size()) {
                return false;
            }

            for (Party rrrParty : rrr.getRightHolderList()) {
                boolean partyFound = false;
                for (Party rrrLocParty : rrr2.getRightHolderList()) {
                    if (rrrParty.getId().equals(rrrLocParty.getId())) {
                        partyFound = true;
                        break;
                    }
                }
                if (!partyFound) {
                    return false;
                }
            }
        }
        return true;
    }

    private List<Rrr> getRrrsByLoc(String locId) {
        HashMap params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, Rrr.QUERY_WHERE_BY_LOCID);
        params.put(CommonSqlProvider.PARAM_ORDER_BY_PART, Rrr.ORDER_BY_BAUNIT_ID);
        params.put(RrrLoc.PARAM_LOC_ID, locId);
        return getRepository().getEntityList(Rrr.class, params);
    }

    @Override
    public LocWithMoth getLocByPageNoAndMoth(LocSearchByMothParams searchParams) {
        if (searchParams.getMoth() == null || searchParams.getPageNumber() == null
                || searchParams.getPageNumber().length() < 1) {
            return null;
        }

        if (searchParams.getMoth().getMothLuj() == null) {
            searchParams.getMoth().setMothLuj("M");
        }

        if(searchParams.getPageNumber() == null){
            searchParams.setPageNumber("");
        }
        
        String mothType;
        if (searchParams.getMoth().getMothLuj().equalsIgnoreCase("M")) {
            mothType = "M";
        } else {
            mothType = "L";
        }

        HashMap params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, Loc.GET_BY_MOTH_ID_AND_PANA_NO);
        params.put(Loc.MOTH_ID_PARAM, searchParams.getMoth().getId());
        params.put((Loc.PANA_NO_PARAM), searchParams.getPageNumber());
        params.put((Loc.MOTH_TYPE_PARAM), mothType);
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        return getRepository().getEntity(LocWithMoth.class, params);
    }

    @Override
    public List<Loc> getLocList(String mothId) {
        HashMap params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, Loc.GET_BY_MOTH_ID);
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        params.put(Loc.MOTH_ID_PARAM, mothId);
        List<Loc> loc = getRepository().getEntityList(Loc.class, params);
        return loc;
    }

    private List<Party> getPartiesByLocAndStatus(String locId, String status) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, RrrLoc.SELECT_GET_PARTY_IDS);
        params.put(RrrLoc.PARAM_LOC_ID, locId);
        params.put(RrrLoc.PARAM_STATUS, status);

        ArrayList<HashMap> rawPartyIds = getRepository().executeSql(params);
        ArrayList<Party> parties = new ArrayList<Party>();

        if (rawPartyIds != null) {
            ArrayList<String> partyIds = new ArrayList<String>();
            for (HashMap hashMap : rawPartyIds) {
                partyIds.add(hashMap.get("id").toString());
            }
            parties = (ArrayList<Party>) partyEJB.getParties(partyIds);
        }

        return parties;
    }

    @Override
    public List<RrrLoc> getRrrLocsById(String locId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, RrrLoc.SELECT_RRR_LOCS);
        params.put(RrrLoc.PARAM_LOC_ID, locId);
        params.put(RrrLoc.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        ArrayList<RrrLoc> rrrLocs = (ArrayList<RrrLoc>) getRepository().getEntityList(RrrLoc.class, params);
        if (rrrLocs != null) {
            for (RrrLoc rrrLoc : rrrLocs) {
                rrrLoc.setRightHolderList(getPartiesByLocAndStatus(locId, rrrLoc.getStatusCode()));
            }
            if (rrrLocs.size() > 2) {
                throw new SOLAException(ServiceMessage.EJB_ADMINISTRATIVE_LOC_CURRUPTED);
            }
        }
        return rrrLocs;
    }

    @Override
    public List<RestrictionReason> getRestrictionReasons(String languageCode) {
        return getRepository().getCodeList(RestrictionReason.class, languageCode);
    }

    @Override
    public List<RestrictionReleaseReason> getRestrictionReleaseReasons(String languageCode) {
        return getRepository().getCodeList(RestrictionReleaseReason.class, languageCode);
    }

    @Override
    public List<RestrictionOffice> getRestrictionOffices(String languageCode) {
        return getRepository().getCodeList(RestrictionOffice.class, languageCode);
    }

    @Override
    public List<OwnerType> getOwnerTypes(String languageCode) {
        return getRepository().getCodeList(OwnerType.class, languageCode);
    }

    @Override
    public List<OwnershipType> getOwnershipTypes(String languageCode) {
        return getRepository().getCodeList(OwnershipType.class, languageCode);
    }

    @Override
    public List<TenancyType> getTenancyTypes(String languageCode) {
        return getRepository().getCodeList(TenancyType.class, languageCode);
    }

    @Override
    @RolesAllowed(RolesConstants.ADMINISTRATIVE_BA_UNIT_SAVE)
    public void deletePendingBaUnit(String baUnitId) {
        if (baUnitId == null) {
            return;
        }
        BaUnitStatusChanger baUnitStatusChanger = getRepository().getEntity(BaUnitStatusChanger.class, baUnitId);

        if (baUnitStatusChanger != null) {
            if (baUnitStatusChanger.getStatusCode().equals(StatusConstants.PENDING)) {
                baUnitStatusChanger.setEntityAction(EntityAction.DELETE);
                getRepository().saveEntity(baUnitStatusChanger);
            } else {
                throw new SOLAException(ServiceMessage.EJB_ADMINISTRATIVE_BAUNIT_MODIFICATION_NOT_ALLOWED);
            }
        }
    }

    @Override
    public Rrr getRrr(String id) {
        Rrr result = null;
        if (id != null) {
            result = getRepository().getEntity(Rrr.class, id);
        }
        return result;
    }

    @Override
    public List<LocWithMoth> getLocListByPageNoAndMoth(LocSearchByMothParams searchParams) {
        if (searchParams.getMoth().getId() == null) {
            searchParams.getMoth().setId("");
        }

        if (searchParams.getPageNumber() == null) {
            searchParams.setPageNumber("");
        }

        HashMap params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, Loc.MothPage_Search_Query);
        params.put(Loc.MOTH_ID_PARAM, searchParams.getMoth().getId());
        params.put((Loc.PANA_NO_PARAM), searchParams.getPageNumber());
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        return getRepository().getEntityList(LocWithMoth.class, params);
    }

    @Override
    public List<Moth> searchMoths(MothSearchParams searchParams) {
        if (searchParams == null) {
            return null;
        }

        if (searchParams.getVdcCode() == null) {
            searchParams.setVdcCode("");
        }
        if (searchParams.getMothLuj() == null) {
            searchParams.setMothLuj("");
        }
        if (searchParams.getMothlujNumber() == null) {
            searchParams.setMothlujNumber("");
        }

        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, Moth.SEARCH_QUERY);
        params.put(Moth.VDC_PARAM, searchParams.getVdcCode());
        params.put(Moth.MOTH_LUJ_PARAM, searchParams.getMothLuj());
        params.put(Moth.MOTH_LUJ_NUMBER_PARAM, searchParams.getMothlujNumber());
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        return getRepository().getEntityList(Moth.class, params);
    }
    
    @Override
    public List<Moth> searchMothsByParts(String searchString) {
        Integer numberOfMaxRecordsReturned = 10;
        HashMap params = new HashMap();
        params.put("search_string", searchString);
        params.put(CommonSqlProvider.PARAM_LIMIT_PART, numberOfMaxRecordsReturned);
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        return getRepository().getEntityList(Moth.class,
                Moth.QUERY_WHERE_SEARCH_BY_MOTH_LUJ_VDC_CODE, params);
    }
}
