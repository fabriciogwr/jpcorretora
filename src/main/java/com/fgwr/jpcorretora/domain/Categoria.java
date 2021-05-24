package com.fgwr.jpcorretora.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fgwr.jpcorretora.enums.TipoCliente;
import com.fgwr.jpcorretora.enums.TipoMovimento;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
public class Categoria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer tipo;
	private String nome;
	private String descricao;

	public Categoria() {

	}

	public Categoria(Integer id, TipoMovimento tipo, String nome, String descricao) {
		this.id = id;
		this.tipo = tipo.getCod();
		this.nome = nome;
		this.descricao = descricao;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TipoMovimento getTipo() {
		return TipoMovimento.toEnum(tipo);
	}

	public void setTipo(TipoMovimento tipo) {
		this.tipo = tipo.getCod();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Categoria other = (Categoria) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public StringProperty cod() {
		StringProperty cod = new SimpleStringProperty(this.id.toString());
		return cod;
	}

	public StringProperty nome() {
		StringProperty nome = new SimpleStringProperty(this.nome);
		return nome;
	}

	public StringProperty descricao() {
		StringProperty descricao = new SimpleStringProperty(this.descricao);
		return descricao;
	}

}
