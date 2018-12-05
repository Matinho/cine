package ar.edu.um.programacion2.cine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Funcion.
 */
@Entity
@Table(name = "funcion")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Funcion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private ZonedDateTime fecha;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "valor", precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;

    @Column(name = "created", nullable = false)
    private ZonedDateTime created;

    @Column(name = "updated", nullable = false)
    private ZonedDateTime updated;

    @OneToMany(mappedBy = "funcion")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Ocupacion> ocupacions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("funcions")
    private Pelicula pelicula;

    @ManyToOne
    @JsonIgnoreProperties("funcions")
    private Sala sala;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getFecha() {
        return fecha;
    }

    public Funcion fecha(ZonedDateTime fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(ZonedDateTime fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Funcion valor(BigDecimal valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public Funcion created(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public Funcion updated(ZonedDateTime updated) {
        this.updated = updated;
        return this;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    public Set<Ocupacion> getOcupacions() {
        return ocupacions;
    }

    public Funcion ocupacions(Set<Ocupacion> ocupacions) {
        this.ocupacions = ocupacions;
        return this;
    }

    public Funcion addOcupacion(Ocupacion ocupacion) {
        this.ocupacions.add(ocupacion);
        ocupacion.setFuncion(this);
        return this;
    }

    public Funcion removeOcupacion(Ocupacion ocupacion) {
        this.ocupacions.remove(ocupacion);
        ocupacion.setFuncion(null);
        return this;
    }

    public void setOcupacions(Set<Ocupacion> ocupacions) {
        this.ocupacions = ocupacions;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public Funcion pelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
        return this;
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
    }

    public Sala getSala() {
        return sala;
    }

    public Funcion sala(Sala sala) {
        this.sala = sala;
        return this;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
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
        Funcion funcion = (Funcion) o;
        if (funcion.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), funcion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Funcion{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", valor=" + getValor() +
            ", created='" + getCreated() + "'" +
            ", updated='" + getUpdated() + "'" +
            "}";
    }
}
