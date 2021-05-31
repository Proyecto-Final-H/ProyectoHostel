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
package domainapp.dom.huesped;

import java.util.List;

import java.util.regex.Pattern;

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


import domainapp.dom.huesped.Huesped.E_titular;

@DomainService(
        nature = NatureOfService.VIEW,
        repositoryFor = Huesped.class
)
@DomainServiceLayout(
        menuOrder = "10"
)
public class Huespedes {
	

	
    //region > title
    public TranslatableString title() {
        return TranslatableString.tr("Húespedes");
    }
    //endregion

    //region > listarHuespedes (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "1")
    public List<Huesped> listarHuespedes() {
        return repositoryService.allInstances(Huesped.class);
        
    }
    
    

    //endregion
    


    //region > buscarPorNombre (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "2")
    public List<Huesped> buscarPorNombre(
            @ParameterLayout(named="Name")
            final String name
    ) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        Huesped.class,
                        "findByName",
                        "name", name));
    }

    
    //endregion
    
    public List<Huesped> buscarPorEmail(
            @ParameterLayout(named="Email")
            final String email
    ) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        Huesped.class,
                        "findByEmail",
                        "email", email));
    }
    
    
    //region > create (action)
    public static class CreateDomainEvent extends ActionDomainEvent<Huespedes> {
        public CreateDomainEvent(final Huespedes source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(sequence = "3")
    public Huesped cargarHuesped(
            final
            @ParameterLayout(named="Nombre")String name, 
    		@ParameterLayout(named="Teléfono")String numTel,
            @Parameter(
                    regexPattern = "(\\w+\\.)*\\w+@(\\w+\\.)+[A-Za-z]+",
                    regexPatternFlags = Pattern.CASE_INSENSITIVE,
                    regexPatternReplacement = "Ingrese una dirección de correo electrónico válida (contienen un símbolo '@') -"   
                )
    		@ParameterLayout(named="Email") String email,

    		@ParameterLayout(named="País")ListaPais pais
    		) {
        final Huesped obj = repositoryService.instantiate(Huesped.class);
        obj.setName(name);
        obj.setNumTel(numTel);
        obj.setEmail(email);
        obj.setPais(pais);
        
        repositoryService.persist(obj);
        return obj;
    }

    //endregion
    
    
    //region > injected services

    @javax.inject.Inject
    RepositoryService repositoryService;

    //endregion
}
