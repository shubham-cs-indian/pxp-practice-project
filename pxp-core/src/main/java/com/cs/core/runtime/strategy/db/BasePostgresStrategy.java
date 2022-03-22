package com.cs.core.runtime.strategy.db;

import org.springframework.beans.factory.annotation.Autowired;

public class BasePostgresStrategy {
  
  @Autowired
  protected String postgresHost;
  
  @Autowired
  protected String postgresPort;
  
  @Autowired
  protected String postgresdatabase;
  
  @Autowired
  protected String postgresuser;
  
  @Autowired
  protected String postgrespassword;
}
