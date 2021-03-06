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
package org.sola.services.ejb.administrative.businesslogic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Status;
import javax.transaction.UserTransaction;
import org.junit.After;
import static org.junit.Assert.*;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.sola.common.DateUtility;
import org.sola.services.common.EntityAction;
import org.sola.services.common.test.AbstractEJBTest;
import org.sola.services.ejb.address.repository.entities.Address;
import org.sola.services.ejb.administrative.repository.entities.*;
import org.sola.services.ejb.cadastre.repository.entities.CadastreObject;
import org.sola.services.ejb.cadastre.repository.entities.SpatialValueArea;
import org.sola.services.ejb.party.businesslogic.PartyEJB;
import org.sola.services.ejb.party.businesslogic.PartyEJBLocal;
import org.sola.services.ejb.party.repository.entities.Party;

/**
 *
 * @author soladev
 */
public class AdministrativeEJBIT extends AbstractEJBTest {

    static private String baUnitId = null;
    private static final String LOGIN_USER = "test";
    private static final String LOGIN_PASS = "test";

    public AdministrativeEJBIT() {
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
     * Test getting RrrLoc
     */
    @Ignore
    @Test
    public void testGetLocs() throws Exception {
        System.out.println("====> Getting Locs by ID");
        AdministrativeEJBLocal instance = (AdministrativeEJBLocal) getEJBInstance(AdministrativeEJB.class.getSimpleName());
        //List<RrrLoc> rrrLocs = instance.getRrrLocsById("a7fdc18d-b76a-43aa-9878-2e115bb52b86");
        List<Loc> rrrLocs = instance.getLocList("32861b4a-727d-4837-99b9-5e03497aac15");
        assertNotNull("No RrrLocs found", rrrLocs);
        assertTrue("RrrLoc list is empty", rrrLocs.size() > 0);
        System.out.println("====> Found " + rrrLocs.size() + " RRRLocs.");
    }

    /**
     * Test of createBaUnit method, of class AdministrativeEJB.
     */
    @Ignore
    @Test
    public void testBaUnitOperations() throws Exception {

        System.out.println("Create ba unit operations");

        AdministrativeEJBLocal instance = (AdministrativeEJBLocal) getEJBInstance(AdministrativeEJB.class.getSimpleName());


        // Manage the scope of the transction 
        UserTransaction tx = getUserTransaction();
        try {
            tx.begin();
            System.out.println("Create new baunit with 2 rrrs, 2 cadastre objects");
            BaUnit baUnit = new BaUnit();
            baUnit.setName("Test BA Unit Name");
            baUnit.setNameFirstPart("nameFirstPart");
            baUnit.setNameLastPart("nameLastPart");
            baUnit.setTypeCode("administrativeUnit");

            baUnit.setBaUnitNotationList(new ArrayList<BaUnitNotation>());
            baUnit.getBaUnitNotationList().add(this.getNotation("ba unit"));

            List<Rrr> rrrs = new ArrayList<Rrr>();
            rrrs.add(this.getRrr("createbaunit-1", "ownership"));
            rrrs.add(this.getRrr("createbaunit-2", "mortgage"));
            baUnit.setRrrList(rrrs);

            baUnit.setCadastreObject(this.getCadastreObject("part1", "part1"));
            BaUnit result = instance.saveBaUnit(null, baUnit);
            assertNotNull(result);
            System.out.println("Creation of baunit succeeded.");
            baUnitId = result.getId();
            System.out.println("Update ba unit. Adding a new rrr");
            result.getRrrList().add(this.getRrr("rrr-update", "ownership"));
            result = instance.saveBaUnit(null, result);
            assertNotNull(result);
            System.out.println("Succeeded.");
            System.out.println("Update ba unit. Adding a new rrrShare to existing rrr");
            result = instance.saveBaUnit(null, result);
            assertNotNull(result);
            System.out.println("Succeeded.");
            System.out.println("Adding new rrr in an existing baunit succeeded.");
            System.out.println("Update ba unit. Remove first rrr in the list");
            result.getRrrList().get(1).setEntityAction(EntityAction.DELETE);
            result = instance.saveBaUnit("4000", result);
            assertNotNull(result);
            System.out.println("Succeeded.");
            tx.commit();
        } catch (Exception ex) {
            System.out.println("BA Unit could not be created: \nReason: " + ex.getMessage());
            fail("Failed.");
        } finally {
            if (tx.getStatus() != Status.STATUS_NO_TRANSACTION) {
                tx.rollback();
                System.out.println("Failed Transction!");
            }
        }

    }

    private Rrr getRrr(String notationText, String type) throws Exception {
        Rrr rrr = new Rrr();
        rrr.setTypeCode(type);
        //rrr.setNr("test-nr");
        BaUnitNotation notation = new BaUnitNotation();
        notation.setNotationText(notationText);
        notation.setRrrId(rrr.getId());

        rrr.setNotation(notation);

        if (type.equals("ownership")) {
            RrrShare rrrShare = this.getRrrShare("1000");
        } else {


            //Add party directly to rrr

            //PartyForRrr partyForRrr = new PartyForRrr();
            Party party = this.getParty("1001");
//        partyForRrr.setParty(party);
//        rrr.setPartyForRrrList(new ArrayList<PartyForRrr>());
//        rrr.getPartyForRrrList().add(partyForRrr);
            rrr.setRightHolderList(new ArrayList<Party>());
            rrr.getRightHolderList().add(party);
        }

        return rrr;
    }

    private CadastreObject getCadastreObject(String firstPart, String lastPart) {
        CadastreObject co = new CadastreObject();
        co.setNameFirstPart(firstPart);
        co.setNameLastPart(DateUtility.now().toString());
        co.setTypeCode("parcel");
        return co;
    }

    private RrrShare getRrrShare(String partyId) throws Exception {

        RrrShare rrrShare = null;
        Party party = this.getParty(partyId);
        if (party != null) {
            rrrShare = new RrrShare();
            rrrShare.setNominator(Short.parseShort("1"));
            rrrShare.setDenominator(Short.parseShort("1"));

//        PartyForRrr partyForRrr = new PartyForRrr();
//        partyForRrr.setParty(party);
//        rrrShare.setPartyForRrrList(new ArrayList<PartyForRrr>());
//        rrrShare.getPartyForRrrList().add(partyForRrr);

            rrrShare.setRightHolderList(new ArrayList<Party>());
            rrrShare.getRightHolderList().add(party);
        }

        return rrrShare;
    }

    private Party getParty(String id) throws Exception {
        PartyEJBLocal instance = (PartyEJBLocal) getEJBInstance(PartyEJB.class.getSimpleName());

        System.out.println(
                "get party with id:" + id);
        Party party = instance.getParty(id);

        return party;
    }

    private BaUnitNotation getNotation(String txt) {
        BaUnitNotation notation = new BaUnitNotation();
        notation.setNotationText(txt);
        //notation.setReferenceNr("ref-tmp");
        return notation;
    }

    /**
     * Test of getBaUnitByCode method, of class AdministrativeEJB.
     */
    @Ignore
    @Test
    public void testGetReferenceDataTypes() throws Exception {

        System.out.println("Testing get reference data methods");
        String languageCode = "en";

        AdministrativeEJBLocal instance = (AdministrativeEJBLocal) getEJBInstance(AdministrativeEJB.class.getSimpleName());

//        System.out.print(
//                "getChangeStatusTypes: ");
////        List result = instance.getChangeStatusTypes(languageCode);
//
//        System.out.println(
//                "Results found:" + result.size());

        System.out.print(
                "test GetBaUnitTypes: ");
        List result = instance.getBaUnitTypes(languageCode);

        System.out.println(
                "Results found:" + result.size());

        System.out.print(
                "test getMortgageTypes: ");
        result = instance.getMortgageTypes(languageCode);

        System.out.println(
                "Results found:" + result.size());

        System.out.print(
                "test getRRRGroupTypes: ");
        result = instance.getRRRGroupTypes(languageCode);

        System.out.println(
                "Results found:" + result.size());

        System.out.print(
                "test getRRRTypes: ");
        result = instance.getRRRTypes(languageCode);

        System.out.println(
                "Results found:" + result.size());

        System.out.println(
                "Results found:" + result.size());
    }

    /**
     * Test of getBaUnitByCode method, of class AdministrativeEJB.
     */
    @Test
    @Ignore
    public void testGetBaUnitById() throws Exception {
        baUnitId = "1096f4cc-8ff4-4322-9730-683efd07c2fc";
        //baUnitId = "3118304";

        System.out.println("get baunit with id:" + baUnitId);

        if (baUnitId == null) {
            System.out.println("Test cannot run baUnitId is not set.");
            return;
        }

        AdministrativeEJBLocal instance = (AdministrativeEJBLocal) getEJBInstance(AdministrativeEJB.class.getSimpleName());

        // Manage the scope of the transction 
        UserTransaction tx = getUserTransaction();

        try {
            tx.begin();
            BaUnit baUnit = instance.getBaUnitById(baUnitId);

            if (baUnit == null) {
                System.out.println("BaUnit not found for id:" + baUnitId);
                return;
            }

            System.out.println("Parent BaUnits:" + baUnit.getParentBaUnits().size());
            System.out.println("Child BaUnits:" + baUnit.getChildBaUnits().size());
            System.out.println("nr of rrr:" + baUnit.getRrrList().size());

            if (baUnit.getRrrList().size() > 0) {
                Rrr rrr = baUnit.getRrrList().get(0);
                System.out.println("Nr of parties in rrr:" + rrr.getRightHolderList().size());
                if (rrr.getRightHolderList().size() > 0) {
                    for (Party party : rrr.getRightHolderList()) {
                        System.out.println(String.format("Party information id:%s and name: %s",
                                party.getId(), party.getName()));
                    }
                }
                System.out.println("Deleting the first rrr in the list.");
                //LocalInfo.setUserName("test4");
                rrr.setEntityAction(EntityAction.DELETE);
                instance.saveBaUnit("4000", baUnit);
                System.out.println("Succeded.");
            }

            tx.commit();
        } catch (Exception ex) {

            System.out.println("BA Unit could not be saved: \nReason: " + ex.getMessage());
            fail("Failed.");
        } finally {
            if (tx.getStatus() != Status.STATUS_NO_TRANSACTION) {
                tx.rollback();
                System.out.println("Failed Transction!");
            }
        }
    }

    @Ignore
    @Test
    public void getMoths() throws Exception {
        System.out.println(">>> Testing getting moths");

        UserTransaction tx = getUserTransaction();
        try {
            AdministrativeEJBLocal instance = (AdministrativeEJBLocal) getEJBInstance(AdministrativeEJB.class.getSimpleName());
            tx.begin();
            List<Moth> result = instance.getMoths("1", "M");
            if (result == null) {
                System.out.println(">>> Found null");
            } else {
                System.out.println(">>> Found " + result.size() + "moths");
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }
    }

    @Ignore
    @Test
    public void getMoth() throws Exception {
        System.out.println(">>> Testing getting Moth");
        UserTransaction tx = getUserTransaction();
        try {
            AdministrativeEJBLocal instance = (AdministrativeEJBLocal) getEJBInstance(AdministrativeEJB.class.getSimpleName());
            tx.begin();
            Moth result = instance.getMoth("1", "M", "test1");
//           // Vdc vdc = result.getVdc();
//            System.out.println(">>> Found " + vdc.getDisplayValue());
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }
    }

    @Ignore
    @Test
    public void saveLOC() throws Exception {
        System.out.println(">>> Testing Saving Loc");
        UserTransaction tx = getUserTransaction();
        try {
            AdministrativeEJBLocal instance = (AdministrativeEJBLocal) getEJBInstance((AdministrativeEJB.class.getSimpleName()));
            tx.begin();
            Loc loc = new Loc();
            loc.setMothId("240237f8-f677-4df6-9a4d-94484f6a1d7f");
            loc.setPanaNo("1");
            instance.saveLoc(loc);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }
    }

    @Ignore
    @Test
    public void getLOC() throws Exception {
        System.err.println(">>> Testing getting Loc using id");
        UserTransaction tx = getUserTransaction();
        try {
            AdministrativeEJBLocal instance = (AdministrativeEJBLocal) getEJBInstance(AdministrativeEJB.class.getSimpleName());
            tx.begin();
            Loc loc = instance.getLoc("8cbe76be-b7e6-4aa9-8ea7-64bd9e19f217");
            //System.out.println(loc.getPanaNo());
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }
    }

    @Ignore
    @Test
    public void getMothById() throws Exception {
        System.out.println(">>> Testing getting moth by id");

        UserTransaction tx = getUserTransaction();
        try {
            AdministrativeEJBLocal instance = (AdministrativeEJBLocal) getEJBInstance(AdministrativeEJB.class.getSimpleName());
            tx.begin();
            Moth result = instance.getMoth("032a8c58-a7a6-4831-aeab-1b0d93849e0e");

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }
    }

    @Ignore
    @Test
    public void saveBaUnit() throws Exception {
        System.out.println(">>> Testing saving Bauint");
        UserTransaction tx = getUserTransaction();
        try {
            AdministrativeEJBLocal instance = (AdministrativeEJBLocal) getEJBInstance(AdministrativeEJB.class.getSimpleName());
            BaUnit baUnit = new BaUnit();
            tx.begin();
            baUnit.setTypeCode("administrativeUnit");
            baUnit.setName("TestBaunit");
            baUnit.setNameFirstPart("TestBaunit");
            baUnit.setNameLastPart("TestBaunit");
            baUnit.setStatusCode("current");
            instance.saveBaUnit(baUnit);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());

        }
    }

    @Ignore
    @Test
    public void testSaveMoth() throws Exception {
        System.out.println(">>> Testing saving moth");
        UserTransaction tx = getUserTransaction();
        try {
            AdministrativeEJBLocal instance = (AdministrativeEJBLocal) getEJBInstance(AdministrativeEJB.class.getSimpleName());
            List<Moth> mothList = new ArrayList<Moth>();
            Moth moth = new Moth();
            moth.setMothlujNumber("MOOO2");
            moth.setVdcCode("43055");
            moth.setMothLuj("M");

            Loc loc = new Loc();
            loc.setMothId(moth.getId());
            loc.setPanaNo("1");

            BaUnit baUnit = new BaUnit();
            baUnit.setTypeCode("administrativeUnit");
            baUnit.setName("TestBaunit");
            baUnit.setNameFirstPart("TestBaunit");
            baUnit.setNameLastPart("TestBaunit");
            baUnit.setStatusCode("current");

            CadastreObject cobj = new CadastreObject();
            cobj.setTypeCode("parcel");
            cobj.setStatusCode("pending");
            cobj.setTransactionId("cadastre-transaction");
            cobj.setParcelno("1501");
            //cobj.setParcelType(0);
//            MapSheet mapSheet = new MapSheet();
//            mapSheet.setMapNumber("M0002");
//            mapSheet.setSheetType(0);
//            //mapSheet.setAlphaCode("1");
//            cobj.setMapSheet(mapSheet);
//            cobj.setMapSheetId(mapSheet.getId());

            SpatialValueArea spValA = new SpatialValueArea();
            spValA.setSpatialUnitId(moth.getId());
            spValA.setTypeCode("officialArea");
            BigDecimal bigVal = new BigDecimal(1526836);
            spValA.setSize(bigVal);


            List<SpatialValueArea> spatialValAreaList = new ArrayList<SpatialValueArea>();
            spatialValAreaList.add(spValA);
            // cobj.setSpatialValueAreaList(spatialValAreaList);
            List<CadastreObject> cadObjList = new ArrayList<CadastreObject>();
            cadObjList.add(cobj);
            baUnit.setCadastreObject(cobj);

            Party owner = new Party();
            owner.setTypeCode("baunit");
            owner.setName("Kumar");
            owner.setLastName("Khadka");
            Address add = new Address();
            add.setDescription("Testing");
            add.setVdcCode("43055");
            owner.setAddress(add);
            List<Party> partyList = new ArrayList<Party>();
            partyList.add(owner);

            List<BaUnit> baUnitList = new ArrayList<BaUnit>();
            baUnitList.add(baUnit);
            mothList.add(moth);
            tx.begin();
            for (Moth mh : mothList) {
                instance.saveMoth(mh);
            }
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }
    }

    @Test
    @Ignore
    public void getLOCByMothIdAndPanaNo() throws Exception {
        System.out.println("Testing getting Loc");
        UserTransaction tx = getUserTransaction();
        try {
            AdministrativeEJBLocal instance = (AdministrativeEJBLocal) getEJBInstance(AdministrativeEJB.class.getSimpleName());
            tx.begin();
            MothBasic moth = new MothBasic();
            moth.setId("9a4915ec-765e-4d1b-9e0a-0a73204cea5f");
            moth.setMothLuj("M");
            LocSearchByMothParams params = new LocSearchByMothParams();
            params.setMoth(moth);
            params.setPageNumber("1");
            Loc loc = instance.getLocByPageNoAndMoth(params);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }
    }

    @Ignore
    @Test
    public void saveBaUnitAsParty() throws Exception {
        System.out.println(">>> Testing saving BaUnitAsParty");
        UserTransaction tx = getUserTransaction();
        try {
            AdministrativeEJBLocal instance = (AdministrativeEJBLocal) getEJBInstance(AdministrativeEJB.class.getSimpleName());
            BaUnitAsParty baUnitContains = new BaUnitAsParty();
            tx.begin();
            baUnitContains.setBaUnitId("a0c54e1c-d655-49ce-8f13-c4d9e06eac45");
            baUnitContains.setPartyId("");
            //  instance.saveBaUnitAsParty(baUnitContains);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());

        }
    }

    @Ignore
    @Test
    public void getRrr() throws Exception {
        System.out.println(">>> Testing getting Moth");
        UserTransaction tx = getUserTransaction();
        try {
            AdministrativeEJBLocal instance = (AdministrativeEJBLocal) getEJBInstance(AdministrativeEJB.class.getSimpleName());
            tx.begin();
            Rrr result = instance.getRrr("e7c3a331-bd70-4f78-94fe-97cf66d64e1a");
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }
    }

    @Test
    @Ignore
    public void getLocListByMothIdAndPanaNo() throws Exception {
        System.out.println("Testing getting Loc");
        UserTransaction tx = getUserTransaction();
        try {
            AdministrativeEJBLocal instance = (AdministrativeEJBLocal) getEJBInstance(AdministrativeEJB.class.getSimpleName());
            tx.begin();
            MothBasic moth = new MothBasic();
            moth.setId("5ab0cbad-3e5b-4623-8df7-84524065b533");
            LocSearchByMothParams params = new LocSearchByMothParams();
            params.setMoth(moth);
            params.setPageNumber("1");
            List<LocWithMoth> result = instance.getLocListByPageNoAndMoth(params);
            if (result == null) {
                System.out.println(">>> Found null");
            } else {
                System.out.println(">>> Found " + result.size() + " Locs");
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }
    }

    /**
     * Test moths search
     */
    @Ignore
    @Test
    public void testMothSearch() throws Exception {
        if (skipIntegrationTest()) {
            return;
        }
        MothSearchParams params = new MothSearchParams();
//        params.setVdcCode("27009");
//        params.setMothlujNumber("1%");
//        params.setMothLuj("M");
        //String testSearch="27009"+" "+"M";
        AdministrativeEJBLocal instance = (AdministrativeEJBLocal) getEJBInstance(AdministrativeEJB.class.getSimpleName());
        List<Moth> result = instance.searchMoths(params);
        assertNotNull(result);

        if (result != null && result.size() > 0) {
            System.out.println("Found " + result.size() + " Moths");
        } else {
            System.out.println("Can't find any Moths");
        }
    }
}
