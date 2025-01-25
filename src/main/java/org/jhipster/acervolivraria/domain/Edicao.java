package org.jhipster.acervolivraria.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Edicao.
 */
@Entity
@Table(name = "edicao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Edicao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "editora", nullable = false)
    private String editora;

    @NotNull
    @Column(name = "data_lancamento", nullable = false)
    private LocalDate dataLancamento;

    @NotNull
    @Column(name = "quantidade_exemplares", nullable = false)
    private Integer quantidadeExemplares;

    @NotNull
    @Column(name = "preco", nullable = false)
    private Float preco;

    @JsonIgnoreProperties(value = { "edicao" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Posicao posicao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "edicaos", "autors" }, allowSetters = true)
    private Livro livro;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Edicao id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEditora() {
        return this.editora;
    }

    public Edicao editora(String editora) {
        this.setEditora(editora);
        return this;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public LocalDate getDataLancamento() {
        return this.dataLancamento;
    }

    public Edicao dataLancamento(LocalDate dataLancamento) {
        this.setDataLancamento(dataLancamento);
        return this;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public Integer getQuantidadeExemplares() {
        return this.quantidadeExemplares;
    }

    public Edicao quantidadeExemplares(Integer quantidadeExemplares) {
        this.setQuantidadeExemplares(quantidadeExemplares);
        return this;
    }

    public void setQuantidadeExemplares(Integer quantidadeExemplares) {
        this.quantidadeExemplares = quantidadeExemplares;
    }

    public Float getPreco() {
        return this.preco;
    }

    public Edicao preco(Float preco) {
        this.setPreco(preco);
        return this;
    }

    public void setPreco(Float preco) {
        this.preco = preco;
    }

    public Posicao getPosicao() {
        return this.posicao;
    }

    public void setPosicao(Posicao posicao) {
        this.posicao = posicao;
    }

    public Edicao posicao(Posicao posicao) {
        this.setPosicao(posicao);
        return this;
    }

    public Livro getLivro() {
        return this.livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Edicao livro(Livro livro) {
        this.setLivro(livro);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Edicao)) {
            return false;
        }
        return getId() != null && getId().equals(((Edicao) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Edicao{" +
            "id=" + getId() +
            ", editora='" + getEditora() + "'" +
            ", dataLancamento='" + getDataLancamento() + "'" +
            ", quantidadeExemplares=" + getQuantidadeExemplares() +
            ", preco=" + getPreco() +
            "}";
    }
}
