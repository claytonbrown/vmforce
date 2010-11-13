package com.vmforce.hmbdemo;

import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.salesforce.connector.SFDCServiceConnector;
import com.sforce.ws.ConnectionException;

/**
 * Handles requests for the application welcome page.
 */
@Controller
public class WelcomeController {

	private Logger logger = org.slf4j.LoggerFactory.getLogger(WelcomeController.class);

	@PersistenceContext
	EntityManager em;
	
	@Autowired
	SFDCServiceConnector sfdc;
		
	/**
	 * Simply selects the welcome view to render by returning void and relying
	 * on the default request-to-view-translator.
	 * @throws ConnectionException 
	 */
	@RequestMapping(value="/", method = RequestMethod.GET)
	public ModelAndView welcome() throws ConnectionException {
		logger.info("Welcome!");
		ModelAndView mv = new ModelAndView();
		mv.addObject("contacts",em.createQuery("SELECT c FROM Contact c").setMaxResults(10).getResultList());
		mv.addObject("userInfo",sfdc.getConnection().getUserInfo());
		mv.setViewName("welcome");
		return mv;
	}
	
	@RequestMapping(value="/{contactId}/create", method = RequestMethod.GET)
	public ModelAndView prepareCreateAttendeeFromContact(@PathVariable String contactId) {
		
		Contact c = em.find(Contact.class, contactId);
		
		HMBAttendee attendee = new HMBAttendee();
		attendee.setRelatedContact(c);

		Calendar cal = Calendar.getInstance();
		cal.set(2010, 8, 15);
		
		attendee.setArrivalDate(cal.getTime());

		cal = Calendar.getInstance();
		cal.set(2010, 8, 17);

		attendee.setDepartureDate(cal.getTime());
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("attendee",attendee);
		mv.addObject("mealOptions",HMBAttendee.MealPrefEnum.values());
		mv.setViewName("create_attendee");
		return mv;
	}

	@RequestMapping(value="/{contactId}/create", method = RequestMethod.POST)
	public ModelAndView createAttendeeFromContact(@ModelAttribute HMBAttendee attendee, @PathVariable String contactId) {
		createAttendee(attendee, contactId);
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("attendee",attendee);
		mv.setViewName("attendee");
		return mv;
	}

	@Transactional
	private void createAttendee(HMBAttendee attendee, String contactId) {
		Contact c = em.find(Contact.class, contactId);

		attendee.setRelatedContact(c);
		
		attendee.setName(c.getFirstName()+" "+c.getLastName());
		
		em.persist(attendee);
	}
	
	

}
