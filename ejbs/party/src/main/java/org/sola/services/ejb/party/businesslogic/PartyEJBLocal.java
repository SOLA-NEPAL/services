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
package org.sola.services.ejb.party.businesslogic;

import java.util.List;
import javax.ejb.Local;
import org.sola.services.common.ejbs.AbstractEJBLocal;
import org.sola.services.ejb.party.repository.entities.*;

/**
 *
 * @author soladev
 */
@Local
public interface PartyEJBLocal extends AbstractEJBLocal {

    Party getParty(String id);

    List<Party> getParties(List<String> partyIds);

    Party saveParty(Party party);

    List<CommunicationType> getCommunicationTypes(String languageCode);

    List<PartyType> getPartyTypes(String languageCode);

    List<PartyRoleType> getPartyRoles(String languageCode);

    List<IdType> getIdTypes(String languageCode);

    List<GenderType> getGenderTypes(String languageCode);

    List<Party> getAgents();

    List<IdOfficeType> getIdOfficeTypes(String languageCode);

    List<GrandFatherType> getGrandFatherTypes(String languageCode);

    List<FatherType> getFatherTypes(String languageCode);

    List<PartyCategory> getPartyCategorys(List<String> partyCategoryIds);
}
