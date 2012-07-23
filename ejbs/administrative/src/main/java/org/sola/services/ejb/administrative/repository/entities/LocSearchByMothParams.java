package org.sola.services.ejb.administrative.repository.entities;

import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

public class LocSearchByMothParams extends AbstractReadOnlyEntity {
    private MothBasic moth;
    private String pageNumber;
    
    public LocSearchByMothParams(){
        super();
    }

    public MothBasic getMoth() {
        return moth;
    }

    public void setMoth(MothBasic moth) {
        this.moth = moth;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }
}