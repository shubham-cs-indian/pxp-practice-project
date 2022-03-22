package com.cs.core.config.interactor.model.language;

public class GetLanguagesInfoModel implements IGetLanguagesInfoModel {
  
  private static final long serialVersionUID = 1L;
  private String            id;
  private String            label;
  private String            type;
  private String            icon;
  private String            iconKey;
  private String            code;
  private String            dateFormat;
  private String            numberFormat;
  private String            localeId;
  private String            abbreviation;
  private Long              iid;
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public String getIcon()
  {
    return icon;
  }
  
  @Override
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
  
  @Override
  public String getIconKey()
  {
    return iconKey;
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.iconKey = iconKey;
  }
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
  
  @Override
  public String getNumberFormat()
  {
    return numberFormat;
  }
  
  @Override
  public void setNumberFormat(String numberFormat)
  {
    this.numberFormat = numberFormat;
  }
  
  @Override
  public String getDateFormat()
  {
    return dateFormat;
  }
  
  @Override
  public void setDateFormat(String dateFormat)
  {
    this.dateFormat = dateFormat;
  }
  
  @Override
  public String getLocaleId()
  {
    return localeId;
  }
  
  @Override
  public void setLocaleId(String localeId)
  {
    this.localeId = localeId;
  }
  
  @Override
  public String getAbbreviation()
  {
    return abbreviation;
  }
  
  @Override
  public void setAbbreviation(String abbreviation)
  {
    this.abbreviation = abbreviation;
  }

  @Override
  public Long getIid()
  {
    return iid;
  }

  @Override
  public void setIid(Long iid)
  {
    this.iid = iid;
  }
}
