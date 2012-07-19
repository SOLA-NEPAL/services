/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.administrative.repository.entities;

import javax.persistence.Table;
import org.sola.services.common.repository.ChildEntity;

@Table(name = "loc", schema = "administrative")
public class LocWithMoth extends Loc {
    
    @ChildEntity(childIdField="mothId",readOnly=true)
    private MothBasic moth;
    
    public LocWithMoth(){
        super();
    }

    public MothBasic getMoth() {
        return moth;
    }

    public void setMoth(MothBasic moth) {
        this.moth = moth;
    }
}
