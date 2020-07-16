package com.digital.kaimur.utils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticConfig {
    @Value("${elasticsearch.host}")
    public String host;
    @Value("${elasticsearch.port}")
    public int port;
    public String getHost() {
return host;
}
public int getPort() {
return port;
    }

	@Bean(destroyMethod = "close")
    public RestHighLevelClient client(){
		RestHighLevelClient  client = null;
        try{
        	 client = new RestHighLevelClient(
                     RestClient.builder(new HttpHost(host,port)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return client;
    }
}