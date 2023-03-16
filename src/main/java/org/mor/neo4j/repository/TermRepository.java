package org.omaha.neo4j.repository;

import org.omaha.neo4j.model.dto.Terminology;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: mor
 * @Date: 2020/12/22 17:40
 */

@Repository
public interface TermRepository extends CrudRepository<Terminology,Long> {

    @Query("match (n:`疾病` {name:'苔癣样皮肤结核' })-[:R001]->(m) return m ")
//    @Query("match (n:`疾病`) return n limit 50")
//    @Query("START  category = node:__types__(className='org.omaha.neo4j.model.Term') " +
//            "MATCH  category-[:R001]->child " +
//            "WHERE  category.name={0}" +
//            "RETURN child")
    List<Terminology> findChildByName(@Param("name") String name, @Param("semanticTag") String semanticTag);
}
