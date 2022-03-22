package com.cs.core.config.interactor.model.hidden;

import java.util.ArrayList;
import java.util.List;

public class GetPropertyTranslationsHiddenModel implements IGetPropertyTranslationsHiddenModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    screens;
  protected String          id;
  protected Integer         versionId;
  protected String          label__en_US;
  protected String          label__fr_FR;
  protected String          label__es_ES;
  protected String          label__en_UK;
  protected String          label__de_DE;
  protected String          code;
  
  @Override
  public List<String> getScreens()
  {
    return screens;
  }
  
  @Override
  public void setScreens(List<String> screens)
  {
    if (screens == null) {
      screens = new ArrayList<>();
    }
    this.screens = screens;
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
  public Integer getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Integer versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public String getLabel__en_US()
  {
    return label__en_US;
  }
  
  @Override
  public void setLabel__en_US(String label__en_US)
  {
    this.label__en_US = label__en_US;
  }
  
  @Override
  public String getLabel__fr_FR()
  {
    return label__fr_FR;
  }
  
  @Override
  public void setLabel__fr_FR(String label__fr_FR)
  {
    this.label__fr_FR = label__fr_FR;
  }
  
  @Override
  public String getLabel__de_DE()
  {
    return label__de_DE;
  }
  
  @Override
  public void setLabel__de_DE(String label__de_DE)
  {
    this.label__de_DE = label__de_DE;
  }
  
  @Override
  public String getLabel__en_UK()
  {
    return label__en_UK;
  }
  
  @Override
  public void setLabel__en_UK(String label__en_UK)
  {
    this.label__en_UK = label__en_UK;
  }
  
  @Override
  public String getLabel__es_ES()
  {
    return label__es_ES;
  }
  
  @Override
  public void setLabel__es_ES(String label__es_ES)
  {
    this.label__es_ES = label__es_ES;
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
}
