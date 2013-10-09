package eu.trentorise.smartcampus.corsi.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.corsi.model.Commento;
import eu.trentorise.smartcampus.corsi.model.Corso;
import eu.trentorise.smartcampus.corsi.model.CorsoLite;
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.CommentiRepository;
import eu.trentorise.smartcampus.corsi.repository.CorsoRepository;
import eu.trentorise.smartcampus.corsi.repository.EventoRepository;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller("corsiController")
public class CorsiController {

	private static final Logger logger = Logger
			.getLogger(CorsiController.class);
	/*
	 * the base url of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${profile.address}")
	private String profileaddress;

	@Autowired
	@Value("${communicator.address}")
	private String communicatoraddress;

	/*
	 * the base appName of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${webapp.name}")
	private String appName;

	@Autowired
	private CorsoRepository corsoRepository;

	@Autowired
	private StudenteRepository studenteRepository;

	@Autowired
	private EventoRepository eventoRepository;

	@Autowired
	private CommentiRepository commentiRepository;

	/*
	 * Ritorna tutti i corsi
	 */
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return List<Corso>
	 * @throws IOException
	 * 
	 * Restituisce al client tutti i corsi presenti nel DB
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corso/all")
	public @ResponseBody
	List<Corso> getCorsiCompletiAll(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {

			List<Corso> getCor = corsoRepository.findAll();

			return getCor;

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	
	
	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param id_corso
	 * @return Corso
	 * @throws IOException
	 * 
	 * Restituisce al client i dati completi di un corso dato l'id
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corso/{id_corso}")
	public @ResponseBody
	Corso getCorsoByID(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("id_corso") Long id_corso)

	throws IOException {
		try {
			logger.info("/corsi/{id_corso}");

			if (id_corso == null)
				return null;

			return UpdateRatingCorso(corsoRepository.findOne(id_corso));
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	
	
	
	/**
	 * 
	 * @param corsoDaAggiornare
	 * @return Il corso le cui valutazioni sono state aggiornate con successo, altrimenti ritorna null
	 * 
	 * Calcola e aggiorna nel DB le valutazioni medie per ogni ambito del corso 
	 * 
	 */
	private Corso UpdateRatingCorso(Corso corsoDaAggiornare) {

		if (corsoDaAggiornare == null)
			return null;

		// cerco la lista dei commenti
		List<Commento> listCom = commentiRepository
				.getCommentoByCorsoAll(corsoDaAggiornare.getId());
		float Rating_carico_studio = 0;
		float Rating_contenuto = 0;
		float Rating_esame = 0;
		float Rating_lezioni = 0;
		float Rating_materiali = 0;
		int len = listCom.size();

		// sommo le valutazioni per ogni ambito
		for (Commento index : listCom) {
			Rating_carico_studio += index.getRating_carico_studio();
			Rating_contenuto += index.getRating_contenuto();
			Rating_esame += index.getRating_esame();
			Rating_lezioni += index.getRating_lezioni();
			Rating_materiali += index.getRating_materiali();
		}

		// calcolo la media per ogni ambito
		corsoDaAggiornare.setRating_carico_studio(Rating_carico_studio / len);
		corsoDaAggiornare.setRating_contenuto(Rating_contenuto / len);
		corsoDaAggiornare.setRating_esame(Rating_esame / len);
		corsoDaAggiornare.setRating_lezioni(Rating_lezioni / len);
		corsoDaAggiornare.setRating_materiali(Rating_materiali / len);

		// calcolo la valutazione media generale del corso
		float sommaValutazioni = corsoDaAggiornare.getRating_carico_studio()
				+ corsoDaAggiornare.getRating_contenuto()
				+ corsoDaAggiornare.getRating_esame()
				+ corsoDaAggiornare.getRating_lezioni()
				+ corsoDaAggiornare.getRating_materiali();

		// setto la media delle valutazioni
		corsoDaAggiornare.setValutazione_media(sommaValutazioni / 5);

		Corso corsoAggiornato = corsoRepository.saveAndFlush(corsoDaAggiornare);

		if (corsoAggiornato == null)
			return null;

		if (corsoAggiornato.getId() != corsoDaAggiornare.getId())
			return null;

		return corsoAggiornato;
	}

	private String getToken(HttpServletRequest request) {
		return (String) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
	}


	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return Collection<Corso>
	 * @throws IOException
	 * 
	 * Restituisce tutti i corsi (corsi da libretto) dello studente che manda la richiesta get
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corso/me")
	public @ResponseBody
	Collection<Corso> getCorsoByMe(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			logger.info("/corso/me");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			// test
			Studente studente = studenteRepository.findStudenteByUserId(userId);
			if (studente == null) {
				studente = new Studente();
				studente.setId(userId);
				studente.setNome(profile.getName());
				studente.setCognome(profile.getSurname());
				studente = studenteRepository.save(studente);

				// studente = studenteRepository.save(studente);

				// TODO caricare corsi da esse3
				// Creare associazione su frequenze

				// TEST
				List<Corso> corsiEsse3 = corsoRepository.findAll();

				String supera = null;
				String interesse = null;
				int z = 0;
				supera = new String();
				interesse = new String();

				for (Corso cors : corsiEsse3) {

					if (z % 2 == 0) {
						supera = supera.concat(String.valueOf(cors.getId())
								.concat(","));
					}
					
					if (z % 4 == 0) {
						interesse = interesse.concat(String.valueOf(cors.getId())
								.concat(","));
					}
					
					z++;
				}
				
				// Set corso follwed by studente
				studente.setCorsi(corsiEsse3);
				studente = studenteRepository.save(studente);

				// Set corsi superati
				studente.setIdsCorsiSuperati(supera);
				studente.setIdsCorsiInteresse(interesse);
				
				studente = studenteRepository.save(studente);
			}

			return studente.getCorsi();

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	
	
	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return Collection<CorsoLite>
	 * @throws IOException
	 * 
	 * Ritorna tutti i corsi che lo studente che ha fatto la richiesta, ha superato, quindi che può votare
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corso/superati/me")
	public @ResponseBody
	Collection<CorsoLite> getCorsiSuperatiByMe(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			logger.info("/corso/superati/me");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			// test
			Studente studente = studenteRepository.findStudenteByUserId(userId);
			if (studente == null) {
				studente = new Studente();
				studente.setId(userId);
				studente.setNome(profile.getName());
				studente.setCognome(profile.getSurname());
				studente = studenteRepository.save(studente);

				// studente = studenteRepository.save(studente);

				// TODO caricare corsi da esse3
				// Creare associazione su frequenze

				// TEST
				List<Corso> corsiEsse3 = corsoRepository.findAll();

				String supera = null;
				String interesse = null;
				int z = 0;
				supera = new String();
				interesse = new String();

				for (Corso cors : corsiEsse3) {

					if (z % 2 == 0) {
						supera = supera.concat(String.valueOf(cors.getId())
								.concat(","));
					}
					
					if (z % 4 == 0) {
						interesse = interesse.concat(String.valueOf(cors.getId())
								.concat(","));
					}
					
					z++;
				}

				// TEST

				// Set corso follwed by studente
				studente.setCorsi(corsiEsse3);
				studente = studenteRepository.save(studente);

				// Set corsi superati
				studente.setIdsCorsiSuperati(supera);
				studente.setIdsCorsiInteresse(interesse);
				
				studente = studenteRepository.save(studente);
			}

			studente = studenteRepository.save(studente);

			return assignCorsiSuperati(studente);

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	
	
	
	

	/**
	 * 
	 * @param stud
	 * @return List<CorsoLite>
	 * 
	 * Metodo che dato lo studente setta la lista dei corsi superati dalla stringa degli ids
	 * 
	 */
	private List<CorsoLite> assignCorsiSuperati(Studente stud) {
		// TODO Auto-generated method stub

		String[] listS = stud.getIdsCorsiSuperati().split(",");

		List<CorsoLite> reurList = new ArrayList<CorsoLite>();
		for (String s : listS) {
			reurList.add(corsoRepository.findOne(Long.valueOf(s)));
		}

		return reurList;
	}

	
	/**
	 * 
	 * @param stud
	 * @return List<CorsoLite>
	 * 
	 * metodo che dato lo studente setta la lista dei corsi di interesse dalla stringa degli ids
	 * 
	 */
	private List<CorsoLite> assignCorsiInteresse(Studente stud) {
		// TODO Auto-generated method stub

		String[] listS = stud.getIdsCorsiInteresse().split(",");

		List<CorsoLite> reurList = new ArrayList<CorsoLite>();
		for (String s : listS) {
			reurList.add(corsoRepository.findOne(Long.valueOf(s)));
		}

		return reurList;
	}
	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param id_dipartimento
	 * @return Collection<Corso>
	 * @throws IOException
	 * 
	 * Metodo che dato un id_dipartimento ritorna al client la lista dei corsi riferiti a quel dipartimento
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corso/dipartimento/{id_dipartimento}")
	public @ResponseBody
	Collection<Corso> getCorsoByDipartimento(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("id_dipartimento") Long id_dipartimento)

	throws IOException {
		try {
			logger.info("/corsi/dipartimento/{id_dipartimento}");
			if (id_dipartimento == null)
				return null;

			return corsoRepository.findCorsoByDipartimentoId(id_dipartimento);

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}


	
	
	/*
	 * Ritorna tutte le recensioni dato l'id di un corso
	 */
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param id_studente
	 * @return Collection<Corso>
	 * @throws IOException
	 * 
	 * 
	 * Restituisce la lista dei corsi (da libretto) riferiti allo studente
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corsi/me")
	public @ResponseBody
	Collection<Corso> getStudente(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			logger.info("/corsi/me");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			Studente studente = studenteRepository.findStudenteByUserId(userId);

			if (studente == null) {
				studente = new Studente();
				studente.setId(userId);
				studente.setNome(profile.getName());
				studente.setCognome(profile.getSurname());
				studente = studenteRepository.save(studente);

				// studente = studenteRepository.save(studente);

				// TODO caricare corsi da esse3
				// Creare associazione su frequenze

				// TEST
				List<Corso> corsiEsse3 = corsoRepository.findAll();

				String supera = null;
				String interesse = null;
				int z = 0;
				supera = new String();
				interesse = new String();

				for (Corso cors : corsiEsse3) {

					if (z % 2 == 0) {
						supera = supera.concat(String.valueOf(cors.getId())
								.concat(","));
					}
					
					if (z % 4 == 0) {
						interesse = interesse.concat(String.valueOf(cors.getId())
								.concat(","));
					}
					
					z++;
				}
				
				// Set corso follwed by studente
				studente.setCorsi(corsiEsse3);
				studente = studenteRepository.save(studente);

				// Set corsi superati
				studente.setIdsCorsiSuperati(supera);
				studente.setIdsCorsiInteresse(interesse);
				
				studente = studenteRepository.save(studente);
				
			}

			studente = studenteRepository.save(studente);

			return studente.getCorsi();

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	
	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param id_corsoLaurea
	 * @return Collection<Corso>
	 * @throws IOException
	 * 
	 * Metodo che dato un id_corsoLaurea ritorna al client la lista dei corsi riferiti a quel corso di laurea
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corso/corsolaurea/{id_corsoLaurea}")
	public @ResponseBody
	Collection<Corso> getCorsoByCorsoLaurea(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("id_corsoLaurea") Long id_corsoLaurea)

	throws IOException {
		try {
			logger.info("/corsi/corsolaurea/{id_corsoLaurea}");
			if (id_corsoLaurea == null)
				return null;

			return corsoRepository.findCorsoByCorsoLaureaId(id_corsoLaurea);

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * getCorsoByCorsoLaurea
	 */
	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param id_corso
	 * @return Boolean
	 * @throws IOException
	 * 
	 * Metodo che dato un id_corso ritorna al client true se il corso è stato superato, false altrimenti
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corso/superato/{id_corso}")
	public @ResponseBody
	Boolean isMyCourseSuperato(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("id_corso") Long id_corso)

	throws IOException {
		try {
			logger.info("/corso/superato/" + id_corso.toString());

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			Studente studente = studenteRepository.findStudenteByUserId(userId);

			if (studente == null) {
				studente = new Studente();
				studente.setId(userId);
				studente.setNome(profile.getName());
				studente.setCognome(profile.getSurname());
				studente = studenteRepository.save(studente);

				// Aggiunta dati fake //////////////////////////////////////////
				List<Corso> corsiEsse3 = corsoRepository.findAll();

				String supera = null;
				String interesse = null;
				int z = 0;
				supera = new String();
				interesse = new String();

				for (Corso cors : corsiEsse3) {

					if (z % 2 == 0) {
						supera = supera.concat(String.valueOf(cors.getId())
								.concat(","));
					}
					
					if (z % 4 == 0) {
						interesse = interesse.concat(String.valueOf(cors.getId())
								.concat(","));
					}
					
					z++;
				}
				///////////////////////////////////////////////////////
				
				// Set corso follwed by studente
				studente.setCorsi(corsiEsse3);
				studente = studenteRepository.save(studente);

				// Set corsi superati
				studente.setIdsCorsiSuperati(supera);
				// Set corsi interesse
				studente.setIdsCorsiInteresse(interesse);
				
				studente = studenteRepository.save(studente);
			}

			// //////////////////////////////////////////////////////////////////////////////////////////////
			studente = studenteRepository.save(studente);
			
			
			
			studente = studenteRepository.save(studente);

			Boolean isSuperato = new Boolean(false);

			List<CorsoLite> corsiSuperati = assignCorsiSuperati(studente);

			for (CorsoLite corso : corsiSuperati) {
				if (corso.getId() == id_corso)
					isSuperato = true;
			}

			return isSuperato;

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * Riceve oggetto Corso e lo salva come corso che seguo
	 */
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param corso
	 * @return boolean
	 * @throws IOException
	 * 
	 * Dato un corso restituisce al client true se il corso è di interesse dello studente altrimenti false 
	 * 
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/corso/seguo")
	//
	public @ResponseBody
	boolean setCorsoAsFollowUnflollow(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@RequestBody Corso corso)

	throws IOException {
		try {
			
			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			// test
			Studente studente = studenteRepository.findStudenteByUserId(userId);
			
			
			if (studente == null) {
				studente = new Studente();
				studente.setId(userId);
				studente.setNome(profile.getName());
				studente.setCognome(profile.getSurname());
				studente = studenteRepository.save(studente);

				// TEST
				List<Corso> corsiEsse3 = corsoRepository.findAll();

				String supera = null;
				String interesse = null;
				int z = 0;
				supera = new String();
				interesse = new String();

				for (Corso cors : corsiEsse3) {

					if (z % 2 == 0) {
						supera = supera.concat(String.valueOf(cors.getId())
								.concat(","));
					}
					
					if (z % 4 == 0) {
						interesse = interesse.concat(String.valueOf(cors.getId())
								.concat(","));
					}
					
					z++;
				}

				// TEST

				// Set corso follwed by studente
				studente.setCorsi(corsiEsse3);
				studente = studenteRepository.save(studente);

				// Set corsi superati
				studente.setIdsCorsiSuperati(supera);
				// Set corsi interesse
				studente.setIdsCorsiInteresse(interesse);
				
				studente = studenteRepository.save(studente);
			}
			
			
			logger.info("/corso/seguo");
			// TODO control valid field
			if (corso == null)
				return false;

			Corso corsoDaSeguire = corsoRepository.findOne(corso.getId());

			if (corsoDaSeguire != null) {
				List<CorsoLite> corsiInteresse = assignCorsiInteresse(studente);
				boolean isSeguito = false;
				
				// controllo se il corso lo seguo gia o no
				for(CorsoLite c: corsiInteresse){
					if(c.getId() == corsoDaSeguire.getId()){
						isSeguito = true;
						break;
					}
				}
				
				Studente studenteAggiornato = null;
				 
				if(isSeguito){
					studente.removeCorsoInteresse(studente, corsoDaSeguire.getId());
					studenteAggiornato = studenteRepository.save(studente);
				}else{
				
				
					studente.addCorsoInteresse(studente, corsoDaSeguire.getId());
					studenteAggiornato = studenteRepository.save(studente);
				
				
				}
				if(studenteAggiornato == null){
					return false;
				}else{
					return true;
				}

			} else
				return false;
		} catch (Exception e) {

			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return false;
		}

	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return Collection<CorsoLite>
	 * @throws IOException
	 * 
	 * Ritorna tutti i corsi che lo studente sta seguendo
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/corso/interesse/me")
	public @ResponseBody
	Collection<CorsoLite> getCorsiInteresseByMe(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)

	throws IOException {
		try {
			logger.info("/corso/interesse/me");

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			// test
			Studente studente = studenteRepository.findStudenteByUserId(userId);
			
			if (studente == null) {
				
				studente = new Studente();
				studente.setId(userId);
				studente.setNome(profile.getName());
				studente.setCognome(profile.getSurname());
				studente = studenteRepository.save(studente);


				// TEST
				List<Corso> corsiEsse3 = corsoRepository.findAll();

				String supera = null;
				String interesse = null;
				int z = 0;
				supera = new String();
				interesse = new String();

				for (Corso cors : corsiEsse3) {

					if (z % 2 == 0) {
						supera = supera.concat(String.valueOf(cors.getId())
								.concat(","));
					}

					if (z % 4 == 0) {
						interesse = interesse.concat(String.valueOf(
								cors.getId()).concat(","));
					}

					z++;
				}

				// TEST

				// Set corso follwed by studente
				studente.setCorsi(corsiEsse3);
				studente = studenteRepository.save(studente);

				// Set corsi superati
				studente.setIdsCorsiSuperati(supera);
				// Set corsi interesse
				studente.setIdsCorsiInteresse(interesse);

			}

			studente = studenteRepository.save(studente);

			return assignCorsiInteresse(studente);

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	
}
