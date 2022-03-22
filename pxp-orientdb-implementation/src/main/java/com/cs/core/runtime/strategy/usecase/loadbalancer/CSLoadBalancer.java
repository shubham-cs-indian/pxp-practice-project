package com.cs.core.runtime.strategy.usecase.loadbalancer;

import com.google.common.collect.Lists;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.LoadBalancerBuilder;
import com.netflix.loadbalancer.Server;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CSLoadBalancer {
  
  static BaseLoadBalancer baseLoadBalancer;
  /*  @Autowired
  protected String        elasticSearchHost;
  @Autowired
  protected String        elasticSearchPort;*/
  List<Server>            elasticServers;
  
  public List<Server> getElasticServers()
  {
    return elasticServers;
  }
  
  public void setElasticServers()
  {
    this.elasticServers = Lists.newArrayList();
    /* Iterator<String> hostIterator = elasticSearchHost.iterator();
    Iterator<String> portIterator = elasticSearchPort.iterator();
    while (hostIterator.hasNext() && portIterator.hasNext()) {
      this.elasticServers
          .add(new Server(hostIterator.next(), Integer.parseInt(portIterator.next())));
    }*/
    System.out.println("==========================================================");
    /*  System.out.println(elasticSearchHost);
    */
    /*  String[] hostList = elasticSearchHost.split(",");
    String[] portList = elasticSearchPort.split(",");*/
    /*  int len = hostList.length;
    int i = 0;
    while (len >= portList.length && i < len) {
      this.elasticServers.add(new Server(hostList[i], Integer.parseInt(portList[i])));
      i++;
    }*/
  }
  
  public BaseLoadBalancer getBaseLoadBalancer()
  {
    setBaseLoadBalancer();
    return baseLoadBalancer;
  }
  
  public void setBaseLoadBalancer()
  {
    setElasticServers();
    if (!(elasticServers.isEmpty()) && elasticServers != null) {
      baseLoadBalancer = LoadBalancerBuilder.newBuilder()
          .buildFixedServerListLoadBalancer(elasticServers);
    }
  }
}
