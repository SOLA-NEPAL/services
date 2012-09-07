package org.sola.services.ejb.search.repository.entities;

import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

public class CadastreObjectSearchParams extends AbstractReadOnlyEntity {

    private String vdcCode;
    private String parcelNo;
    private String wardNo;
    private String mapSheetCode;
    
    public CadastreObjectSearchParams() {
    }

    public String getVdcCode() {
        return vdcCode;
    }

    public void setVdcCode(String vdcCode) {
        this.vdcCode = vdcCode;
    }

    public String getParcelNo() {
        return parcelNo;
    }

    public void setParcelNo(String parcelNo) {
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
