/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.search.repository.entities;

import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

/**
 *
 * @author Kumar
 */
public class ParcelSearchParams extends AbstractReadOnlyEntity {

    private String vdcCode;
    private int parcelNo;
    private String wardNo;
    private String mapSheetCode;

    public ParcelSearchParams() {
    }

    public String getVdcCode() {
        return vdcCode;
    }

    public void setVdcCode(String vdcCode) {
        this.vdcCode = vdcCode;
    }

    public int getParcelNo() {
        return parcelNo;
    }

    public void setParcelNo(int parcelNo) {
        this.parcelNo = parcelNo;
    }

    public String getWardNo() {
        return wardNo;
    }

    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
    }

    public String getMapSheetCode() {
        return mapSheetCode;
    }

    public void setMapSheetCode(String mapSheetCode) {
        this.mapSheetCode = mapSheetCode;
    }
}
