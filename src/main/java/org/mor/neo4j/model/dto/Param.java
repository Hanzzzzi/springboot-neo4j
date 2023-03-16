package org.omaha.neo4j.model.dto;

/**
 * 前台传递封装参数
 */
public class Param {

	private String id;
	private String node;
	private String relation;
	private String property;
	private String label;

	// 术语起始节点
	private String nodeFromTerm;
	// 起始术语标签
	private String FromLabel;
	// 源实体
	private String FromEntity;
	// 术语目标节点
	private String nodeToTerm;
	// 目标术语标签
	private String ToLabel;
	// 目标实体
	private String ToEntity;

	private String where;
	private String update;
	private String result;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getNodeFromTerm() {
		return nodeFromTerm;
	}

	public void setNodeFromTerm(String nodeFromTerm) {
		this.nodeFromTerm = nodeFromTerm;
	}

	public String getFromLabel() {
		return FromLabel;
	}

	public void setFromLabel(String fromLabel) {
		FromLabel = fromLabel;
	}

	public String getNodeToTerm() {
		return nodeToTerm;
	}

	public void setNodeToTerm(String nodeToTerm) {
		this.nodeToTerm = nodeToTerm;
	}

	public String getToLabel() {
		return ToLabel;
	}

	public void setToLabel(String toLabel) {
		ToLabel = toLabel;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public String getUpdate() {
		return update;
	}

	public void setUpdate(String update) {
		this.update = update;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getFromEntity() {
		return FromEntity;
	}

	public void setFromEntity(String fromEntity) {
		FromEntity = fromEntity;
	}

	public String getToEntity() {
		return ToEntity;
	}

	public void setToEntity(String toEntity) {
		ToEntity = toEntity;
	}
}
