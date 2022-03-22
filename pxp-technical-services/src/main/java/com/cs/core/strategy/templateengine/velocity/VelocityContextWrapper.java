package com.cs.core.strategy.templateengine.velocity;

import org.apache.velocity.VelocityContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("velocityContextWrapper")
@Scope("prototype")
public class VelocityContextWrapper{
  
  private VelocityContext velocityContext;
  
  private VelocityContextWrapper () {
    velocityContext = new  VelocityContext();
  }
  
  public VelocityContext getVelocityContext () {
    return velocityContext;
  }
  
  public void put(String key, Object value) {
    velocityContext.put(key, value);
  } 
  
}
