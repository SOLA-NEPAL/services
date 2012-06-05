/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.cadastre.repository.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.entities.AbstractEntity;

/**
 *
 * @author KumarKhadka
 */
@Table(name = "map_sheet", schema = "cadastre")
public class MapSheet extends AbstractEntity {   

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "map_number")
    private String mapNumber;
    @Column(name = "sheet_type")
    private int sheetType;
    @Column(name = "alpha_code")
    private String alpha_code;

    public String getAlpha_code() {
        return alpha_code;
    }

    public void setAlpha_code(String alpha_code) {
        this.alpha_code = alpha_code;
    }

    public String getId() {
        id = id == null ? id = generateId() : id;
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMapNumber() {
        return mapNumber;
    }

    public void setMapNumber(String mapNumber) {
        this.mapNumber = mapNumber;
    }

    public int getSheetType() {
        return sheetType;
    }

    public void setSheetType(int sheetType) {
        this.sheetType = sheetType;
    }
    
}
