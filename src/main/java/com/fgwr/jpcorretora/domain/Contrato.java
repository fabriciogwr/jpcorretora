package com.fgwr.jpcorretora.domain;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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

    @OneToOne
    private Imovel imovel;

    private Integer qtdParcelas;
    private Double valorDeCadaParcela;
    
    @OneToMany(mappedBy ="contrato", cascade = CascadeType.ALL)
    private List<Duplicata> duplicatas;
    
    public Contrato () {
    	
    }

	public Contrato(Integer id, Date data, Integer qtdParcelas, Double valorDeCadaParcela ) {
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

	public Integer getQtdParcelas() {
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

	public List<Duplicata> getDuplicatas() {
		return duplicatas;
	}

	public void setDuplicatas(List<Duplicata> duplicatas) {
		this.duplicatas = duplicatas;
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

	
	
	public StringProperty cliente() {
		StringProperty cliente = new SimpleStringProperty(this.cliente.getNome());
		return cliente;
	}
	
	public StringProperty proprietario() {
		StringProperty proprietario = new SimpleStringProperty(this.getImovel().getProprietario().getNome());
		return proprietario;
	}
	
	public StringProperty num() {
		StringProperty num = new SimpleStringProperty(this.id.toString());
		return num;
	}
	
	public StringProperty imovel() {
		StringProperty imovel = new SimpleStringProperty(this.imovel.getId().toString());
		return imovel;
	
	}
	
	public StringProperty vigencia() {
		StringProperty vigencia = new SimpleStringProperty(this.qtdParcelas.toString());
		return vigencia;
	
	}
	
	public StringProperty mensalidade() {
		NumberFormat real = NumberFormat.getNumberInstance();
		real.setMinimumFractionDigits(2);
		real.setMaximumFractionDigits(2);
		StringProperty mensalidade = new SimpleStringProperty("R$ " + real.format(this.valorDeCadaParcela));
		return mensalidade;
	}
	
	public StringProperty dataInicio() {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		StringProperty data = new SimpleStringProperty(df.format(this.data));
		return data;
	}
	
	public StringProperty dataFim() {
		Calendar gCal = new GregorianCalendar();
		gCal.setTime(this.data);
		gCal.add(Calendar.MONTH, qtdParcelas-1);
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		StringProperty data = new SimpleStringProperty(df.format(gCal.getTime()));
		return data;
	}
}
