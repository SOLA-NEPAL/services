package org.sola.services.ejb.search.repository.entities;

import javax.persistence.Column;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

public class LocSearchResult extends AbstractReadOnlyEntity {

    public static final String PARAM_BA_UNIT_ID = "baUnitId";
    public static final String PARAM_PARTY_ID = "partyId";
    public static final String PARAM_LOC_ID = "locId";
    public static final String QUERY_SELECT =
            "SELECT DISTINCT l.id, l.pana_no, moth.mothluj_no, l.tmp_pana_no, moth.moth_luj, "
            + "(SELECT string_agg(COALESCE(p.name, '') || ' ' || COALESCE(p.last_name, ''), '; ') AS owners FROM "
            + "  administrative.party_for_rrr pr LEFT JOIN party.party p ON pr.party_id=p.id WHERE pr.rrr_id=r.id) AS owners "
            + "FROM (administrative.loc l INNER JOIN administrative.moth moth ON l.moth_id=moth.id) "
            + "LEFT JOIN administrative.rrr r ON r.loc_id=l.id "
            + "WHERE (r.status_code='current' OR r.id IS NULL) AND "
            + "(r.id IN (SELECT pr2.rrr_id FROM administrative.party_for_rrr pr2 WHERE pr2.party_id=#{" + PARAM_PARTY_ID + "}) OR #{" + PARAM_PARTY_ID + "}='') "
            + "AND (r.ba_unit_id=#{" + PARAM_BA_UNIT_ID + "} OR #{" + PARAM_BA_UNIT_ID + "}='') "
            + "AND (l.id=#{" + PARAM_LOC_ID + "} OR #{" + PARAM_LOC_ID + "}='') "
            + "LIMIT 100";
    @Column(name = "id")
    private String id;
    @Column(name = "pana_no")
    private String panaNumber;
    @Column(name = "mothluj_no")
    private String mothNumber;
    @Column(name = "tmp_pana_no")
    private String tmpPanaNumber;
    @Column(name = "moth_luj")
    private String mothLuj;
    @Column(name = "owners")
    private String owners;

    public LocSearchResult() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMothLuj() {
        return mothLuj;
    }

    public void setMothLuj(String mothLuj) {
        this.mothLuj = mothLuj;
    }

    public String getMothNumber() {
        return mothNumber;
    }

    public void setMothNumber(String mothNumber) {
        this.mothNumber = mothNumber;
    }

    public String getOwners() {
        return owners;
    }

    public void setOwners(String owners) {
        this.owners = owners;
    }

    public String getPanaNumber() {
        return panaNumber;
    }

    public void setPanaNumber(String panaNumber) {
        this.panaNumber = panaNumber;
    }

    public String getTmpPanaNumber() {
        return tmpPanaNumber;
    }

    public void setTmpPanaNumber(String tmpPanaNumber) {
        this.tmpPanaNumber = tmpPanaNumber;
    }
}
