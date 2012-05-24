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

import java.util.ArrayList;
import java.util.List;
import javax.transaction.UserTransaction;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.sola.services.common.repository.RepositoryUtility;
import org.sola.services.common.repository.entities.ChildEntityInfo;
import org.sola.services.common.test.AbstractEJBTest;
import org.sola.services.ejb.address.repository.entities.Address;
import org.sola.services.ejb.party.repository.entities.*;

/**
 *
 * @author Manoku
 */
public class PartyEJBIT extends AbstractEJBTest {

    private static String ADDR_MODULE_NAME = "sola-address-1_0-SNAPSHOT";
    private static String PARTY_MODULE_NAME = "sola-party-1_0-SNAPSHOT";
    private static final String LOGIN_USER = "test";
    private static final String LOGIN_PASS = "test";

    public PartyEJBIT() {
        super();
    }

    @Before
    public void setUp() throws Exception {
        login(LOGIN_USER, LOGIN_PASS);
    }

    @After
    public void tearDown() throws Exception {
        logout();
    }

    /**
     * Test of getCommunicationTypes method, of class PartyEJB.
     */
    @Ignore
    @Test
    public void testGetCommunicationTypes() throws Exception {
        System.out.println("getCommunicationTypes");
        PartyEJBLocal instance = (PartyEJBLocal) getEJBInstance(PartyEJB.class.getSimpleName());
        //List expResult = null;
        System.out.println("In english");
        List<CommunicationType> result = instance.getCommunicationTypes("en");
        //assertEquals(expResult, result);
        System.err.println("Found:" + result.size());
        if (result.size() > 0) {
            System.out.println("First item display_value is:" + result.get(0).getDisplayValue());
        }
        System.out.println("In italian");
        result = instance.getCommunicationTypes("it");
        //assertEquals(expResult, result);
        System.err.println("Found:" + result.size());
        if (result.size() > 0) {
            System.out.println("First item display_value is:" + result.get(0).getDisplayValue());
        }
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getAgents method, of class PartyEJB.
     */
    @Ignore
    @Test
    public void testGetAgents() throws Exception {
        System.out.println("getAgents");
        PartyEJBLocal instance = (PartyEJBLocal) getEJBInstance(PartyEJB.class.getSimpleName());
        List<Party> result = instance.getAgents();
        assertNotNull(result);
        assertTrue(result.size() > 0);
        for (Party p : result) {
            assertEquals(Party.TYPE_CODE_NON_NATURAL_PERSON, p.getTypeCode());
        }

    }

    //@Ignore
    @Test
    public void testGetParty() throws Exception {
//        System.out.println("get party");
//        List<ChildEntityInfo> c = RepositoryUtility.getChildEntityInfo(Party.class);
//        Class<?> clazz = c.get(0).getEntityClass();
//        Class<?> clazz2 = c.get(1).getEntityClass();
//        System.out.println("do nothing");
        
        
            PartyEJBLocal instance = (PartyEJBLocal) getEJBInstance(PartyEJB.class.getSimpleName());
             Party owner = new Party();
            Party list = instance.getParty("pqr11");
            
           
    }

    /**
     * Test of saveParty method, of class PartyEJB.
     */
    @Ignore
    @Test
    public void testSaveParty() throws Exception {
        System.out.println(">>> Testing saving land owner");
        UserTransaction tx = getUserTransaction();
        try {
            PartyEJBLocal instance = (PartyEJBLocal) getEJBInstance(PartyEJB.class.getSimpleName());
            List<Party> list = new ArrayList<Party>();
            Party owner = new Party();
            owner.setId("pqr11");
            owner.setTypeCode("baunit");
            owner.setName("sfdf");
            owner.setLastName("sfdf");

            owner.setAddressId("myIdsd");
            Address add = new Address();
            add.setDescription("dfof");
            owner.setAddress(add);
            List<PartyRole> roleList = new ArrayList<PartyRole>();
            PartyRole role = new PartyRole();
            role.setPartyId("ofikfrri");
            role.setRoleCode("bank");
            roleList.add(role);
            owner.setRoleList(roleList);
            owner.setRightHolder(false);
            list.add(owner);
            tx.begin();
            for (Party onn : list) {
                instance.saveParty(onn);
            }
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }
    }

}
