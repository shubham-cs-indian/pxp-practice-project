package com.cs.api.estordbmsmigration.model.migration;

public class ConfigDataForHelperTablesModel implements IConfigDataForHelperTablesModel {
  
  private static final long serialVersionUID = 1L;
  private String            cid              = "";
  private long              iid              = 0;
  private String            code             = "";
  private String            type             = "";
  private boolean           isNature         = false;
  private String            userName         = "";
  private String            side1ElementId   = "";
  private String            side2ElementId   = "";
  
  @Override
  public String getCid()
  {
    return cid;
  }
  
  @Override
  public void setCid(String cid)
  {
    this.cid = cid;
  }
  
  @Override
  public void setIId(long iid)
  {
    this.iid = iid;
  }
  
  @Override
  public long getIId()
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
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setIsNature(boolean isNature)
  {
    this.isNature = isNature;
  }

  @Override
  public boolean getIsNature()
  {
    return isNature;
  }
  
  @Override
  public void setUserName(String userName)
  {
    this.userName = userName;
  }
  
  @Override
  public String getUserName()
  {
    return userName;
  }

  @Override
  public void setSide1ElementId(String side1ElementId)
  {
    this.side1ElementId = side1ElementId;
  }

  @Override
  public String getSide1ElementId()
  {
    return side1ElementId;
  }

  @Override
  public void setSide2ElementId(String side2ElementId)
  {
    this.side2ElementId = side2ElementId;
  }

  @Override
  public String getSide2ElementId()
  {
    return side2ElementId;
  }

}
