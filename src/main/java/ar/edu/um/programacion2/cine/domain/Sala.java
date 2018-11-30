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
 * A Sala.
 */
@Entity
@Table(name = "sala")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Sala implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 10, max = 150)
    @Column(name = "descripcion", length = 150, nullable = false)
    private String descripcion;

    @NotNull
    @Min(value = 0)
    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @NotNull
    @Column(name = "created", nullable = false)
    private ZonedDateTime created;

    @NotNull
    @Column(name = "updated", nullable = false)
    private ZonedDateTime updated;

    @OneToMany(mappedBy = "sala")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Funcion> funcions = new HashSet<>();

    @OneToMany(mappedBy = "sala")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Butaca> butacas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Sala descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public Sala capacidad(Integer capacidad) {
        this.capacidad = capacidad;
        return this;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public Sala created(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public Sala updated(ZonedDateTime updated) {
        this.updated = updated;
        return this;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    public Set<Funcion> getFuncions() {
        return funcions;
    }

    public Sala funcions(Set<Funcion> funcions) {
        this.funcions = funcions;
        return this;
    }

    public Sala addFuncion(Funcion funcion) {
        this.funcions.add(funcion);
        funcion.setSala(this);
        return this;
    }

    public Sala removeFuncion(Funcion funcion) {
        this.funcions.remove(funcion);
        funcion.setSala(null);
        return this;
    }

    public void setFuncions(Set<Funcion> funcions) {
        this.funcions = funcions;
    }

    public Set<Butaca> getButacas() {
        return butacas;
    }

    public Sala butacas(Set<Butaca> butacas) {
        this.butacas = butacas;
        return this;
    }

    public Sala addButaca(Butaca butaca) {
        this.butacas.add(butaca);
        butaca.setSala(this);
        return this;
    }

    public Sala removeButaca(Butaca butaca) {
        this.butacas.remove(butaca);
        butaca.setSala(null);
        return this;
    }

    public void setButacas(Set<Butaca> butacas) {
        this.butacas = butacas;
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
        Sala sala = (Sala) o;
        if (sala.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sala.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Sala{" +
            "id=" + getId() +
            ", descripcion='" + getDescripcion() + "'" +
            ", capacidad=" + getCapacidad() +
            ", created='" + getCreated() + "'" +
            ", updated='" + getUpdated() + "'" +
            "}";
    }
}
