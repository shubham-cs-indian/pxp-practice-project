package com.cs.core.strategy.templateengine.velocity;

import org.apache.velocity.tools.generic.DateTool;
import org.springframework.stereotype.Component;

@Component("velocityDateToolWrapper")
public class VelocityDateToolWrapper{
  
  private DateTool dateToolObject;
  
  private VelocityDateToolWrapper(){
    if(dateToolObject == null){
      this.setDateToolObject(new DateTool());
    }
  }

  public DateTool getDateToolObject()
  {
    return dateToolObject;
  }

  public void setDateToolObject(DateTool dateToolObject)
  {
    this.dateToolObject = dateToolObject;
  }
  
}
