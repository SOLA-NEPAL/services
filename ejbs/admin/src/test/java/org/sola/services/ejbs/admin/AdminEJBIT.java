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
package org.sola.services.ejbs.admin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.transaction.UserTransaction;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.sola.services.common.EntityAction;
import org.sola.services.common.test.AbstractEJBTest;
import org.sola.services.ejbs.admin.businesslogic.AdminEJB;
import org.sola.services.ejbs.admin.businesslogic.AdminEJBLocal;
import org.sola.services.ejbs.admin.businesslogic.repository.entities.*;

public class AdminEJBIT extends AbstractEJBTest {

    public AdminEJBIT() {
        super();
    }
    private static final String USER_ID = "tester-user-id";
    private static final String USER_NAME = "tester-user-name";
    private static final String GROUP_ID = "Nepal-LMO" ;//" tester-group-id";
    private static final String LANG = "en";
    private static final String LOGIN_USER = "test";
    private static final String LOGIN_PASS = "test";
    private static final String DISTRIC_CODE = "KASKI-TEMP";
    private static final String LMO_CODE = "KASKI-TEMP";

    @Before
    public void setUp() throws Exception {
        login(LOGIN_USER, LOGIN_PASS);
    }

    @After
    public void tearDown() throws Exception {
        logout();
    }

    @Ignore
    @Test
    public void getNepaliDate() throws Exception {
        AdminEJBLocal instance = (AdminEJBLocal) getEJBInstance(AdminEJB.class.getSimpleName());
        String nepaliDate = instance.getNepaliDate(Calendar.getInstance().getTime());
        assertNotNull("Can't convert to Nepli date", nepaliDate);
        System.out.println(">>> Nepali date is " + nepaliDate);
    }

    @Ignore
    @Test
    public void getGregorianDate() throws Exception {
        AdminEJBLocal instance = (AdminEJBLocal) getEJBInstance(AdminEJB.class.getSimpleName());
        Date date = instance.getGregorianDate("2068-12-22");
        assertNotNull("Can't convert to Gregorian date", date);
        System.out.println(">>> Gregorian date is " + date.toString());
    }

    /**
     * Test creating user.
     */
    
    @Ignore
    @Test
    public void testCreateUser() throws Exception {
        System.out.println(">>> Create new user.");
        UserTransaction tx = getUserTransaction();
        try {
            AdminEJBLocal instance = (AdminEJBLocal) getEJBInstance(AdminEJB.class.getSimpleName());
            tx.begin();

            // Get group
            Group group = instance.getGroup(GROUP_ID);
            tx.commit();

            assertNotNull("Failed to get group", group);

            tx.begin();

            UserGroup userGroup = new UserGroup();
            userGroup.setGroupId(GROUP_ID);
            userGroup.setEntityAction(EntityAction.INSERT);

            List<UserGroup> listUserGroups = new ArrayList<UserGroup>();
            listUserGroups.add(userGroup);
            

            User user = new User();
            user.setId(USER_ID);
            user.setActive(true);
            user.setDescription("Test user");
            user.setEntityAction(EntityAction.INSERT);
            user.setFirstName("First name test user");
            user.setLastName("Last name for user");
            user.setLoaded(false);
            user.setUserName(USER_NAME);
            user.setUserGroups(listUserGroups);


            User savedUser = instance.saveUser(user);
            tx.commit();

            assertNotNull("Failed to create user", savedUser);
            System.out.println(">>> User has been created!");

            // Re-read user
            tx.begin();
            savedUser = instance.getUser(USER_NAME);
            savedUser.getUserGroups();
            tx.commit();

            // Check user
            assertEquals("User id is wrong", savedUser.getId(), user.getId());
            assertEquals("User active flag is wrong", savedUser.isActive(), user.isActive());
            assertEquals("User description is wrong", savedUser.getDescription(), user.getDescription());
            assertEquals("User first name is wrong", savedUser.getFirstName(), user.getFirstName());
            assertEquals("User name is wrong", savedUser.getUserName(), user.getUserName());
            assertNotNull("User groups is null", savedUser.getUserGroups());
            assertEquals("User number of groups is wrong", savedUser.getUserGroups().size(), user.getUserGroups().size());

            System.out.println(">>> User was saved with correct values.");
        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }
    }
    
    
    /**
     * Test loading languages
     */
    
    @Ignore
    @Test
    public void testLoadLanguages() throws Exception {
        System.out.println(">>> Loading all languages.");
        UserTransaction tx = getUserTransaction();
        try {
            AdminEJBLocal instance = (AdminEJBLocal) getEJBInstance(AdminEJB.class.getSimpleName());
            tx.begin();
            List<Language> result = instance.getLanguages(LANG);
            tx.commit();

            assertNotNull("List of languages is null.", result);
            System.out.println(">>> Found " + result.size() + " languages.");

        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }
    }

    /**
     * Test roles loading
     */
    @Ignore
    @Test
    public void testLoadAllRoles() throws Exception {
        System.out.println(">>> Loading all roles");
        UserTransaction tx = getUserTransaction();
        try {
            AdminEJBLocal instance = (AdminEJBLocal) getEJBInstance(AdminEJB.class.getSimpleName());
            tx.begin();
            List<Role> result = instance.getRoles();
            tx.commit();

            assertNotNull("List of roles is null.", result);
            System.out.println(">>> Found " + result.size() + " roles.");

        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }
    }

    /**
     * Test getting current user roles
     */
    @Ignore
    @Test
    public void testGetCurrentUserRoles() throws Exception {
        System.out.println(">>> Loading roles for current user.");
        UserTransaction tx = getUserTransaction();
        try {
            AdminEJBLocal instance = (AdminEJBLocal) getEJBInstance(AdminEJB.class.getSimpleName());
            tx.begin();
            List<Role> result = instance.getCurrentUserRoles();
            tx.commit();

            assertNotNull("List of roles for current user is null.", result);
            System.out.println(">>> Found " + result.size() + " roles for current user.");

        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }
    }

    /**
     * Test if user has admin rights
     */
    @Ignore
    @Test
    public void testIsUserAdmin() throws Exception {
        System.out.println(">>> Checking if user has admin rights.");
        UserTransaction tx = getUserTransaction();
        try {
            AdminEJBLocal instance = (AdminEJBLocal) getEJBInstance(AdminEJB.class.getSimpleName());
            tx.begin();
            boolean result = instance.isUserAdmin();
            tx.commit();

            if (result) {
                System.out.println(">>> Current user has admin rights.");
            } else {
                System.out.println(">>> Current user doesn't have admin rights.");
            }
        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }
    }

    /**
     * Test password change
     */
    @Ignore
    @Test
    public void testPasswordChange() throws Exception {
        System.out.println(">>> Changing password");
        UserTransaction tx = getUserTransaction();
        try {
            AdminEJBLocal instance = (AdminEJBLocal) getEJBInstance(AdminEJB.class.getSimpleName());
            tx.begin();
            instance.changePassword("usr", "test");
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }
    }

    /**
     * Test creating group
     */
    @Ignore
    @Test
    public void testCraeteGroup() throws Exception {
        System.out.println(">>> Create group");
        UserTransaction tx = getUserTransaction();
        try {
            AdminEJBLocal instance = (AdminEJBLocal) getEJBInstance(AdminEJB.class.getSimpleName());
            tx.begin();
            // Get roles
            List<Role> roles = instance.getRoles();
            tx.commit();

            assertNotNull("List of roles is null.", roles);

            tx.begin();

            List<GroupRole> groupRoles = new ArrayList<GroupRole>();
            if (roles != null) {
                for (Role role : roles) {
                    GroupRole groupRole = new GroupRole();
                    groupRole.setRoleCode(role.getCode());
                    groupRole.setEntityAction(EntityAction.INSERT);
                    groupRoles.add(groupRole);
                }
            }

            Group userGroup = new Group();
            userGroup.setId(GROUP_ID);
            userGroup.setName("Test group");
            userGroup.setDescription("This is test group");
            userGroup.setLoaded(false);
            userGroup.setEntityAction(EntityAction.INSERT);
            userGroup.setGroupRoles(groupRoles);

            Group result = instance.saveGroup(userGroup);

            tx.commit();

            assertNotNull("Failed to create group", result);

            System.out.println(">>> Group has been saved!");
        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }
    }

    /**
     * Test groups loading
     */
    @Ignore
    @Test
    public void testLoadAllGroups() throws Exception {
        System.out.println(">>> Loading all groups");
        UserTransaction tx = getUserTransaction();
        try {
            AdminEJBLocal instance = (AdminEJBLocal) getEJBInstance(AdminEJB.class.getSimpleName());
            tx.begin();
            List<Group> result = instance.getGroups();
            tx.commit();

            assertNotNull("List of groups is null.", result);
            assertFalse("List of groups is empty.", result.size() <= 0);

            System.out.println(">>> Found " + result.size() + " groups.");
        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }
    }

    /**
     * Test groups summary loading
     */
    @Ignore
    @Test
    public void testLoadAllGroupsSummary() throws Exception {
        System.out.println(">>> Loading all groups summary");
        UserTransaction tx = getUserTransaction();
        try {
            AdminEJBLocal instance = (AdminEJBLocal) getEJBInstance(AdminEJB.class.getSimpleName());
            tx.begin();
            List<GroupSummary> result = instance.getGroupsSummary();
            tx.commit();

            assertNotNull("List of groups summary is null.", result);
            assertFalse("List of groups summary is empty.", result.size() <= 0);

            System.out.println(">>> Found " + result.size() + " groups summaries.");
        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }
    }

    
   

    /**
     * Test get all users.
     */
    @Ignore
    @Test
    public void testGetUsers() throws Exception {
        System.out.println(">>> Getting all users.");
        UserTransaction tx = getUserTransaction();
        try {
            AdminEJBLocal instance = (AdminEJBLocal) getEJBInstance(AdminEJB.class.getSimpleName());
            tx.begin();
            List<User> users = instance.getUsers();
            tx.commit();
            assertNotNull("Failed to get users", users);

            System.out.println(">>> Found " + users.size() + " users!");

        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }
    }

    /**
     * Test changing group.
     */
    @Ignore
    @Test
    public void testModifyGroup() throws Exception {
        System.out.println(">>> Modifying group");
        UserTransaction tx = getUserTransaction();
        try {
            AdminEJBLocal instance = (AdminEJBLocal) getEJBInstance(AdminEJB.class.getSimpleName());
            tx.begin();
            // Get group
            Group group = instance.getGroup(GROUP_ID);
            if (group != null) {
                group.getGroupRoles();
            }
            tx.commit();

            assertNotNull("Failed to get group", group);
            assertNotNull("Group name is null", group.getName());
            assertNotNull("Group description is null", group.getDescription());
            assertNotNull("Group roles is null", group.getGroupRoles());
            int rolesSize = group.getGroupRoles().size();

            group.setEntityAction(EntityAction.UPDATE);

            if (rolesSize > 1) {
                group.getGroupRoles().get(0).setEntityAction(EntityAction.DELETE);
                rolesSize -= 1;
            }

            group.setName("Updated Test group");
            group.setDescription("Updated description for group");

            tx.begin();
            Group updatedGroup = instance.saveGroup(group);
            tx.commit();

            // Re-read group
            tx.begin();
            updatedGroup = instance.getGroup(GROUP_ID);
            if (updatedGroup != null) {
                updatedGroup.getGroupRoles();
            }
            tx.commit();

            assertNotNull("Failed to get modified group", updatedGroup);
            assertEquals("Group name wasn't modified", updatedGroup.getName(), group.getName());
            assertEquals("Group description wasn't modified", updatedGroup.getDescription(), group.getDescription());
            assertNotNull("Modified group roles is null", group.getGroupRoles());
            assertEquals("Group roles wasn't modified", updatedGroup.getGroupRoles().size(), rolesSize);

            System.out.println(">>> Group has been modified!");

        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }
    }

    /**
     * Test deleting group.
     */
    @Ignore
    @Test
    public void testDeleteGroup() throws Exception {
        System.out.println(">>> Deleting group");
        UserTransaction tx = getUserTransaction();
        try {
            AdminEJBLocal instance = (AdminEJBLocal) getEJBInstance(AdminEJB.class.getSimpleName());
            tx.begin();
            // Get group
            Group group = instance.getGroup(GROUP_ID);
            tx.commit();

            assertNotNull("Failed to get group", group);

            group.setEntityAction(EntityAction.DELETE);

            tx.begin();
            Group deltedGroup = instance.saveGroup(group);
            tx.commit();

            assertNull("Failed to delete group", deltedGroup);

            System.out.println(">>> Group has been deleted!");

        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }
    }

    /**
     * Test deleting user.
     */
    @Ignore
    @Test
    public void testDeleteUser() throws Exception {
        System.out.println(">>> Deleting user");
        UserTransaction tx = getUserTransaction();
        try {
            AdminEJBLocal instance = (AdminEJBLocal) getEJBInstance(AdminEJB.class.getSimpleName());
            tx.begin();
            // Get user
            User user = instance.getUser(USER_NAME);
            tx.commit();

            assertNotNull("Failed to get user", user);

            user.setEntityAction(EntityAction.DELETE);

            tx.begin();
            User deltedUser = instance.saveUser(user);
            tx.commit();

            assertNull("Failed to delete user", deltedUser);

            System.out.println(">>> User has been deleted!");

        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }
    }

   @Ignore
    @Test
    public void testGetNepaliMonth() throws Exception {
        System.out.println(">>> Loading all Months.");
        try {
            AdminEJBLocal instance = (AdminEJBLocal) getEJBInstance(AdminEJB.class.getSimpleName());
            List<NepaliMonth> result = instance.getNepaliMonths();

            assertNotNull("List of Months is null.", result);
            System.out.println(">>> Found " + result.size() + " months.");

            result = instance.getNepaliMonths(2068);

            assertNotNull("List of Months is null.", result);
            System.out.println(">>> Found " + result.size() + " months in 2068.");

            NepaliMonth nepMonth = instance.getNepaliMonth(2068, 1);

            assertNotNull("Can't find Nepali month for 2068, 1.", nepMonth);
            System.out.println(">>> Number of days in Nepali month 1 of 2068 = " + nepMonth.getDayss());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Ignore
    @Test
    public void testSaveNepaliMonth() throws Exception {
        System.out.println(">>> Testing saving nepali month");
        UserTransaction tx = getUserTransaction();
        try {
            AdminEJBLocal instance = (AdminEJBLocal) getEJBInstance(AdminEJB.class.getSimpleName());
            List<NepaliMonth> list = new ArrayList<NepaliMonth>();
            NepaliMonth month = new NepaliMonth();
            month.setNepYear(2076);
            month.setNepMonth(1);
            month.setDayss(31);
            list.add(month);

            month = new NepaliMonth();
            month.setNepYear(2076);
            month.setNepMonth(2);
            month.setDayss(30);
            list.add(month);

            month = new NepaliMonth();
            month.setNepYear(2076);
            month.setNepMonth(3);
            month.setDayss(29);
            list.add(month);

            tx.begin();
            for (NepaliMonth nepMonth : list) {
                instance.saveNepaliMonth(nepMonth);
            }
            tx.commit();

            System.out.println(">>> Nepali month list saved");

            list.get(0).setDayss(28);

            tx.begin();
            instance.saveNepaliMonth(list.get(0));
            tx.commit();

            System.out.println(">>> Nepali month is updated ");

            for (NepaliMonth nepaliMonth : list) {
                nepaliMonth.setEntityAction(EntityAction.DELETE);
            }

            tx.begin();
            for (NepaliMonth nepMonth : list) {
                instance.saveNepaliMonth(nepMonth);
            }
            tx.commit();

            //assertNull("Failed to delete user", deltedUser);

            System.out.println(">>> Nepali month was deleted!");

        } catch (Exception e) {
            tx.rollback();
            fail(e.getMessage());
        }
    }

    @Ignore
    @Test
    public void testGetNepaliYear() throws Exception {
        System.out.println(">>> Loading all Year.");
        try {
            AdminEJBLocal instance = (AdminEJBLocal) getEJBInstance(AdminEJB.class.getSimpleName());
            List<Integer> result = instance.getNepaliYear();


            assertNotNull("List of Year is null.", result);
            System.out.println(">>> Found " + result);



        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    
    @Ignore
    @Test
    public void testGetOffices() throws Exception {
        System.out.println(">>> Loading offices.");
        try {
            AdminEJBLocal instance = (AdminEJBLocal) getEJBInstance(AdminEJB.class.getSimpleName());
            List<Office> result = instance.getCodeEntityList(Office.class);

            assertNotNull("Can't find offices.", result);
            System.out.println(">>> Found " + result.size() + " offices");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
