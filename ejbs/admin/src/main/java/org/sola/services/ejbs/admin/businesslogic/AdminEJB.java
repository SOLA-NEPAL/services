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

                BigInteger bigInt = new BigInteger(1, hash);
                hashString = bigInt.toString(16);

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

        if (list.size() > 0 && list.get(0) != null && list.get(0).size() > 0) {
            return (Date) ((Map.Entry) list.get(0).entrySet().iterator().next()).getValue();
        } else {
            return null;
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
       List<Integer> yr=new ArrayList<Integer>();
       List<NepaliMonth> list= getRepository().getEntityList(NepaliMonth.class);
       for(NepaliMonth i : list){
           if(yr.contains(i.getNepYear())==false)
           yr.add(i.getNepYear());
       } 
       return yr;       
    }
    
   @Override    
    @RolesAllowed(RolesConstants.ADMIN_MANAGE_SETTINGS)
    public LandOwner saveLandOwner(LandOwner owner) {
        return getRepository().saveEntity(owner);
    }   
   
   
    @Override
    public List<String> getOfficeCode() {       
       List<String> officeCode=new ArrayList<String>();
       List<Office> list= getRepository().getEntityList(Office.class);
       for(Office i : list){
           //if(yr.contains(i.getLmoCode())==false)
           officeCode.add(i.getCode());
       } 
       return officeCode;       
    }

    @Override
    public List<String> getOfficeNames() {
       List<String> officeName=new ArrayList<String>();
       List<Office> list= getRepository().getEntityList(Office.class);
       for(Office i : list){
           //if(yr.contains(i.getOfficeName())==false)
           officeName.add(i.getDisplayValue());
       } 
       return officeName; 
    }

    @Override
    public List<String> getDistrictNames() {
       List<String> districtName=new ArrayList<String>();
       List<District> list= getRepository().getEntityList(District.class);
       for(District i : list){
           //if(yr.contains(i.getOfficeName())==false)
           districtName.add(i.getDistrictName());
       } 
       return districtName; 
    }

    @Override
    public List<Integer> getDistrictCodes() {
       List<Integer> districtCode=new ArrayList<Integer>();
       List<District> list= getRepository().getEntityList(District.class);
       for(District i : list){
           //if(yr.contains(i.getOfficeName())==false)
           districtCode.add(i.getDistrictCode());
       } 
       return districtCode; 
    }
    
    
    
    
//    @Override
//    public List<String> getVdc() {     
//       List<String> vdc=new ArrayList<String>();
//       List<Vdc> list= getRepository().getEntityList(Vdc.class);   
//       for(Vdc i:list){
//           vdc.add(i.getVdcName());
//       }
//       return vdc; 
//    }  
    
     
    
    @Override
    public List<Vdc> getVdcList() {
        return getRepository().getEntityList(Vdc.class);
    }

    @Override
    public Vdc getVdcByCode(String vdcCode) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, Vdc.GET_BY_VDC_CODE);
        params.put(Vdc.VDC_CODE_PARAM,vdcCode);        
        return getRepository().getEntity(Vdc.class, params);
    }

    @Override
    public Vdc getVdcByName(String vdcName) {
        Map params = new HashMap<String, Object>();
        params.put(CommonSqlProvider.PARAM_WHERE_PART, Vdc.GET_BY_VDC_NAME);
        params.put(Vdc.VDC_NAME_PARAM,vdcName);        
        return getRepository().getEntity(Vdc.class, params);
    }
   
}
