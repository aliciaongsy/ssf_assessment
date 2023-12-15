package vttp.ssf.assessment.eventmanagement.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vttp.ssf.assessment.eventmanagement.models.Details;
import vttp.ssf.assessment.eventmanagement.repositories.RedisRepository;
import vttp.ssf.assessment.eventmanagement.services.DatabaseService;

@Controller
@RequestMapping
public class RegistrationController {

    @Autowired
    DatabaseService dbSvc;

    @Autowired
    RedisRepository redisRepo;

    // TODO: Task 6
    @GetMapping(path = "/events/register/{id}")
    public String register(@PathVariable("id") String id, HttpSession sess, Model model) {
        sess.setAttribute("id", id);

        System.out.println(id);
        model.addAttribute("event", redisRepo.findById(id));
        model.addAttribute("details", new Details());
        return "eventregister";
    }

    // TODO: Task 7
    @PostMapping("/registration/register")
    public String processRegistration(@Valid @ModelAttribute("details") Details details, BindingResult result,
            HttpSession sess, Model model) {

        // System.out.println(result);
        System.out.println(details.getNumberOfTickets());

        // check for validation error
        if (result.hasErrors()) {
            // model.addAttribute("id", (String) sess.getAttribute("id"));
            // return "redirect:/events/register/{id}";

            model.addAttribute("event", redisRepo.findById((String) sess.getAttribute("id")));
            sess.setAttribute("details", details);
            return "eventregister";
        }

        // check age
        if (dbSvc.calculateAge(details.getBirthDate(),new Date())<21){
            System.out.println("age:"+dbSvc.calculateAge(details.getBirthDate(),new Date()));
            model.addAttribute("error","Your age does not meet the requirement.");
            return "errorregistration";
        }

        // check count
        if (!(dbSvc.countCheck((String) sess.getAttribute("id"), details.getNumberOfTickets()))){
            System.out.println("Number of tickets requested" + details.getNumberOfTickets());
            model.addAttribute("error","Your request for tickets exceeded the event size.");
            return "errorregistration";
        }
        // no error
        // increment participants count
        redisRepo.incrementCount((String) sess.getAttribute("id"), details.getNumberOfTickets());
        model.addAttribute("event", redisRepo.findById((String) sess.getAttribute("id")));
        return "successregistration";
    }
}
