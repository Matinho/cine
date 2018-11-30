package ar.edu.um.programacion2.cine.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * A Pelicula.
 */
@Entity
@Table(name = "pelicula")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Pelicula implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "titulo", length = 100, nullable = false)
    private String titulo;

    @NotNull
    @Column(name = "estreno", nullable = false)
    private ZonedDateTime estreno;

    @NotNull
    @Column(name = "created", nullable = false)
    private ZonedDateTime created;

    @NotNull
    @Column(name = "updated", nullable = false)
    private ZonedDateTime updated;

    @OneToMany(mappedBy = "pelicula")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Funcion> funcions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("peliculas")
    private Calificacion calificacion;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Pelicula titulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public ZonedDateTime getEstreno() {
        return estreno;
    }

    public Pelicula estreno(ZonedDateTime estreno) {
        this.estreno = estreno;
        return this;
    }

    public void setEstreno(ZonedDateTime estreno) {
        this.estreno = estreno;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public Pelicula created(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public Pelicula updated(ZonedDateTime updated) {
        this.updated = updated;
        return this;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    public Set<Funcion> getFuncions() {
        return funcions;
    }

    public Pelicula funcions(Set<Funcion> funcions) {
        this.funcions = funcions;
        return this;
    }

    public Pelicula addFuncion(Funcion funcion) {
        this.funcions.add(funcion);
        funcion.setPelicula(this);
        return this;
    }

    public Pelicula removeFuncion(Funcion funcion) {
        this.funcions.remove(funcion);
        funcion.setPelicula(null);
        return this;
    }

    public void setFuncions(Set<Funcion> funcions) {
        this.funcions = funcions;
    }

    public Calificacion getCalificacion() {
        return calificacion;
    }

    public Pelicula calificacion(Calificacion calificacion) {
        this.calificacion = calificacion;
        return this;
    }

    public void setCalificacion(Calificacion calificacion) {
        this.calificacion = calificacion;
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
        Pelicula pelicula = (Pelicula) o;
        if (pelicula.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pelicula.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Pelicula{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", estreno='" + getEstreno() + "'" +
            ", created='" + getCreated() + "'" +
            ", updated='" + getUpdated() + "'" +
            "}";
    }
}
