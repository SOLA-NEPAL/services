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
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.sola.services.ejb.cadastre.businesslogic;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.UserTransaction;
import org.junit.After;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.sola.services.common.test.AbstractEJBTest;
import org.sola.services.ejb.cadastre.repository.entities.CadastreObject;
import org.sola.services.ejb.cadastre.repository.entities.CadastreObjectNode;
import org.sola.services.ejb.cadastre.repository.entities.CadastreObjectTarget;
import org.sola.services.ejb.cadastre.repository.entities.MapSheet;

/**
 *
 * @author Manoku
 */
public class CadastreEJBIT extends AbstractEJBTest {

    private static final String LOGIN_USER = "test";
    private static final String LOGIN_PASS = "test";

    public CadastreEJBIT() {
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
     * Test of createApplication method, of class ApplicationEJB.
     */
    @Test
    @Ignore
    public void testDev() throws Exception {
        if (skipIntegrationTest()) {
            return;
        }
        System.out.println("testDev");
        CadastreEJBLocal instance = (CadastreEJBLocal) getEJBInstance(CadastreEJB.class.getSimpleName());

        List resultList = instance.getCadastreObjectByParts("Lot");
        System.out.println("Number of cadastre objects found:" + resultList.size());
        String id = "test";
        if (resultList.size() > 0) {
            CadastreObject co = (CadastreObject) resultList.get(0);
            id = co.getId();
           // System.out.println("Result of Areas (total):" + co.getSpatialValueAreaList().size());
        }

        double x = 1778224, y = 5928786;
        int srid = 2193;
        System.out.println("getCadastreObjectByPoint");
        CadastreObject resultObject = instance.getCadastreObjectByPoint(x, y, srid);
        if (resultObject != null) {
            System.out.println("Result :" + resultObject);
        } else {
            System.out.println("Result : NOT FOUND");
        }

        System.out.println("getCadastreObjectTypes");
        resultList = instance.getCadastreObjectTypes("en");
        System.out.println("Result :" + resultList.size());

        System.out.println("getCadastreObject");
        resultObject = instance.getCadastreObject(id);
        if (resultObject != null) {
            System.out.println("Result :" + resultObject);
        } else {
            System.out.println("Result : NOT FOUND");
        }

        System.out.println("getCadastreObjects");
        List<String> listIds = new ArrayList<String>();
        listIds.add(id);
        listIds.add("id2");
        resultList = instance.getCadastreObjects(listIds);
        System.out.println("Result :" + resultList);

        System.out.println("getCadastreObjectNode");
        CadastreObjectNode nodeObj = instance.getCadastreObjectNode(1782700, 5926205, 1782726, 5926209, 2193);
        System.out.println("Result :" + nodeObj.toString());

        System.out.println("getCadastreObjectNodePotential");
        nodeObj = instance.getCadastreObjectNodePotential(1784900, 5925445, 1784950, 5925512, 2193);
        System.out.println("Result :" + nodeObj.toString());
    }

    //<editor-fold defaultstate="collapsed" desc="By Kumar">
    //***********************************************************************************************************
    @Test
    @Ignore
    public void testSaveCadestreObject() throws Exception {
        System.out.println(">>> Testing saving CadastreObject");
        UserTransaction tx = getUserTransaction();
        try {
            tx.begin();
            CadastreEJBLocal instance = (CadastreEJBLocal) getEJBInstance(CadastreEJB.class.getSimpleName());
            CadastreObject cobj = new CadastreObject();
            cobj.setTransactionId("cadastre-transaction");
            cobj.setMapSheetId("0");
            cobj.setParcelno("12012");
            cobj.setLandTypeCode("0");
            //Address add=new Address();
            cobj.setAddressId("e176651e-8bb3-4f77-8218-1d6b20cbe942");
            //cobj.setParcelType(0);
            instance.saveCadastreObject(cobj);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }

    }

    @Test
    @Ignore
    public void testSaveMapSheet() throws Exception {
        System.out.println(">>> Testing saving Map Sheet");
        UserTransaction tx = getUserTransaction();
        try {
            tx.begin();
            CadastreEJBLocal instance = (CadastreEJBLocal) getEJBInstance(CadastreEJB.class.getSimpleName());
            MapSheet mapSheet = new MapSheet();
            //mapSheet.setAlpha_code("1");
            mapSheet.setMapNumber("0000006");
            mapSheet.setSheetType(0);
            //             cobj.setTypeCode("parcel");
            //             cobj.setStatusCode("pending");
            //             //Transaction bx=new Transaction();
            //             cobj.setTransactionId("first");
            //             cobj.setParcelno(15258);
            //             cobj.setParcelType(0);
            instance.saveMapSheet(mapSheet);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }

    }

    @Test
    @Ignore
    public void testGetMapSheetList() throws Exception {
        System.out.println(">>> Testing getting Map Sheet list");
        UserTransaction tx = getUserTransaction();
        try {
            tx.begin();
            CadastreEJBLocal instance = (CadastreEJBLocal) getEJBInstance(CadastreEJB.class.getSimpleName());
            List<MapSheet> mapSheetList = instance.getMapSheetList();

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }

    }

    @Test
    @Ignore
    public void testGetParcel() throws Exception {
        System.out.println(">>> Testing getting Map Sheet list");
        UserTransaction tx = getUserTransaction();
        try {
            tx.begin();
            CadastreEJBLocal instance = (CadastreEJBLocal) getEJBInstance(CadastreEJB.class.getSimpleName());
            CadastreObject Parcel = instance.getCadastreObject("27009", "5", 5);
            //CadastreObject Parcel = instance.getCadastreObject("41");

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }

    }
    
    @Test
    @Ignore
    public void testgetcadastreobjectTargetbyCadastreobjectid() throws Exception {
        System.out.println(">>> Testing getting Map Sheet list");
        UserTransaction tx = getUserTransaction();
        try {
            tx.begin();
            CadastreEJBLocal instance = (CadastreEJBLocal) getEJBInstance(CadastreEJB.class.getSimpleName());
            List<CadastreObjectTarget> Parcel = instance.getCadastreObjectTargetsByCadastreObject("799082db-ef48-4fc0-b3e3-42c03fe2cd27");
            //CadastreObject Parcel = instance.getCadastreObject("41");

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }

    }
    
    
    
    //***********************************************************************************************************
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="By Kabindra">
    @Test
    @Ignore
    public void testForLineIntersection() throws Exception {
        CadastreEJBLocal instance = (CadastreEJBLocal) getEJBInstance(CadastreEJB.class.getSimpleName());
        String geom = "POLYGON((633311.7061890641 3066689.178425605,633311.714179482 3066689.171859174,633311.7206479947 3066689.1637892835,633311.725317841 3066689.1545612104,633311.7279892174 3066689.144569787,633311.7285478263 3066689.1342425053,633311.7269697671 3066689.1240212275,633311.7233225588 3066689.11434328,633309.4987133129 3066684.669123234,633309.4935892622 3066684.660960021,633309.4869941704 3066684.6539315996,633309.479173095 3066684.6482991287,633309.4704166484 3066684.6442718976,633309.461050199 3066684.6419995488,633309.4514217812 3066684.6415665164,633309.4418891638 3066684.642988892,633309.4328065556 3066684.646213822,633299.9438065556 3066689.0869638217,633299.9351647431 3066689.092126971,633299.927732321 3066689.0989165315,633299.921810567 3066689.107057285,633299.9176395233 3066689.1162192407,633299.9153882656 3066689.1260310127,633299.91514805 3066689.136094875,633299.9169286137 3066689.1460028836,633299.9206577806 3066689.15535341,633302.9241011379 3066694.919841939,633302.9291410265 3066694.92764689,633302.9355379102 3066694.9343846645,633302.9430709234 3066694.9398226277,633302.951479973 3066694.9437730224,633302.960474719 3066694.9460994527,633302.9697445988 3066694.9467215934,633302.9789695506 3066694.9456179645,633302.9878310636 3066694.942826671,633302.9960231754 3066694.9384440877,633311.7061890641 3066689.178425605))";
        int srid = 97261;
        List<CadastreObject> the_parcels = instance.getCadastreObjectBy_Intersection(geom, srid);
    }

    @Test
    @Ignore
    public void testForLineByByteIntersection() throws Exception {
        CadastreEJBLocal instance = (CadastreEJBLocal) getEJBInstance(CadastreEJB.class.getSimpleName());
        String geom = "0103000020ED7B01000100000006000000F0A7C64BDB532341D122DBA59D654741FA7E6A5CD6532341FED478C19A65474122B072E8BA5323416F1283589E654741CDAB725BBF532341DF0A8091A06547414C378931C053234121B072FCA0654741F0A7C64BDB532341D122DBA59D654741";
        int srid = 97261;
        List<CadastreObject> the_parcels = instance.getCadastreObjectBy_ByteIntersection(geom, srid);

    }
    //</editor-fold>
}
