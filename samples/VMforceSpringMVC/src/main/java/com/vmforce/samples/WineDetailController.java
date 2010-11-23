package com.vmforce.samples;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles requests for the application home page.
 */
@Controller
public class WineDetailController {

	private static final Logger logger = LoggerFactory.getLogger(WineDetailController.class);

	@PersistenceContext
	EntityManager em;

	@Transactional
	@ModelAttribute("wine")
	public Wine initWine(@PathVariable String id) {
		return em.find(Wine.class, id);
	}
	
	@RequestMapping(value="/wine/{id}", method=RequestMethod.GET)
	public ModelAndView view(ModelAndView mv, @ModelAttribute Wine wine) {
		mv.addObject("wine", wine);
		mv.addObject("varietyOptions", Wine.Variety.values());
		mv.setViewName("view_wine");
		return mv;
	}

	@Transactional
	@RequestMapping(value="/wine/{id}", method=RequestMethod.POST)
	public String update(ModelAndView mv, @ModelAttribute Wine wine) {
		em.merge(wine);
		return "redirect:/wine/"+wine.getId();
	}
	
}

