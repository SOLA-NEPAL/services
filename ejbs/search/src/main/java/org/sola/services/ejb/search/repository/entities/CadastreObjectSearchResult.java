/**
 * ******************************************************************************************
 * Copyright (C) 2012 - Food and Agriculture Organization of the United Nations (FAO).
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice,this list
 *       of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice,this list
 *       of conditions and the following disclaimer in the documentation and/or other
 *       materials provided with the distribution.
 *    3. Neither the name of FAO nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,STRICT LIABILITY,OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
package org.sola.services.ejb.search.repository.entities;

import javax.persistence.Column;
import javax.persistence.Table;
import org.sola.services.common.repository.AccessFunctions;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

/**
 *
 * @author Elton Manoku
 */
@Table(name = "cadastre_object", schema = "cadastre")
public class CadastreObjectSearchResult extends AbstractReadOnlyEntity {

    public static final String SEARCH_STRING_PARAM = "search_string";
    public static final String SEARCH_BY_NUMBER = "NUMBER";
    public static final String SEARCH_BY_BAUNIT = "BAUNIT";
    public static final String SEARCH_BY_OWNER_OF_BAUNIT = "OWNER_OF_BAUNIT";
    public static final String SEARCH_BY_BAUNIT_ID = "BAUNIT_ID";
    
    private static final String QUERY_WHERE_BY_OFFICE = "(office_code=#{"
            + AbstractReadOnlyEntity.PARAM_OFFICE_CODE + "} OR office_code IS NULL)";
    private static final String QUERY_WHERE_BY_OFFICE2 = "(co.office_code=#{"
            + AbstractReadOnlyEntity.PARAM_OFFICE_CODE + "} OR co.office_code IS NULL)";
    
    public static final String QUERY_SELECT_SEARCHBY_IDS = "SELECT distinct co.id, co.dataset_id, "
            + "(co.name_firstpart || '/ ' || co.name_lastpart) as label, "
            + "st_asewkb(co.geom_polygon) as the_geom, co.office_code "
            + "FROM cadastre.cadastre_object co WHERE co.id IN (%s)";
    
    public static final String QUERY_WHERE_SEARCHBY_NUMBER = "status_code= 'current' and "
            + "compare_strings(#{search_string}, name_firstpart || ' ' || name_lastpart) "
            + "AND " + QUERY_WHERE_BY_OFFICE;
    
    public static final String QUERY_SELECT_SEARCHBY_BAUNIT = "distinct co.id, co.dataset_id, "
            + "ba_unit.name_firstpart || '/ ' || ba_unit.name_lastpart || "
            + "' > ' || co.name_firstpart || '/ ' || co.name_lastpart as label, "
            + "st_asewkb(geom_polygon) as the_geom, co.office_code";

    public static final String QUERY_FROM_SEARCHBY_BAUNIT = "cadastre.cadastre_object co "
            + "inner join administrative.ba_unit on co.id = ba_unit.cadastre_object_id";

    public static final String QUERY_WHERE_SEARCHBY_BAUNIT = 
            "(co.status_code= 'current' or ba_unit.status_code= 'current')"
            + "and compare_strings(#{search_string}, "
            + "ba_unit.name_firstpart || ' ' || ba_unit.name_lastpart) AND " + QUERY_WHERE_BY_OFFICE2;

    public static final String QUERY_SELECT_SEARCHBY_OWNER_OF_BAUNIT = " distinct co.id, co.dataset_id, "
            + "coalesce(party.name, '') || ' ' || coalesce(party.last_name, '') || "
            + "' > ' || co.name_firstpart || '/ ' || co.name_lastpart as label, "
            + "st_asewkb(co.geom_polygon) as the_geom, co.office_code";
    
    public static final String QUERY_FROM_SEARCHBY_OWNER_OF_BAUNIT = "cadastre.cadastre_object co "
            + "inner join administrative.ba_unit ba "
            + "on co.id = ba.cadastre_object_id "
            + "inner join administrative.rrr "
            + "on (ba.id = rrr.ba_unit_id and rrr.status_code = 'current' "
            + "and rrr.type_code = 'ownership') "
            + "inner join administrative.party_for_rrr pfr on rrr.id = pfr.rrr_id "
            + "inner join party.party on pfr.party_id= pfr.party_id ";
    
    public static final String QUERY_WHERE_SEARCHBY_OWNER_OF_BAUNIT = 
            "(co.status_code= 'current' or ba_unit.status_code= 'current') "
            + "and compare_strings(#{search_string}, "
            + "coalesce(party.name, '') || ' ' || coalesce(party.last_name, '')) "
            + "AND " + QUERY_WHERE_BY_OFFICE2;
    
    /** 
     * Ideally status should be current, that means splitted parcels should be approved first.
     * In this case, add into query the following statement - AND status_code = 'current'.
    */ 
    
    public static final String QUERY_WHERE_GET_NEW_PARCELS = "transaction_id IN "
            + "(SELECT cot.transaction_id FROM administrative.ba_unit ba "
            + "INNER JOIN cadastre.cadastre_object_target cot ON ba.cadastre_object_id = cot.cadastre_object_id "
            + "WHERE ba.id = #{search_string}) "
            + "AND (SELECT COUNT(1) FROM administrative.ba_unit WHERE cadastre_object_id = cadastre_object.id) = 0 "
            + "AND " + QUERY_WHERE_BY_OFFICE;
    
    @Column(name = "id")
    private String id;
    @Column(name = "label")
    @AccessFunctions(onSelect = "name_firstpart || '/ ' || name_lastpart")
    private String label;
    @Column(name = "the_geom")
    @AccessFunctions(onSelect = "st_asewkb(geom_polygon)")
    private byte[] theGeom;
    @Column(name = "office_code", updatable = false)
    private String officeCode;
    @Column(name="dataset_id")
    private String datasetId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public byte[] getTheGeom() {
        return theGeom;
    }

    public void setTheGeom(byte[] theGeom) {
        this.theGeom = theGeom;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }
}
