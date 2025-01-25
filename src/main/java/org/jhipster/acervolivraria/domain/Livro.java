package org.jhipster.acervolivraria.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jhipster.acervolivraria.domain.enumeration.Genero;

/**
 * A Livro.
 */
@Entity
@Table(name = "livro")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Livro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero")
    private Genero genero;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "livro")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "posicao", "livro" }, allowSetters = true)
    private Set<Edicao> edicaos = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_livro__autor", joinColumns = @JoinColumn(name = "livro_id"), inverseJoinColumns = @JoinColumn(name = "autor_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "livros" }, allowSetters = true)
    private Set<Autor> autors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Livro id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Livro titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Genero getGenero() {
        return this.genero;
    }

    public Livro genero(Genero genero) {
        this.setGenero(genero);
        return this;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public Set<Edicao> getEdicaos() {
        return this.edicaos;
    }

    public void setEdicaos(Set<Edicao> edicaos) {
        if (this.edicaos != null) {
            this.edicaos.forEach(i -> i.setLivro(null));
        }
        if (edicaos != null) {
            edicaos.forEach(i -> i.setLivro(this));
        }
        this.edicaos = edicaos;
    }

    public Livro edicaos(Set<Edicao> edicaos) {
        this.setEdicaos(edicaos);
        return this;
    }

    public Livro addEdicao(Edicao edicao) {
        this.edicaos.add(edicao);
        edicao.setLivro(this);
        return this;
    }

    public Livro removeEdicao(Edicao edicao) {
        this.edicaos.remove(edicao);
        edicao.setLivro(null);
        return this;
    }

    public Set<Autor> getAutors() {
        return this.autors;
    }

    public void setAutors(Set<Autor> autors) {
        this.autors = autors;
    }

    public Livro autors(Set<Autor> autors) {
        this.setAutors(autors);
        return this;
    }

    public Livro addAutor(Autor autor) {
        this.autors.add(autor);
        return this;
    }

    public Livro removeAutor(Autor autor) {
        this.autors.remove(autor);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Livro)) {
            return false;
        }
        return getId() != null && getId().equals(((Livro) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Livro{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", genero='" + getGenero() + "'" +
            "}";
    }
}
