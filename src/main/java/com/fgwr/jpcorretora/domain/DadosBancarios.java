package com.fgwr.jpcorretora.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fgwr.jpcorretora.enums.Banco;
import com.fgwr.jpcorretora.enums.TipoConta;

@Entity
public class DadosBancarios implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Cliente cliente;
    
    @ManyToOne
    private Proprietario proprietario;

    private Integer banco;
    private String agencia;
    private String conta;
    private Integer tipo;
    private String titular;
    
    public DadosBancarios() {
    	
    }

    public DadosBancarios(Integer id, Banco banco, String agencia, String conta, TipoConta tipo, String titular) {
        this.id = id;
        this.banco = banco.getCod();
        this.agencia = agencia;
        this.conta = conta;
        this.tipo = tipo.getCod();
        this.titular = titular;
    }

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Banco getBanco() {
        return Banco.toEnum(banco);
    }

    public void setBanco(Banco banco) {
        this.banco = banco.getCod();
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public TipoConta getTipo() {
        return TipoConta.toEnum(tipo);
    }

    public void setTipo(TipoConta tipo) {
        this.tipo = tipo.getCod();
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }


    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

	public Proprietario getProprietario() {
		return proprietario;
	}

	public void setProprietario(Proprietario proprietario) {
		this.proprietario = proprietario;
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
		DadosBancarios other = (DadosBancarios) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

    
}
