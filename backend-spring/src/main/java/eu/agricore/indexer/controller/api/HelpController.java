package eu.agricore.indexer.controller.api;

import eu.agricore.indexer.model.Help;

import eu.agricore.indexer.service.HelpService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ApiIgnore
@CrossOrigin(allowCredentials = "true")
@RestController
@RequestMapping("/api/v1/help")
public class HelpController {
	
	@Autowired
	private HelpService helpService;


	@GetMapping(value = "")
	public ResponseEntity<?> getHelpForm() {
		Help result = new Help();
		Optional<List<Help>> helpList = helpService.findAll();
		if(helpList.isPresent() && !helpList.isEmpty()){
			if(!helpList.get().isEmpty()){
				result = helpList.get().get(0);
			} else {
				result.setContent("Help page is empty");
			}
		}
		return ResponseEntity.ok()
        		.body(result);
	}

	@GetMapping(value = "/owner", params = "owner")
	public ResponseEntity<?> getHelpFormByOwner(@RequestParam(value = "owner") String owner) {
		List<Help> result = new ArrayList<>();
		Optional<List<Help>> helpList = helpService.findByOwner(owner);
		if(helpList.isPresent() && !helpList.isEmpty()){
			result = helpList.get();
		}
		return ResponseEntity.ok()
				.body(result);
	}
	
	@PostMapping(value = "")
	public ResponseEntity<Object> createHelp(@RequestBody @Valid Help help) {
		Help helpSaved = helpService.createHelp(help);
        return ResponseEntity.ok()
        		.body(helpSaved);
	}

	@PutMapping(value = "")
	public ResponseEntity<Object> updateHelp(@RequestBody @Valid Help help) {
		Help helpSaved = helpService.updateHelp(help);
		return ResponseEntity.ok()
				.body(helpSaved);
	}


}
