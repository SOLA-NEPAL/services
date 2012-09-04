/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.cadastre.repository.entities;

import javax.persistence.Table;
import org.sola.services.common.repository.DefaultSorter;
import org.sola.services.common.repository.entities.AbstractCodeEntity;

/**
 *
 * @author Kumar
 */
@Table(name = "area_unit_type", schema = "cadastre")
@DefaultSorter(sortString = "display_value")
public class AreaUnitType extends AbstractCodeEntity {

    public static final String CANCEL = "cancel";

    public AreaUnitType() {
        super();
    }
}
