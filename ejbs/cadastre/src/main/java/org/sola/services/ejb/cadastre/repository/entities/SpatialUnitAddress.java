/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.cadastre.repository.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.entities.AbstractVersionedEntity;

/**
 *
 * @author KumarKhadka
 */
@Table(name = "spatial_unit_address", schema = "cadastre")
public class SpatialUnitAddress extends AbstractVersionedEntity {

    public static final String SPATIAL_UNIT_ID_PARAM = "spatialUnitId";
    public static final String GET_BY_SPATIAL_UNIT_ID = "spatial_unit_id=#{" + SPATIAL_UNIT_ID_PARAM + "} ";
    @Id
    @Column(name = "spatial_unit_id")
    private String spatialUnitId;
    @Column(name = "address_id")
    private String addressId;

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getSpatialUnitId() {
        return spatialUnitId;
    }

    public void setSpatialUnitId(String spatialUnitId) {
        this.spatialUnitId = spatialUnitId;
    }
}
