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
package org.sola.services.ejbs.admin.businesslogic;

import java.util.Date;
import java.util.List;
import org.sola.services.common.ejbs.AbstractEJBLocal;
import org.sola.services.ejbs.admin.businesslogic.repository.entities.*;

public interface AdminEJBLocal extends AbstractEJBLocal {

    List<User> getUsers();

    /**
     * Returns user.
     */
    User getUser(String userName);

    /**
     * Returns currently logged user.
     */
    User getCurrentUser();

    /**
     * Saves user.
     */
    User saveUser(User user);

    /**
     * Sets password for user.
     */
    boolean changePassword(String userName, String password);
    
    /**
     * Sets new password for the current user.
     */
    boolean changeCurrentUserPassword(String oldPassword, String newPassword);

    /**
     * Returns all application roles.
     */
    List<Role> getRoles();

    /**
     * Returns user roles, assigned through the group.
     */
    List<Role> getUserRoles(String userName);

    /**
     * Returns current user roles, assigned through the group.
     */
    List<Role> getCurrentUserRoles();

    /**
     * Returns application role.
     */
    Role getRole(String roleCode);

    /**
     * Returns all groups.
     */
    List<Group> getGroups();

    /**
     * Returns all groups with summary information.
     */
    List<GroupSummary> getGroupsSummary();

    /**
     * Returns group by ID.
     */
    Group getGroup(String groupId);

    /**
     * Creates/saves group.
     */
    Group saveGroup(Group group);

    /**
     * Updates role.
     */
    Role saveRole(Role role);

    /**
     * Returns true if current user belongs to any of admin roles.
     */
    boolean isUserAdmin();

    /**
     * Returns list of available languages.
     */
    List<Language> getLanguages(String lang);

    /**
     * Get list of departments by Office ID.
     */
    List<Department> getDepartments(String officeCode, String lang);

    /**
     * Checks if user belongs to the given department.
     */
    boolean checkUserFromDepartment(String userId, String departmentCode);

    /**
     * Checks if user belongs to the given office.
     */
    boolean checkUserFromOffice(String userId, String officeCode);

    /**
     * Get list of Offices by District code.
     */
    List<Office> getOfficesByDistrict(String districtCode, String lang);

    /**
     * Returns office object related to the current user.
     */
    Office getCurrentOffice();

    /**
     * List of nepali year from database with out any parameter given
     */
    List<Integer> getNepaliYear();

    /**
     * Returns List of Nepali date from database with out any parameter given
     */
    List<NepaliMonth> getNepaliMonths();

    /**
     * Returns List of Nepali date from database
     *
     * @param year parameter to return list of month in given year
     */
    List<NepaliMonth> getNepaliMonths(int year);

    /**
     * Returns the entity NepaliMonth
     *
     * @param year nepali year
     * @param month nepali month
     */
    NepaliMonth getNepaliMonth(int year, int month);

    /**
     * Returns List of Nepali date from database
     *
     * @param year parameter to return list of month in given year
     */
    NepaliMonth saveNepaliMonth(NepaliMonth nepaliMonth);

    /**
     * Returns Nepali date
     *
     * @parma date Date to convert
     */
    String getNepaliDate(Date date);

    /**
     * Returns Gregorian date
     *
     * @parma nepaliDate Nepali date
     */
    Date getGregorianDate(String nepaliDate);

    String getCurrentOfficeCode();

    /**
     * Checks provided office code against the office code of the current user.
     * By default exception will be thrown if codes are different.
     *
     * @param officeCode Provided office code to check
     */
    boolean checkOfficeCode(String officeCode);

    /**
     * Checks provided office code against the office code of the current user.
     *
     * @param officeCode Provided office code to check
     * @param throwException Boolean flag indicating whether to throw an
     * exception in case of office code difference with current user office
     * code.
     */
    boolean checkOfficeCode(String officeCode, boolean throwException);

    /**
     * Returns current fiscal year.
     */
    FiscalYear getCurrentFiscalYear();

    /**
     * Returns current fiscal year code.
     */
    String getCurrentFiscalYearCode();

    /**
     * Returns list of fiscal year entity
     */
    List<FiscalYear> getFiscalYears(String languageCode);
    
    /** 
     * Checks current user rights for the given VDC and/or Ward. Returns true if user has access.
     * @param vdcCode Code of VDC to check.
     * @param wardNumber Ward number to check.
     * @param throwException Flag, indicating whether to throw an exception or not in case of the lack of access.
     */
    boolean checkVdcWardAccess(String vdcCode, String wardNumber, boolean throwException);
    
    /** 
     * Checks user rights for the given VDC and/or Ward. Returns true if user has access.
     * @param userName User name to check
     * @param vdcCode Code of VDC to check.
     * @param wardNumber Ward number to check.
     * @param throwException Flag, indicating whether to throw an exception or not in case of the lack of access.
     */
    boolean checkVdcWardAccess(User user, String vdcCode, String wardNumber, boolean throwException);
}
