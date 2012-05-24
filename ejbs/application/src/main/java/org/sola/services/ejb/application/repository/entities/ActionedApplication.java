/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.application.repository.entities;

import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

public class ActionedApplication extends AbstractReadOnlyEntity {

    private String applicationId;
    private int rowVerion;

    public ActionedApplication() {
        super();
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public int getRowVerion() {
        return rowVerion;
    }

    public void setRowVerion(int rowVerion) {
        this.rowVerion = rowVerion;
    }
}
