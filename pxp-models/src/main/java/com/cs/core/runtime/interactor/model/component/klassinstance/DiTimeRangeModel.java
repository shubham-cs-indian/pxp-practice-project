package com.cs.core.runtime.interactor.model.component.klassinstance;

public class DiTimeRangeModel implements IDiTimeRangeModel {
  
  private static final long serialVersionUID = 1L;
  private String            to;
  private String            from;
  
  @Override
  public String getTo()
  {
    return to;
  }
  
  @Override
  public void setTo(String to)
  {
    this.to = to;
  }
  
  @Override
  public String getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(String from)
  {
    this.from = from;
  }
}
