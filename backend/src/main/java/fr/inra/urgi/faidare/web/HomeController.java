package fr.inra.urgi.faidare.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for the home page, which doesn't display much
 * @author JB Nizet
 */
@Controller
@RequestMapping("")
public class HomeController {
    @GetMapping
    public ModelAndView home() {
        return new ModelAndView("index");
    }
}
