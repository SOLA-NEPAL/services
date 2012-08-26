/**
 * ******************************************************************************************
 * Copyright (C) 2012 - Food and Agriculture Organization of the United Nations (FAO).
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice,this list
 *       of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice,this list
 *       of conditions and the following disclaimer in the documentation and/or other
 *       materials provided with the distribution.
 *    3. Neither the name of FAO nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,STRICT LIABILITY,OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.address.businesslogic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.sola.services.ejb.address.repository.entities.Address;
import org.sola.services.common.ejbs.AbstractEJB;
import org.sola.services.common.repository.CommonSqlProvider;
import org.sola.services.ejb.address.repository.entities.Vdc;

/**
 * 
 * @author soladev
 */
@Stateless
@EJB(name = "java:global/SOLA/AddressEJBLocal", beanInterface = AddressEJBLocal.class)
public class AddressEJB extends AbstractEJB implements AddressEJBLocal {

    @Override
    protected void postConstruct() {
        setEntityPackage(Address.class.getPackage().getName());
    }
    
    @Override
    public Address getAddress(String id) {
        return getRepository().getEntity(Address.class, id);
    }

    @Override
    public Address saveAddress(Address address) {
        return getRepository().saveEntity(address);
    }

    @Override
    public List<Address> getAddresses(List<String> ids) {
        return getRepository().getEntityListByIds(Address.class, ids);
    }
    
    @Override
    public List<Vdc> getVdcs(String districtCode, String lang) {
        HashMap params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, Vdc.WHERE_BY_DISTRICT_CODE);
        params.put(CommonSqlProvider.PARAM_LANGUAGE_CODE, lang);
        params.put(Vdc.PARAM_DISTRICT_CODE, districtCode);
        return getRepository().getEntityList(Vdc.class, params);
    }
    
    @Override
    public List<Vdc> getVdcList() {
        return getRepository().getEntityList(Vdc.class);
    }

    @Override
    public Vdc getVdcByCode(String vdcCode) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, Vdc.GET_BY_VDC_CODE);
        params.put(Vdc.VDC_CODE_PARAM, vdcCode);
        return getRepository().getEntity(Vdc.class, params);
    }

    @Override
    public Vdc getVdcByName(String vdcName) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, Vdc.GET_BY_VDC_NAME);
        params.put(Vdc.VDC_NAME_PARAM, vdcName);
        return getRepository().getEntity(Vdc.class, params);
    }
    
    @Override

    public Vdc saveVdc(Vdc vdc) {
        return getRepository().saveEntity(vdc);
    }
}
