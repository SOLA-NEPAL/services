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
package org.sola.services.ejb.system.businesslogic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.sola.common.RolesConstants;
import org.sola.common.SOLAException;
import org.sola.common.messaging.ServiceMessage;
import org.sola.services.common.br.ValidationResult;
import org.sola.services.common.ejbs.AbstractEJB;
import org.sola.services.common.repository.CommonSqlProvider;
import org.sola.services.ejb.system.br.Result;
import org.sola.services.ejb.system.repository.entities.*;

@Stateless
@EJB(name = "java:global/SOLA/SystemEJBLocal", beanInterface = SystemEJBLocal.class)
public class SystemEJB extends AbstractEJB implements SystemEJBLocal {

    @Override
    protected void postConstruct() {
        setEntityPackage(Br.class.getPackage().getName());
    }

    @Override
    public BigDecimal getTaxRate() {
        // Note that the String constructor is perferred for BigDecimal
        return new BigDecimal("0.075");
    }

    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    @Override
    public Br getBr(String id, String lang) {
        if (lang == null) {
            return getRepository().getEntity(Br.class, id);
        } else {
            Map params = new HashMap<String, Object>();
            params.put(CommonSqlProvider.PARAM_LANGUAGE_CODE, lang);
            return getRepository().getEntity(Br.class, id, lang);
        }
    }

    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    @Override
    public Br saveBr(Br br) {
        return getRepository().saveEntity(br);
    }

    private BrCurrent getBrCurrent(String id, String languageCode) {
        BrCurrent result = null;
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_LANGUAGE_CODE, languageCode);
        params.put(CommonSqlProvider.PARAM_WHERE_PART, BrCurrent.QUERY_WHERE_BYID);
        params.put(BrCurrent.QUERY_PARAMETER_ID, id);
        result = getRepository().getEntity(BrCurrent.class, params);
        if (result == null) {
            throw new SOLAException(ServiceMessage.RULE_NOT_FOUND, new Object[]{id});
        }
        return result;
    }

    @Override
    public BrReport getBrReport(String id) {
        Map params = new HashMap<String, Object>();
        return getRepository().getEntity(BrReport.class, id);
    }

    @Override
    public List<BrReport> getBrs(List<String> ids) {
        Map params = new HashMap<String, Object>();
        return getRepository().getEntityListByIds(BrReport.class, ids);
    }

    @Override
    public List<BrReport> getAllBrs() {
        return getRepository().getEntityList(BrReport.class);
    }

    @Override
    public List<BrValidation> getBrForValidatingApplication(String momentCode) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, BrValidation.QUERY_WHERE_FORAPPLICATION);
        params.put(BrValidation.QUERY_PARAMETER_MOMENTCODE, momentCode);
        params.put(CommonSqlProvider.PARAM_ORDER_BY_PART, BrValidation.QUERY_ORDERBY_ORDEROFEXECUTION);
        return getRepository().getEntityList(BrValidation.class, params);
    }

    @Override
    public List<BrValidation> getBrForValidatingService(
            String momentCode, String requestTypeCode) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, BrValidation.QUERY_WHERE_FORSERVICE);
        params.put(BrValidation.QUERY_PARAMETER_MOMENTCODE, momentCode);
        params.put(BrValidation.QUERY_PARAMETER_REQUESTTYPE, requestTypeCode);
        params.put(CommonSqlProvider.PARAM_ORDER_BY_PART, BrValidation.QUERY_ORDERBY_ORDEROFEXECUTION);
        return getRepository().getEntityList(BrValidation.class, params);
    }

    @Override
    public List<BrValidation> getBrForValidatingRrr(String momentCode, String rrrType) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, BrValidation.QUERY_WHERE_FORRRR);
        params.put(BrValidation.QUERY_PARAMETER_MOMENTCODE, momentCode);
        params.put(BrValidation.QUERY_PARAMETER_RRRTYPE, rrrType);
        params.put(CommonSqlProvider.PARAM_ORDER_BY_PART, BrValidation.QUERY_ORDERBY_ORDEROFEXECUTION);
        return getRepository().getEntityList(BrValidation.class, params);
    }

    @Override
    public List<BrValidation> getBrForValidatingTransaction(
            String targetCode, String momentCode, String requestTypeCode) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, BrValidation.QUERY_WHERE_FOR_TRANSACTION);
        params.put(BrValidation.QUERY_PARAMETER_TARGETCODE, targetCode);
        params.put(BrValidation.QUERY_PARAMETER_REQUESTTYPE, requestTypeCode);
        params.put(BrValidation.QUERY_PARAMETER_MOMENTCODE, momentCode);
        params.put(CommonSqlProvider.PARAM_ORDER_BY_PART, BrValidation.QUERY_ORDERBY_ORDEROFEXECUTION);
        return getRepository().getEntityList(BrValidation.class, params);
    }

    private HashMap checkRuleBasic(
            BrCurrent br, HashMap<String, Serializable> parameters) {
        HashMap ruleResult = null;
        try {
            if (br.getTechnicalTypeCode().equals("drools")) {
                //Here is supposed to come the code which runs the business rule using drools engine.
            } else if (br.getTechnicalTypeCode().equals("sql")) {
                String sqlStatement = br.getBody();
                ruleResult = getResultObjectFromStatement(sqlStatement, parameters);
                if (ruleResult == null) {
                    ruleResult = new HashMap();
                }
                if (!ruleResult.containsKey(Result.VALUE_FIELD_NAME)) {
                    ruleResult.put(Result.VALUE_FIELD_NAME, null);
                }
            }
            return ruleResult;
        } catch (Exception ex) {
            throw new SOLAException(ServiceMessage.RULE_FAILED_EXECUTION, new Object[]{br.getId(), ex});
        }
    }

    /**
     * It returns the first row of the result set. It is used especially from
     * business rules.
     *
     * @param sqlStatement
     * @param params
     * @return
     */
    private HashMap getResultObjectFromStatement(String sqlStatement, Map params) {
        params = params == null ? new HashMap<String, Object>() : params;
        params.put(CommonSqlProvider.PARAM_QUERY, sqlStatement);
        // Returns a single result
        //return getRepository().getScalar(Object.class, params); 
        // To use if more than one result is required. 
        List<HashMap> resultList = getRepository().executeSql(params);
        HashMap result = null;
        if (!resultList.isEmpty()) {
            result = resultList.get(0);
        }
        return result;
    }

    @Override
    public Result checkRuleGetResultSingle(
            String brName, HashMap<String, Serializable> parameters) {
        BrCurrent br = this.getBrCurrent(brName, "en");
        Result result = new Result();
        result.setName(brName);
        HashMap rawResult = this.checkRuleBasic(br, parameters);
        result.setValue(rawResult.get(Result.VALUE_FIELD_NAME));
        return result;
    }

    @Override
    public List<ValidationResult> checkRulesGetValidation(
            List<BrValidation> brListToValidate, String languageCode,
            HashMap<String, Serializable> parameters) {
        List<ValidationResult> validationResultList = new ArrayList<ValidationResult>();
        if (brListToValidate != null) {
            for (BrValidation brForValidation : brListToValidate) {
                validationResultList.add(
                        this.checkRuleGetValidation(brForValidation, languageCode, parameters));
            }
        }
        return validationResultList;
    }

    private ValidationResult checkRuleGetValidation(
            BrValidation brForValidation, String languageCode,
            HashMap<String, Serializable> parameters) {

        BrCurrent br = this.getBrCurrent(brForValidation.getBrId(), languageCode);
        ValidationResult result = new ValidationResult();
        result.setName(br.getId());
        HashMap rawResult = this.checkRuleBasic(br, parameters);
        // Result can be null for some checks, so default to True in these cases. 
        if (rawResult.get(Result.VALUE_FIELD_NAME) == null) {
            rawResult.put(Result.VALUE_FIELD_NAME, Boolean.TRUE);
        }
        result.setSuccessful(rawResult.get(Result.VALUE_FIELD_NAME).equals(Boolean.TRUE));
        //Replace parameters if they exist
        String feedback = br.getFeedback();
        for (Object keyObj : rawResult.keySet()) {
            if (keyObj.equals(Result.VALUE_FIELD_NAME)) {
                continue;
            }
            feedback = feedback.replace(keyObj.toString(), rawResult.get(keyObj).toString());
        }
        result.setFeedback(feedback);
        result.setSeverity(brForValidation.getSeverityCode());
        return result;
    }

    @Override
    public boolean validationSucceeded(List<ValidationResult> validationResultList) {
        for (ValidationResult validationResult : validationResultList) {
            if (validationResult.getSeverity().equals(BrValidation.SEVERITY_CRITICAL)
                    && !validationResult.isSuccessful()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<ParcelType> getParcelTypes(String languageCode) {
        return getRepository().getCodeList(ParcelType.class, languageCode);
    }

    @Override
    public List<LandClass> getLandClasses(String languageCode) {
        return getRepository().getCodeList(LandClass.class, languageCode);
    }

    @Override
    public List<LandUse> getLandUses(String languageCode) {
        return getRepository().getCodeList(LandUse.class, languageCode);
    }
}
