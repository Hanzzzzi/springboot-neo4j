package org.omaha.neo4j.model.dto;

import org.neo4j.ogm.annotation.NodeEntity;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author: mor
 * @Date: 2020/12/22 17:35
 */
@NodeEntity()
public class Terminology implements Serializable {

    private static final long serialVersionUID = 6759164544354356349L;

    private String conceptId;

    private String entity;

    private String term;


    private Map<String, Object> relatedMap;

    public String getConceptId() {
        return conceptId;
    }

    public void setConceptId(String conceptId) {
        this.conceptId = conceptId;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Map<String, Object> getRelatedMap() {
        return relatedMap;
    }

    public void setRelatedMap(Map<String, Object> relatedMap) {
        this.relatedMap = relatedMap;
    }
}
