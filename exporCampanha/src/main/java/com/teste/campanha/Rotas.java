package com.teste.campanha;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.teste.campanha.modelos.Campanha;
import com.teste.campanha.modelos.CampanhaCliente;
import com.teste.campanha.modelos.Cliente;
import com.teste.campanha.modelos.Time;
import com.teste.campanha.repositorios.RepositorioCampanha;
import com.teste.campanha.repositorios.RepositorioCampanhaCliente;
import com.teste.campanha.repositorios.RepositorioCliente;
import com.teste.campanha.repositorios.RepositorioTime;

import Exception.ForbiddenException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class Rotas {

	@Autowired
	private RepositorioCampanha repositorioCampanha;
	
	@Autowired
	private RepositorioTime repositorioTime;
	
	@Autowired
	private RepositorioCliente repositorioCliente;
	
	@Autowired
	private RepositorioCampanhaCliente repositorioCampanhaCliente;
	
	@GetMapping("/campanhas")
	public List<Campanha> retornarCampanhas(){
		return repositorioCampanha.campanhas();
	}
	
	@GetMapping("/campanha/{id}")
	public Optional<Campanha> campanhaPorId(@PathVariable Long id){
		return repositorioCampanha.findById(id);
	}
	
	@Transactional
	@PostMapping("/campanha")
	public Campanha criarCampanha(HttpServletRequest request, @RequestBody Campanha campanha) throws ParseException{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		Long timeId = campanha.getTimeId();
		campanha.setTime(timePorId(timeId).get());
		Date inicioVigencia = campanha.getInicioVigencia();
		Date fimVigencia = campanha.getFimVigencia();
		List<Campanha> campanhasMesmaVigencia = repositorioCampanha.campanhasPorVigencia(simpleDateFormat.format(inicioVigencia),simpleDateFormat.format(fimVigencia));
		GregorianCalendar cal = new GregorianCalendar();
		if(!campanhasMesmaVigencia.isEmpty()) {
			List<Campanha> campanhas = repositorioCampanha.findAll();
			for (Campanha campanhaAtiva : campanhas) {
				Date fimVigenciaAtiva = campanhaAtiva.getFimVigencia();
				cal.setTime(fimVigenciaAtiva);
				cal.add(Calendar.DATE, 1);
				while(!repositorioCampanha.campanhasPorFimVigencia(simpleDateFormat.format(cal.getTime())).isEmpty()) {
					cal.add(Calendar.DATE, 1);
				}
				campanhaAtiva.setFimVigencia(cal.getTime());
			}
		}
		repositorioCampanha.save(campanha);
		List<Cliente> clientes = repositorioCliente.clientesPorTime(timeId.toString());
		for (Cliente cliente : clientes) {
			criarRelacao(cliente.getId(), campanha.getId());
		}
		return campanha;
	}
	
	@Transactional
	@PostMapping("/campanha/{id}")
	public Campanha editarCampanha(HttpServletRequest request, @RequestBody Campanha campanha, @PathVariable Long id){
		campanha.setId(id);
		Long timeId = campanha.getTimeId();
		campanha.setTime(timePorId(timeId).get());
		return repositorioCampanha.save(campanha);
	}
	
	@DeleteMapping("/campanha/{id}")
	public void deletarCampanha(@PathVariable Long id){
		repositorioCampanha.deleteById(id);
	}
	
	@GetMapping("/time/{id}")
	public Optional<Time> timePorId(@PathVariable Long id){
		return repositorioTime.findById(id);
	}
	
	@Transactional
	@PostMapping("/time")
	public Time criarTime(HttpServletRequest request, @RequestBody Time time){
		time = repositorioTime.save(time);
		return time;
	}
	
	@GetMapping("/clientes")
	public List<Cliente> retornarCliente(){
		return repositorioCliente.findAll();
	}
	
	@Transactional
	@PostMapping("/cliente")
	public Cliente criarCliente(HttpServletRequest request, @RequestBody Cliente cliente){
		String email = cliente.getEmail();
		if(!repositorioCliente.clientesPorEmail(email).isEmpty()) {
			throw new ForbiddenException("O e-mail ja foi cadastrado");
		}
		Long timeId = cliente.getTimeId();
		cliente.setTime(timePorId(timeId).get());
		List<Campanha> campanhas = repositorioCampanha.campanhasPorTime(timeId.toString());
		cliente = repositorioCliente.save(cliente);
		for (Campanha campanha : campanhas) {
			criarRelacao(cliente.getId(), campanha.getId());
		}
		return cliente;
	}
	
	@Transactional
	@PostMapping("/relacao/{cliente_id}/{campanha_id}")
	public CampanhaCliente criarRelacao(@PathVariable Long cliente_id, @PathVariable Long campanha_id){
		Optional<Cliente> cliente = repositorioCliente.findById(cliente_id);
		Optional<Campanha> campanha = repositorioCampanha.findById(campanha_id);
		CampanhaCliente campanhaCliente = new CampanhaCliente();
		campanhaCliente.setCliente(cliente.get());
		campanhaCliente.setCampanha(campanha.get());
		return repositorioCampanhaCliente.save(campanhaCliente);
	}
}
