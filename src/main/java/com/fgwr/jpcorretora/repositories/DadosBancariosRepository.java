package com.fgwr.jpcorretora.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fgwr.jpcorretora.domain.DadosBancarios;

@Repository
public interface DadosBancariosRepository extends JpaRepository <DadosBancarios, Integer>{

}
