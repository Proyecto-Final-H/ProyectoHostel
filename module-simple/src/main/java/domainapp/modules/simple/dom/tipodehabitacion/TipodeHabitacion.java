/*
 *	Copyright ( C ) 2017 , GESTION HOSTEL
 *
 *   GESTION HOSTEL is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package domainapp.dom.tipodehabitacion;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.eventbus.PropertyDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.util.ObjectContracts;


@javax.jdo.annotations.PersistenceCapable(
        identityType=IdentityType.DATASTORE,
        schema = "simple",
        table = "TipodeHabitacion"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")
@javax.jdo.annotations.Version(
//        strategy=VersionStrategy.VERSION_NUMBER,
        strategy= VersionStrategy.DATE_TIME,
        column="version")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "find", language = "JDOQL",
                value = "SELECT "
                        + "FROM dom.tipodehabitacion.TipodeHabitacion"),
        @javax.jdo.annotations.Query(
                name = "findByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM dom.tipodehabitacion.TipodeHabitacion "
                        + "WHERE name.indexOf(:name) >= 0 ")
})
@DomainObject
public class TipodeHabitacion implements Comparable<TipodeHabitacion> {

    public static final int NAME_LENGTH = 40;


    public TranslatableString title() {
        return TranslatableString.tr("Tipo de habitación: {descripcion}", "descripcion", getDescripcion());
    }


    public static class NameDomainEvent extends PropertyDomainEvent<TipodeHabitacion,String> {}
    @javax.jdo.annotations.Column(
            allowsNull="false",
            length = NAME_LENGTH
    )
    @Property(
        domainEvent = NameDomainEvent.class
    )

    
    private Integer camas;
	@javax.jdo.annotations.Column(allowsNull="false")
    public Integer getCamas() {
        return camas;
    }
    public void setCamas(final Integer camas) {
        this.camas = camas;
    }

    
    
    private Etipodeprecio tprecio;

    @Persistent
    @MemberOrder(sequence = "3")
	@javax.jdo.annotations.Column(allowsNull="false")
	
    public Etipodeprecio getTprecio() {
    	return tprecio;
    }
    
    public void setTprecio (Etipodeprecio tprecio)  {
    	this.tprecio = tprecio;
    }
   
    private Etipodesexo tsexo;

    @Persistent
    @MemberOrder(sequence = "3")
	@javax.jdo.annotations.Column(allowsNull="false")
	
    public Etipodesexo getTsexo() {
    	return tsexo;
    }
    
   
    
    public void setTsexo (Etipodesexo tsexo)  {
    	this.tsexo = tsexo;
    }
    
    
    private String descripcion;
	@javax.jdo.annotations.Column(allowsNull="false")
	public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(final String descripcion) {
        this.descripcion = descripcion;
    }
    
    
    private int precio;

    @Persistent
    
	@javax.jdo.annotations.Column(allowsNull="false")
	
    public int getPrecio() {
    	return precio;
    }
    
   
    
    public void setPrecio (int precio)  {
    	this.precio = precio;
    }
    

    
    
    

    public static class DeleteDomainEvent extends ActionDomainEvent<TipodeHabitacion> {}
    @Action(
            domainEvent = DeleteDomainEvent.class,
            semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE
    )
    public void delete() {
        repositoryService.remove(this);
    }



    @Override
    public int compareTo(final TipodeHabitacion other) {
        return ObjectContracts.compare(this, other, "name");
    }

    
    public enum Etipodeprecio{
    	
    	Privada,Dormis
    	}
    public enum Etipodesexo{
    	
    	Masculino,Femenino,Mixto
    	}
    
    @javax.inject.Inject
    RepositoryService repositoryService;

}
