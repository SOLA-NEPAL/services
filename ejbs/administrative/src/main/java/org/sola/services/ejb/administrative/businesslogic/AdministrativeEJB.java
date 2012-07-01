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
import org.sola.services.common.EntityAction;
import org.sola.services.common.LocalInfo;
import org.sola.services.common.br.ValidationResult;
import org.sola.services.common.ejbs.AbstractEJB;
import org.sola.services.common.repository.CommonSqlProvider;
import org.sola.services.ejb.administrative.repository.entities.*;
import org.sola.services.ejb.cadastre.businesslogic.CadastreEJBLocal;
import org.sola.services.ejb.party.businesslogic.PartyEJBLocal;
import org.sola.services.ejb.source.businesslogic.SourceEJBLocal;
import org.sola.services.ejb.system.businesslogic.SystemEJBLocal;
import org.sola.services.ejb.system.repository.entities.BrValidation;
import org.sola.services.ejb.transaction.businesslogic.TransactionEJBLocal;
import org.sola.services.ejb.transaction.repository.entities.RegistrationStatusType;
import org.sola.services.ejb.transaction.repository.entities.Transaction;
import org.sola.services.ejb.transaction.repository.entities.TransactionBasic;
import org.sola.services.ejbs.admin.businesslogic.AdminEJBLocal;

/**
 *
 */
@Stateless
@EJB(name = "java:global/SOLA/AdministrativeEJBLocal", beanInterface = AdministrativeEJBLocal.class)
public class AdministrativeEJB extends AbstractEJB
        implements AdministrativeEJBLocal {

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
    public BaUnit getBaUnitByCode(String nameFirstpart, String nameLastpart) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, BaUnit.QUERY_WHERE_BYPROPERTYCODE);
        params.put(BaUnit.QUERY_PARAMETER_FIRSTPART, nameFirstpart);
        params.put(BaUnit.QUERY_PARAMETER_LASTPART, nameLastpart);
        return getRepository().getEntity(BaUnit.class, params);
    }

    @Override
    @RolesAllowed(RolesConstants.ADMINISTRATIVE_BA_UNIT_SAVE)
    public BaUnit createBaUnit(String serviceId, BaUnit baUnit) {
        if (baUnit == null) {
            return null;
        }
        baUnit.setStatusCode("pending");
        baUnit.setTypeCode("administrativeUnit");
        return saveBaUnit(serviceId, baUnit);
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
                baUnit.setStatusCode(approvedStatus);
                baUnit.setTransactionId(transactionId);
                getRepository().saveEntity(baUnit);
            }
        }

        params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, Rrr.QUERY_WHERE_BYTRANSACTIONID);
        params.put(Rrr.QUERY_PARAMETER_TRANSACTIONID, transactionId);
        List<RrrStatusChanger> rrrStatusChangerList =
                getRepository().getEntityList(RrrStatusChanger.class, params);
        for (RrrStatusChanger rrr : rrrStatusChangerList) {
            
            adminEJB.checkOfficeCode(rrr.getOfficeCode());
            
            validationResult.addAll(this.validateRrr(rrr, languageCode));
            if (systemEJB.validationSucceeded(validationResult) && !validateOnly) {
                rrr.setStatusCode(approvedStatus);
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

        BaUnitTarget baUnitTarget = new BaUnitTarget();
        baUnitTarget.setBaUnitId(baUnitId);
        baUnitTarget.setTransactionId(transaction.getId());
        getRepository().saveEntity(baUnitTarget);

        return getBaUnitById(baUnitId);
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
    //<editor-fold defaultstate="collapsed" desc="By Kumar">
    //***********************************************************************************************************

    @Override
    public Moth saveMoth(Moth moth) {
        if(moth.isNew()){
            moth.setOfficeCode(adminEJB.getCurrentOfficeCode());
        } else {
            adminEJB.checkOfficeCode(moth.getOfficeCode());
        }
        return getRepository().saveEntity(moth);
    }

    @Override
    public Moth getMoth(String id) {
        return getRepository().getEntity(Moth.class, id);
    }

    @Override
    public List<Moth> getMoths(String vdcCode, String mothLuj) {
        HashMap params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, Moth.GET_BY_VDC_AND_MOTHLUJ);
        params.put(Moth.VDC_PARAM, vdcCode);
        params.put(Moth.MOTH_LUJ_PARAM, mothLuj);
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
        return getRepository().getEntity(Moth.class, params);
    }

    @Override
    public Loc saveLoc(Loc loc) {
        if(loc.isNew()){
            loc.setOfficeCode(adminEJB.getCurrentOfficeCode());
        } else {
            adminEJB.checkOfficeCode(loc.getOfficeCode());
        }
        return getRepository().saveEntity(loc);
    }

    @Override
    public Loc getLoc(String id) {
        return getRepository().getEntity(Loc.class, id);
    }

    @Override
    public BaUnit saveBaUnit(BaUnit baUnit) {
        if(baUnit.isNew()){
            baUnit.setOfficeCode(adminEJB.getCurrentOfficeCode());
        }else{
            adminEJB.checkOfficeCode(baUnit.getOfficeCode());
        }
        return getRepository().saveEntity(baUnit);
    }

    @Override
    public Loc getLocByPageNoAndMothId(int panaNo, String mothId) {
        HashMap params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, Loc.GET_BY_MOTH_ID_AND_PANA_NO);
        params.put(Loc.MOTH_ID_PARAM, mothId);
        params.put((Loc.PANA_NO_PARAM), panaNo);
        return getRepository().getEntity(Loc.class, params);
    }   
    
    @Override
    public List<Loc> getLocList(String mothId) {
        HashMap params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, Loc.GET_BY_MOTH_ID);
        params.put(Loc.MOTH_ID_PARAM, mothId);        
        List<Loc> loc = getRepository().getEntityList(Loc.class, params);
        return loc;
    }
    
     @Override
    public List<BaUnitAsParty> getBaUnitAsPartyList(String partyId) {
        HashMap params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, BaUnitAsParty.GET_BY_PARTY_ID);
        params.put(BaUnitAsParty.PARTY_ID_PARAM, partyId);        
        List<BaUnitAsParty> bau = getRepository().getEntityList(BaUnitAsParty.class, params);
        return bau;
    }
    //***********************************************************************************************************
    //</editor-fold>    
}
