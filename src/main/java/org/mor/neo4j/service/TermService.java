package org.omaha.neo4j.service;

import org.omaha.neo4j.model.dto.Terminology;
import org.omaha.neo4j.repository.TermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: mor
 * @Date: 2020/12/22 17:43
 */

@Service
public class TermService {

    @Autowired
    private TermRepository termRepository;

    public List<Terminology> findChildByName(String name, String semanticTag) {
        return termRepository.findChildByName(name,semanticTag);
    }
}
