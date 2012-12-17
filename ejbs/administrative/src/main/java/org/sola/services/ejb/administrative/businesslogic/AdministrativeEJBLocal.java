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

import java.util.List;
import javax.ejb.Local;
import org.sola.services.common.ejbs.AbstractSolaTransactionEJBLocal;
import org.sola.services.ejb.administrative.repository.entities.*;

/**
 * Provides local interface for administrative ejbs.
 */
@Local
public interface AdministrativeEJBLocal extends AbstractSolaTransactionEJBLocal {

    List<ChangeStatusType> getChangeStatusTypes(String languageCode);

    List<BaUnitType> getBaUnitTypes(String languageCode);

    List<MortgageType> getMortgageTypes(String languageCode);

    List<RrrGroupType> getRRRGroupTypes(String languageCode);

    List<RrrType> getRRRTypes(String languageCode);

    BaUnit getBaUnitById(String id);

    Rrr getRrr(String id);

    BaUnit getBaUnitByCode(String nameFirstPart, String nameLastPart);

    BaUnit saveBaUnit(String serviceId, BaUnit baUnit);

    List<BaUnit> getBaUnitsByTransactionId(String transactionId);

    List<BaUnitRelType> getBaUnitRelTypes(String languageCode);

    BaUnit terminateBaUnit(String baUnitId, String serviceId);

    BaUnit cancelBaUnitTermination(String baUnitId);

    List<RrrLoc> getRrrLocsById(String locId);

    Moth saveMoth(Moth moth);

    Moth getMoth(String id);

    List<Moth> getMoths(String vdcCode, String mothLuj);

    Moth getMoth(String vdcCode, String mothLuj, String mothLujNumber);

    Loc saveLoc(Loc loc);

    Loc getLoc(String id);

    LocWithMoth getLocWithMoth(String id);

    BaUnit saveBaUnit(BaUnit baUnit);

    LocWithMoth getLocByPageNoAndMoth(LocSearchByMothParams searchParams);

    List<LocWithMoth> getLocListByPageNoAndMoth(LocSearchByMothParams searchParams);

    List<Loc> getLocList(String mothId);

    List<RestrictionReason> getRestrictionReasons(String languageCode);

    List<RestrictionReleaseReason> getRestrictionReleaseReasons(String languageCode);

    List<RestrictionOffice> getRestrictionOffices(String languageCode);

    List<OwnerType> getOwnerTypes(String languageCode);

    List<OwnershipType> getOwnershipTypes(String languageCode);

    List<TenancyType> getTenancyTypes(String languageCode);

    void deletePendingBaUnit(String baUnitId);

    List<Moth> searchMoths(MothSearchParams params);
}
