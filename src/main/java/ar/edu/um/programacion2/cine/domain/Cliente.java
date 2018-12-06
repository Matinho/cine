package ar.edu.um.programacion2.cine.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Cliente.
 */
@Entity
@Table(name = "cliente")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(value = 0)
    @Max(value = 99999999)
    @Column(name = "documento", nullable = false)
    private Integer documento;

    @NotNull
    @Size(min = 1, max = 42)
    @Column(name = "apellido", length = 42, nullable = false)
    private String apellido;

    @NotNull
    @Size(min = 1, max = 42)
    @Column(name = "nombre", length = 42, nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "created", nullable = false)
    private ZonedDateTime created;

    @NotNull
    @Column(name = "updated", nullable = false)
    private ZonedDateTime updated;

    @OneToMany(mappedBy = "cliente")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Ticket> tickets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDocumento() {
        return documento;
    }

    public Cliente documento(Integer documento) {
        this.documento = documento;
        return this;
    }

    public void setDocumento(Integer documento) {
        this.documento = documento;
    }

    public String getApellido() {
        return apellido;
    }

    public Cliente apellido(String apellido) {
        this.apellido = apellido;
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public Cliente nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public Cliente created(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public Cliente updated(ZonedDateTime updated) {
        this.updated = updated;
        return this;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public Cliente tickets(Set<Ticket> tickets) {
        this.tickets = tickets;
        return this;
    }

    public Cliente addTicket(Ticket ticket) {
        this.tickets.add(ticket);
        ticket.setCliente(this);
        return this;
    }

    public Cliente removeTicket(Ticket ticket) {
        this.tickets.remove(ticket);
        ticket.setCliente(null);
        return this;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
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
        Cliente cliente = (Cliente) o;
        if (cliente.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cliente.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Cliente{" +
            "id=" + getId() +
            ", documento=" + getDocumento() +
            ", apellido='" + getApellido() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", created='" + getCreated() + "'" +
            ", updated='" + getUpdated() + "'" +
            "}";
    }
}
