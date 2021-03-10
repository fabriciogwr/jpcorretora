package com.fgwr.jpcorretora.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fgwr.jpcorretora.enums.EstadoCivil;
import com.fgwr.jpcorretora.enums.TipoCliente;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


@Entity
public class Proprietario implements Serializable {
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;
    private String email;
    private String cpfOuCnpj;
    private String rg;
    private Integer tipo;
    private Integer estadoCivil;
    private String profissao;
    
    @OneToMany(orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Imovel> imovel = new ArrayList<>();
    

    @OneToMany(mappedBy = "proprietario", cascade = CascadeType.ALL)
	private List<Endereco> enderecos = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TELEFONE_PROP")
    private Set<String> telefones = new HashSet<>();
    
    @OneToOne(mappedBy = "proprietario", cascade = CascadeType.ALL)
    private DadosBancarios dadosBancarios;

    private String obs;
    
    
	public Proprietario() {
    	
    }

	public Proprietario(Integer id, String nome, Date dataNascimento, String email, String cpfOuCnpj, String rg, String obs, TipoCliente tipo, EstadoCivil estadoCivil, String profissao) {
        super();
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.cpfOuCnpj = cpfOuCnpj;
        this.rg = rg;
        this.obs = obs;
        this.tipo = tipo.getCod();
        this.estadoCivil = estadoCivil.getCod();
		this.profissao = profissao;
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

    
    public Date getDataNascimento() {
		return dataNascimento;
	}

	public String getDataNascimentoString() {
    	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    	String dataNascimentoString = df.format(this.dataNascimento);
		return dataNascimentoString;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpfOuCnpj() {
        return cpfOuCnpj;
    }

    public void setCpfOuCnpj(String cpfOuCnpj) {
        this.cpfOuCnpj = cpfOuCnpj;
    }
    
    public String getRg() {
    	return rg;
    }
    
    public void setRg(String rg) {
    	this.rg = rg;
    }

    public List<Endereco> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
	}

    public Set<String> getTelefones() {
        return telefones;
    }

    public void setTelefones(Set<String> telefones) {
        this.telefones = telefones;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public TipoCliente getTipo() {
		return TipoCliente.toEnum(tipo);
	}

	public void setTipo(TipoCliente tipo) {
		this.tipo = tipo.getCod();
	}

	
	public EstadoCivil getEstadoCivil() {
		return EstadoCivil.toEnum(estadoCivil);
	}

	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil.getCod();
	}

	public String getProfissao() {
		return profissao;
	}

	public void setProfissao(String profissao) {
		this.profissao = profissao;
	}

	public List<Imovel> getImovel() {
		return imovel;
	}

	public void setImovel(List<Imovel> imovel) {
		this.imovel = imovel;
	}

	public DadosBancarios getDadosBancarios() {
		return dadosBancarios;
	}

	public void setDadosBancarios(DadosBancarios dadosBancarios) {
		this.dadosBancarios = dadosBancarios;
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
		Proprietario other = (Proprietario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public StringProperty nome() {
		StringProperty nome = new SimpleStringProperty((String) this.nome);
		return nome;
	}
	
	public StringProperty cod() {
		StringProperty cod = new SimpleStringProperty((String) this.id.toString());
		return cod;
	}
	
	
}

