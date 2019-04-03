package com.teste.campanha.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teste.campanha.modelos.CampanhaCliente;

@Repository
public interface RepositorioCampanhaCliente extends JpaRepository<CampanhaCliente, Long>{

}
