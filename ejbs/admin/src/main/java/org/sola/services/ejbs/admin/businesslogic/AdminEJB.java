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

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.*;
import java.util.Map.Entry;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.sola.common.RolesConstants;
import org.sola.common.SOLAException;
import org.sola.common.StringUtility;
import org.sola.common.messaging.ServiceMessage;
import org.sola.services.common.LocalInfo;
import org.sola.services.common.ejbs.AbstractEJB;
import org.sola.services.common.repository.CommonSqlProvider;
import org.sola.services.ejbs.admin.businesslogic.repository.entities.*;

/**
 * Contains business logic methods to administer system settings, users and
 * roles.
 */
@Stateless
@EJB(name = "java:global/SOLA/AdminEJBLocal", beanInterface = AdminEJBLocal.class)
public class AdminEJB extends AbstractEJB implements AdminEJBLocal {

    public static final String DATE_PARAM = "date";
    public static final String TO_NEPALI_DATE_FUNCTION = "select english_to_nepalidatestring from public.english_to_nepalidatestring(#{" + DATE_PARAM + "})";
    public static final String TO_GREGORIAN_DATE_FUNCTION = "select nepali_to_englishdate from public.nepali_to_englishdate(#{" + DATE_PARAM + "})";

    @Override
    protected void postConstruct() {
        setEntityPackage(User.class.getPackage().getName());
    }

    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    @Override
    public List<User> getUsers() {
        return getRepository().getEntityList(User.class);
    }

    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    @Override
    public User getUser(String userName) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, User.QUERY_WHERE_USERNAME);
        params.put(User.PARAM_USERNAME, userName);
        return getRepository().getEntity(User.class, params);
    }

    @PermitAll
    @Override
    public User getCurrentUser() {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, User.QUERY_WHERE_USERNAME);
        params.put(User.PARAM_USERNAME, this.getUserName());
        return getRepository().getEntity(User.class, params);
    }

    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    @Override
    public User saveUser(User user) {
        return getRepository().saveEntity(user);
    }

    @PermitAll
    @Override
    public List<Role> getRoles() {
        return getRepository().getEntityList(Role.class);
    }

    @PermitAll
    @Override
    public Role getRole(String roleCode) {
        return getRepository().getEntity(Role.class, roleCode);
    }

    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    @Override
    public List<Group> getGroups() {
        return getRepository().getEntityList(Group.class);
    }

    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    @Override
    public Group saveGroup(Group userGroup) {
        return getRepository().saveEntity(userGroup);
    }

    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    @Override
    public Group getGroup(String groupId) {
        return getRepository().getEntity(Group.class, groupId);
    }

    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    @Override
    public Role saveRole(Role role) {
        return getRepository().saveEntity(role);
    }

    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    @Override
    public List<GroupSummary> getGroupsSummary() {
        return getRepository().getEntityList(GroupSummary.class);
    }

    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    @Override
    public boolean changePassword(String userName, String password) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, User.QUERY_SET_PASSWORD);
        params.put(User.PARAM_PASSWORD, getPasswordHash(password));
        params.put(User.PARAM_USERNAME, userName);

        ArrayList<HashMap> list = getRepository().executeFunction(params);

        if (list.size() > 0 && list.get(0) != null && list.get(0).size() > 0) {
            return ((Integer) ((Entry) list.get(0).entrySet().iterator().next()).getValue()) > 0;
        } else {
            return false;
        }
    }

    @Override
    public boolean changeCurrentUserPassword(String oldPassword, String newPassword) {
        User user = getCurrentUser();
        if (user == null || oldPassword == null || newPassword == null) {
            return false;
        }

        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, User.QUERY_GET_PASSWORD);
        params.put(User.PARAM_USERNAME, getCurrentUser().getUserName());

        ArrayList<HashMap> list = getRepository().executeFunction(params);

        if (list.size() > 0 && list.get(0) != null && list.get(0).size() > 0) {
            String currentPassword = (String) ((Entry) list.get(0).entrySet().iterator().next()).getValue();
            if (currentPassword == null) {
                return false;
            }

            oldPassword = getPasswordHash(oldPassword);
            if (!oldPassword.equals(currentPassword)) {
                return false;
            }
            return changePassword(user.getUserName(), newPassword);
        } else {
            return false;
        }
    }

    /**
     * Returns SHA-256 hash for the password.
     *
     * @param password Password string to hash.
     */
    private String getPasswordHash(String password) {
        String hashString = null;

        if (password != null && password.length() > 0) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(password.getBytes("UTF-8"));
                byte[] hash = md.digest();

                // SOLA Ticket #410 - Fix password encyption. Ensure 0 is prepended
                // if the hex length is == 1 
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < hash.length; i++) {
                    String hex = Integer.toHexString(0xff & hash[i]);
                    if (hex.length() == 1) {
                        sb.append('0');
                    }
                    sb.append(hex);
                }
                
                hashString = sb.toString();

            } catch (Exception e) {
                e.printStackTrace(System.err);
                return null;
            }
        }

        return hashString;
    }

    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    @Override
    public List<Role> getUserRoles(String userName) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, Role.QUERY_GET_ROLES_BY_USER_NAME);
        params.put(User.PARAM_USERNAME, userName);
        isInRole(userName);
        return getRepository().getEntityList(Role.class, params);
    }

    @PermitAll
    @Override
    public List<Role> getCurrentUserRoles() {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, Role.QUERY_GET_ROLES_BY_USER_NAME);
        params.put(User.PARAM_USERNAME, this.getUserName());
        return getRepository().getEntityList(Role.class, params);
    }

    @PermitAll
    @Override
    public boolean isUserAdmin() {
        return isInRole(RolesConstants.ADMIN_MANAGE_SECURITY, RolesConstants.ADMIN_MANAGE_REFDATA,
                RolesConstants.ADMIN_MANAGE_SETTINGS);
    }

    @PermitAll
    @Override
    public List<Language> getLanguages(String lang) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_LANGUAGE_CODE, lang);
        params.put(CommonSqlProvider.PARAM_ORDER_BY_PART, "item_order");
        return getRepository().getEntityList(Language.class, params);
    }

    @Override
    public List<Department> getDepartments(String officeCode, String lang) {
        HashMap params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, Department.WHERE_BY_OFFICE_CODE);
        if (lang != null && !lang.isEmpty()) {
            params.put(CommonSqlProvider.PARAM_LANGUAGE_CODE, lang);
        }
        params.put(Department.PARAM_OFFICE_CODE, officeCode);
        return getRepository().getEntityList(Department.class, params);
    }

    @Override
    public List<Office> getOfficesByDistrict(String districtCode, String lang) {
        HashMap params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, Office.WHERE_BY_DISTRICT_CODE);
        params.put(CommonSqlProvider.PARAM_LANGUAGE_CODE, lang);
        params.put(Office.PARAM_DISTRICT_CODE, districtCode);
        return getRepository().getEntityList(Office.class, params);
    }

    @Override
    public boolean checkUserFromDepartment(String userId, String departmentCode) {
        User user = getRepository().getEntity(User.class, userId);
        if (user != null) {
            return user.getDepartmentCode().equals(departmentCode);
        } else {
            return false;
        }
    }

    @Override
    public boolean checkUserFromOffice(String userId, String officeCode) {
        User user = getRepository().getEntity(User.class, userId);
        if (user != null) {
            return user.getDepartment().getOfficeCode().equals(officeCode);
        } else {
            return false;
        }
    }

    @Override
    public String getNepaliDate(Date date) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, TO_NEPALI_DATE_FUNCTION);
        params.put(DATE_PARAM, date);

        ArrayList<HashMap> list = getRepository().executeFunction(params);

        if (list.size() > 0 && list.get(0) != null && list.get(0).size() > 0) {
            return (String) ((Map.Entry) list.get(0).entrySet().iterator().next()).getValue();
        } else {
            return null;
        }
    }

    @Override
    public Date getGregorianDate(String nepaliDate) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_QUERY, TO_GREGORIAN_DATE_FUNCTION);
        params.put(DATE_PARAM, nepaliDate);

        ArrayList<HashMap> list = getRepository().executeFunction(params);
        Date gDate = null;
        if (list.size() > 0 && list.get(0) != null && list.get(0).size() > 0) {
            //return (Date) ((Map.Entry) list.get(0).entrySet().iterator().next()).getValue();
            gDate = (Date) ((Map.Entry) list.get(0).entrySet().iterator().next()).getValue();
        }// else {
        //return null;
        //}
        if (gDate == null) {
            throw new SOLAException(ServiceMessage.EXCEPTION_INVALID_NEPALI_DATE);
        } else {
            return gDate;
        }
    }

    @Override
    public List<NepaliMonth> getNepaliMonths() {
        return getRepository().getEntityList(NepaliMonth.class);
    }

    @Override
    public List<NepaliMonth> getNepaliMonths(int year) {
        HashMap params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, NepaliMonth.GET_BY_YEAR);
        params.put(NepaliMonth.YEAR_PARAM, year);
        return getRepository().getEntityList(NepaliMonth.class, params);
    }

    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SETTINGS)
    @Override
    public NepaliMonth saveNepaliMonth(NepaliMonth nepaliMonth) {
        return getRepository().saveEntity(nepaliMonth);
    }

    @Override
    public NepaliMonth getNepaliMonth(int year, int month) {
        HashMap params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, NepaliMonth.GET_BY_YEAR_AND_MONTH);
        params.put(NepaliMonth.YEAR_PARAM, year);
        params.put(NepaliMonth.MONTH_PARAM, month);
        return getRepository().getEntity(NepaliMonth.class, params);
    }

    @Override
    public List<Integer> getNepaliYear() {
        List<Integer> yr = new ArrayList<Integer>();
        List<NepaliMonth> list = getRepository().getEntityList(NepaliMonth.class);
        for (NepaliMonth i : list) {
            if (yr.contains(i.getNepYear()) == false) {
                yr.add(i.getNepYear());
            }
        }
        return yr;
    }

    @Override
    public Office getCurrentOffice() {
        Office currentOffice = LocalInfo.get(LocalInfo.CURRENT_OFFICE, Office.class, true);

        if (currentOffice == null) {
            Map params = new HashMap<String, Object>();
            params.put(CommonSqlProvider.PARAM_WHERE_PART, Office.WHERE_BY_USERNAME);
            params.put(User.PARAM_USERNAME, getUserName());
            currentOffice = getRepository().getEntity(Office.class, params);
            if (currentOffice != null) {
                LocalInfo.set(LocalInfo.CURRENT_OFFICE, currentOffice, true, true);
            }
        }
        return currentOffice;
    }

    @Override
    public String getCurrentOfficeCode() {
        Office office = getCurrentOffice();
        if (office != null) {
            return office.getCode();
        } else {
            return null;
        }
    }

    @Override
    public boolean checkOfficeCode(String officeCode) {
        return checkOfficeCode(officeCode, true);
    }

    @Override
    public boolean checkOfficeCode(String officeCode, boolean throwException) {
        return checkOfficeCode(officeCode, getCurrentOfficeCode(), throwException);
    }

    @Override
    public FiscalYear getCurrentFiscalYear() {
        FiscalYear currentFiscalYear = LocalInfo.get(LocalInfo.CURRENT_FISCAL_YEAR, FiscalYear.class, true);

        if (currentFiscalYear == null) {
            Map params = new HashMap<String, Object>();
            params.put(CommonSqlProvider.PARAM_WHERE_PART, FiscalYear.WHERE_GET_BY_CURRENT);
            currentFiscalYear = getRepository().getEntity(FiscalYear.class, params);
            if (currentFiscalYear != null) {
                LocalInfo.set(LocalInfo.CURRENT_FISCAL_YEAR, currentFiscalYear, true, true);
            }
        }
        return currentFiscalYear;
    }

    @Override
    public String getCurrentFiscalYearCode() {
        FiscalYear currentFiscalYear = getCurrentFiscalYear();
        if (currentFiscalYear != null) {
            return currentFiscalYear.getCode();
        } else {
            return null;
        }
    }

    @Override
    public List<FiscalYear> getFiscalYears(String languageCode) {
        return getRepository().getCodeList(FiscalYear.class, languageCode);
    }

    @Override
    public boolean checkVdcWardAccess(String vdcCode, String wardNumber, boolean throwException) {
        User user = LocalInfo.get(LocalInfo.CURRENT_USER, User.class, true);
        if (user == null) {
            user = getCurrentUser();
            if (user != null) {
                LocalInfo.set(LocalInfo.CURRENT_USER, user, true, true);
            }
        }
        return checkVdcWardAccess(user, vdcCode, wardNumber, throwException);
    }

    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SECURITY)
    @Override
    public boolean checkVdcWardAccess(User user, String vdcCode, String wardNumber, boolean throwException) {
        if (vdcCode == null || vdcCode.isEmpty()) {
            return true;
        }

        if (user == null || user.getVdcs() == null || user.getVdcs().size() < 1) {
            if (throwException) {
                throw new SOLAException(ServiceMessage.EXCEPTION_VDC_ACCESS_DENIED);
            }
            return false;
        }

        for (UserVdc uservdc : user.getVdcs()) {
            if (StringUtility.empty(uservdc.getVdcCode()).equals(StringUtility.empty(vdcCode))) {
                if (StringUtility.isEmpty(uservdc.getWardNumber())) {
                    return true;
                }
                if (StringUtility.empty(uservdc.getWardNumber()).equals(StringUtility.empty(wardNumber))) {
                    return true;
                }
            }
        }

        if (throwException) {
            throw new SOLAException(ServiceMessage.EXCEPTION_VDC_ACCESS_DENIED);
        }
        return false;
    }
}
