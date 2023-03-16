package org.omaha.neo4j.config;

import org.neo4j.driver.*;
import org.springframework.context.annotation.Configuration;

/**
 * 通过config注入
 * @Author: mor
 * @Date: 2020/12/25 14:57
 */

@Configuration
public class Neo4jDriverConfig {

    @org.springframework.beans.factory.annotation.Value("${neo4j.url}")
    private String neo4jUrl;

    @org.springframework.beans.factory.annotation.Value("${neo4j.username}")
    private String neo4jUserName;

    @org.springframework.beans.factory.annotation.Value("${neo4j.password}")
    private String neo4jPassWord;

    /**
     * 创建驱动
     * @return
     */
    public Driver createDriver(){
        //neo4j的服务器的配置
        //配置连接池大小
        Config config = Config.builder().withEncryption()
                .withTrustStrategy(Config.TrustStrategy.trustAllCertificates())
                .withMaxConnectionPoolSize(32).build();
        return GraphDatabase.driver( neo4jUrl, AuthTokens.basic( neo4jUserName, neo4jPassWord ),config );
    }
}
