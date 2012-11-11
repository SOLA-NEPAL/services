/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.administrative.repository.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Table;
import org.sola.services.common.repository.ChildEntityList;

/**
 *
 * @author KumarKhadka
 */
@Table(name = "moth", schema = "administrative")
public class Moth extends MothBasic {

    public static final String VDC_PARAM = "vdcCode";
    public static final String MOTH_LUJ_PARAM = "mothLuj";
    public static final String MOTH_LUJ_NUMBER_PARAM = "mothlujNumber";
    public static final String GET_BY_VDC_AND_MOTHLUJ = "vdc_code=#{" + VDC_PARAM 
            + "} and moth_luj=#{" + MOTH_LUJ_PARAM + "} and office_code=#{" + PARAM_OFFICE_CODE + "}";
    public static final String GET_BY_VDC_MOTHLUJ_AND_MOTHLUJ_NUMBER = "vdc_code=#{" 
            + VDC_PARAM + "} and moth_luj=#{" + MOTH_LUJ_PARAM + "} and mothluj_no=#{" 
            + MOTH_LUJ_NUMBER_PARAM + "} and office_code=#{" + PARAM_OFFICE_CODE + "}";    
    
    public static final String Search_Query =
            "SELECT DISTINCT m.id, m.mothluj_no, m.moth_luj,m.office_code, m.vdc_code, m.rowversion, m.rowidentifier "
            + "FROM (administrative.moth m INNER JOIN address.vdc v ON m.vdc_code=v.code) "
            + "WHERE (#{" + VDC_PARAM + "} = '' OR POSITION(LOWER(#{" + VDC_PARAM + "}) IN LOWER(COALESCE(m.vdc_code, ''))) > 0) "
            + "AND (#{" + MOTH_LUJ_PARAM + "} = '' OR POSITION(LOWER(#{" + MOTH_LUJ_PARAM + "}) IN LOWER(COALESCE(m.moth_luj, ''))) > 0) "
            + "AND (#{" + MOTH_LUJ_NUMBER_PARAM + "} = '' OR LOWER(#{" + MOTH_LUJ_NUMBER_PARAM + "}) = LOWER(COALESCE(m.mothluj_no, ''))) "
            + "AND (COALESCE(m.office_code, '') = #{" + PARAM_OFFICE_CODE + "} OR #{" + PARAM_OFFICE_CODE + "}='') "
            + "ORDER BY m.mothluj_no "
            + "LIMIT 101";
    
    @ChildEntityList(parentIdField = "mothId")
    private List<Loc> locList;

    public List<Loc> getLocList() {
        locList = locList == null ? new ArrayList<Loc>() : locList;
        return locList;
    }

    public void setLocList(List<Loc> locList) {
        this.locList = locList;
    }
}
