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
package org.sola.services.ejb.cadastre.businesslogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.sola.common.RolesConstants;
import org.sola.services.common.ejbs.AbstractEJB;
import org.sola.services.common.repository.CommonSqlProvider;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;
import org.sola.services.ejb.cadastre.repository.entities.*;
import org.sola.services.ejbs.admin.businesslogic.AdminEJBLocal;

/**
 * Implementation of {
 * <p/>
 * @link CadastreEJBLocal} interface.
 */
@Stateless
@EJB(name = "java:global/SOLA/CadastreEJBLocal", beanInterface = CadastreEJBLocal.class)
public class CadastreEJB extends AbstractEJB implements CadastreEJBLocal {

    @EJB
    AdminEJBLocal adminEJB;
    
    @Override
    public List<CadastreObjectType> getCadastreObjectTypes(String languageCode) {
        return getRepository().getCodeList(CadastreObjectType.class, languageCode);
    }

    @Override
    public CadastreObject getCadastreObject(String id) {
        return getRepository().getEntity(CadastreObject.class, id);
    }

    @Override
    public List<CadastreObject> getCadastreObjects(List<String> cadastreObjIds) {
        return getRepository().getEntityListByIds(CadastreObject.class, cadastreObjIds);
    }

    @Override
    public List<CadastreObject> getCadastreObjectByParts(String searchString) {
        Integer numberOfMaxRecordsReturned = 10;
        HashMap params = new HashMap();
        params.put("search_string", searchString);
        params.put(CommonSqlProvider.PARAM_LIMIT_PART, numberOfMaxRecordsReturned);
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        return getRepository().getEntityList(CadastreObject.class,
                CadastreObject.QUERY_WHERE_SEARCHBYPARTS, params);
    }
    
    @Override
    public List<CadastreObject> getPendingParcelByParts(String searchString) {
        Integer numberOfMaxRecordsReturned = 10;
        HashMap params = new HashMap();
        params.put("search_string", searchString);
        params.put(CommonSqlProvider.PARAM_LIMIT_PART, numberOfMaxRecordsReturned);
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        return getRepository().getEntityList(CadastreObject.class,
                CadastreObject.QUERY_WHERE_SEARCHBYPARTS_PENDING, params);
    }

    @Override
    public CadastreObject getCadastreObjectByPoint(double x, double y, int srid) {
        HashMap params = new HashMap();
        params.put("x", x);
        params.put("y", y);
        params.put("srid", srid);
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        return getRepository().getEntity(
                CadastreObject.class, CadastreObject.QUERY_WHERE_SEARCHBYPOINT, params);
    }

    @Override
    public CadastreObject saveCadastreObject(CadastreObject cadastreObject) {
        if(cadastreObject.isNew()){
            cadastreObject.setOfficeCode(adminEJB.getCurrentOfficeCode());
        } else {
            adminEJB.checkOfficeCode(cadastreObject.getOfficeCode());
        }
        return getRepository().saveEntity(cadastreObject);
    }

    @Override
    public List<CadastreObject> getCadastreObjectsByBaUnit(String baUnitId) {
        HashMap params = new HashMap();
        params.put("ba_unit_id", baUnitId);
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        return getRepository().getEntityList(CadastreObject.class,
                CadastreObject.QUERY_WHERE_SEARCHBYBAUNIT, params);
    }

    @Override
    public List<CadastreObject> getCadastreObjectsByService(String serviceId) {
        HashMap params = new HashMap();
        params.put("service_id", serviceId);
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        return getRepository().getEntityList(CadastreObject.class,
                CadastreObject.QUERY_WHERE_SEARCHBYSERVICE, params);
    }

    /**
     *
     * @param transactionId
     * @param statusCode
     */
    @Override
    public void ChangeStatusOfCadastreObjects(
            String transactionId, String filter, String statusCode) {
        HashMap params = new HashMap();
        params.put("transaction_id", transactionId);
        List<CadastreObjectStatusChanger> involvedCoList =
                getRepository().getEntityList(CadastreObjectStatusChanger.class, filter, params);
        for (CadastreObjectStatusChanger involvedCo : involvedCoList) {
            adminEJB.checkOfficeCode(involvedCo.getOfficeCode());
            involvedCo.setStatusCode(statusCode);
            getRepository().saveEntity(involvedCo);
        }
    }

    @Override
    public List<CadastreObjectTarget> getCadastreObjectTargetsByTransaction(String transactionId) {
        Map params = new HashMap<String, Object>();
        params.put(
                CommonSqlProvider.PARAM_WHERE_PART,
                CadastreObjectTarget.QUERY_WHERE_SEARCHBYTRANSACTION);
        params.put("transaction_id", transactionId);
        return getRepository().getEntityList(CadastreObjectTarget.class, params);
    }

    @Override
    public List<SurveyPoint> getSurveyPointsByTransaction(String transactionId) {
        Map params = new HashMap<String, Object>();
        params.put(
                CommonSqlProvider.PARAM_WHERE_PART,
                SurveyPoint.QUERY_WHERE_SEARCHBYTRANSACTION);
        params.put("transaction_id", transactionId);
        return getRepository().getEntityList(SurveyPoint.class, params);
    }

    @Override
    public List<CadastreObject> getCadastreObjectsByTransaction(String transactionId) {
        Map params = new HashMap<String, Object>();
        params.put(
                CommonSqlProvider.PARAM_WHERE_PART,
                CadastreObject.QUERY_WHERE_SEARCHBYTRANSACTION);
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        params.put("transaction_id", transactionId);
        return getRepository().getEntityList(CadastreObject.class, params);

    }

    @Override
    public CadastreObjectNode getCadastreObjectNode(
            double xMin, double yMin, double xMax, double yMax, int srid) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_FROM_PART,
                CadastreObjectNode.QUERY_GET_BY_RECTANGLE_FROM_PART);
        params.put(CommonSqlProvider.PARAM_WHERE_PART,
                CadastreObjectNode.QUERY_GET_BY_RECTANGLE_WHERE_PART);
        params.put(CommonSqlProvider.PARAM_LIMIT_PART, 1);
        params.put("minx", xMin);
        params.put("miny", yMin);
        params.put("maxx", xMax);
        params.put("maxy", yMax);
        params.put("srid", srid);
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        CadastreObjectNode cadastreObjectNode = getRepository().getEntity(
                CadastreObjectNode.class, params);
        if (cadastreObjectNode != null) {
            params.clear();
            params.put("geom", cadastreObjectNode.getGeom());
            cadastreObjectNode.setCadastreObjectList(getRepository().getEntityList(
                    CadastreObject.class, CadastreObject.QUERY_WHERE_SEARCHBYGEOM, params));
        }
        return cadastreObjectNode;

    }

    @Override
    public CadastreObjectNode getCadastreObjectNodePotential(
            double xMin, double yMin, double xMax, double yMax, int srid) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_FROM_PART,
                CadastreObjectNode.QUERY_GET_BY_RECTANGLE_POTENTIAL_FROM_PART);
        params.put(CommonSqlProvider.PARAM_LIMIT_PART, 1);
        params.put("minx", xMin);
        params.put("miny", yMin);
        params.put("maxx", xMax);
        params.put("maxy", yMax);
        params.put("srid", srid);
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        CadastreObjectNode cadastreObjectNode = getRepository().getEntity(
                CadastreObjectNode.class, params);
        if (cadastreObjectNode != null) {
            params.clear();
            params.put("geom", cadastreObjectNode.getGeom());
            cadastreObjectNode.setCadastreObjectList(getRepository().getEntityList(
                    CadastreObject.class, CadastreObject.QUERY_WHERE_SEARCHBYGEOM, params));
        }
        return cadastreObjectNode;
    }

    @Override
    public List<CadastreObjectNodeTarget> getCadastreObjectNodeTargetsByTransaction(
            String transactionId) {
        Map params = new HashMap<String, Object>();
        params.put(
                CommonSqlProvider.PARAM_WHERE_PART,
                CadastreObjectNodeTarget.QUERY_WHERE_SEARCHBYTRANSACTION);
        params.put("transaction_id", transactionId);
        return getRepository().getEntityList(CadastreObjectNodeTarget.class, params);
    }

    @Override
    public List<CadastreObjectTargetRedefinition> getCadastreObjectRedefinitionTargetsByTransaction(
            String transactionId) {
        Map params = new HashMap<String, Object>();
        params.put(
                CommonSqlProvider.PARAM_WHERE_PART,
                CadastreObjectTarget.QUERY_WHERE_SEARCHBYTRANSACTION);
        params.put("transaction_id", transactionId);
        return getRepository().getEntityList(CadastreObjectTargetRedefinition.class, params);
    }

    @Override
    public void approveCadastreRedefinition(String transactionId) {
        List<CadastreObjectTargetRedefinition> targetObjectList =
                this.getCadastreObjectRedefinitionTargetsByTransaction(transactionId);
        for (CadastreObjectTargetRedefinition targetObject : targetObjectList) {
            CadastreObjectStatusChanger cadastreObject =
                    this.getRepository().getEntity(CadastreObjectStatusChanger.class,
                    targetObject.getCadastreObjectId());
            adminEJB.checkOfficeCode(cadastreObject.getOfficeCode());
            cadastreObject.setGeomPolygon(targetObject.getGeomPolygon());
            cadastreObject.setTransactionId(transactionId);
            cadastreObject.setApprovalDatetime(null);
            this.saveEntity(cadastreObject);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="By Kabindra"> 
    //--------------------------------------------------------------------------
    @Override
    public List<CadastreObject> getCadastreObjectBy_Intersection(String geom, int srid) {
        HashMap params = new HashMap();
        params.put("geom", geom);
        params.put("srid", srid);
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        return getRepository().getEntityList(
                CadastreObject.class, CadastreObject.QUERY_WHERE_SEARCHBY_STRING_INTERSECTION, params);
    }

    @Override
    public List<CadastreObject> getCadastreObjectBy_ByteIntersection(String geom, int srid) {
        HashMap params = new HashMap();
        params.put("geom", geom);
        params.put("srid", srid);
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        return getRepository().getEntityList(
                CadastreObject.class, CadastreObject.QUERY_WHERE_SEARCHBY_BYTE_INTERSECTION, params);
    }
    
    @Override
    public List<ParcelType> getParcelTypeList(){
        return getRepository().getEntityList(ParcelType.class);
    }
    
     @Override
    public List<ParcelType> getParcelTypeList(String languageCode){
        //return getRepository().getEntityList(ParcelType.class);
        return getRepository().getCodeList(ParcelType.class, languageCode);
    }
     
    @Override
    public List<CadastreObject> getCadastreObjectListMem(List<String> mapSheetCode) {
        List<CadastreObject> CadObjs=new ArrayList<CadastreObject>();
        for (int i=0;i<mapSheetCode.size();i++){
            List<CadastreObject> tmpCadObjs= this.loadCadastreObjectList(mapSheetCode.get(i));
            CadObjs.addAll(tmpCadObjs);
        }
        
        return CadObjs;
    }
    
    @Override
    public List<ConstructionObject> getConstructionObjectListMem(List<String> mapSheetCode) {
        List<ConstructionObject> CadObjs=new ArrayList<ConstructionObject>();
        for (int i=0;i<mapSheetCode.size();i++){
            List<ConstructionObject> tmpCadObjs= this.loadConstructionObjectList(mapSheetCode.get(i));
            CadObjs.addAll(tmpCadObjs);
        }
        
        return CadObjs;
    }
    
    @Override
    public List<ConstructionObject> loadConstructionObjectList(String mapSheetCode) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_SELECT_PART, ConstructionObject.GET_BY_SELECT_PART);
        params.put(CommonSqlProvider.PARAM_FROM_PART, ConstructionObject.GET_BY_FROM_PART);
        params.put(CommonSqlProvider.PARAM_WHERE_PART, ConstructionObject.GET_BY_WHERE_PART);
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        params.put(CadastreObject.MAP_SHEET_CODE_PARAM, mapSheetCode);
        return getRepository().getEntityList(ConstructionObject.class, params);
    }
    
    @Override
    public List<CadastreObject> getCadastreObjectByExactParts(String firstpart, String lastpart){
        //Integer numberOfMaxRecordsReturned = 10;
        HashMap params = new HashMap();
        params.put("firstpart", firstpart);
        params.put("lastpart",lastpart);
        //params.put(CommonSqlProvider.PARAM_LIMIT_PART, numberOfMaxRecordsReturned);
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        return getRepository().getEntityList(CadastreObject.class,
                CadastreObject.QUERY_WHERE_SEARCHBY_EXACT_PARTS, params);
    }
    
    @Override
    public List<HashMap> getWardList(String vdccode) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, CadastreObject.GET_BY_WARD_IN_VDC);
        params.put(CadastreObject.VDC_PARAM, vdccode);
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        return getRepository().executeSql(params);
    }

    @Override
    public List<MapSheet> loadWardMapSheet(int mapSheetType,String vdccode, String wardno) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_SELECT_PART, MapSheet.GET_BY_WARD_SELECT_PART);
        params.put(CommonSqlProvider.PARAM_FROM_PART,MapSheet.GET_BY_WARD_FROM_PART);
        params.put(CommonSqlProvider.PARAM_WHERE_PART, MapSheet.GET_BY_WARD_WHERE_PART);
        params.put(MapSheet.VDC_PARAM, vdccode);
        params.put(MapSheet.WARD_NO_PARAM, wardno);
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        return getRepository().getEntityList(MapSheet.class, params);
    }

    @Override
    public List<MapSheet> loadVDCMapSheet(int mapSheetType,String vdccode) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_SELECT_PART, MapSheet.GET_BY_WARD_SELECT_PART);
        params.put(CommonSqlProvider.PARAM_FROM_PART,MapSheet.GET_BY_WARD_FROM_PART);
        params.put(CommonSqlProvider.PARAM_WHERE_PART, MapSheet.GET_BY_VDC_WHERE_PART);
        params.put(MapSheet.VDC_PARAM, vdccode);
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        return getRepository().getEntityList(MapSheet.class, params);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="By Kumar">
    //*****************************************************************************************************************************
    @Override
    public MapSheet getMapSheet(String id) {
        return getRepository().getEntity(MapSheet.class, id);
    }

    @Override
    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SETTINGS)
    public MapSheet saveMapSheet(MapSheet mapSheet) {
        return getRepository().saveEntity(mapSheet);
    }

    @Override
    public List<MapSheet> getMapSheetList() {
        return getRepository().getEntityList(MapSheet.class);
    }

    @Override
    public List<CadastreObject> loadCadastreObjectList(String mapSheetCode) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, CadastreObject.GET_CADASTRE_BY_MAPSHEET_CODE);
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        params.put(CadastreObject.MAP_SHEET_CODE_PARAM, mapSheetCode);
        return getRepository().getEntityList(CadastreObject.class, params);
    }

    @Override
    public List<CadastreObject> getCadastreObjectList(String vdcCode, String wardNo) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, CadastreObject.GET_BY_VDC_AND_WARD_NO);
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        params.put(CadastreObject.VDC_PARAM, vdcCode);
        params.put(CadastreObject.WARD_NO_PARAM, wardNo);
        return getRepository().getEntityList(CadastreObject.class, params);
    }

    @Override
    public List<MapSheet> loadMapSheet(int mapSheetType) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, MapSheet.GET_BY_MAYSHEET_TYPE);
        params.put(MapSheet.MAPSHEET_TYPE_PARAM, mapSheetType);
        return getRepository().getEntityList(MapSheet.class, params);
    }

    //modified by Kabindra.
    @Override
    public CadastreObject getCadastreObject(String vdcCode, String wardNo, int parcelNo) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_SELECT_PART, CadastreObject.GET_BY_ADMIN_BOUNDARY_SELECT_PART);
        params.put(CommonSqlProvider.PARAM_FROM_PART,CadastreObject.GET_BY_ADMIN_BOUNDARY_FROM_PART);
        params.put(CommonSqlProvider.PARAM_WHERE_PART, CadastreObject.GET_BY_ADMIN_BOUNDARY_WHERE_PART);
        params.put(CadastreObject.VDC_PARAM, vdcCode);
        params.put(CadastreObject.PARCEL_NO_PARAM, parcelNo);
        params.put(CadastreObject.WARD_NO_PARAM, wardNo);
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        return getRepository().getEntity(CadastreObject.class, params);
    }

    @Override
    public CadastreObject getCadastreObject(String mapSheetCode, int parcelNo) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, CadastreObject.GET_BY_MAPSHEET_AND_PARCELNO);
        params.put(CadastreObject.MAP_SHEET_CODE_PARAM, mapSheetCode);
        params.put(CadastreObject.PARCEL_NO_PARAM, parcelNo);
        params.put(AbstractReadOnlyEntity.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        return getRepository().getEntity(CadastreObject.class, params);
    }
    //*****************************************************************************************************************************
    //</editor-fold>

    @Override
    public List<MapSheet> getMapSheetListByOffice(String officeCode, String lang) {
        HashMap params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, MapSheet.WHERE_BY_OFFICE_CODE);
        params.put(CommonSqlProvider.PARAM_LANGUAGE_CODE, lang);
        params.put(MapSheet.PARAM_OFFICE_CODE, officeCode);
        return getRepository().getEntityList(MapSheet.class, params);
    }
    
    @Override
    public List<MapSheet> getMapSheetListByOffice(String lang) {
        HashMap params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, MapSheet.WHERE_BY_OFFICE_CODE);
        params.put(CommonSqlProvider.PARAM_LANGUAGE_CODE, lang);
        params.put(MapSheet.PARAM_OFFICE_CODE, adminEJB.getCurrentOfficeCode());
        return getRepository().getEntityList(MapSheet.class, params);
    }
}
