package org.sola.services.ejb.search.repository.entities;

import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

public class RestrictionInfoParams extends AbstractReadOnlyEntity {
    private String langCode;
    private String refNumber;
    private Integer refDate;
    
    public RestrictionInfoParams(){
        super();
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public Integer getRefDate() {
        return refDate;
    }

    public void setRefDate(Integer refDate) {
        this.refDate = refDate;
    }

    public String getRefNumber() {
        return refNumber;
    }

    public void setRefNumber(String refNumber) {
        this.refNumber = refNumber;
    }
}
