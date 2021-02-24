package com.fgwr.jpcorretora.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Contrato implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.DATE)
    private Date data = new Date();

    @OneToOne
    private Cliente cliente;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="imovel_id")
    private Imovel imovel;

    private int qtdParcelas;
    private Double valorDeCadaParcela;
    
    @OneToMany(mappedBy ="contrato")
    private List<Duplicata> duplicatas;
    
    public Contrato () {
    	
    }

	public Contrato(Integer id, Date data, int qtdParcelas, Double valorDeCadaParcela ) {
		super();
		this.id = id;
		this.data = data;
		this.qtdParcelas = qtdParcelas;
		this.valorDeCadaParcela = valorDeCadaParcela;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Imovel getImovel() {
		return imovel;
	}

	public void setImovel(Imovel imovel) {
		this.imovel = imovel;
	}

	public int getQtdParcelas() {
		return qtdParcelas;
	}

	public void setQtdParcelas(int qtdParcelas) {
		this.qtdParcelas = qtdParcelas;
	}

	public Double getValorDeCadaParcela() {
		return valorDeCadaParcela;
	}

	public void setValorDeCadaParcela(Double valorDeCadaParcela) {
		this.valorDeCadaParcela = valorDeCadaParcela;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Contrato other = (Contrato) obj;
		if (id != other.id)
			return false;
		return true;
	}    

}
