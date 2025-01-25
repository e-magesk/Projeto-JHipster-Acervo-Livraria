package org.jhipster.acervolivraria.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jhipster.acervolivraria.domain.enumeration.Nacionalidade;

/**
 * A Autor.
 */
@Entity
@Table(name = "autor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Autor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "nacionalidade")
    private Nacionalidade nacionalidade;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "autors")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "edicaos", "autors" }, allowSetters = true)
    private Set<Livro> livros = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Autor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Autor nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Nacionalidade getNacionalidade() {
        return this.nacionalidade;
    }

    public Autor nacionalidade(Nacionalidade nacionalidade) {
        this.setNacionalidade(nacionalidade);
        return this;
    }

    public void setNacionalidade(Nacionalidade nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public Set<Livro> getLivros() {
        return this.livros;
    }

    public void setLivros(Set<Livro> livros) {
        if (this.livros != null) {
            this.livros.forEach(i -> i.removeAutor(this));
        }
        if (livros != null) {
            livros.forEach(i -> i.addAutor(this));
        }
        this.livros = livros;
    }

    public Autor livros(Set<Livro> livros) {
        this.setLivros(livros);
        return this;
    }

    public Autor addLivro(Livro livro) {
        this.livros.add(livro);
        livro.getAutors().add(this);
        return this;
    }

    public Autor removeLivro(Livro livro) {
        this.livros.remove(livro);
        livro.getAutors().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Autor)) {
            return false;
        }
        return getId() != null && getId().equals(((Autor) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Autor{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", nacionalidade='" + getNacionalidade() + "'" +
            "}";
    }
}
