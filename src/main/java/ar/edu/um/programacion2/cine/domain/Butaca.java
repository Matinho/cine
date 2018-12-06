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
 * A Butaca.
 */
@Entity
@Table(name = "butaca")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Butaca implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "fila", length = 1, nullable = false)
    private String fila;

    @NotNull
    @Min(value = 1)
    @Column(name = "numero", nullable = false)
    private Integer numero;

    @NotNull
    @Size(min = 10, max = 150)
    @Column(name = "descripcion", length = 150, nullable = false)
    private String descripcion;

    @NotNull
    @Column(name = "created", nullable = false)
    private ZonedDateTime created;

    @NotNull
    @Column(name = "updated", nullable = false)
    private ZonedDateTime updated;

    @OneToMany(mappedBy = "butaca")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Ocupacion> ocupacions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("butacas")
    private Sala sala;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFila() {
        return fila;
    }

    public Butaca fila(String fila) {
        this.fila = fila;
        return this;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

    public Integer getNumero() {
        return numero;
    }

    public Butaca numero(Integer numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Butaca descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public Butaca created(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public Butaca updated(ZonedDateTime updated) {
        this.updated = updated;
        return this;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    public Set<Ocupacion> getOcupacions() {
        return ocupacions;
    }

    public Butaca ocupacions(Set<Ocupacion> ocupacions) {
        this.ocupacions = ocupacions;
        return this;
    }

    public Butaca addOcupacion(Ocupacion ocupacion) {
        this.ocupacions.add(ocupacion);
        ocupacion.setButaca(this);
        return this;
    }

    public Butaca removeOcupacion(Ocupacion ocupacion) {
        this.ocupacions.remove(ocupacion);
        ocupacion.setButaca(null);
        return this;
    }

    public void setOcupacions(Set<Ocupacion> ocupacions) {
        this.ocupacions = ocupacions;
    }

    public Sala getSala() {
        return sala;
    }

    public Butaca sala(Sala sala) {
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
        Butaca butaca = (Butaca) o;
        if (butaca.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), butaca.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Butaca{" +
            "id=" + getId() +
            ", fila='" + getFila() + "'" +
            ", numero=" + getNumero() +
            ", descripcion='" + getDescripcion() + "'" +
            ", created='" + getCreated() + "'" +
            ", updated='" + getUpdated() + "'" +
            "}";
    }

    public void realizarPago(){

    }
}