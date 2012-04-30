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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.services.ejb.cadastre.repository.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.AccessFunctions;
import org.sola.services.common.repository.entities.AbstractVersionedEntity;
/**
 *
 * @author Shrestha_Kabin
 */
@Table(name="segments",schema="cadastre")
public class Segment extends AbstractVersionedEntity {
    public static final String QUERY_WHERE_SEARCHBYPOINT=
            "ST_Intersects(the_geom,SetSRID(ST_Point(#{x},#{y},#{srid}))";
    public static final String QUERY_WHERE_SEARCHBYTRANSACTION =
            "transaction_id = #{transaction_id}";
    
    @Id
    @Column(name="sid")
    private int sid;
    
    @Column(name="segno") 
    private int segno;
    
    @Column(name="the_geom")
    @AccessFunctions(onSelect = "st_asewk(geom_polygon)",
                     onChange="get_geometry_with_srid(#{the_geom})")
    private byte[] geomLine;
    
    @Column(name="bound_type")
    private int boundtype;
    
    @Column(name="id")
    private int pid;
    
    @Column(name="mbound_type")
    private int mboundtype;
    
    @Column(name="abound_type")
    private int aboundtype;
    
    @Column(name="shape+length")
    private double shapelenth;
    
    public Segment(){
        super();
    }

    public int getAboundtype() {
        return aboundtype;
    }

    public void setAboundtype(int aboundtype) {
        this.aboundtype = aboundtype;
    }

    public int getBoundtype() {
        return boundtype;
    }

    public void setBoundtype(int boundtype) {
        this.boundtype = boundtype;
    }

    public byte[] getGeomLine() {
        return geomLine;
    }

    public void setGeomLine(byte[] geomLine) {
        this.geomLine = geomLine;
    }

    public int getId() {
        return pid;
    }

    public void setId(int id) {
        this.pid = id;
    }

    public int getMboundtype() {
        return mboundtype;
    }

    public void setMboundtype(int mboundtype) {
        this.mboundtype = mboundtype;
    }

    public int getSegno() {
        return segno;
    }

    public void setSegno(int segno) {
        this.segno = segno;
    }

    public double getShapelenth() {
        return shapelenth;
    }

    public void setShapelenth(double shapelenth) {
        this.shapelenth = shapelenth;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    } 
}
