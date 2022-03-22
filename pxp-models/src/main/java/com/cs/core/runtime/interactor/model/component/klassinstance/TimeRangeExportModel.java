package com.cs.core.runtime.interactor.model.component.klassinstance;

public class TimeRangeExportModel implements ITimeRangeExportModel {
  
  private static final long serialVersionUID = 1L;
  protected String          from;
  protected String          to;
  
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
}
