package org.sola.services.ejb.search.repository.entities;

import javax.persistence.Column;
import org.sola.services.common.repository.entities.AbstractReadOnlyEntity;

public class Owner extends AbstractReadOnlyEntity {

    public static final String PARAM_LANG = "languageCode";
    public static final String PARAM_BA_UNIT_ID = "baUnitId";
    
    public static final String QUERY_SELECT = ""
            + "SELECT DISTINCT p.id, (COALESCE(p.name, '') || ' ' "
            + "   || COALESCE(p.last_name, '')) AS full_name, "
            + "  (get_translation(COALESCE(d.display_value, ''), #{" + PARAM_LANG + "}) "
            + "   || (CASE WHEN d.code IS NULL THEN '' ELSE ', ' END) "
            + "   || get_translation(COALESCE(vdc.display_value, ''), #{" + PARAM_LANG + "}) "
            + "   || (CASE WHEN vdc.code IS NULL THEN '' ELSE '-' END) "
            + "   || a.ward_no) AS address, p.fathers_name, p.grandfather_name "
            + "FROM (administrative.rrr r "
            + "  INNER JOIN (administrative.party_for_rrr pr "
            + "    INNER JOIN (party.party p "
            + "      LEFT JOIN (address.address a "
            + "        LEFT JOIN (address.vdc vdc "
            + "          LEFT JOIN address.district d "
            + "          ON vdc.district_code = d.code) "
            + "        ON a.vdc_code = vdc.code) "
            + "      ON p.address_id = a.id) "
            + "    ON pr.party_id = p.id) "
            + "  ON r.id = pr.rrr_id) "
            + "  INNER JOIN administrative.rrr_type rt "
            + "  ON r.type_code = rt.code "
            + "WHERE r.ba_unit_id = #{" + PARAM_BA_UNIT_ID + "} "
            + "  AND rt.rrr_group_type_code = 'ownership' "
            + "  AND r.status_code = 'current'";

    @Column(name="id")
    private String id;
    @Column(name="full_name")
    private String fullName;
    @Column(name="address")
    private String address;
    @Column(name="fathers_name")
    private String fatherName;
    @Column(name="grandfather_name")
    private String grandfatherName;
    
    public Owner() {
        super();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGrandfatherName() {
        return grandfatherName;
    }

    public void setGrandfatherName(String grandfatherName) {
        this.grandfatherName = grandfatherName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
