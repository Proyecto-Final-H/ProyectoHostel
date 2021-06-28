package domainapp.dom.caja;
import java.util.ArrayList;
import java.util.List;
import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.eventbus.PropertyDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.util.ObjectContracts;
import org.apache.isis.applib.value.Blob;
import org.apache.isis.applib.annotation.MemberOrder;
import domainapp.dom.huesped.Huesped;
import domainapp.dom.reportes.GenerarReporte;
import domainapp.dom.reportes.TicketReporte;
import domainapp.dom.reserva.Reserva;
import domainapp.dom.caja.RepoCaja;
import net.sf.jasperreports.engine.JRException;
import org.joda.time.LocalDate;
@javax.jdo.annotations.PersistenceCapable(
        identityType=IdentityType.DATASTORE,
        schema = "simple",
        table = "Caja"
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
                        + "FROM domainapp.dom.Caja "),
        @javax.jdo.annotations.Query(
                name = "findByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.Caja "
                        + "WHERE name.indexOf(:name) >= 0 ")
})
public class Caja implements Comparable<Caja> {

    public static final int NAME_LENGTH = 40;
    public String title() {
        return "Pagos";
    }
    private Reserva reserva;
    @javax.jdo.annotations.Column(allowsNull="false")
    public Reserva getReserva() {
        return reserva;
    }
    @javax.jdo.annotations.Column(allowsNull="false")
    public void setReserva(final Reserva reserva) {
        this.reserva = reserva;
    }
    private Double monto;
    @javax.jdo.annotations.Column(allowsNull="false")
    public Double getMonto() {
        return monto;
    }
    public void setMonto(final Double monto) {
        this.monto = monto;
    }
    private String concepto;
    @javax.jdo.annotations.Column(allowsNull="false")
    public String getConcepto() {
        return concepto;
    }
    public void setConcepto(final String concepto) {
        this.concepto = concepto;
    }
    @MemberOrder(sequence = "2")
	@Column(allowsNull="false")
	@PropertyLayout(named="Fecha de Pago")
	private LocalDate fechaDePago;
	public LocalDate getFechaDePago() {return fechaDePago;}
	public void setFechaDePago(LocalDate fechaDePago) {this.fechaDePago = fechaDePago;}
    @ActionLayout(named="Eliminar Ingreso")
    public void delete() {
        repositoryService.remove(this);
    }
public String imprimirTicket() throws JRException{
		List<Object> objectsReport = new ArrayList<Object>();
		
		TicketReporte caja = new TicketReporte();
		caja.setFechaDePago(String.valueOf(getFechaDePago()));
		objectsReport.add(caja);
		String nombreArchivo ="reportes/Caja_"/* + String.valueOf(caja.getNroRecibo())*/ ;
		GenerarReporte.generarReporte("reportes/Ticketdepago.jrxml", objectsReport, nombreArchivo);
    @javax.inject.Inject
    RepositoryService repositoryService;
    @javax.inject.Inject	
	RepoCaja repoCaja;

}
