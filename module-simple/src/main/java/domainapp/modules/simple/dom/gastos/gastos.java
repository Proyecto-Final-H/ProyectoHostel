package domainapp.dom.gastos;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.eventbus.PropertyDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.util.ObjectContracts;

//import domainapp.dom.huesped.Huesped;
//import domainapp.dom.reserva.Reserva;

@javax.jdo.annotations.PersistenceCapable(
        identityType=IdentityType.DATASTORE,
        schema = "simple",
        table = "Gastos"
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
                        + "FROM domainapp.dom.Gastos "),
        @javax.jdo.annotations.Query(
                name = "findByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.Gastos "
                        + "WHERE name.indexOf(:name) >= 0 ")
})
@javax.jdo.annotations.Unique(name="Gastos_name_UNQ", members = {"name"})
@DomainObject
public class gastos implements Comparable<gastos> {

    public static final int NAME_LENGTH = 40;
    public TranslatableString title() {
        return TranslatableString.tr("Gastos: {name}", "name", getName());
    }
    public static class NameDomainEvent extends PropertyDomainEvent<gastos,String> {}
    @javax.jdo.annotations.Column(
            allowsNull="false",
            length = NAME_LENGTH
    )
    @Property(
        domainEvent = NameDomainEvent.class
    )
    private String name;
    public String getName() {
        return name;
    }
    public void setName(final String name) {
        this.name = name;
    }
    public TranslatableString validateName(final String name) {
        return name != null && name.contains("!")? TranslatableString.tr("Exclamation mark is not allowed"): null;
    }
    
    private String proveedor;
    @javax.jdo.annotations.Column(allowsNull="false")
    public String getProveedor() {
    	return proveedor;
    }
    public void setProveedor(final String proveedor) {
    	this.proveedor = proveedor;
    }
    private Double monto;
    @javax.jdo.annotations.Column(allowsNull="false")
    public Double getMonto() {
        return monto;
    }
    public void setMonto(final Double monto) {
        this.monto = monto;
    }
    
    private String categoria;
    @javax.jdo.annotations.Column(allowsNull="false")
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(final String categoria) {
        this.categoria = categoria;
    }


    public static class DeleteDomainEvent extends ActionDomainEvent<gastos> {}
    @Action(
            domainEvent = DeleteDomainEvent.class,
            semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE
    )
    public void delete() {
        repositoryService.remove(this);
    }
    @Override
    public int compareTo(final gastos other) {
        return ObjectContracts.compare(this, other, "name");
    }
    @javax.inject.Inject
    RepositoryService repositoryService;
}
