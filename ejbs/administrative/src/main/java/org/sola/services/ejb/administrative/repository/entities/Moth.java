/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.administrative.repository.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Table;
import org.sola.services.common.repository.ChildEntityList;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

/**
 *
 * @author KumarKhadka
 */
@Table(name = "moth", schema = "administrative")
public class Moth extends MothBasic {

    public static final String VDC_PARAM = "vdcCode";
    public static final String MOTH_LUJ_PARAM = "mothLuj";
    public static final String TEST = "m.mothluj_no";
    public static final String MOTH_LUJ_NUMBER_PARAM = "mothlujNumber";
    private static final String QUERY_WHERE_BY_OFFICE = "(office_code=#{"
            + AbstractReadOnlyEntity.PARAM_OFFICE_CODE + "} OR office_code IS NULL)";
    public static final String GET_BY_VDC_AND_MOTHLUJ = "vdc_code=#{" + VDC_PARAM
            + "} and moth_luj=#{" + MOTH_LUJ_PARAM + "} and office_code=#{" + PARAM_OFFICE_CODE + "}";
    public static final String GET_BY_VDC_MOTHLUJ_AND_MOTHLUJ_NUMBER = "vdc_code=#{"
            + VDC_PARAM + "} and moth_luj=#{" + MOTH_LUJ_PARAM + "} and mothluj_no=#{"
            + MOTH_LUJ_NUMBER_PARAM + "} and office_code=#{" + PARAM_OFFICE_CODE + "}";
    public static final String SEARCH_QUERY =
            "SELECT DISTINCT m.id, m.mothluj_no, m.moth_luj,m.office_code, m.vdc_code, m.rowversion, m.rowidentifier "
            + "FROM (administrative.moth m INNER JOIN address.vdc v ON m.vdc_code=v.code) "
            + "WHERE " + GET_BY_VDC_AND_MOTHLUJ 
            + " AND POSITION(LOWER(#{" + MOTH_LUJ_NUMBER_PARAM + "}) IN LOWER(COALESCE(m.mothluj_no, ''))) = 1 "
            + "ORDER BY m.mothluj_no "
            + "LIMIT 101";
    
    public static final String QUERY_WHERE_SEARCH_BY_MOTH_LUJ_VDC_CODE = "compare_strings(#{search_string}, vdc_code || ' ' || moth_luj) "
            + "AND " + QUERY_WHERE_BY_OFFICE;
}
