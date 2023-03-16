package org.omaha.neo4j.model;

import org.neo4j.ogm.annotation.NodeEntity;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * @Author: mor
 * @Date: 2020/12/22 17:35
 */
@NodeEntity()
public class TermResult implements Serializable {

    private static final long serialVersionUID = 6759164544354356349L;

    private String label;

    private String conceptId;

    private String entity;

    private String term;

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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "TermResult{" +
                "label='" + label + '\'' +
                ", conceptId='" + conceptId + '\'' +
                ", entity='" + entity + '\'' +
                ", term='" + term + '\'' +
                '}';
    }
}
