package com.fgwr.jpcorretora.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fgwr.jpcorretora.enums.EstadoPagamento;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
public class Duplicata implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	@ManyToOne
	@JoinColumn(name="contrato_id")
	private Contrato contrato;
	
	private Integer parcela;
	
	@Temporal(TemporalType.DATE)
	private Date dataVencimento;
	
	private Double valor;
	
	private Integer estado;
	
	@OneToOne(mappedBy="duplicata")
	@JoinColumn(name="recibo_id")
	private Recibo recibo;
	

	public Duplicata () {
		
	}


	public Duplicata(Integer id, Integer parcela, Date dataVencimento, Double valor, EstadoPagamento estado) {
		super();
		this.id = id;
		this.parcela = parcela;
		this.dataVencimento = dataVencimento;
		this.valor = valor;
		this.estado = estado.getCod();
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Contrato getContrato() {
		return contrato;
	}


	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}


	public Integer getParcela() {
		return parcela;
	}


	public void setParcela(Integer parcela) {
		this.parcela = parcela;
	}


	public Date getDataVencimento() {
		return dataVencimento;
	}


	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}


	public Double getValor() {
		return valor;
	}


	public void setValor(Double valor) {
		this.valor = valor;
	}


	public EstadoPagamento getEstado() {
		return EstadoPagamento.toEnum(estado);
	}


	public void setEstado(EstadoPagamento estado) {
		this.estado = estado.getCod();
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
		Duplicata other = (Duplicata) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public StringProperty contrato() {
		StringProperty contrato = new SimpleStringProperty(this.contrato.getId().toString());
		return contrato;
	}
	
	public StringProperty parcela() {
		StringProperty nome = new SimpleStringProperty(this.parcela.toString());
		return nome;
	}
	
	public StringProperty vencimento() {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		StringProperty vencimento = new SimpleStringProperty(df.format(dataVencimento));
		return vencimento;
	}
	
	public StringProperty valor() {
		StringProperty valor = new SimpleStringProperty("R$ "+ this.valor.toString());
		return valor;
	}
	
	public StringProperty estado() {
		StringProperty estado = new SimpleStringProperty(this.getEstado().getDescricao());
		return estado;
	}
	
	public StringProperty dataPgto() {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		StringProperty dataPgto = new SimpleStringProperty(df.format(recibo.getDataPagamento()));
		return dataPgto;
	}
		
}
