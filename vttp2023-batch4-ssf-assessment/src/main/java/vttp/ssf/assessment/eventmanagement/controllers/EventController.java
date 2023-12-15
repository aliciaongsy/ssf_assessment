package vttp.ssf.assessment.eventmanagement.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import vttp.ssf.assessment.eventmanagement.models.Event;
import vttp.ssf.assessment.eventmanagement.services.DatabaseService;

@Controller
@RequestMapping
public class EventController {

	@Autowired
	DatabaseService dbSvc;

	//TODO: Task 5
	@GetMapping(path = "/events/listing")
	public String displayEvents(Model model, HttpSession sess){
		model.addAttribute("events",dbSvc.retrieveList());
		sess.setAttribute("events",dbSvc.retrieveList());

		List<String> dateList = new ArrayList<>();
		for (Event e:dbSvc.retrieveList()){
			dateList.add(dbSvc.convertDate(e.getEventDate()));
			model.addAttribute("date:%s".formatted(e.getEventId()), dbSvc.convertDate(e.getEventDate()));
		}
		
		return "eventlist";
	}

}
