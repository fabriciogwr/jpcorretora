package com.fgwr.jpcorretora.domain;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fgwr.jpcorretora.enums.EstadoPagamento;
import com.fgwr.jpcorretora.enums.MeioPagamento;

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
	
	@ManyToOne
	@JoinColumn(name="cliente_id")
	private Cliente cliente;
	
	private Integer parcela;
	
	@Temporal(TemporalType.DATE)
	private Date dataVencimento;
	
	@Temporal(TemporalType.DATE)
	private Date dataPagamento;
	
	private Double valor;
	
	private Double valorPago;
	
	private Integer estado;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Recibo recibo;
	
	private Integer meioPagamento;
	
	@OneToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Receita receita;
	

	public Duplicata () {
		
	}


	public Duplicata(Integer id, Integer parcela, Date dataVencimento, Double valor, EstadoPagamento estado, Date dataPagamento, MeioPagamento meioPagamento) {
		super();
		this.id = id;
		this.parcela = parcela;
		this.dataVencimento = dataVencimento;
		this.valor = valor;
		this.estado = estado.getCod();
		this.dataPagamento = dataPagamento;
	}


	public MeioPagamento getMeioPagamento() {
        return MeioPagamento.toEnum(meioPagamento);
    }

    public void setMeioPagamento(MeioPagamento meioPagamento) {
        this.meioPagamento = meioPagamento.getCod();
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


	public Date getDataPagamento() {
		return dataPagamento;
	}


	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}


	public Double getValor() {
		return valor;
	}


	public void setValor(Double valor) {
		this.valor = valor;
	}


	public Receita getReceita() {
		return receita;
	}


	public void setReceita(Receita receita) {
		this.receita = receita;
	}


	public EstadoPagamento getEstado() {
		return EstadoPagamento.toEnum(estado);
	}


	public void setEstado(EstadoPagamento estado) {
		this.estado = estado.getCod();
	}


	public Recibo getRecibo() {
		return recibo;
	}


	public void setRecibo(Recibo recibo) {
		this.recibo = recibo;
	}
	
	public String getDataVencimentoForm() {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String vencimento = (df.format(dataVencimento));
		return vencimento;
	}


	public Cliente getCliente() {
		return cliente;
	}


	public void setCliente(Cliente cliente) {
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
		Duplicata other = (Duplicata) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public StringProperty cliente() {
		StringProperty cliente = new SimpleStringProperty(this.contrato.getCliente().getNome());
		return cliente;
	}
	
	public StringProperty contrato() {
		StringProperty contrato = new SimpleStringProperty(this.contrato.getId().toString());
		return contrato;
	}
	
	public StringProperty parcela() {		
		StringProperty parc = (parcela.toString().length()==1) ? new SimpleStringProperty("0" + this.parcela.toString()) : new SimpleStringProperty(this.parcela.toString());
		return parc;
	}
	
	public StringProperty vencimento() {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		StringProperty vencimento = new SimpleStringProperty(df.format(dataVencimento));
		return vencimento;
	}
	
	public StringProperty valor() {
		NumberFormat real = NumberFormat.getNumberInstance();
		real.setMinimumFractionDigits(2);
		real.setMaximumFractionDigits(2);
		StringProperty valor = new SimpleStringProperty("R$ " + real.format(this.valor));
		return valor;
	}
	
	public StringProperty estado() {
		StringProperty estado = new SimpleStringProperty(this.getEstado().getDescricao());
		return estado;
	}
	
	public StringProperty meioPgto() {
		StringProperty meioPgto = new SimpleStringProperty();
		if (getMeioPagamento() != null ) {
		meioPgto = new SimpleStringProperty(this.getMeioPagamento().getDescricao());
		} else {
			meioPgto = new SimpleStringProperty("");
		}
		return meioPgto;
		
	}
	
	public StringProperty dataPgto() {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		StringProperty dataPgto = new SimpleStringProperty("");
		if (getDataPagamento() != null ) {
		dataPgto = new SimpleStringProperty(df.format(getDataPagamento())); 
		
	} else {
		dataPgto = new SimpleStringProperty("");
	}
		
		return dataPgto;
	}


	public Double getValorPago() {
		return valorPago;
	}


	public void setValorPago(Double valorPago) {
		this.valorPago = valorPago;
	}
}
