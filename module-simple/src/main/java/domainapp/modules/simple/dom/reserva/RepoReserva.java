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
@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        repositoryFor = Reserva.class
)
@DomainServiceLayout(
        named="Reservas",
        menuOrder = "1"
)
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
    // Autocompleta el Huésped a partir de su email:
    public Collection<Huesped> autoComplete0CrearReserva(final @MinLength(2) String email) {
        return huespedes.buscarPorEmail(email);
    }
     public List<Habitacion> choices3CrearReserva() {
     
        return habitaciones.listarHabitaciones();
   	}
     
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

