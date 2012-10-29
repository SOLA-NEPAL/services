package org.sola.services.ejb.search.repository.entities;

import java.math.BigDecimal;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

public class RestrictionSearchParams extends AbstractReadOnlyEntity {
    private String languageCode;
    private String vdcCode;
    private String mapSheetId;
    private String wardNo;
    private String parcelNo;
    private String bundleNo;
    private String bundlePageNo;
    private String restrictionReasonCode;
    private String restrtictionOfficeName;
    private String referenceNo;
    private String sourceTypeCode;
    private String serialNo;
    private String ownerName;
    private String ownerLastName;
    private String regNumber;
    private Integer regDateFrom;
    private Integer regDateTo;
    private Integer refDateFrom;
    private Integer refDateTo;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    
    public RestrictionSearchParams(){
        super();
    }

    public String getBundleNo() {
        return bundleNo;
    }

    public void setBundleNo(String bundleNo) {
        this.bundleNo = bundleNo;
    }

    public String getBundlePageNo() {
        return bundlePageNo;
    }

    public void setBundlePageNo(String bundlePageNo) {
        this.bundlePageNo = bundlePageNo;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getMapSheetId() {
        return mapSheetId;
    }

    public void setMapSheetId(String mapSheetId) {
        this.mapSheetId = mapSheetId;
    }

    public String getOwnerLastName() {
        return ownerLastName;
    }

    public void setOwnerLastName(String ownerLastName) {
        this.ownerLastName = ownerLastName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getParcelNo() {
        return parcelNo;
    }

    public void setParcelNo(String parcelNo) {
        this.parcelNo = parcelNo;
    }

    public BigDecimal getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(BigDecimal priceFrom) {
        this.priceFrom = priceFrom;
    }

    public BigDecimal getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(BigDecimal priceTo) {
        this.priceTo = priceTo;
    }

    public Integer getRefDateFrom() {
        return refDateFrom;
    }

    public void setRefDateFrom(Integer refDateFrom) {
        this.refDateFrom = refDateFrom;
    }

    public Integer getRefDateTo() {
        return refDateTo;
    }

    public void setRefDateTo(Integer refDateTo) {
        this.refDateTo = refDateTo;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public Integer getRegDateFrom() {
        return regDateFrom;
    }

    public void setRegDateFrom(Integer regDateFrom) {
        this.regDateFrom = regDateFrom;
    }

    public Integer getRegDateTo() {
        return regDateTo;
    }

    public void setRegDateTo(Integer regDateTo) {
        this.regDateTo = regDateTo;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public String getRestrictionReasonCode() {
        return restrictionReasonCode;
    }

    public void setRestrictionReasonCode(String restrictionReasonCode) {
        this.restrictionReasonCode = restrictionReasonCode;
    }

    public String getRestrtictionOfficeName() {
        return restrtictionOfficeName;
    }

    public void setRestrtictionOfficeName(String restrtictionOfficeName) {
        this.restrtictionOfficeName = restrtictionOfficeName;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getSourceTypeCode() {
        return sourceTypeCode;
    }

    public void setSourceTypeCode(String sourceTypeCode) {
        this.sourceTypeCode = sourceTypeCode;
    }

    public String getVdcCode() {
        return vdcCode;
    }

    public void setVdcCode(String vdcCode) {
        this.vdcCode = vdcCode;
    }

    public String getWardNo() {
        return wardNo;
    }

    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
    }
}
