package com.cs.api.estordbmsmigration.model.migration;

public class IIDCodeModel implements IIIDCodeModel {
  
  private static final long serialVersionUID = 1L;
  private Long              iid              = 0l;
  private String            code             = "";
  
  @Override
  public void setIID(Long iid)
  {
    this.iid = iid;
  }
  
  @Override
  public Long getIID()
  {
    return iid;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
  
  @Override
  public String getCode()
  {
    return code;
  }
  
}
