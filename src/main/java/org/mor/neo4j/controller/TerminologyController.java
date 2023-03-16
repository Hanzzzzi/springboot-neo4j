package org.omaha.neo4j.controller;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.neo4j.driver.*;

import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;
import org.neo4j.driver.types.Relationship;
import org.omaha.neo4j.config.Neo4jDriverConfig;
import org.omaha.neo4j.model.dto.Param;
import org.omaha.neo4j.model.TermResult;
import org.omaha.neo4j.model.dto.RelationGroup;
import org.omaha.neo4j.result.ResultVO;
import org.omaha.neo4j.result.ResultCode;
import org.omaha.neo4j.result.ResultHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 图数据库的主要操作类
 */
@RestController
@RequestMapping(value = "/term")
public class TerminologyController {

	@Autowired
	private Neo4jDriverConfig driverConfig;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 获取属性关系
	 * @param request
	 * @param response
	 * @param term
	 * @param semanticTag
	 */
    @RequestMapping(value = "/getRelation")
	public ResultVO getRelation(HttpServletRequest request, HttpServletResponse response,
					 @RequestParam("term")String term, @RequestParam("semanticTag") String semanticTag) {
		List<RelationGroup> relationGroupList = new ArrayList<>();
		Map<Long,Object> maps = new HashMap<>();
		try{
			Session session = driverConfig.createDriver().session();
			List<RelationGroup> finalRelationGroupList = Lists.newArrayList();
			Result result = session.run("match a=(n:" + semanticTag + " {term:'" + term + "' })-[*]->(m) return a");
			List<Record> lists = result.list();
			for (Record list : lists) {
				for (String key : list.keys()) {
					Path path = list.get(key).asPath();
					// 遍历节点，放入maps，方便取值
					Iterable<Node> nodes = path.nodes();
					nodes.forEach(node -> {
						Map<String, Object> objectMap = node.asMap();
						String newNode = JSONObject.toJSONString(objectMap);
						TermResult termResult = JSONObject.parseObject(newNode, TermResult.class);
						node.labels().forEach(label->{
							termResult.setLabel(label);
						});
						maps.put(node.id(),termResult);

					});

					Iterable<Relationship> relationships = path.relationships();
					relationships.forEach(relationship -> {

						TermResult startNode = (TermResult) maps.get(relationship.startNodeId());
						TermResult endNode = (TermResult) maps.get(relationship.endNodeId());

						RelationGroup relationGroup = new RelationGroup();
						relationGroup.setSource(startNode.getTerm().concat("(").concat(startNode.getLabel()).concat(")"));
						relationGroup.setRela(relationship.get("property").asString());
						if(endNode.getTerm()==null){
							relationGroup.setTarget(endNode.getConceptId());
						}else {
							relationGroup.setTarget(endNode.getTerm().concat("(").concat(endNode.getLabel()).concat(")"));
						}
						// 如果起始跟目标相同，目标加上标签区分
						if(relationGroup.getSource().equals(relationGroup.getTarget())){
							relationGroup.setTarget(endNode.getTerm());
						}
//						relationGroup.setTarget(endNode.getTerm().concat("(").concat(endNode.getLabel()).concat(")"));
						relationGroup.setType("resolved");
						relationGroup.setGroups(0);

						finalRelationGroupList.add(relationGroup);

					});
				}
			}
			relationGroupList.addAll(finalRelationGroupList);
			session.close();

		}catch(Exception e){
//			logger.info(e.getCause().getMessage());
			e.printStackTrace();
			return ResultHelper.getMsgFail(ResultCode.INTERFACE_ERROR);
		}
		if(CollectionUtils.isEmpty(relationGroupList)){
			return ResultHelper.getMsgSuc(ResultCode.TIPS_INFO);
		}
		relationGroupList = relationGroupList.stream()
				.collect(Collectors.collectingAndThen(
						Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getSource() + ";" + o.getTarget()))), ArrayList::new));
		return ResultHelper.getMsgSucResult(ResultCode.SUCCESS,relationGroupList);

	}

	/**
	 * 获取父节点
	 * @param request
	 * @param response
	 * @param term
	 * @param semanticTag
	 */
	@RequestMapping(value = "/findParentByName")
	public ResultVO findParentByName(HttpServletRequest request, HttpServletResponse response,
								 @RequestParam("term")String term, @RequestParam("semanticTag") String semanticTag) {
			List<RelationGroup> relationGroupList = new ArrayList<>();
			Map<Long,Object> maps = new HashMap<>();
			try{
				Session session = driverConfig.createDriver().session();
				List<RelationGroup> finalRelationGroupList = Lists.newArrayList();
				Result result = session.run("match a=(n:" + semanticTag + " {term:'" + term + "' })-[:R001*]->(m) return a");
				List<Record> lists = result.list();
				for (Record list : lists) {
					for (String key : list.keys()) {
						Path path = list.get(key).asPath();

						Iterable<Node> nodes = path.nodes();
						nodes.forEach(node -> {
							Map<String, Object> objectMap = node.asMap();
							String newNode = JSONObject.toJSONString(objectMap);
							TermResult termResult = JSONObject.parseObject(newNode, TermResult.class);
							node.labels().forEach(label->{
								termResult.setLabel(label);
							});
							maps.put(node.id(),termResult);

						});

						Iterable<Relationship> relationships = path.relationships();
						relationships.forEach(relationship -> {
							// 遍历每一条关系，组成RelationGroup
							TermResult startNode = (TermResult) maps.get(relationship.startNodeId());
							TermResult endNode = (TermResult) maps.get(relationship.endNodeId());

							RelationGroup relationGroup = new RelationGroup();
							relationGroup.setSource(startNode.getTerm());
							relationGroup.setRela(relationship.get("property").asString());
							relationGroup.setTarget(endNode.getTerm());
							relationGroup.setType("resolved");
							relationGroup.setGroups(0);

							finalRelationGroupList.add(relationGroup);

						});
					}
				}
				relationGroupList.addAll(finalRelationGroupList);
				session.close();

			}catch(Exception e){
				logger.info(e.getMessage());
				return ResultHelper.getMsgFail(ResultCode.INTERFACE_ERROR);
			}
			if(CollectionUtils.isEmpty(relationGroupList)){
				return ResultHelper.getMsgSuc(ResultCode.TIPS_INFO);
			}
			relationGroupList = relationGroupList.stream()
					.collect(Collectors.collectingAndThen(
							Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getSource() + ";" + o.getTarget()))), ArrayList::new));
			return ResultHelper.getMsgSucResult(ResultCode.SUCCESS,relationGroupList);
	}

	/**
	 * 获取子节点
	 * @param request
	 * @param response
	 * @param term
	 * @param semanticTag
	 */
	@RequestMapping(value = "/findChildByName")
	public ResultVO findChildByName(HttpServletRequest request, HttpServletResponse response,
					 @RequestParam("term")String term, @RequestParam("semanticTag") String semanticTag) {
		List<RelationGroup> relationGroupList = new ArrayList<>();
		Map<Long,Object> maps = new HashMap<>();
		try{
			Session session = driverConfig.createDriver().session();
			List<RelationGroup> finalRelationGroupList = Lists.newArrayList();
			Result result = session.run("match a=(n:" + semanticTag + " {term:'" + term + "' })<-[:R001*]-(m) return a");
			List<Record> lists = result.list();
			for (Record list : lists) {
				for (String key : list.keys()) {
					Path path = list.get(key).asPath();

					Iterable<Node> nodes = path.nodes();
					nodes.forEach(node -> {
						Map<String, Object> objectMap = node.asMap();
						String newNode = JSONObject.toJSONString(objectMap);
						TermResult termResult = JSONObject.parseObject(newNode, TermResult.class);
						node.labels().forEach(label->{
							termResult.setLabel(label);
						});
						maps.put(node.id(),termResult);

					});

					Iterable<Relationship> relationships = path.relationships();

					relationships.forEach(relationship -> {

						// 根据得到的开始结束节点id到map查询对应的实体
						TermResult startNode = (TermResult) maps.get(relationship.startNodeId());
						TermResult endNode = (TermResult) maps.get(relationship.endNodeId());

						RelationGroup relationGroup = new RelationGroup();
						relationGroup.setSource(startNode.getTerm());
						relationGroup.setRela(relationship.get("property").asString());
						relationGroup.setTarget(endNode.getTerm());
						relationGroup.setType("resolved");
						relationGroup.setGroups(0);

						finalRelationGroupList.add(relationGroup);
					});
				}
			}
			relationGroupList.addAll(finalRelationGroupList);
			session.close();

		}catch(Exception e){
			logger.info(e.getMessage());
			return ResultHelper.getMsgFail(ResultCode.INTERFACE_ERROR);
		}
		if(CollectionUtils.isEmpty(relationGroupList)){
			return ResultHelper.getMsgSuc(ResultCode.TIPS_INFO);
		}
		relationGroupList = relationGroupList.stream()
				.collect(Collectors.collectingAndThen(
						Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getSource() + ";" + o.getTarget()))), ArrayList::new));
		return ResultHelper.getMsgSucResult(ResultCode.SUCCESS,relationGroupList);
	}

	/**
	 * 删除某两个节点的关系
	 * @param request
	 * @param response
	 * @param param
	 */
    @RequestMapping(value = "/deleteRelation")
	public ResultVO deleteRelation(HttpServletRequest request, HttpServletResponse response,
								   @RequestParam("fromLabel")String fromLabel,@RequestParam("toLabel")String toLabel,
								   @RequestParam("fromTerm")String fromTerm,@RequestParam("fromEntity")String fromEntity,
								   @RequestParam("toTerm")String toTerm,@RequestParam("toEntity")String toEntity,
								   @RequestParam("relation")String relation ){
//			@RequestBody Param param) {
//		if(StringUtils.isBlank(param.getNodeFromTerm()) || StringUtils.isBlank(param.getNodeToTerm())|| StringUtils.isBlank(param.getFromEntity())|| StringUtils.isBlank(param.getToEntity())){
//			return ResultHelper.getMsgFail(ResultCode.INVALID_PARAMETERS);
//		}
		try{
			Session session = driverConfig.createDriver().session();
			fromTerm = packageParam(fromTerm);
			fromEntity = packageParam(fromEntity);
			toTerm = packageParam(toTerm);
			toEntity = packageParam(toEntity);
//			session.run("MATCH (a:" + param.getFromLabel() + "), (b:" + param.getToLabel() + ") " +
//					"WHERE a.term = " + param.getNodeFromTerm() + " AND a.entity = " + param.getFromEntity()+ " AND b.term = " + param.getNodeToTerm()+ " AND b.entity = " + param.getToEntity()
//					+ " optional match (a)-[r:" + param.getRelation() + "]->(b) delete r");

			Result result = session.run("MATCH (a:" + fromLabel + "), (b:" + toLabel + ") " +
					"WHERE a.term = " + fromTerm + " AND a.entity = " + fromEntity + " AND b.term = " + toTerm + " AND b.entity = " + toEntity
					+ " optional match (a)-[r:" + relation + "]->(b) delete r");
	        
	       session.close();
		}catch(Exception e){
			e.printStackTrace();
			return ResultHelper.getMsgFail(ResultCode.INTERFACE_ERROR);
		}

		return ResultHelper.getMsgSuc(ResultCode.SUCCESS);
	}

	/**
	 * 创建两个节点之间的某个关系
	 * @param request
	 * @param response
	 * @param param
	 */
    @RequestMapping(value = "/createRelation")
	public ResultVO relate(HttpServletRequest request, HttpServletResponse response,
							@RequestParam("fromLabel")String fromLabel,@RequestParam("toLabel")String toLabel,
							@RequestParam("fromTerm")String fromTerm,@RequestParam("fromEntity")String fromEntity,
						   	@RequestParam("toTerm")String toTerm,@RequestParam("toEntity")String toEntity,
							@RequestParam("relation")String relation ){
//							@RequestBody Param param) {
//		if(StringUtils.isBlank(param.getNodeFromTerm()) || StringUtils.isBlank(param.getNodeToTerm())|| StringUtils.isBlank(param.getFromEntity())|| StringUtils.isBlank(param.getToEntity())){
//			return ResultHelper.getMsgFail(ResultCode.INVALID_PARAMETERS);
//		}

		try{
			Session session = driverConfig.createDriver().session();
			Set<String> toTermSet = new HashSet<>();
			Set<String> fromTermSet = new HashSet<>();
			if(relation.equals("R001")){
				Result fromResult = session.run("match a=(n:" + fromLabel + " {term:'" + fromTerm + "' })-[:R001*]->(m) return m");
				Result toResult = session.run("match a=(n:" + toLabel + " {term:'" + toTerm + "' })-[:R001*]->(m) return m");
				List<Record> toList = toResult.list();
				List<Record> fromList = fromResult.list();

				boolean flag1 = false;
				boolean flag2 = false;
				for (Record record : toList) {
					toTermSet.add(record.get(0).get("term").asString());
				}
				for (Record record : fromList) {
					fromTermSet.add(record.get(0).get("term").asString());
				}

				if(!CollectionUtils.isEmpty(toTermSet)){
					if(!toTermSet.contains(fromTerm)){
						flag1 = true;
					}
				}
				if(!CollectionUtils.isEmpty(fromTermSet)){
					if(!fromTermSet.contains(toTerm)){
						flag2 = true;
					}
				}
				if(!(flag1 && flag2)){
					return ResultHelper.getMsgFail(ResultCode.RELATIONSHIP_CONFLICT);
				}

			}
//	        session.run("MATCH (a:" + param.getFromLabel() + "), (b:" + param.getToLabel() + ") " +
//	        		"WHERE a.term = " + param.getNodeFromTerm() + " AND a.entity = " + param.getFromEntity()+ " AND b.term = " + param.getNodeToTerm()+ " AND b.entity = " + param.getToEntity()
//	        		+ " CREATE (a)-[:" + param.getRelation() + "]->(b)");

			fromTerm = packageParam(fromTerm);
			fromEntity = packageParam(fromEntity);
			toTerm = packageParam(toTerm);
			toEntity = packageParam(toEntity);
			session.run("MATCH (a:" + fromLabel + "), (b:" + toLabel + ") "
					+" WHERE a.term = " + fromTerm + " AND a.entity = " + fromEntity + " AND b.term = " + toTerm + " AND b.entity = " + toEntity
					+ " CREATE (a)-[:" + relation + "]->(b)");

			session.close();
		}catch(Exception e){
			e.printStackTrace();
			return ResultHelper.getMsgFail(ResultCode.INTERFACE_ERROR);
		}
		return ResultHelper.getMsgSuc(ResultCode.SUCCESS);
	}

	private String packageParam(String param){

    	param = "'".concat(param).concat("'");

    	return param;
	}

}