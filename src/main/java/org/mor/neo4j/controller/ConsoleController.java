package org.omaha.neo4j.controller;

import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.omaha.neo4j.config.Neo4jDriverConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: mor
 * @Date: 2021/1/4 15:15
 */

@RestController
public class ConsoleController {

    @Autowired
    private Neo4jDriverConfig driverConfig;

    @RequestMapping(value = { "", "/", "/index" })
    public ModelAndView index(ModelMap map) throws IOException {
        List<String> allLabels = getAllLabels();
        map.put("labels",allLabels);
        return new ModelAndView("/index");
    }

    private List<String> getAllLabels(){
        List<String> labelList = new ArrayList<>();
        try{
            Session session = driverConfig.createDriver().session();
            Result result = session.run("call db.labels(); ");
            List<Record> list = result.list();
            for (Record record : list) {
                labelList.add(record.get(0).asString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return labelList;
    }
}
