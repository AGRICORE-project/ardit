package eu.agricore.indexer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class HomeController {
    @RequestMapping ("/api/v1")
    public String apiRoot() {
    	return "redirect:/swagger-ui.html";
    }
    @RequestMapping ("/")
    public String home() {
    	return "redirect:/api/v1";
    }
}
