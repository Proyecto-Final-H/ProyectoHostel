/*
 *	Copyright ( C ) 2019 , GESTION HOSTEL
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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.repository.RepositoryService;

import domainapp.dom.simple.SimpleObject;

import domainapp.dom.tipodehabitacion.TipodeHabitacion.Etipodeprecio;
import domainapp.dom.tipodehabitacion.TipodeHabitacion.Etipodesexo;


/*import domainapp.dom.simple.TipodeHabitacion.Etipodeprecio; 
import domainapp.dom.simple.TipodeHabitacion.Etipodesexo; 
*/
@DomainService(
        nature = NatureOfService.VIEW,
        repositoryFor = TipodeHabitacion.class
)
@DomainServiceLayout(
        menuOrder = "10",
        named="Habitaciones"
)
public class RepoTipodeHabitacion {

    //region > title
    public TranslatableString title() {
        return TranslatableString.tr("Tipo de Habitación");
    }
    //endregion

    //region > listAll (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "1")
    public List<TipodeHabitacion> listarTiposDeHabitaciones() {
        return repositoryService.allInstances(TipodeHabitacion.class);
    }
    //endregion
    //region > create (action)
    public static class CreateDomainEvent extends ActionDomainEvent<RepoTipodeHabitacion> {
        public CreateDomainEvent(final RepoTipodeHabitacion source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(sequence = "3")
    public TipodeHabitacion crearTipoDeHabitacion(
            
           final @Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named="Cantidad de Camas") Integer camas,
           final @Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named="Tipo de Precio") Etipodeprecio tprecio,
           final @Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named="Precio") int precio,

           final @Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named="Género:") Etipodesexo tsexo
           ) {
        final TipodeHabitacion obj = repositoryService.instantiate(TipodeHabitacion.class);
        
        obj.setCamas(camas);
        obj.setTprecio(tprecio);
        obj.setPrecio(precio);
        obj.setTsexo(tsexo);
        obj.setDescripcion("Camas: " + camas + ", tipo de precio: " + tprecio.toString() + ", sexo:" + tsexo.toString());
       
        repositoryService.persist(obj);
        return obj;
    }
    
    public Collection<Integer> choices0CrearTipoDeHabitacion() {
        return Arrays.asList(2,3,4,6,8,10);
    }
    //endregion

    //region > injected services

    @javax.inject.Inject
    RepositoryService repositoryService;

    //endregion
}
