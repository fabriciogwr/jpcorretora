package com.fgwr.jpcorretora.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Referencia implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
 
    private Integer id;
    
    private String nome;
    
    @ManyToMany
    @JoinTable(name="CLIENTE_REFERENCIA", joinColumns = @JoinColumn(name="referencia_id"), inverseJoinColumns = @JoinColumn(name="cliente_id"))
    private List<Cliente> cliente = new ArrayList<>() ;

    @ElementCollection
    @CollectionTable(name = "TELEFONE_REF")
    private Set<String> telefones = new HashSet<>();


    public Referencia() {
    	
    }
    
    public Referencia(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<String> getTelefones() {
        return telefones;
    }

    public void setTelefones(Set<String> telefones) {
        this.telefones = telefones;
    }

    public List <Cliente> getCliente() {
		return cliente;
	}


	public void setCliente(List <Cliente> cliente) {
		this.cliente = cliente;
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
		Referencia other = (Referencia) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	
    
}
