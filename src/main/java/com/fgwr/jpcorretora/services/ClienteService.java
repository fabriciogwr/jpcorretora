package com.fgwr.jpcorretora.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fgwr.jpcorretora.domain.Cliente;
import com.fgwr.jpcorretora.domain.Endereco;
import com.fgwr.jpcorretora.dto.ClienteDTO;
import com.fgwr.jpcorretora.dto.ClienteNewDTO;
import com.fgwr.jpcorretora.enums.EstadoCivil;
import com.fgwr.jpcorretora.enums.TipoCliente;
import com.fgwr.jpcorretora.enums.TipoEndereco;
import com.fgwr.jpcorretora.repositories.ClienteRepository;
import com.fgwr.jpcorretora.repositories.EnderecoRepository;
import com.fgwr.jpcorretora.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	ClienteRepository repo;
	
	@Autowired
	EnderecoRepository enderecoRepository;

	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado! Id: " + id)); 
	}
	
/*public Cliente findByCpfOuCnpj(String cpfOuCnpj) {
		
		Cliente obj = repo.findByCpfOuCnpj(cpfOuCnpj);
		if (obj == null) {
			throw new ObjectNotFoundException("Duplicatas não encontradas!");
		}
		return obj;
	}*/

	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		return obj;
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}
	
	public List<Cliente> findAll() {
		return repo.findAll();
	}
	
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getDataNascimento(), objDto.getEmail(), null, null, null, null, null, null, null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getDataNascimento(), objDto.getEmail(), objDto.getCpfOuCnpj(), objDto.getRg(), objDto.getObs(), null, null, TipoCliente.toEnum(objDto.getTipoCli()), EstadoCivil.toEnum(objDto.getEstadoCivil()), objDto.getProfissao());
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), objDto.getCidade(), objDto.getEstado(), TipoEndereco.toEnum(objDto.getTipoEnd()));
		cli.getEnderecos().add(end);
		
		return cli;
		
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}

}
