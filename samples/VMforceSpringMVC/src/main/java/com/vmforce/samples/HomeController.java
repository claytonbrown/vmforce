package com.vmforce.samples;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@PersistenceContext
	EntityManager em;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView home(ModelAndView mv) {
		mv.addObject("wines", em.createQuery("SELECT w FROM Wine w").getResultList());
		mv.setViewName("home");
		return mv;
	}
	
	@RequestMapping(value="/", method=RequestMethod.POST)
	public String newWine() {
		Wine w = createWine();
		logger.info("Created wine: "+w.getName()+" (with id "+w.getId()+")");
		
		return "redirect:/";
	}
	
	@Transactional
	private Wine createWine() {
		Producer p = new Producer();
		p.setName("La Tache AOC");
		em.persist(p);
		Wine w = new Wine();
		w.setName("1929 La Tache AOC");
		w.setProducer(p);
		em.persist(w);		
		return w;
	}
	
}

