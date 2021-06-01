package com.fgwr.jpcorretora.domain;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fgwr.jpcorretora.enums.EstadoPagamento;
import com.fgwr.jpcorretora.enums.MeioPagamento;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
public class Despesa implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	private String recebedor;
	private String descricao;
	
	@ManyToOne
	private Categoria categoria;
	
	@Temporal(TemporalType.DATE)
	private Date dataVencimento;
	
	@Temporal(TemporalType.DATE)
	private Date dataPagamento;
	
	private Double valor;
	
	private Double valorPago;
	
	private Integer estado;
	
	private Integer meioPagamento;
	

	public Despesa () {
		
	}


	public Despesa(Integer id, String descricao, Date dataVencimento, Double valor, EstadoPagamento estado, Date dataPagamento, MeioPagamento meioPagamento) {
		super();
		this.id = id;
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


	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
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
	
	public String getDataVencimentoForm() {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String vencimento = (df.format(dataVencimento));
		return vencimento;
	}



	public String getRecebedor() {
		return recebedor;
	}


	public void setRecebedor(String recebedor) {
		this.recebedor = recebedor;
	}


	public Categoria getCategoria() {
		return categoria;
	}


	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
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
		Despesa other = (Despesa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public StringProperty recebedor() {
		StringProperty recebedor = new SimpleStringProperty(this.recebedor);
		return recebedor;
	}
	
	public StringProperty categoria() {
		StringProperty categoria = new SimpleStringProperty(this.categoria.getNome());
		return categoria;
	}
	
	public StringProperty descricao() {		
		StringProperty descricao = new SimpleStringProperty(this.descricao);
		return descricao;
	}
	
	public StringProperty vencimento() {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		StringProperty vencimento = new SimpleStringProperty(df.format(dataVencimento));
		return vencimento;
	}
	
	public StringProperty pagamento() {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		StringProperty pagamento = (this.dataPagamento == null) ? new SimpleStringProperty("") : new SimpleStringProperty(df.format(dataPagamento));
		return pagamento;
	}
	
	public StringProperty valor() {
		NumberFormat real = NumberFormat.getNumberInstance();
		real.setMinimumFractionDigits(2);
		real.setMaximumFractionDigits(2);
		StringProperty valor = (this.estado == EstadoPagamento.QUITADO.getCod()) ? new SimpleStringProperty("R$ " + real.format(this.valorPago)) : new SimpleStringProperty("R$ " + real.format(this.valor));
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
