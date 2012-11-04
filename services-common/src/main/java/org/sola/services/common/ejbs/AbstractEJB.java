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
package org.sola.services.common.ejbs;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import org.sola.common.RolesConstants;
import org.sola.common.SOLAException;
import org.sola.common.messaging.ServiceMessage;
import org.sola.services.common.LocalInfo;
import org.sola.services.common.repository.entities.AbstractCodeEntity;
import org.sola.services.common.repository.CommonRepository;
import org.sola.services.common.repository.CommonRepositoryImpl;
import org.sola.services.common.repository.DatabaseConnectionManager;
import org.sola.services.common.repository.entities.AbstractEntity;

@DeclareRoles({
    RolesConstants.APPLICATION_VIEW_APPS,
    RolesConstants.APPLICATION_CREATE_APPS,
    RolesConstants.APPLICATION_EDIT_APPS,
    RolesConstants.APPLICATION_PRINT_STATUS_REPORT,
    RolesConstants.APPLICATION_ASSIGN_TO_DEPARTMENT,
    RolesConstants.APPLICATION_ASSIGN_TO_ALL,
    RolesConstants.APPLICATION_SERVICE_START,
    RolesConstants.APPLICATION_SERVICE_COMPLETE,
    RolesConstants.APPLICATION_SERVICE_CANCEL,
    RolesConstants.APPLICATION_SERVICE_REVERT,
    RolesConstants.APPLICATION_APPROVE,
    RolesConstants.APPLICATION_REJECT,
    RolesConstants.APPLICATION_VALIDATE,
    RolesConstants.APPLICATION_ARCHIVE,
    RolesConstants.ADMINISTRATIVE_BA_UNIT_SAVE,
    RolesConstants.ADMINISTRATIVE_BA_UNIT_PRINT_CERT,
    RolesConstants.ADMINISTRATIVE_BA_UNIT_SEARCH,
    RolesConstants.ADMINISTRATIVE_MOTH_MANAGEMENT,
    RolesConstants.SOURCE_TRANSACTIONAL,
    RolesConstants.SOURCE_SAVE,
    RolesConstants.SOURCE_SEARCH,
    RolesConstants.SOURCE_PRINT,
    RolesConstants.GIS_VIEW_MAP,
    RolesConstants.GIS_PRINT,
    RolesConstants.CADASTRE_PARCEL_SAVE,
    RolesConstants.CADASTRE_MAP_SHEET_SAVE,
    RolesConstants.CADASTRE_PARCEL_DETAILS_SAVE,
    RolesConstants.PARTY_SAVE,
    RolesConstants.PARTY_RIGHTHOLDERS_SAVE,
    RolesConstants.REPORTS_VIEW,
    RolesConstants.ARCHIVE_ARCHIVE_APPS,
    RolesConstants.ADMIN_MANAGE_SECURITY,
    RolesConstants.ADMIN_MANAGE_REFDATA,
    RolesConstants.ADMIN_MANAGE_SETTINGS,
    RolesConstants.ADMIN_MANAGE_BR,
})
public abstract class AbstractEJB implements AbstractEJBLocal {

    @Resource
    private SessionContext sessionContext;
    private CommonRepository repository;
    private String entityPackage;

    /**
     * Returns name of entities package. Should be set explicitely.
     */
    public String getEntityPackage() {
        return entityPackage;
    }

    /**
     * Sets name of the entities package.
     */
    public void setEntityPackage(String entityPackage) {
        this.entityPackage = entityPackage;
    }

    public CommonRepository getRepository() {
        return repository;
    }

    public void setRepository(CommonRepository repository) {
        this.repository = repository;
    }

    @Override
    public void setDbConnectionManager(DatabaseConnectionManager dbConnectionManager) {
        if (this.repository != null) {
            this.repository.setDbConnectionManager(dbConnectionManager);
        }
    }

    /**
     * Checks if current user belongs to any of provided roles.
     *
     * @param roles List of roles to check.
     */
    public boolean isInRole(String... roles) {
        boolean result = false;
        if (roles != null) {
            for (String role : roles) {
                if (sessionContext.isCallerInRole(role)) {
                    result = true;
                    break;
                }
            }
        } else {
            result = true;
        }
        return result;
    }

    /**
     * Obtains the user name of the currently logged in user.
     *
     * @return The user name.
     */
    protected String getUserName() {
        return sessionContext.getCallerPrincipal().getName();
    }

    /**
     * This method is invoked after the container has completed resource
     * injection for the EJB. To perform additional tasks as part of the
     * postConstruct process in descendent EJB's, override the postConstruct
     * method.
     */
    @PostConstruct
    private void onPostConstruct() {
        repository = new CommonRepositoryImpl();
        postConstruct();
    }

    /**
     * This method has no implementation and can be overridden in descendent EJB
     * classes to perform setup actions following the injection of resources
     * into the EJB. Note that in accordance with EJB postConstruct rules, this
     * methods cannot throw a checked exception. Refer to
     * download.oracle.com/javaee/6/api/javax/annotation/PostConstruct.html
     */
    protected void postConstruct() {
    }

    /*
     * This method is triggered by every method invocation on the EJB. Sets the
     * username for the current ejb context. Also triggers the beforeInvoke and
     * afterInvoke methods. It is necessary to set the user name as part of the
     * invoke rather than during postConstruct as the security context is not
     * initialized until after postConstruct.
     */
    @AroundInvoke
    private Object onInvoke(InvocationContext ctx) throws Exception {

        String userName = sessionContext.getCallerPrincipal().getName();
        if (userName == null || userName.isEmpty()) {
            userName = "SOLA_ANONYMOUS";
        }
        LocalInfo.setUserName(userName);

        beforeInvoke(ctx);
        Object result = ctx.proceed();
        afterInvoke(ctx);
        return result;
    }

    /**
     * This method has no implementation and can be overridden in descendent EJB
     * classes to perform actions just prior to invoking the EJB method.
     */
    protected void beforeInvoke(InvocationContext ctx) throws Exception {
    }

    /**
     * This method has no implementation and can be overridden in descendent EJB
     * classes to perform actions just after invoking the EJB method.
     */
    protected void afterInvoke(InvocationContext ctx) throws Exception {
    }

    /**
     * Performs clean up tasks such as removing all LocalInfo references before
     * destroying the EJB.
     */
    @PreDestroy
    private void onPreDestroy() {
        preDestroy();
        LocalInfo.remove();
    }

    /**
     * This method has no implementation and can be overridden in descendent EJB
     * classes to perform cleanup actions just prior to destroying the EJB
     * object.
     */
    protected void preDestroy() {
    }

    /**
     * Saves {@link AbstractCodeEntity} object in a generic way.
     *
     * @param codeEntity Entity code instance to save.
     */
    @RolesAllowed(RolesConstants.ADMIN_MANAGE_REFDATA)
    @Override
    public <T extends AbstractCodeEntity> T saveCodeEntity(T codeEntity) {
        if (!codeEntity.getClass().getPackage().getName().equals(getEntityPackage())) {
            throw new SOLAException(ServiceMessage.EXCEPTION_ENTITY_PACKAGE_VIOLATION);
        }
        return getRepository().saveEntity(codeEntity);
    }

    /**
     * Returns {@link AbstractCodeEntity} object in a generic way.
     *
     * @param codeEntityClass Entity class.
     * @param code Code value to use for retrieving entity.
     */
    @Override
    public <T extends AbstractCodeEntity> T getCodeEntity(Class<T> codeEntityClass, String code) {
        return getCodeEntity(codeEntityClass, code, null);
    }

    /**
     * Returns {@link AbstractCodeEntity} object in a generic way.
     *
     * @param codeEntityClass Entity class.
     * @param code Code value to use for retrieving entity.
     * @param lang Language code
     */
    @Override
    public <T extends AbstractCodeEntity> T getCodeEntity(Class<T> codeEntityClass, String code, String lang) {
        if (!codeEntityClass.getPackage().getName().equals(getEntityPackage())) {
            throw new SOLAException(ServiceMessage.EXCEPTION_ENTITY_PACKAGE_VIOLATION);
        }
        return getRepository().getCode(codeEntityClass, code, lang);
    }

    /**
     * Returns list of {@link AbstractCodeEntity} object in a generic way.
     *
     * @param codeEntityClass Entity class.
     * @param lang Language code.
     */
    @Override
    public <T extends AbstractCodeEntity> List<T> getCodeEntityList(Class<T> codeEntityClass, String lang) {
        if (!codeEntityClass.getPackage().getName().equals(getEntityPackage())) {
            throw new SOLAException(ServiceMessage.EXCEPTION_ENTITY_PACKAGE_VIOLATION);
        }
        return getRepository().getCodeList(codeEntityClass, lang);
    }

    /**
     * Returns list of {@link AbstractCodeEntity} object in a generic way.
     *
     * @param codeEntityClass Entity class.
     */
    @Override
    public <T extends AbstractCodeEntity> List<T> getCodeEntityList(Class<T> codeEntityClass) {
        return getCodeEntityList(codeEntityClass, null);
    }

    @Override
    public <T extends AbstractEntity> T saveEntity(T entityObject) {
        return this.getRepository().saveEntity(entityObject);
    }

    /**
     * Checks provided office codes to be equal, returns false if they are not.
     *
     * @param officeCode Office code to check
     * @param userOfficeCode The office code of user
     * @param throwException Boolean flag indicating whether to throw an
     * exception in case of office codes difference.
     */
    public boolean checkOfficeCode(String officeCode, String userOfficeCode, boolean throwException) {
        boolean result;

        if (officeCode == null && userOfficeCode == null) {
            result = true;
        } else {
            if ((officeCode == null && userOfficeCode != null) || (officeCode != null && userOfficeCode == null)) {
                result = true;
            } else if (officeCode != null && userOfficeCode == null) {
                result = false;
            } else {
                result = officeCode.equalsIgnoreCase(userOfficeCode);
            }
        }
        
        if(!result && throwException){
            throw new SOLAException(ServiceMessage.EXCEPTION_OBJECT_OUT_OF_OFFICE);
        }
        return result;
    }
}
