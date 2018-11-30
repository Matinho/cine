package ar.edu.um.programacion2.cine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Ocupacion.
 */
@Entity
@Table(name = "ocupacion")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Ocupacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "valor", precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;

    @NotNull
    @Column(name = "created", nullable = false)
    private ZonedDateTime created;

    @NotNull
    @Column(name = "updated", nullable = false)
    private ZonedDateTime updated;

    @ManyToOne
    @JsonIgnoreProperties("ocupacions")
    private Ticket ticket;

    @ManyToOne
    @JsonIgnoreProperties("ocupacions")
    private Entrada entrada;

    @ManyToOne
    @JsonIgnoreProperties("ocupacions")
    private Butaca butaca;

    @ManyToOne
    @JsonIgnoreProperties("ocupacions")
    private Funcion funcion;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Ocupacion valor(BigDecimal valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public Ocupacion created(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public Ocupacion updated(ZonedDateTime updated) {
        this.updated = updated;
        return this;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public Ocupacion ticket(Ticket ticket) {
        this.ticket = ticket;
        return this;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Entrada getEntrada() {
        return entrada;
    }

    public Ocupacion entrada(Entrada entrada) {
        this.entrada = entrada;
        return this;
    }

    public void setEntrada(Entrada entrada) {
        this.entrada = entrada;
    }

    public Butaca getButaca() {
        return butaca;
    }

    public Ocupacion butaca(Butaca butaca) {
        this.butaca = butaca;
        return this;
    }

    public void setButaca(Butaca butaca) {
        this.butaca = butaca;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public Ocupacion funcion(Funcion funcion) {
        this.funcion = funcion;
        return this;
    }

    public void setFuncion(Funcion funcion) {
        this.funcion = funcion;
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
        Ocupacion ocupacion = (Ocupacion) o;
        if (ocupacion.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ocupacion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Ocupacion{" +
            "id=" + getId() +
            ", valor=" + getValor() +
            ", created='" + getCreated() + "'" +
            ", updated='" + getUpdated() + "'" +
            "}";
    }
}
