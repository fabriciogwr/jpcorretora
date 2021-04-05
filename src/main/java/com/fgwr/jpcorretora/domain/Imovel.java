package com.fgwr.jpcorretora.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fgwr.jpcorretora.enums.EstadoImovel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
public class Imovel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.DATE)
    private Date dataAngariacao;
    
    @ManyToOne
    private Proprietario proprietario;
    
    @OneToOne
    private Contrato contrato;
    
    @OneToOne(cascade = CascadeType.REMOVE)
    private Endereco endereco;
    
    private Integer estadoImovel;
    private String descricao;
    
    private Boolean danoSala;
    private Boolean danoCozinha;
    private Boolean danoQuarto;
    private Boolean danoBanheiro;
    private Boolean danoDispensa;
    private Boolean danoAreaServico;
    private Boolean danoGaragem;
    private Boolean danoTomadas;
    private Boolean danoLampadas;
    private Boolean danoChuveiro;
    private Boolean danoFechaduras;
    private Boolean danoChaves;
    private Boolean danoPortaoEletro;
    private Boolean danoCercaEletrica;
    private Boolean danoControle;
    private Boolean danoInfiltracao;
    private Boolean danoPia;
    private Boolean danoVasoSanitario;
    private Boolean danoBox;
    private Boolean danoArCondicionado;
    private Boolean danoMoveisVinculados;
    private Boolean danoPortas;
    private Boolean danoJanelas;
    private Boolean danoPortao;
    private Boolean danoPinturaInterna;
    private Boolean danoPinturaExterna;
    
    @Temporal(TemporalType.DATE)
    private Date dataLaudo;
    
    private String corretor;

	

	
	public Imovel() {
    	
    }
    
    public Imovel(Integer id, Date dataAngariacao, EstadoImovel estadoImovel, String descricao,
    		Boolean danoSala, Boolean danoCozinha, Boolean danoQuarto, Boolean danoBanheiro, Boolean danoDispensa,
			Boolean danoAreaServico, Boolean danoGaragem, Boolean danoTomadas, Boolean danoLampadas,
			Boolean danoChuveiro, Boolean danoFechaduras, Boolean danoChaves, Boolean danoPortaoEletro,
			Boolean danoCercaEletrica, Boolean danoControle, Boolean danoInfiltracao, Boolean danoPia,
			Boolean danoVasoSanitario, Boolean danoBox, Boolean danoArCondicionado, Boolean danoMoveisVinculados,
			Boolean danoPortas, Boolean danoJanelas, Boolean danoPortao, Boolean danoPinturaInterna,
			Boolean danoPinturaExterna, Date dataLaudo, String corretor ) {
        this.id = id;
        this.dataAngariacao = dataAngariacao;
        this.descricao = descricao;
        this.estadoImovel = estadoImovel.getCod();
        this.danoSala = danoSala;
		this.danoCozinha = danoCozinha;
		this.danoQuarto = danoQuarto;
		this.danoBanheiro = danoBanheiro;
		this.danoDispensa = danoDispensa;
		this.danoAreaServico = danoAreaServico;
		this.danoGaragem = danoGaragem;
		this.danoTomadas = danoTomadas;
		this.danoLampadas = danoLampadas;
		this.danoChuveiro = danoChuveiro;
		this.danoFechaduras = danoFechaduras;
		this.danoChaves = danoChaves;
		this.danoPortaoEletro = danoPortaoEletro;
		this.danoCercaEletrica = danoCercaEletrica;
		this.danoControle = danoControle;
		this.danoInfiltracao = danoInfiltracao;
		this.danoPia = danoPia;
		this.danoVasoSanitario = danoVasoSanitario;
		this.danoBox = danoBox;
		this.danoArCondicionado = danoArCondicionado;
		this.danoMoveisVinculados = danoMoveisVinculados;
		this.danoPortas = danoPortas;
		this.danoJanelas = danoJanelas;
		this.danoPortao = danoPortao;
		this.danoPinturaInterna = danoPinturaInterna;
		this.danoPinturaExterna = danoPinturaExterna;
		this.dataLaudo = dataLaudo;
		this.corretor = corretor;
    }

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Proprietario getProprietario() {
		return proprietario;
	}

	public void setProprietario(Proprietario proprietario) {
		this.proprietario = proprietario;
	}

	public Date getDataAngariacao() {
        return dataAngariacao;
    }
	
	public String getDataAngariacaoString() {
    	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    	String dataAngariacaoString = df.format(this.dataAngariacao);
		return dataAngariacaoString;
	}

    public void setDataAngariacao(Date dataAngariacao) {
        this.dataAngariacao = dataAngariacao;
    }

	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public EstadoImovel getEstadoImovel() {
		return EstadoImovel.toEnum(estadoImovel);
	}

	public void setEstadoImovel(EstadoImovel estadoImovel) {
		this.estadoImovel = estadoImovel.getCod();
	}
	

	public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public Boolean isDanoSala() {
		return danoSala;
	}

	public void setDanoSala(Boolean danoSala) {
		this.danoSala = danoSala;
	}

	public Boolean isDanoCozinha() {
		return danoCozinha;
	}

	public void setDanoCozinha(Boolean danoCozinha) {
		this.danoCozinha = danoCozinha;
	}

	public Boolean isDanoQuarto() {
		return danoQuarto;
	}

	public void setDanoQuarto(Boolean danoQuarto) {
		this.danoQuarto = danoQuarto;
	}

	public Boolean isDanoBanheiro() {
		return danoBanheiro;
	}

	public void setDanoBanheiro(Boolean danoBanheiro) {
		this.danoBanheiro = danoBanheiro;
	}

	public Boolean isDanoDispensa() {
		return danoDispensa;
	}

	public void setDanoDispensa(Boolean danoDispensa) {
		this.danoDispensa = danoDispensa;
	}

	public Boolean isDanoAreaServico() {
		return danoAreaServico;
	}

	public void setDanoAreaServico(Boolean danoAreaServico) {
		this.danoAreaServico = danoAreaServico;
	}

	public Boolean isDanoGaragem() {
		return danoGaragem;
	}

	public void setDanoGaragem(Boolean danoGaragem) {
		this.danoGaragem = danoGaragem;
	}

	public Boolean isDanoTomadas() {
		return danoTomadas;
	}

	public void setDanoTomadas(Boolean danoTomadas) {
		this.danoTomadas = danoTomadas;
	}

	public Boolean isDanoLampadas() {
		return danoLampadas;
	}

	public void setDanoLampadas(Boolean danoLampadas) {
		this.danoLampadas = danoLampadas;
	}

	public Boolean isDanoChuveiro() {
		return danoChuveiro;
	}

	public void setDanoChuveiro(Boolean danoChuveiro) {
		this.danoChuveiro = danoChuveiro;
	}

	public Boolean isDanoFechaduras() {
		return danoFechaduras;
	}

	public void setDanoFechaduras(Boolean danoFechaduras) {
		this.danoFechaduras = danoFechaduras;
	}

	public Boolean isDanoChaves() {
		return danoChaves;
	}

	public void setDanoChaves(Boolean danoChaves) {
		this.danoChaves = danoChaves;
	}

	public Boolean isDanoPortaoEletro() {
		return danoPortaoEletro;
	}

	public void setDanoPortaoEletro(Boolean danoPortaoEletro) {
		this.danoPortaoEletro = danoPortaoEletro;
	}

	public Boolean isDanoCercaEletrica() {
		return danoCercaEletrica;
	}

	public void setDanoCercaEletrica(Boolean danoCercaEletrica) {
		this.danoCercaEletrica = danoCercaEletrica;
	}

	public Boolean isDanoControle() {
		return danoControle;
	}

	public void setDanoControle(Boolean danoControle) {
		this.danoControle = danoControle;
	}

	public Boolean isDanoInfiltracao() {
		return danoInfiltracao;
	}

	public void setDanoInfiltracao(Boolean danoInfiltracao) {
		this.danoInfiltracao = danoInfiltracao;
	}

	public Boolean isDanoPia() {
		return danoPia;
	}

	public void setDanoPia(Boolean danoPia) {
		this.danoPia = danoPia;
	}

	public Boolean isDanoVasoSanitario() {
		return danoVasoSanitario;
	}

	public void setDanoVasoSanitario(Boolean danoVasoSanitario) {
		this.danoVasoSanitario = danoVasoSanitario;
	}

	public Boolean isDanoBox() {
		return danoBox;
	}

	public void setDanoBox(Boolean danoBox) {
		this.danoBox = danoBox;
	}

	public Boolean isDanoArCondicionado() {
		return danoArCondicionado;
	}

	public void setDanoArCondicionado(Boolean danoArCondicionado) {
		this.danoArCondicionado = danoArCondicionado;
	}

	public Boolean isDanoMoveisVinculados() {
		return danoMoveisVinculados;
	}

	public void setDanoMoveisVinculados(Boolean danoMoveisVinculados) {
		this.danoMoveisVinculados = danoMoveisVinculados;
	}

	public Boolean isDanoPortas() {
		return danoPortas;
	}

	public void setDanoPortas(Boolean danoPortas) {
		this.danoPortas = danoPortas;
	}

	public Boolean isDanoJanelas() {
		return danoJanelas;
	}

	public void setDanoJanelas(Boolean danoJanelas) {
		this.danoJanelas = danoJanelas;
	}

	public Boolean isDanoPortao() {
		return danoPortao;
	}

	public void setDanoPortao(Boolean danoPortao) {
		this.danoPortao = danoPortao;
	}

	public Boolean isDanoPinturaInterna() {
		return danoPinturaInterna;
	}

	public void setDanoPinturaInterna(Boolean danoPinturaInterna) {
		this.danoPinturaInterna = danoPinturaInterna;
	}

	public Boolean isDanoPinturaExterna() {
		return danoPinturaExterna;
	}

	public void setDanoPinturaExterna(Boolean danoPinturaExterna) {
		this.danoPinturaExterna = danoPinturaExterna;
	}

	public Date getDataLaudo() {
		return dataLaudo;
	}

	public void setDataLaudo(Date dataLaudo) {
		this.dataLaudo = dataLaudo;
	}

	public String getCorretor() {
		return corretor;
	}

	public void setCorretor(String corretor) {
		this.corretor = corretor;
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
		Imovel other = (Imovel) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	public StringProperty proprietario() {
		StringProperty proprietario = new SimpleStringProperty((String) this.proprietario.getNome());
		return proprietario;
	}
	
	public StringProperty endereco() {
		StringProperty endereco = new SimpleStringProperty((String) this.endereco.getLogradouro() + ", " + this.endereco.getNumero());
		return endereco;
	}
	
	public StringProperty dataAngariacao() {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		StringProperty dataAngariacao = new SimpleStringProperty(df.format(this.dataAngariacao));
		return dataAngariacao;
	}
	
	public StringProperty cod() {
		StringProperty cod = new SimpleStringProperty((String) this.id.toString());
		return cod;
	}
	
	public StringProperty locacao() {
		Boolean locado = (this.getContrato() == null) ? false : true;
		StringProperty locadoStr;
		if(locado) {
		locadoStr = new SimpleStringProperty("Já alugado");
		} else {
			locadoStr = new SimpleStringProperty("Disponível");
		}return locadoStr;
		
	}

}
