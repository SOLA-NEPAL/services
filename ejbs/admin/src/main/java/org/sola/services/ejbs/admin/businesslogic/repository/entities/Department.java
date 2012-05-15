package org.sola.services.ejbs.admin.businesslogic.repository.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.DefaultSorter;
import org.sola.services.common.repository.entities.AbstractVersionedEntity;

@Table(name="department", schema="system")
@DefaultSorter(sortString="name")
public class Department extends AbstractVersionedEntity {
    @Id
    @Column
    private String id;
    
    @Column(name="office_code")
    private int officeCode;
    
    @Column
    private String name;
    
    public Department(){
        super();
    }

    public String getId() {
        if (id == null) {
            id = generateId();
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(int officeCode) {
        this.officeCode = officeCode;
    }
}
