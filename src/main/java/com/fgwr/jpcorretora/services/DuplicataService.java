package com.fgwr.jpcorretora.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.domain.Contrato;
import com.fgwr.jpcorretora.domain.Duplicata;
import com.fgwr.jpcorretora.enums.EstadoPagamento;
import com.fgwr.jpcorretora.repositories.DuplicataRepository;
import com.fgwr.jpcorretora.services.exceptions.ObjectNotFoundException;

@Service
public class DuplicataService {

	@Autowired
	DuplicataRepository repo;

	public List<Duplicata> preencherDuplicata(Contrato contrato, Cliente cliente) {

		List<Duplicata> vencimentos = new ArrayList<>();
		int qtdParcelas = contrato.getQtdParcelas();

		Calendar cal = Calendar.getInstance();
		Date now = new Date();

		for (int i = 1; i <= qtdParcelas; i++) {
			Duplicata duplicata = new Duplicata();
			cal.setTime(now);
			cal.add(Calendar.MONTH, i - 1);
			duplicata.setId(null);
			duplicata.setDataVencimento(cal.getTime());
			duplicata.setEstado(EstadoPagamento.PENDENTE);
			duplicata.setContrato(contrato);
			duplicata.setValor(contrato.getValorDeCadaParcela());
			duplicata.setParcela(i);
			vencimentos.add(duplicata);
			duplicata.setCliente(cliente);
		}
		return vencimentos;
	}

	public List<Duplicata> preencherDuplicata(Contrato contrato, Integer primeiraParcela, Cliente cliente) {

		List<Duplicata> vencimentos = new ArrayList<>();
		int qtdParcelas = contrato.getQtdParcelas();
		Calendar cal = Calendar.getInstance();

		for (int i = 1; i <= qtdParcelas; i++) {
			cal = Calendar.getInstance();
			System.out.println("Execução" + i);
			Duplicata duplicata = new Duplicata();
			if (i == 1) {
				if (primeiraParcela > cal.get(Calendar.DAY_OF_MONTH)) {

					Double pc1 = (contrato.getValorDeCadaParcela() / 100) * 1;
					Double pc2 = (contrato.getValorDeCadaParcela() / 100) * 2;

					Double valorPorDia = pc1 / cal.getActualMaximum(Calendar.DAY_OF_MONTH);
					Double diferencaDeVencimento = pc2
							+ (valorPorDia * (primeiraParcela - cal.get(Calendar.DAY_OF_MONTH)));
					Double valorPrimeiraParcela = contrato.getValorDeCadaParcela() + diferencaDeVencimento;
					duplicata.setValor(valorPrimeiraParcela);
					cal.set(Calendar.DAY_OF_MONTH, primeiraParcela);
					duplicata.setDataVencimento(cal.getTime());

					duplicata.setEstado(EstadoPagamento.PENDENTE);
					duplicata.setContrato(contrato);
					duplicata.setCliente(contrato.getCliente());
					duplicata.setParcela(i);
					vencimentos.add(duplicata);
					cal = Calendar.getInstance();
					cal.add(Calendar.MONTH, i - 1);

				} else if (primeiraParcela < cal.get(Calendar.DAY_OF_MONTH)) {
					System.out.println("i = 1 < /" + cal.getTime());
					Double pc1 = (contrato.getValorDeCadaParcela() / 100) * 1;
					Double pc2 = (contrato.getValorDeCadaParcela() / 100) * 2;

					Double valorPorDia = pc1 / cal.getActualMaximum(Calendar.DAY_OF_MONTH);
					LocalDate date = Calendar.getInstance().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

					cal.add(Calendar.MONTH, 1);
					cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), primeiraParcela);
					LocalDate date2 = cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

					Double diferencaDeVencimento = pc2
							+ (valorPorDia * Math.toIntExact(ChronoUnit.DAYS.between(date, date2)));

					Double valorPrimeiraParcela = contrato.getValorDeCadaParcela() + diferencaDeVencimento;
					duplicata.setValor(valorPrimeiraParcela);
					duplicata.setDataVencimento(cal.getTime());
					duplicata.setEstado(EstadoPagamento.PENDENTE);
					duplicata.setContrato(contrato);
					duplicata.setCliente(contrato.getCliente());
					duplicata.setParcela(i);
					vencimentos.add(duplicata);
					cal = Calendar.getInstance();
					cal.add(Calendar.MONTH, i - 1);

				}

				

			}
			if (i > 1) {
				cal.add(Calendar.MONTH, i - 1);
				duplicata.setValor(contrato.getValorDeCadaParcela());
				duplicata.setDataVencimento(cal.getTime());
				duplicata.setEstado(EstadoPagamento.PENDENTE);
				duplicata.setContrato(contrato);
				duplicata.setParcela(i);
				duplicata.setCliente(cliente);
				vencimentos.add(duplicata);
				
			}
		}

		return vencimentos;
	}

	public List<Duplicata> insert(Contrato ctr, Cliente cli) {
		List<Duplicata> vs = preencherDuplicata(ctr, cli);
		repo.saveAll(vs);
		return vs;
	}

	public Duplicata find(Integer id) {

		Optional<Duplicata> dup = repo.findById(id);
		return dup.orElseThrow(() -> new ObjectNotFoundException("Duplicata não encontrada!"));

	}

	public List<Duplicata> findAll() {
		return repo.findAll();
	}

	public List<Duplicata> findByContrato(Contrato contrato) {

		List<Duplicata> dup = repo.findByContrato(contrato);
		if (dup == null) {
			throw new ObjectNotFoundException("Duplicatas não encontradas!");
		}
		return dup;
	}

	public List<Duplicata> findEstado(Integer cod) {
		List<Duplicata> dup = repo.findByEstado(cod);
		return dup;
	}

	/*
	 * public List<Duplicata> findCpfOuCnpj(String cpfOuCnpj) {
	 * 
	 * List<Duplicata> dup = repo.findCpfOuCnpj(cpfOuCnpj); if (dup == null) { throw
	 * new ObjectNotFoundException("Duplicatas não encontradas!"); } return dup; }
	 */

	public List<Duplicata> findVencimento(Date dataVencimento) {

		List<Duplicata> dup = repo.findByDataVencimento(dataVencimento);
		if (dup == null) {
			throw new ObjectNotFoundException("Duplicatas não encontradas!");
		}
		return dup;
	}

	public Duplicata atualizarEstadoPgto(Duplicata dup) {
		Duplicata newDup = find(dup.getId());
		updateData(newDup, dup);
		return repo.save(newDup);
	}

	private void updateData(Duplicata newDup, Duplicata dup) {
		newDup.setEstado(dup.getEstado());
	}

	public List<Duplicata> findCliente(String nome) {

		List<Duplicata> dup = repo.findCliente(nome);
		if (dup == null) {
			throw new ObjectNotFoundException("Duplicatas não encontradas!");
		}
		return dup;
	}
}
