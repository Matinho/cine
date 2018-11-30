package ar.edu.um.programacion2.cine.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Ticket.
 */
@Entity
@Table(name = "ticket")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "fecha_transaccion", nullable = false)
    private LocalDate fechaTransaccion;

    @NotNull
    @Min(value = 1)
    @Column(name = "butacas", nullable = false)
    private Integer butacas;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "importe", precision = 10, scale = 2, nullable = false)
    private BigDecimal importe;

    @NotNull
    @Size(min = 36, max = 36)
    @Column(name = "pago_uuid", length = 36, nullable = false)
    private String pagoUuid;

    @NotNull
    @Column(name = "created", nullable = false)
    private ZonedDateTime created;

    @NotNull
    @Column(name = "updated", nullable = false)
    private ZonedDateTime updated;

    @OneToMany(mappedBy = "ticket")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Ocupacion> ocupacions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("tickets")
    private Cliente cliente;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaTransaccion() {
        return fechaTransaccion;
    }

    public Ticket fechaTransaccion(LocalDate fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
        return this;
    }

    public void setFechaTransaccion(LocalDate fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public Integer getButacas() {
        return butacas;
    }

    public Ticket butacas(Integer butacas) {
        this.butacas = butacas;
        return this;
    }

    public void setButacas(Integer butacas) {
        this.butacas = butacas;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public Ticket importe(BigDecimal importe) {
        this.importe = importe;
        return this;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public String getPagoUuid() {
        return pagoUuid;
    }

    public Ticket pagoUuid(String pagoUuid) {
        this.pagoUuid = pagoUuid;
        return this;
    }

    public void setPagoUuid(String pagoUuid) {
        this.pagoUuid = pagoUuid;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public Ticket created(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public Ticket updated(ZonedDateTime updated) {
        this.updated = updated;
        return this;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    public Set<Ocupacion> getOcupacions() {
        return ocupacions;
    }

    public Ticket ocupacions(Set<Ocupacion> ocupacions) {
        this.ocupacions = ocupacions;
        return this;
    }

    public Ticket addOcupacion(Ocupacion ocupacion) {
        this.ocupacions.add(ocupacion);
        ocupacion.setTicket(this);
        return this;
    }

    public Ticket removeOcupacion(Ocupacion ocupacion) {
        this.ocupacions.remove(ocupacion);
        ocupacion.setTicket(null);
        return this;
    }

    public void setOcupacions(Set<Ocupacion> ocupacions) {
        this.ocupacions = ocupacions;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Ticket cliente(Cliente cliente) {
        this.cliente = cliente;
        return this;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ticket ticket = (Ticket) o;
        if (ticket.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ticket.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Ticket{" +
            "id=" + getId() +
            ", fechaTransaccion='" + getFechaTransaccion() + "'" +
            ", butacas=" + getButacas() +
            ", importe=" + getImporte() +
            ", pagoUuid='" + getPagoUuid() + "'" +
            ", created='" + getCreated() + "'" +
            ", updated='" + getUpdated() + "'" +
            "}";
    }
}
