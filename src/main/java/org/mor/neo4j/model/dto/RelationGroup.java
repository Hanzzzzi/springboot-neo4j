package org.omaha.neo4j.model.dto;

import java.io.Serializable;

/**
 * @Author: mor
 * @Date: 2020/12/28 20:32
 */
public class RelationGroup implements Serializable {

    private static final long serialVersionUID = 4881101502274887598L;

    private String source;

    private String rela;

    private String target;

    private String type;

    private Integer groups;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRela() {
        return rela;
    }

    public void setRela(String rela) {
        this.rela = rela;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getGroups() {
        return groups;
    }

    public void setGroups(Integer groups) {
        this.groups = groups;
    }
}
