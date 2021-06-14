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
package domainapp.dom.habitacion;

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
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.repository.RepositoryService;

import domainapp.dom.huesped.Huesped;
import domainapp.dom.huesped.Huespedes;
import domainapp.dom.tipodehabitacion.TipodeHabitacion;
import domainapp.dom.tipodehabitacion.RepoTipodeHabitacion;


@DomainService(
        nature = NatureOfService.VIEW,
        repositoryFor = Habitaciones.class
)

@DomainServiceLayout(
        menuOrder = "10",
        named="Habitaciones"
)
public class Habitaciones {

    //region > title
    public TranslatableString title() {
        return TranslatableString.tr("Habitacion");
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
    public List<Habitacion> listarHabitaciones() {
        return repositoryService.allInstances(Habitacion.class);
    }
    //endregion

    //region > findByName (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "2")
    public List<Habitacion> buscarPorNombre(
            @ParameterLayout(named="Name")
            final String name
    ) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                		Habitacion.class,
                        "findByName",
                        "name", name));
    }
    //endregion

    //region > create (action)
    public static class CreateDomainEvent extends ActionDomainEvent<Habitaciones> {
        public CreateDomainEvent(final Habitaciones source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(sequence = "1")
    public Habitacion crearHabitacion(
            final @ParameterLayout(named="Nombre") String name,
            final @ParameterLayout(named="Tipo de Habitación")	 TipodeHabitacion tipodeHabitacion
           ) {
        final Habitacion obj = repositoryService.instantiate(Habitacion.class);
        obj.setName(name);
        obj.setTipodeHabitacion(tipodeHabitacion);
        repositoryService.persist(obj);
        return obj;
    }
    
    @Programmatic
    public List<TipodeHabitacion> choices1CrearHabitacion() {
        return repoTipodeHabitacion.listarTiposDeHabitaciones();
   	}
    //endregion

    //region > injected services
}
