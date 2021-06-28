package domainapp.dom.caja;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.math.BigDecimal;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.joda.time.LocalDate;

//import domainapp.dom.habitacion.Habitacion;
import domainapp.dom.huesped.Huesped;
import domainapp.dom.huesped.Huespedes;
import domainapp.dom.reserva.Reserva;
import domainapp.dom.reserva.RepoReserva;

@DomainService(
        nature = NatureOfService.VIEW,
        repositoryFor = Caja.class
)
@DomainServiceLayout(
        menuOrder = "10",
        named="Caja"
)

public class RepoCaja {

    //region > title
    public TranslatableString title() {
        return TranslatableString.tr("Caja");
    }
    //endregion

    //region > listarCompras (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "1")
    public List<Caja> listarCompras() {
        return repositoryService.allInstances(Caja.class);
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
    public List<Caja> buscarPorNombre(
            @ParameterLayout(named="Name")
            final String name
    ) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        Caja.class,
                        "findByName",
                        "name", name));
    }
    //endregion

    //region > registrarCompra (action)
    public static class CreateDomainEvent extends ActionDomainEvent<RepoCaja> {
        public CreateDomainEvent(final RepoCaja source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(sequence = "3")
    public Caja registrarCompra(
            
            final @ParameterLayout(named="Reserva") Reserva reserva,
            final @ParameterLayout(named="Monto (ARS)") Double monto,
            final @ParameterLayout(named="Concepto") String concepto
    		) {
        final Caja obj = repositoryService.instantiate(Caja.class);
        
        obj.setReserva(reserva);
        obj.setMonto(monto);
        obj.setConcepto(concepto);
        repositoryService.persist(obj);
       
        obj.setFechaDePago(LocalDate.now());
        return obj;
    }
    
    
 
    @Programmatic
    public List<Reserva> choices0RegistrarCompra() {
        
        return repoReserva.listarReservas();
   	}

    public Collection<String> choices2RegistrarCompra() {
        return Arrays.asList("Pago de Estadía", "Adicional");
    }
    //endregion

    //region > injected services

    @javax.inject.Inject
    RepositoryService repositoryService;

    @javax.inject.Inject
    private RepoReserva repoReserva;
    //endregion
}
