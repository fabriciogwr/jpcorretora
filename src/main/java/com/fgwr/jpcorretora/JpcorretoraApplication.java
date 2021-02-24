package com.fgwr.jpcorretora;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fgwr.jpcorretora.domain.Checklist;
import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.domain.Contrato;
import com.fgwr.jpcorretora.domain.DadosBancarios;
import com.fgwr.jpcorretora.domain.Duplicata;
import com.fgwr.jpcorretora.domain.Endereco;
import com.fgwr.jpcorretora.domain.Imovel;
import com.fgwr.jpcorretora.domain.Recibo;
import com.fgwr.jpcorretora.domain.Referencia;
import com.fgwr.jpcorretora.enums.EstadoCivil;
import com.fgwr.jpcorretora.enums.EstadoImovel;
import com.fgwr.jpcorretora.enums.EstadoPagamento;
import com.fgwr.jpcorretora.enums.TipoCliente;
import com.fgwr.jpcorretora.enums.TipoConta;
import com.fgwr.jpcorretora.enums.TipoEndereco;
import com.fgwr.jpcorretora.repositories.ClienteRepository;
import com.fgwr.jpcorretora.repositories.ContratoRepository;
import com.fgwr.jpcorretora.repositories.DadosBancariosRepository;
import com.fgwr.jpcorretora.repositories.DuplicataRepository;
import com.fgwr.jpcorretora.repositories.EnderecoRepository;
import com.fgwr.jpcorretora.repositories.ImovelRepository;
import com.fgwr.jpcorretora.repositories.ReciboRepository;
import com.fgwr.jpcorretora.repositories.ReferenciaRepository;
import com.fgwr.jpcorretora.services.ClienteService;
import com.fgwr.jpcorretora.services.DuplicataService;
import com.fgwr.jpcorretora.services.ImovelService;

import javafx.application.Application;

@SpringBootApplication
public class JpcorretoraApplication implements CommandLineRunner {
	
	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private ReferenciaRepository referenciaRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	ImovelRepository imovelRepository;

	@Autowired
	DadosBancariosRepository dadosBancariosRepository;

	@Autowired
	ContratoRepository contratoRepository;

	@Autowired
	DuplicataRepository duplicataRepository;

	@Autowired
	ReciboRepository reciboRepository;

	@Autowired
	ClienteService cs;
	
	@Autowired
	DuplicataService ds;
	
	@Autowired
	ImovelService is;
	
	public static void main(String[] args) {
		Application.launch(FrontApp.class, args);

	}

	@Override
	public void run(String... args) throws Exception {

		/*
		Calendar cal = Calendar.getInstance();
		
		cal.set(1994, 7, 1);
		Cliente cli1 = new Cliente(null, "Fabrício", cal.getTime(), "fabricio.gwr@gmail.com", "01284257258", "1161177", "cliente ok", TipoCliente.PESSOAFISICA, EstadoCivil.SOLTEIRO, "Desenvolvedor de Sistemas");
		cal.set(1980, 1, 21);
		Cliente cli2 = new Cliente(null, "Fábio", cal.getTime(), "fabio@gmail.com", "01234567891", "12345678", "cliente ok", TipoCliente.PESSOAJURIDICA, EstadoCivil.CASADO, "Corretor de Imóveis");

		Referencia ref1 = new Referencia(null, "Fábia");
		Referencia ref2 = new Referencia(null, "Mell");

		cli1.getTelefones().addAll(Arrays.asList("993557900", "34225987"));

		cli1.getReferencia().addAll(Arrays.asList(ref1, ref2));
		cli2.getReferencia().addAll(Arrays.asList(ref2));

		ref1.getTelefones().addAll(Arrays.asList("123456789", "465498987"));

		ref1.getCliente().addAll(Arrays.asList(cli1));
		ref2.getCliente().addAll(Arrays.asList(cli1, cli2));

		DadosBancarios db1 = new DadosBancarios(null, "Caixa Econômica Federal", "1824", "12345-6", TipoConta.CORRENTEPF, "FABRICIO GUSTAVO W ROCHA");
		
		db1.setCliente(cli1);

		Endereco e1 = new Endereco(null, "Av Erivaldo Venceslau da Silva", "2356", null, "Bodanese", "76981-068", "Vilhena", "RO", TipoEndereco.ENDERECORESIDENCIAL );
		Endereco e2 = new Endereco(null, "Rua 8215", "2773", null, "Barão 3", "76981-068", "Vilhena", "RO", TipoEndereco.ENDERECORESIDENCIAL );
		
		Imovel i1 = new Imovel(null, "José Bonifácil", cal.getTime(), EstadoImovel.USADO, "Imovel bom", false, false, false, true, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, "A verificar rachaduras banheiro", cal.getTime(), "Fabio");
		Imovel i2 = new Imovel(null, "Molares", cal.getTime(), EstadoImovel.USADO, "Imovel bom", true, false, false, true, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, "A verificar rachaduras banheiro", cal.getTime(), "Fabricio");
		
		i1.setCliente(cli1);
		i2.setCliente(cli2);
		
		
		Contrato ctr1 = new Contrato(null, cal.getTime(), 6, 650.00);
		Contrato ctr2 = new Contrato(null, cal.getTime(), 5, 500.00);
		
		i1.setContrato(ctr1);
		i2.setContrato(ctr2);
		
		i1.setEndereco(e1);
		i2.setEndereco(e2);
		
		ctr1.setImovel(i1);
		ctr2.setImovel(i2);
		
		cli1.setContrato(ctr1);
		cli2.setContrato(ctr2);
		
		ctr1.setCliente(cli1);
		ctr2.setCliente(cli2);
		
		

		List<Duplicata> vs1 = ds.preencherDuplicata(ctr1);
		List<Duplicata> vs2 = ds.preencherDuplicata(ctr2);

		Recibo rec1 = new Recibo(null, cli1, ctr1.getValorDeCadaParcela(), ctr1.getQtdParcelas(), vs1.get(0).getDataVencimento(), cal.getTime(), vs1.get(0));



		clienteRepository.saveAll(Arrays.asList(cli1, cli2));
		referenciaRepository.saveAll(Arrays.asList(ref1, ref2));
		enderecoRepository.saveAll(Arrays.asList(e1, e2));
		imovelRepository.saveAll(Arrays.asList(i1, i2));
		dadosBancariosRepository.saveAll(Arrays.asList(db1));
		contratoRepository.saveAll(Arrays.asList(ctr1, ctr2));
		duplicataRepository.saveAll(vs1);
		duplicataRepository.saveAll(vs2);
		reciboRepository.saveAll(Arrays.asList(rec1));
		

		Duplicata dup = vs1.get(0);
		dup.setEstado(EstadoPagamento.QUITADO);

		duplicataRepository.save(dup);

		vs1.remove(0);


		for (Duplicata duplicata : vs1) {
			duplicata.setEstado(EstadoPagamento.CANCELADO);

		} 
		duplicataRepository.saveAll(vs1);
		
		
		/*List <Duplicata> dupsCpf = ds.findCpfOuCnpj("01284257258");
		for (Duplicata duplicata : dupsCpf) {
			System.out.println(duplicata.getValor());
		}*/

	/*	List <Duplicata> dupsCli = ds.findCliente("Fabrício");
		for (Duplicata duplicata : dupsCli) {
			System.out.println(duplicata.getDataVencimento());
		}*/

	/*	List <Duplicata> dupsCont = ds.findContrato(1);
		for (Duplicata duplicata : dupsCont) {
			System.out.println(duplicata.getDataVencimento());
		}
		
		Duplicata dupsId = ds.find(1);

		System.out.println(dupsId.getValor());
		
		Cliente cliId = cs.find(1);

		System.out.println(cliId.getNome());
		
		List<Cliente> allcli = cs.findAll();
		for (Cliente cliente : allcli) {
			clienteData.add(cliente);
			System.out.println(cliente.getNome());
		}*/

		//List<Cliente> allcli = cs.findAll();



	//Duplicata vs2 = ds.find(4);
	//vs2.setEstado(EstadoPagamento.PENDENTE);
	//duplicataRepository.save(vs2);
	}



}