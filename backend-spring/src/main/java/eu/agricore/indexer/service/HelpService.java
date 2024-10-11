package eu.agricore.indexer.service;

import eu.agricore.indexer.model.Help;

import eu.agricore.indexer.repository.HelpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class HelpService {

    @Autowired
    private HelpRepository helpRepository;

    @Transactional(readOnly = true)
    public Optional<List<Help>> findAll() {
        List<Help> help = (List<Help>) helpRepository.findAllByOrderByCreatedAtDesc();
        return Optional.of(help);
    }

    @Transactional(readOnly = true)
    public Optional<List<Help>> findByOwner(String owner) {
        List<Help> help = helpRepository.findByOwner(owner);
        return Optional.of(help);
    }

    @Transactional()
    public Help createHelp(Help help) {
        Help result = helpRepository.save(help);
        return result;
    }

    @Transactional()
    public Help updateHelp(Help help) {
        help.setLastUpdate(new Date());
        Help result = helpRepository.save(help);
        return result;
    }

    @Transactional
    public void deleteHelpUnknown(String owner) {
        helpRepository.deleteHelpUnknown(owner);
    }


}
