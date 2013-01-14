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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import org.sola.services.common.ejbs.AbstractEJBLocal;
import org.sola.services.ejb.search.repository.entities.*;
import org.sola.services.ejb.search.spatial.QueryForNavigation;
import org.sola.services.ejb.search.spatial.QueryForSelect;
import org.sola.services.ejb.search.spatial.ResultForNavigationInfo;
import org.sola.services.ejb.search.spatial.ResultForSelectionInfo;

/**
 *
 * @author manoku
 */
@Local
public interface SearchEJBLocal extends AbstractEJBLocal {

    GenericResult getGenericResultList(String queryName, Map params);

    List<ApplicationSearchResult> searchApplications(ApplicationSearchParams params);

    List<ApplicationSearchResult> getAssignedApplications(String locale);

    ResultForNavigationInfo getSpatialResult(QueryForNavigation spatialQuery, String officeCode);

    List<ConfigMapLayer> getConfigMapLayerList(String languageCode);

    List<ResultForSelectionInfo> getSpatialResultFromSelection(
            List<QueryForSelect> queriesForSelection);

    HashMap<String, String> getMapSettingList();

    List<PartySearchResult> searchParties(PartySearchParams searchParams);

    List<SourceSearchResult> searchSources(SourceSearchParams searchParams);

    List<UserSearchResult> getUsersByOffice(String officeCode);

    List<UserSearchResult> getUsersByDepartment(String departmentCode);

    List<UserSearchResult> getUsersWithAssignRightByDepartment(String departmentCode);

    List<UserSearchResult> getUsersWithAssignRightByOffice(String officeCode);

    List<UserSearchResult> searchUsers(UserSearchParams searchParams);

    List<ApplicationLogResult> getApplicationLog(String applicationId);

    List<BrSearchResult> searchBr(BrSearchParams searchParams, String lang);

    List<BaUnitSearchResult> searchBaUnits(BaUnitSearchParams searchParams);

    BaUnitSearchResult searchBaUnitById(String baUnitId);

    List<BaUnitSearchResult> searchBaUnitsByIds(List<String> ids);

    List<DynamicQuery> getQueryListAll();

    List<CadastreObjectSearchResult> searchCadastreObjects(String searchBy, String searchString);
    
    List<CadastreObjectSearchResult> searchCadastreObjects(List<String> ids);

    List<CadastreObjectSearchResultExt> searchCadastreObjects(String langCode, CadastreObjectSearchParams searchParams);

    List<BaUnitSearchResult> getAllBaUnitsByService(String serviceId);

    LocDetails getLocDetails(String locId, boolean current, String lang);

    List<LocSearchResult> searchLocs(LocSearchParams params);

    List<RestrictionSearchResult> searchRestrictions(RestrictionSearchParams params);

    List<RestrictionInfo> searchRestrictionInfo(RestrictionInfoParams params);

    String getCrs(int srid);
}
