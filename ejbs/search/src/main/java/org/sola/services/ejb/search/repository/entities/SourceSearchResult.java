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

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.sola.services.common.repository.CommonSqlProvider;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

@Table(name = "source", schema = "source")
public class SourceSearchResult extends AbstractReadOnlyEntity {

    public static final String QUERY_PARAM_TYPE_CODE = "typeCode";
    public static final String QUERY_PARAM_LA_NUMBER = "laNumber";
    public static final String QUERY_PARAM_REF_NUMBER = "refNumber";
    public static final String QUERY_PARAM_FROM_RECORDATION_DATE = "fromRecordationDate";
    public static final String QUERY_PARAM_TO_RECORDATION_DATE = "toRecordationDate";
    public static final String QUERY_PARAM_FROM_SUBMISSION_DATE = "fromSubmissionDate";
    public static final String QUERY_PARAM_TO_SUBMISSION_DATE = "toSubmissionDate";
    public static final String QUERY_PARAM_APPLICATION_NUMBER = "applicationNumber";
    
    public static final String SEARCH_QUERY =
            "SELECT DISTINCT s.id, s.la_nr, s.reference_nr, s.archive_id, s.ext_archive_id, s.type_code, "
            + "get_translation(st.display_value, #{" + CommonSqlProvider.PARAM_LANGUAGE_CODE + "}) AS typeDisplayValue, "
            + "s.acceptance, s.recordation, s.submission, s.status_code, s.office_code, "
            + "get_translation(t.display_value, #{" + CommonSqlProvider.PARAM_LANGUAGE_CODE + "}) AS statusDisplayValue "
            + "FROM ((source.source s LEFT JOIN (application.application_uses_source aus "
            + "INNER JOIN application.application app ON aus.application_id=app.id) ON s.id=aus.source_id) "
            + "LEFT JOIN transaction.reg_status_type t on s.status_code = t.code) "
            + "LEFT JOIN source.administrative_source_type AS st ON s.type_code = st.code "
            + "WHERE (type_code = #{" + QUERY_PARAM_TYPE_CODE + "} OR COALESCE(#{" + QUERY_PARAM_TYPE_CODE + "}, '') = '') "
            //+ "AND POSITION(COALESCE(#{" + QUERY_PARAM_LA_NUMBER + "}, '') IN COALESCE(la_nr, '')) > 0 "             
            //+ "AND POSITION(COALESCE(#{" + QUERY_PARAM_REF_NUMBER + "}, '') IN COALESCE(reference_nr, '')) > 0 "
            + "AND (COALESCE(la_nr, '') = #{" + QUERY_PARAM_LA_NUMBER + "} OR #{" + QUERY_PARAM_LA_NUMBER + "}='') "
            + "AND (COALESCE(reference_nr, '') = #{" + QUERY_PARAM_REF_NUMBER + "} OR #{" + QUERY_PARAM_REF_NUMBER + "}='') "
            + "AND (recordation BETWEEN #{" + QUERY_PARAM_FROM_RECORDATION_DATE + "} "
            + "AND #{" + QUERY_PARAM_TO_RECORDATION_DATE + "} OR (recordation IS NULL)) "
            + "AND (submission BETWEEN #{" + QUERY_PARAM_FROM_SUBMISSION_DATE + "} "
            + "AND #{" + QUERY_PARAM_TO_SUBMISSION_DATE + "} OR (submission IS NULL)) "
            + "AND (COALESCE(app.nr, '') = #{" + QUERY_PARAM_APPLICATION_NUMBER + "} OR #{" + QUERY_PARAM_APPLICATION_NUMBER + "}='') "
            + "LIMIT 101";
    @Id
    @Column
    private String id;
    @Column(name = "la_nr")
    private String laNr;
    @Column(name = "reference_nr")
    private String referenceNr;
    @Column(name = "archive_id")
    private String archiveId;
    @Column(name = "ext_archive_id")
    private String archiveDocumentId;
    @Column(name = "type_code")
    private String typeCode;
    @Column(name = "typeDisplayValue")
    private String typeDisplayValue;
    @Column(name = "acceptance")
    @Temporal(TemporalType.DATE)
    private Date acceptance;
    @Column(name = "recordation")
    private Integer recordation;
    @Column(name = "submission")
    @Temporal(TemporalType.DATE)
    private Date submission;
    @Column(name = "statusDisplayValue")
    private String statusDisplayValue;
    @Column(name = "status_code")
    private String statusCode;
    @Column(name = "office_code", updatable = false)
    private String officeCode;
    
    public SourceSearchResult() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getAcceptance() {
        return acceptance;
    }

    public void setAcceptance(Date acceptance) {
        this.acceptance = acceptance;
    }

    public String getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(String archiveId) {
        this.archiveId = archiveId;
    }

    public String getLaNr() {
        return laNr;
    }

    public void setLaNr(String laNr) {
        this.laNr = laNr;
    }

    public Integer getRecordation() {
        return recordation;
    }

    public void setRecordation(Integer recordation) {
        this.recordation = recordation;
    }

    public String getReferenceNr() {
        return referenceNr;
    }

    public void setReferenceNr(String referenceNr) {
        this.referenceNr = referenceNr;
    }

    public String getStatusDisplayValue() {
        return statusDisplayValue;
    }

    public void setStatusDisplayValue(String statusDisplayValue) {
        this.statusDisplayValue = statusDisplayValue;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Date getSubmission() {
        return submission;
    }

    public void setSubmission(Date submission) {
        this.submission = submission;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeDisplayValue() {
        return typeDisplayValue;
    }

    public void setTypeDisplayValue(String typeDisplayValue) {
        this.typeDisplayValue = typeDisplayValue;
    }

    public String getArchiveDocumentId() {
        return archiveDocumentId;
    }

    public void setArchiveDocumentId(String archiveDocumentId) {
        this.archiveDocumentId = archiveDocumentId;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }
}
