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
package org.sola.services.ejb.search.businesslogic;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.sola.common.RolesConstants;
import org.sola.common.SOLAException;
import org.sola.common.messaging.ServiceMessage;
import org.sola.services.common.ejbs.AbstractEJB;
import org.sola.services.common.repository.CommonSqlProvider;
import org.sola.services.common.repository.entities.AbstractEntity;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;
import org.sola.services.ejb.search.repository.SearchSqlProvider;
import org.sola.services.ejb.search.repository.entities.*;
import org.sola.services.ejb.search.spatial.QueryForNavigation;
import org.sola.services.ejb.search.spatial.QueryForSelect;
import org.sola.services.ejb.search.spatial.ResultForNavigationInfo;
import org.sola.services.ejb.search.spatial.ResultForSelectionInfo;
import org.sola.services.ejb.transaction.businesslogic.TransactionEJBLocal;
import org.sola.services.ejb.transaction.repository.entities.TransactionBasic;
import org.sola.services.ejbs.admin.businesslogic.AdminEJBLocal;

@Stateless
@EJB(name = "java:global/SOLA/SearchEJBLocal", beanInterface = SearchEJBLocal.class)
public class SearchEJB extends AbstractEJB implements SearchEJBLocal {

    @EJB
    AdminEJBLocal adminEJB;
    @EJB
    TransactionEJBLocal transactionEJB;

    private DynamicQuery getDynamicQuery(String queryName, Map params) {
        DynamicQuery query = null;
        // Retrieve the dynamic query from the database. Use localization if it is provided
        // as a query parameter. 
        if (params != null && params.containsKey(CommonSqlProvider.PARAM_LANGUAGE_CODE)) {
            query = getRepository().getEntity(DynamicQuery.class, queryName,
                    params.get(CommonSqlProvider.PARAM_LANGUAGE_CODE).toString());
        } else {
            query = getRepository().getEntity(DynamicQuery.class, queryName);
        }
        if (query == null) {
            // Raise an error to indicate the dynamic query does not exist
            throw new SOLAException(ServiceMessage.GENERAL_UNEXPECTED,
                    new Object[]{"Dynamic query " + queryName + " does not exist."});
        }
        return query;
    }

    // Returns a generic result from the dynamic query
    private ArrayList<HashMap> executeDynamicQuery(DynamicQuery query, Map params) {
        params = params == null ? new HashMap<String, Object>() : params;
        params.put(CommonSqlProvider.PARAM_QUERY, query.getSql());
        return getRepository().executeSql(params);
    }

    // Overloaded version of executeDynamicQuery that returns a list of entities from the query
    private <T extends AbstractReadOnlyEntity> List<T> executeDynamicQuery(Class<T> entityClass,
            String queryName, Map params) {
        params = params == null ? new HashMap<String, Object>() : params;
        DynamicQuery query = getDynamicQuery(queryName, params);
        params.put(CommonSqlProvider.PARAM_QUERY, query.getSql());
        return getRepository().getEntityList(entityClass, params);
    }

    @Override
    public GenericResult getGenericResultList(String queryName, Map params) {

        GenericResult result = new GenericResult();
        DynamicQuery query = getDynamicQuery(queryName, params);
        if (query.getFieldList() == null || query.getFieldList().isEmpty()) {
            throw new RuntimeException("The field list is missing. If there is a query to be used to"
                    + " return dynamic result, you have to define a field list.");
        }
        ArrayList<HashMap> queryResult = executeDynamicQuery(query, params);

        // Create the generic result from the query result. 
        if (queryResult != null && !queryResult.isEmpty()) {

            String[] fieldNames = null;
            List<String> queryFields = new ArrayList<String>();
            List<String> displayNames = new ArrayList<String>();

            // Get any query fields and display names from the dynamic query configuration. 
            //if (query.getQueryFieldNames() != null) {
            // asList returns a fixed lenght list backed by the array so need to create a 
            // new list based on the array instead. 
            queryFields = new ArrayList<String>(Arrays.asList(query.getQueryFieldNames()));
            displayNames = new ArrayList<String>(Arrays.asList(query.getFieldDisplayNames()));
            //}

            // Need to cycle through a few of the results to get the remaining query fields  
            // (i.e. those not identifeid in the configuration) because any null values from the 
            // dynamic query are completely omitted from the hashmap for that row.  This issue may 
            // be fixed in later versions of Mybatis (i.e. later than 3.0.6). 
//            int count = 0;
//            for (Map rowMap : queryResult) {
//                for (Object field : rowMap.keySet()) {
//                    if (!queryFields.contains(field.toString())) {
//                        queryFields.add(field.toString());
//                        displayNames.add(field.toString());
//                    }
//                }
//                count++;
//                if (count > 5) {
//                    // Have cycled through at least 5 results, so exit the for loop. 
//                    break;
//                }
//            }
            fieldNames = queryFields.toArray(new String[0]);
            result.setFieldNames(displayNames.toArray(new String[0]));

            for (HashMap map : queryResult) {
                Object[] values = new Object[fieldNames.length];
                for (int i = 0; i < fieldNames.length; i++) {
                    values[i] = map.get(fieldNames[i]);
                }
                result.addRow(values);
            }
        }
        return result;
    }

    @Override
    @RolesAllowed(RolesConstants.APPLICATION_VIEW_APPS)
    public List<ApplicationSearchResult> searchApplications(ApplicationSearchParams params) {
        // Process params

        Map queryParams = new HashMap<String, Object>();
        queryParams.put(CommonSqlProvider.PARAM_FROM_PART, ApplicationSearchResult.QUERY_FROM);

        queryParams.put(CommonSqlProvider.PARAM_LANGUAGE_CODE, params.getLocale());
        queryParams.put(ApplicationSearchResult.QUERY_PARAM_CONTACT_NAME,
                params.getContactPerson() == null ? "%" : params.getContactPerson().trim() + "%");
        queryParams.put(ApplicationSearchResult.QUERY_PARAM_AGENT_NAME,
                params.getAgent() == null ? "%" : params.getAgent().trim() + "%");
        queryParams.put(ApplicationSearchResult.QUERY_PARAM_APP_NR,
                params.getNr() == null ? "%" : params.getNr().trim() + "%");
        queryParams.put(ApplicationSearchResult.QUERY_PARAM_FROM_LODGE_DATE,
                params.getFromDate() == null ? new GregorianCalendar(1, 1, 1).getTime() : params.getFromDate());
        queryParams.put(ApplicationSearchResult.QUERY_PARAM_TO_LODGE_DATE,
                params.getToDate() == null ? new GregorianCalendar(2500, 1, 1).getTime() : params.getToDate());
        queryParams.put(ApplicationSearchResult.QUERY_PARAM_OFFICE_CODE,
                adminEJB.getCurrentOfficeCode() == null ? "" : adminEJB.getCurrentOfficeCode());

        queryParams.put(CommonSqlProvider.PARAM_WHERE_PART, ApplicationSearchResult.QUERY_WHERE_SEARCH_APPLICATIONS);
        queryParams.put(CommonSqlProvider.PARAM_ORDER_BY_PART, ApplicationSearchResult.QUERY_ORDER_BY);
        queryParams.put(CommonSqlProvider.PARAM_LIMIT_PART, "100");

        return getRepository().getEntityList(ApplicationSearchResult.class, queryParams);
    }

    @Override
    @RolesAllowed(RolesConstants.SOURCE_SEARCH)
    public List<SourceSearchResult> searchSources(SourceSearchParams searchParams) {
        Map params = new HashMap<String, Object>();

        params.put(SourceSearchResult.QUERY_PARAM_FROM_RECORDATION_DATE,
                searchParams.getFromRecordationDate() == null
                ? 0 : searchParams.getFromRecordationDate());
        params.put(SourceSearchResult.QUERY_PARAM_TO_RECORDATION_DATE,
                searchParams.getToRecordationDate() == null
                ? 99999999 : searchParams.getToRecordationDate());
        params.put(SourceSearchResult.QUERY_PARAM_FROM_SUBMISSION_DATE,
                searchParams.getFromSubmissionDate() == null
                ? new GregorianCalendar(1, 1, 1).getTime()
                : searchParams.getFromSubmissionDate());
        params.put(SourceSearchResult.QUERY_PARAM_TO_SUBMISSION_DATE,
                searchParams.getToSubmissionDate() == null
                ? new GregorianCalendar(2500, 1, 1).getTime()
                : searchParams.getToSubmissionDate());
        params.put(SourceSearchResult.QUERY_PARAM_TYPE_CODE,
                searchParams.getTypeCode() == null ? "" : searchParams.getTypeCode());
        params.put(SourceSearchResult.QUERY_PARAM_REF_NUMBER,
                searchParams.getRefNumber() == null ? "" : searchParams.getRefNumber());
        params.put(SourceSearchResult.QUERY_PARAM_LA_NUMBER,
                searchParams.getLaNumber() == null ? "" : searchParams.getLaNumber());
        params.put(CommonSqlProvider.PARAM_LANGUAGE_CODE,
                searchParams.getLocale() == null ? "en" : searchParams.getLocale());
        params.put(SourceSearchResult.QUERY_PARAM_APPLICATION_NUMBER,
                searchParams.getAppNumber() == null ? "" : searchParams.getAppNumber());

        params.put(CommonSqlProvider.PARAM_QUERY, SourceSearchResult.SEARCH_QUERY);
        return getRepository().getEntityList(SourceSearchResult.class, params);
    }

    @Override
    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    public List<UserSearchResult> searchUsers(UserSearchParams searchParams) {
        if (searchParams.getDepartmentCode() == null) {
            searchParams.setDepartmentCode("");
        }

        if (searchParams.getUserName() == null) {
            searchParams.setUserName("");
        }

        if (searchParams.getFirstName() == null) {
            searchParams.setFirstName("");
        }

        if (searchParams.getLastName() == null) {
            searchParams.setLastName("");
        }

        if (searchParams.getLocale() == null) {
            searchParams.setLocale("");
        }

        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, UserSearchResult.QUERY_ADVANCED_USER_SEARCH);
        params.put("userName", searchParams.getUserName());
        params.put("firstName", searchParams.getFirstName());
        params.put("lastName", searchParams.getLastName());
        params.put("departmentCode", searchParams.getDepartmentCode());
        params.put(UserSearchResult.PARAM_LANG_CODE, searchParams.getLocale());
        return getRepository().getEntityList(UserSearchResult.class, params);
    }

    @Override
    @RolesAllowed(RolesConstants.APPLICATION_VIEW_APPS)
    public List<ApplicationSearchResult> getAssignedApplications(String locale) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_FROM_PART, ApplicationSearchResult.QUERY_FROM);
        params.put(CommonSqlProvider.PARAM_LANGUAGE_CODE, locale);

        if (isInRole(RolesConstants.APPLICATION_ASSIGN_TO_ALL)) {
            params.put(CommonSqlProvider.PARAM_WHERE_PART, ApplicationSearchResult.QUERY_WHERE_GET_ASSIGNED_ALL);
            params.put(ApplicationSearchResult.QUERY_PARAM_OFFICE_CODE,
                    adminEJB.getCurrentOfficeCode() == null ? "" : adminEJB.getCurrentOfficeCode());
        } else if (isInRole(RolesConstants.APPLICATION_ASSIGN_TO_DEPARTMENT)) {
            params.put(CommonSqlProvider.PARAM_WHERE_PART, ApplicationSearchResult.QUERY_WHERE_GET_ASSIGNED_DEPARTMENT);
            params.put(ApplicationSearchResult.QUERY_PARAM_USER_NAME, getUserName());
        } else {
            params.put(CommonSqlProvider.PARAM_WHERE_PART, ApplicationSearchResult.QUERY_WHERE_GET_ASSIGNED);
            params.put(ApplicationSearchResult.QUERY_PARAM_USER_NAME, getUserName());
        }

        params.put(CommonSqlProvider.PARAM_ORDER_BY_PART, ApplicationSearchResult.QUERY_ORDER_BY);
        params.put(CommonSqlProvider.PARAM_LIMIT_PART, "100");

        return getRepository().getEntityList(ApplicationSearchResult.class, params);
    }

    @Override
    public List<UserSearchResult> getUsersByOffice(String officeCode) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, UserSearchResult.QUERY_USERS_BY_OFFICE);
        params.put(UserSearchResult.PARAM_OFFICE_CODE, officeCode);
        return getRepository().getEntityList(UserSearchResult.class, params);
    }

    @Override
    public List<PartySearchResult> searchParties(PartySearchParams searchParams) {
        if (searchParams.getName() == null) {
            searchParams.setName("");
        }
        if (searchParams.getLastName() == null) {
            searchParams.setLastName("");
        }
        if (searchParams.getDistrictCode() == null) {
            searchParams.setDistrictCode("");
        }
        if (searchParams.getFatherName() == null) {
            searchParams.setFatherName("");
        }
        if (searchParams.getGrandFartherName() == null) {
            searchParams.setGrandFartherName("");
        }
        if (searchParams.getIdIssueDate() == null) {
            searchParams.setIdIssueDate(0);
        }
        if (searchParams.getIdNumber() == null) {
            searchParams.setIdNumber("");
        }
        if (searchParams.getLangCode() == null) {
            searchParams.setName("en");
        }
        if (searchParams.getStreet() == null) {
            searchParams.setStreet("");
        }
        if (searchParams.getVdcCode() == null) {
            searchParams.setVdcCode("");
        }
        if (searchParams.getWardNumber() == null) {
            searchParams.setWardNumber("");
        }
        if (searchParams.getTypeCode() == null) {
            searchParams.setTypeCode("");
        }
        if (searchParams.getRoleTypeCode() == null) {
            searchParams.setRoleTypeCode("");
        }

        searchParams.setName(searchParams.getName().trim());

        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, PartySearchResult.SEARCH_QUERY);
        params.put(PartySearchResult.PARAM_NAME, searchParams.getName());
        params.put(PartySearchResult.PARAM_TYPE_CODE, searchParams.getTypeCode());
        params.put(PartySearchResult.PARAM_ROLE_TYPE_CODE, searchParams.getRoleTypeCode());
        params.put(PartySearchResult.PARAM_LAST_NAME, searchParams.getLastName());
        params.put(PartySearchResult.PARAM_DISTRICT_CODE, searchParams.getDistrictCode());
        params.put(PartySearchResult.PARAM_FATHER_NAME, searchParams.getFatherName());
        params.put(PartySearchResult.PARAM_GRANDFATHER_NAME, searchParams.getGrandFartherName());
        params.put(PartySearchResult.PARAM_ID_ISSUE_DATE, searchParams.getIdIssueDate());
        params.put(PartySearchResult.PARAM_ID_NUMBER, searchParams.getIdNumber());
        params.put(PartySearchResult.PARAM_LANG, searchParams.getLangCode());
        params.put(PartySearchResult.PARAM_STREET, searchParams.getStreet());
        params.put(PartySearchResult.PARAM_VDC_CODE, searchParams.getVdcCode());
        params.put(PartySearchResult.PARAM_WARD, searchParams.getWardNumber());
        return getRepository().getEntityList(PartySearchResult.class, params);
    }

    @Override
    public ResultForNavigationInfo getSpatialResult(
            QueryForNavigation spatialQuery, String officeCode) {
        if(spatialQuery.getDatasetId()==null){
            spatialQuery.setDatasetId("");
        }
        
        Map params = new HashMap<String, Object>();
        params.put("minx", spatialQuery.getWest());
        params.put("miny", spatialQuery.getSouth());
        params.put("maxx", spatialQuery.getEast());
        params.put("maxy", spatialQuery.getNorth());
        params.put("srid", spatialQuery.getSrid());
        params.put("datasetId", spatialQuery.getDatasetId());
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, officeCode);
        ResultForNavigationInfo spatialResultInfo = new ResultForNavigationInfo();
        getRepository().setLoadInhibitors(new Class[]{DynamicQueryField.class});
        List<SpatialResult> result = executeDynamicQuery(SpatialResult.class,
                spatialQuery.getQueryName(), params);
        getRepository().clearLoadInhibitors();
        spatialResultInfo.setToAdd(result);
        return spatialResultInfo;
    }

    @Override
    public List<ConfigMapLayer> getConfigMapLayerList(String languageCode) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_LANGUAGE_CODE, languageCode);
        params.put(CommonSqlProvider.PARAM_QUERY, ConfigMapLayer.QUERY_SQL);
        return getRepository().getEntityList(ConfigMapLayer.class, params);
    }

    @Override
    public List<ResultForSelectionInfo> getSpatialResultFromSelection(
            List<QueryForSelect> queriesForSelection) {
        List<ResultForSelectionInfo> results = new ArrayList<ResultForSelectionInfo>();
        for (QueryForSelect queryInfo : queriesForSelection) {
            Map params = new HashMap<String, Object>();
            params.put(ResultForSelectionInfo.PARAM_GEOMETRY, queryInfo.getFilteringGeometry());
            params.put(ResultForSelectionInfo.PARAM_SRID, queryInfo.getSrid());
            params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
            if (queryInfo.getLocale() != null) {
                params.put(CommonSqlProvider.PARAM_LANGUAGE_CODE, queryInfo.getLocale());
            } else {
                params.put(CommonSqlProvider.PARAM_LANGUAGE_CODE, "en");
            }
            ResultForSelectionInfo resultInfo = new ResultForSelectionInfo();
            resultInfo.setId(queryInfo.getId());
            resultInfo.setResult(this.getGenericResultList(queryInfo.getQueryName(), params));
            results.add(resultInfo);
        }
        return results;
    }

    @Override
    public HashMap<String, String> getMapSettingList() {
        return this.getSettingList(Setting.QUERY_SQL_FOR_MAP_SETTINGS);
    }

    private HashMap<String, String> getSettingList(String queryBody) {
        HashMap settingMap = new HashMap();
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, queryBody);
        List<Setting> settings = getRepository().getEntityList(Setting.class, params);
        if (settings != null && !settings.isEmpty()) {
            for (Setting setting : settings) {
                settingMap.put(setting.getId(), setting.getVl());
            }
        }
        return settingMap;
    }

    @Override
    @RolesAllowed(RolesConstants.APPLICATION_VIEW_APPS)
    public List<ApplicationLogResult> getApplicationLog(String applicationId) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, SearchSqlProvider.buildApplicationLogSql());
        params.put(SearchSqlProvider.PARAM_APPLICATION_ID, applicationId);
        return getRepository().getEntityList(ApplicationLogResult.class, params);

    }

    @RolesAllowed(RolesConstants.ADMIN_MANAGE_BR)
    @Override
    public List<BrSearchResult> searchBr(BrSearchParams searchParams, String lang) {
        Map params = new HashMap<String, Object>();

        if (searchParams.getDisplayName() == null) {
            searchParams.setDisplayName("");
        }
        if (searchParams.getTargetCode() == null) {
            searchParams.setTargetCode("");
        }
        if (searchParams.getTechnicalTypeCode() == null) {
            searchParams.setTechnicalTypeCode("");
        }

        searchParams.setDisplayName(searchParams.getDisplayName().trim());

        params.put(CommonSqlProvider.PARAM_QUERY, BrSearchResult.SELECT_QUERY);
        params.put("lang", lang);
        params.put("displayName", searchParams.getDisplayName());
        params.put("technicalTypeCode", searchParams.getTechnicalTypeCode());
        params.put("targetCode", searchParams.getTargetCode());
        return getRepository().getEntityList(BrSearchResult.class, params);
    }

    @Override
    public List<DynamicQuery> getQueryListAll() {
        return this.getRepository().getEntityList(DynamicQuery.class);
    }

    @Override
    @RolesAllowed(RolesConstants.ADMINISTRATIVE_BA_UNIT_SEARCH)
    public List<BaUnitSearchResult> searchBaUnits(BaUnitSearchParams searchParams) {
        Map params = new HashMap<String, Object>();

        if (searchParams.getParcelNo() == null) {
            searchParams.setParcelNo("");
        }
        if (searchParams.getVdcCode() == null) {
            searchParams.setVdcCode("");
        }
        if (searchParams.getWardNo() == null) {
            searchParams.setWardNo("");
        }
        if (searchParams.getMapSheetCode() == null) {
            searchParams.setMapSheetCode("");
        }
        if (searchParams.getMoth() == null) {
            searchParams.setMoth("");
        }
        if (searchParams.getLoc() == null) {
            searchParams.setLoc("");
        }
        if (searchParams.getRightHolderId() == null) {
            searchParams.setRightHolderId("");
        }

        params.put(CommonSqlProvider.PARAM_QUERY, BaUnitSearchResult.SEARCH_QUERY);
        params.put(BaUnitSearchResult.PARAM_VDC_CODE, searchParams.getVdcCode());
        params.put(BaUnitSearchResult.PARAM_WARD, searchParams.getWardNo());
        params.put(BaUnitSearchResult.PARAM_MAPSHEET_CODE, searchParams.getMapSheetCode());
        params.put(BaUnitSearchResult.PARAM_PARCEL_NO, searchParams.getParcelNo());
        params.put(BaUnitSearchResult.PARAM_MOTH, searchParams.getMoth());
        params.put(BaUnitSearchResult.PARAM_LOC, searchParams.getLoc());
        params.put(BaUnitSearchResult.PARAM_RIGHTHOLDER_ID, searchParams.getRightHolderId());
        params.put(BaUnitSearchResult.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        return getRepository().getEntityList(BaUnitSearchResult.class, params);
    }

    @Override
    public List<CadastreObjectSearchResult> searchCadastreObjects(
            String searchBy, String searchString) {
        String wherePart = null;
        String selectPart = null;
        String fromPart = null;
        Integer numberOfMaxRecordsReturned = 30;

        if (searchBy.equals(CadastreObjectSearchResult.SEARCH_BY_NUMBER)) {
            wherePart = CadastreObjectSearchResult.QUERY_WHERE_SEARCHBY_NUMBER;
        } else if (searchBy.equals(CadastreObjectSearchResult.SEARCH_BY_BAUNIT)) {
            selectPart = CadastreObjectSearchResult.QUERY_SELECT_SEARCHBY_BAUNIT;
            fromPart = CadastreObjectSearchResult.QUERY_FROM_SEARCHBY_BAUNIT;
            wherePart = CadastreObjectSearchResult.QUERY_WHERE_SEARCHBY_BAUNIT;
        } else if (searchBy.equals(CadastreObjectSearchResult.SEARCH_BY_OWNER_OF_BAUNIT)) {
            selectPart = CadastreObjectSearchResult.QUERY_SELECT_SEARCHBY_OWNER_OF_BAUNIT;
            fromPart = CadastreObjectSearchResult.QUERY_FROM_SEARCHBY_OWNER_OF_BAUNIT;
            wherePart = CadastreObjectSearchResult.QUERY_WHERE_SEARCHBY_OWNER_OF_BAUNIT;
        } else if (searchBy.equals(CadastreObjectSearchResult.SEARCH_BY_BAUNIT_ID)) {
            wherePart = CadastreObjectSearchResult.QUERY_WHERE_GET_NEW_PARCELS;
            numberOfMaxRecordsReturned = 0;
        }

        List<CadastreObjectSearchResult> result = new ArrayList<CadastreObjectSearchResult>();
        if (wherePart != null) {
            Map params = new HashMap<String, Object>();
            if (numberOfMaxRecordsReturned > 0) {
                params.put(CommonSqlProvider.PARAM_LIMIT_PART, numberOfMaxRecordsReturned);
            }
            if (fromPart != null) {
                params.put(CommonSqlProvider.PARAM_FROM_PART, fromPart);
            }
            if (selectPart != null) {
                params.put(CommonSqlProvider.PARAM_SELECT_PART, selectPart);
            }
            params.put(CommonSqlProvider.PARAM_WHERE_PART, wherePart);
            params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
            params.put(CadastreObjectSearchResult.SEARCH_STRING_PARAM, searchString);
            result = this.getRepository().getEntityList(CadastreObjectSearchResult.class, params);
        }
        return result;
    }

    @Override
    public List<UserSearchResult> getUsersByDepartment(String departmentCode) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, UserSearchResult.QUERY_USERS_BY_DEPARTMENT);
        params.put(UserSearchResult.PARAM_DEPARTMENT_CODE, departmentCode);
        return getRepository().getEntityList(UserSearchResult.class, params);
    }

    @Override
    @RolesAllowed({RolesConstants.ADMIN_MANAGE_SECURITY, RolesConstants.APPLICATION_ASSIGN_TO_ALL, RolesConstants.APPLICATION_ASSIGN_TO_DEPARTMENT})
    public List<UserSearchResult> getUsersWithAssignRightByDepartment(String departmentCode) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, UserSearchResult.QUERY_USERS_WITH_ASSIGN_RIGHT_BY_DEPARTMENT);
        params.put(UserSearchResult.PARAM_DEPARTMENT_CODE, departmentCode);
        return getRepository().getEntityList(UserSearchResult.class, params);
    }

    @Override
    @RolesAllowed({RolesConstants.ADMIN_MANAGE_SECURITY, RolesConstants.APPLICATION_ASSIGN_TO_ALL, RolesConstants.APPLICATION_ASSIGN_TO_DEPARTMENT})
    public List<UserSearchResult> getUsersWithAssignRightByOffice(String officeCode) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, UserSearchResult.QUERY_USERS_WITH_ASSIGN_RIGHT_BY_OFFICE);
        params.put(UserSearchResult.PARAM_OFFICE_CODE, officeCode);
        return getRepository().getEntityList(UserSearchResult.class, params);
    }

    @Override
    public List<CadastreObjectSearchResultExt> searchCadastreObjects(String langCode, CadastreObjectSearchParams searchParams) {
        if (searchParams.getParcelNo() == null) {
            searchParams.setParcelNo("");
        }
        if (searchParams.getVdcCode() == null) {
            searchParams.setVdcCode("");
        }
        if (searchParams.getWardNo() == null) {
            searchParams.setWardNo("");
        }
        if (searchParams.getMapSheetCode() == null) {
            searchParams.setMapSheetCode("");
        }

        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, CadastreObjectSearchResultExt.PARCEL_SEARCH_QUERY);
        params.put(CadastreObjectSearchResultExt.VDC_PARAM, searchParams.getVdcCode());
        params.put(CadastreObjectSearchResultExt.PARCEL_NO_PARAM, searchParams.getParcelNo());
        params.put(CadastreObjectSearchResultExt.WARD_NO_PARAM, searchParams.getWardNo());
        params.put(CadastreObjectSearchResultExt.MAP_SHEET_CODE_PARAM, searchParams.getMapSheetCode());
        params.put(CadastreObjectSearchResultExt.LANGUAGE_CODE_PARAM, langCode);
        params.put(AbstractEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        return getRepository().getEntityList(CadastreObjectSearchResultExt.class, params);
    }

    @Override
    public BaUnitSearchResult searchBaUnitById(String baUnitId) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, BaUnitSearchResult.SEARCH_BY_ID_QUERY);
        params.put(BaUnitSearchResult.PARAM_BA_UNIT_ID, baUnitId);
        params.put(BaUnitSearchResult.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        return getRepository().getEntity(BaUnitSearchResult.class, params);
    }

    @Override
    public List<BaUnitSearchResult> searchBaUnitsByIds(List<String> ids) {
        if (ids == null || ids.size() < 1) {
            return new ArrayList<BaUnitSearchResult>();
        }

        Map params = new HashMap<String, Object>();
        String query = BaUnitSearchResult.SELECT_PART + "WHERE b.office_code = #{"
                + BaUnitSearchResult.PARAM_OFFICE_CODE + "} AND b.id IN (";
        int i = 0;
        for (String id : ids) {
            query = query + "#{idVal" + i + "}, ";
            params.put("idVal" + i, id);
            i++;
        }
        query = query.substring(0, query.length() - 2) + ")";

        params.put(CommonSqlProvider.PARAM_QUERY, query);
        params.put(BaUnitSearchResult.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        return getRepository().getEntityList(BaUnitSearchResult.class, params);
    }

    @Override
    public List<BaUnitSearchResult> getAllBaUnitsByService(String serviceId) {
        TransactionBasic transaction = transactionEJB.getTransactionByServiceId(serviceId, false, TransactionBasic.class);
        if (transaction == null) {
            return null;
        }

        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, BaUnitSearchResult.QUERY_WITH_ACTION);
        params.put(BaUnitSearchResult.PARAM_TRANSACTION_ID, transaction.getId());
        params.put(BaUnitSearchResult.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        return getRepository().getEntityList(BaUnitSearchResult.class, params);
    }

    @Override
    public LocDetails getLocDetails(String locId, String lang) {
        LocDetails locDetails = new LocDetails();

        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, PartyLoc.QUERY_GET_BY_LOC);
        params.put(PartyLoc.PARAM_LOC_ID, locId);
        params.put(PartyLoc.PARAM_LANG, lang);

        locDetails.setParties(getRepository().getEntityList(PartyLoc.class, params));

        params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, RrrLocDetails.QUERY_SELECT);
        params.put(RrrLocDetails.PARAM_LOC_ID, locId);
        params.put(RrrLocDetails.PARAM_LANG, lang);

        locDetails.setRrrs(getRepository().getEntityList(RrrLocDetails.class, params));
        return locDetails;
    }

    @Override
    public List<LocSearchResult> searchLocs(LocSearchParams searchParams) {
        if (searchParams == null) {
            return null;
        }

        if (searchParams.getBaUnitId() == null) {
            searchParams.setBaUnitId("");
        }
        if (searchParams.getPartyId() == null) {
            searchParams.setPartyId("");
        }
        if (searchParams.getLocId() == null) {
            searchParams.setLocId("");
        }

        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, LocSearchResult.QUERY_SELECT);
        params.put(LocSearchResult.PARAM_BA_UNIT_ID, searchParams.getBaUnitId());
        params.put(LocSearchResult.PARAM_LOC_ID, searchParams.getLocId());
        params.put(LocSearchResult.PARAM_PARTY_ID, searchParams.getPartyId());

        return getRepository().getEntityList(LocSearchResult.class, params);
    }

    @Override
    public List<RestrictionSearchResult> searchRestrictions(RestrictionSearchParams searchParams) {
        if (searchParams == null) {
            return null;
        }

        if (searchParams.getBundleNo() == null) {
            searchParams.setBundleNo("");
        }
        if (searchParams.getBundlePageNo() == null) {
            searchParams.setBundlePageNo("");
        }
        if (searchParams.getLanguageCode() == null) {
            searchParams.setLanguageCode("en");
        }
        if (searchParams.getMapSheetId() == null) {
            searchParams.setMapSheetId("");
        }
        if (searchParams.getOwnerLastName() == null) {
            searchParams.setOwnerLastName("");
        }
        if (searchParams.getOwnerName() == null) {
            searchParams.setOwnerName("");
        }
        if (searchParams.getParcelNo() == null) {
            searchParams.setParcelNo("");
        }
        if (searchParams.getPriceFrom() == null) {
            searchParams.setPriceFrom(new BigDecimal(0));
        }
        if (searchParams.getPriceTo() == null) {
            searchParams.setPriceTo(new BigDecimal("99999999999999999999"));
        }
        if (searchParams.getRefDateFrom() == null) {
            searchParams.setRefDateFrom(0);
        }
        if (searchParams.getRefDateTo() == null) {
            searchParams.setRefDateTo(99999999);
        }
        if (searchParams.getReferenceNo() == null) {
            searchParams.setReferenceNo("");
        }
        if (searchParams.getRegDateFrom() == null) {
            searchParams.setRegDateFrom(0);
        }
        if (searchParams.getRegDateTo() == null) {
            searchParams.setRegDateTo(99999999);
        }
        if (searchParams.getRegNumber() == null) {
            searchParams.setRegNumber("");
        }
        if (searchParams.getRestrictionReasonCode() == null) {
            searchParams.setRestrictionReasonCode("");
        }
        if (searchParams.getRestrtictionOfficeName() == null) {
            searchParams.setRestrtictionOfficeName("");
        }
        if (searchParams.getSerialNo() == null) {
            searchParams.setSerialNo("");
        }
        if (searchParams.getSourceTypeCode() == null) {
            searchParams.setSourceTypeCode("");
        }
        if (searchParams.getVdcCode() == null) {
            searchParams.setVdcCode("");
        }
        if (searchParams.getWardNo() == null) {
            searchParams.setWardNo("");
        }

        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, RestrictionSearchResult.QUERY_SELECT);
        params.put(RestrictionSearchResult.PARAM_BUNDLE_NO, searchParams.getBundleNo());
        params.put(RestrictionSearchResult.PARAM_BUNDLE_PAGE_NO, searchParams.getBundlePageNo());
        params.put(RestrictionSearchResult.PARAM_LANG, searchParams.getLanguageCode());
        params.put(RestrictionSearchResult.PARAM_MAP_SHEET_ID, searchParams.getMapSheetId());
        params.put(RestrictionSearchResult.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        params.put(RestrictionSearchResult.PARAM_OWNER_LAST_NAME, searchParams.getOwnerLastName());
        params.put(RestrictionSearchResult.PARAM_OWNER_NAME, searchParams.getOwnerName());
        params.put(RestrictionSearchResult.PARAM_PARCEL_NO, searchParams.getParcelNo());
        params.put(RestrictionSearchResult.PARAM_PRICE_FROM, searchParams.getPriceFrom());
        params.put(RestrictionSearchResult.PARAM_PRICE_TO, searchParams.getPriceTo());
        params.put(RestrictionSearchResult.PARAM_REFERENCE_NO, searchParams.getReferenceNo());
        params.put(RestrictionSearchResult.PARAM_REF_DATE_FROM, searchParams.getRefDateFrom());
        params.put(RestrictionSearchResult.PARAM_REF_DATE_TO, searchParams.getRefDateTo());
        params.put(RestrictionSearchResult.PARAM_REG_DATE_FROM, searchParams.getRegDateFrom());
        params.put(RestrictionSearchResult.PARAM_REG_DATE_TO, searchParams.getRegDateTo());
        params.put(RestrictionSearchResult.PARAM_REG_NUMBER, searchParams.getRegNumber());
        params.put(RestrictionSearchResult.PARAM_RESTR_OFFICE, searchParams.getRestrtictionOfficeName());
        params.put(RestrictionSearchResult.PARAM_RESTR_REASON_CODE, searchParams.getRestrictionReasonCode());
        params.put(RestrictionSearchResult.PARAM_SERIAL_NO, searchParams.getSerialNo());
        params.put(RestrictionSearchResult.PARAM_SOURCE_TYPE_CODE, searchParams.getSourceTypeCode());
        params.put(RestrictionSearchResult.PARAM_VDC_CODE, searchParams.getVdcCode());
        params.put(RestrictionSearchResult.PARAM_WARD_NO, searchParams.getWardNo());

        return getRepository().getEntityList(RestrictionSearchResult.class, params);
    }

    @Override
    public List<RestrictionInfo> searchRestrictionInfo(RestrictionInfoParams searchParams) {
        if (searchParams.getLangCode() == null) {
            searchParams.setLangCode("en");
        }
        if (searchParams.getRefDate() == null) {
            searchParams.setRefDate(0);
        }
        if (searchParams.getRefNumber() == null) {
            searchParams.setRefNumber("");
        }

        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, RestrictionInfo.QUERY_SELECT);
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        params.put(RestrictionInfo.PARAM_LANG, searchParams.getLangCode());
        params.put(RestrictionInfo.PARAM_REF_DATE, searchParams.getRefDate());
        params.put(RestrictionInfo.PARAM_REF_NUM, searchParams.getRefNumber());

        List<RestrictionInfo> result = getRepository().getEntityList(RestrictionInfo.class, params);

        if (result != null) {
            for (RestrictionInfo restirction : result) {
                params.clear();
                params.put(CommonSqlProvider.PARAM_QUERY, Owner.QUERY_SELECT);
                params.put(Owner.PARAM_LANG, searchParams.getLangCode());
                params.put(Owner.PARAM_BA_UNIT_ID, restirction.getBaUnitId());

                restirction.setOwners(getRepository().getEntityList(Owner.class, params));

                params.clear();
                params.put(CommonSqlProvider.PARAM_QUERY, RestrictionRrr.QUERY_SELECT);
                params.put(RestrictionRrr.PARAM_REF_DATE, searchParams.getRefDate());
                params.put(RestrictionRrr.PARAM_REF_NUM, searchParams.getRefNumber());
                params.put(RestrictionRrr.PARAM_BA_UNIT_ID, restirction.getBaUnitId());
                restirction.setRrrs(getRepository().getEntityList(RestrictionRrr.class, params));
            }
        }
        return result;
    }
    
    @Override
    public String getCrs(int srid){
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, "select srtext from public.spatial_ref_sys where srid=#{srid}");
        params.put("srid", srid);

        ArrayList<HashMap> list = getRepository().executeFunction(params);

        if (list.size() > 0 && list.get(0) != null && list.get(0).size() > 0) {
            return (String)((Entry) list.get(0).entrySet().iterator().next()).getValue();
        } else {
            return null;
        }
    }
}
