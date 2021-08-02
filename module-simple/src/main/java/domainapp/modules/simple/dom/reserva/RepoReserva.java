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
package domainapp.dom.reserva;

	
import java.math.BigDecimal;
import java.sql.Date;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.lucene.util.Counter;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import domainapp.dom.habitacion.Habitacion;
import domainapp.dom.habitacion.Habitaciones;
import domainapp.dom.huesped.Huesped;
import domainapp.dom.huesped.Huespedes;
import domainapp.dom.reserva.estado.Solicitada;
import domainapp.dom.tipodehabitacion.TipodeHabitacion;
import domainapp.dom.reserva.estado.Solicitada;


@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        repositoryFor = Reserva.class
)
@DomainServiceLayout(
        named="Reservas",
        menuOrder = "1"
)

public class RepoReserva {
	
	
	
    //region > listActuales (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "5")
    public List<Reserva> listarActuales() {
        return repositorio.allMatches(new QueryDefault<>(
        		Reserva.class,
        		"listarActuales"));
    	
    	/* return repositorio.allInstances(new QueryDefault<>(
        		Reserva.class,
        		"listarActuales")); */
    }
    
  //region > listAll (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "1")
    public List<Reserva> listarReservas() {
        return repositorio.allInstances(Reserva.class);
    }
    
    //region > buscarPorNombre (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "2")
    public List<Reserva> buscarPorHuesped(
            @ParameterLayout(named="Huesped:")
            final Huesped huesped
            
    ) {
        return repositorio.allMatches(
                new QueryDefault<>(
                        Reserva.class,
                        "findByHuesped",
                        "huesped", huesped));
    }
    
    @Programmatic
    public List<Huesped> choices0BuscarPorHuesped() {
        return huespedes.listarHuespedes();
   	}
    
    @ActionLayout(
            cssClassFa = "fa fa-thumbs-up"
    )
    
    	
        @Action(semantics = SemanticsOf.IDEMPOTENT)
        @MemberOrder(sequence = "20")
    
    public Reserva crearReserva(final
            @ParameterLayout(named="Huesped (ingrese email del titular)") Huesped huesped,
            @ParameterLayout(named="Fecha llegada") LocalDate fechaIn,
            @Parameter(
                    regexPattern = "^[1-9][0-9]*$",
                    regexPatternReplacement = "Ingrese un número de noches correcto."   
                )
            @ParameterLayout(named="Estadia (# noches)") int estadia,
            @ParameterLayout(named="Habitación") Habitacion habitacion,
    		@ParameterLayout(named="Huéspedes") Integer numHues,
    		
    		@ParameterLayout(named="Canal de venta")@Parameter(optionality = Optionality.MANDATORY) String canalVenta
    		
    		)
 {
    	
    	Reserva mireserva = repositorio.instantiate(Reserva.class);
    	mireserva.setHuesped(huesped);
    	mireserva.setFechaIn(fechaIn);
    	mireserva.setFechaSal(new LocalDate(fechaIn.plusDays(estadia)));
    	mireserva.setEstadia(estadia);
    	mireserva.setHabitacion(habitacion);
    	mireserva.setNumHues(numHues);
    	
    	
    	
    	mireserva.setCanalVenta(canalVenta);
    	mireserva.setGasto(new BigDecimal(habitacion.getTipodeHabitacion().getPrecio() * estadia * numHues));
    	
    	mireserva.getEstado().reservar();
    	repositorio.persist(mireserva);
    	return mireserva;
    }
    
    // Fin de Region Crear Reserva.
    
    //Región validar Reserva
    
    public List<Integer> choices4CrearReserva(
            final Huesped huesped,
            final LocalDate fechaIn,
            final int estadia,
            final Habitacion habitacion,
    		final Integer numHues,
    		final String canalVenta
    		)
    {
    	ArrayList<Integer> list= new ArrayList<Integer>(); 
    	if(habitacion!=null)
    	{
    	final int maximoDeCamas=habitacion.getTipodeHabitacion().getCamas();
        for (int i = 1; i <=  maximoDeCamas; i++) {
        	list.add(i);
        }
    	
    	}
       	 
             return list;
    }
    
    
    
    
    

    public String validate1CrearReserva(final LocalDate fechaIn){
    	if (fechaIn.isBefore(LocalDate.now())){
    	return "Corregir la fecha inicial.";}
    else
    	{return "";}}
    /*
    @Programmatic
    public String validate4CrearReserva(final Integer numHues, final Habitacion habitacion){
     	 
     	 if (numHues < habitacion.getTipodeHabitacion().getCamas())
     	 {return "no";}
     	 else 
     	 {return "eee";}}	
	*/

    /*
    public String validate5CrearReserva(final Integer numHues,
    									final Habitacion habitacion)
    {if (numHues > habitacion.getTipodeHabitacion().getCamas()){
    	return "La habitación no admite ese número de huéspedes.";}
    else
	{return "Uh";}}
	*/
    
    /*
    //@Programmatic
    public String validate4CrearReserva(final Integer numHues, final Habitacion hab){
    Integer dispo = hab.getTipodeHabitacion().getCamas();
    if (numHues > dispo){
    	return "La habitación no admite ese número de huéspedes.";}
    if (numHues < dispo){
    	return "La habitación no admite ese número de huéspedes.";}
    else
    {return "";}}
*/

	


    
    
    
   
    

    
    

    

    
  //Fin región validar Reserva
    
    // Autocompleta el Huésped a partir de su email:
    public Collection<Huesped> autoComplete0CrearReserva(final @MinLength(2) String email) {
        return huespedes.buscarPorEmail(email);
        
        
        
    }
    
    /*
    @Programmatic
    @Action(semantics=SemanticsOf.IDEMPOTENT)
	public Collection<Integer> choices4CrearReserva(Habitacion habitacion) {
    	Collection<Integer> camasDispo = new ArrayList<Integer>();
    	Integer i = 0;
    	Integer disponible = habitacion.getTipodeHabitacion().getCamas();
    	while (i < disponible)
    	{
    		camasDispo.add(i);
    		i++;
    	}
    	return camasDispo;
    	                
    }
    */
    
    
    
    
    
    

    
    // Lista las habitaciones creadas en la clase correspondiente:    
     public List<Habitacion> choices3CrearReserva() {
     
        return habitaciones.listarHabitaciones();
   	}
     
     
     
     /*
     public List<Integer> choices4CrearReserva() {
    	 
    	 List list = Arrays.asList(2, 4, 10);
          return list;
     	}
     
     */
     
     /*
     @Programmatic
     public List<Integer> choices4CrearReserva(Habitacion habitacion) {
     	List<Integer> camasDispo = new ArrayList<Integer>();
     	Integer i = 0;
     	Integer disponible = habitacion.getTipodeHabitacion().getCamas();
     	while (i < disponible)
     	{
     		camasDispo.add(i);
     		i++;
     	}
     	return camasDispo;
         
    	}	
    */
     
    
     
     
     /*
     public Collection<Integer> choices4CrearReserva(Reserva reserva) {
         //return productService.quantityChoicesFor(product);  
         //return habitacion.getTipodeHabitacion().getCamas().camasChoicesFor(camas)
    	 Integer dispo = reserva.getHabitacion().getTipodeHabitacion().getCamas();
    	 return dispo.numHuesChoicesFor(reserva);
     }
     */
     
     /*
 	 @Programmatic
     public Collection<Integer> choices4CrearReserva(Habitacion habitacion) {
    	 
    	 //return Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8 , 9, 10, 11, 12);
    	 Integer max = habitacion.getTipodeHabitacion().getCamas().intValue(); 
    	 Collection<Integer> lista = IntStream.rangeClosed(0, max).boxed().collect(Collectors.toList());
    	 return lista;
    	 
    	 
     }
     */
    
     
     public List<String> choices5CrearReserva() {
         return Arrays.asList("Despegar","Avantrip", "AlMundo", "Otro");
         
     }

     
    
    
    @javax.inject.Inject
    private Huespedes huespedes;
    
    @javax.inject.Inject
    private Habitaciones habitaciones;
    
    
    @Inject
    RepositoryService repositorio;
    
    
   
    
    
    
    
    
    

    
    //endregion
}

