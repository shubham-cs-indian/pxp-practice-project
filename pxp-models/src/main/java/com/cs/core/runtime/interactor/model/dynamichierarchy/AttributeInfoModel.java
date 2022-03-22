package com.cs.core.runtime.interactor.model.dynamichierarchy;

import com.cs.core.config.interactor.entity.visualattribute.HTMLVisualAttributeFrameStructureValidator;
import com.cs.core.config.interactor.entity.visualattribute.IVisualAttributeValidator;
import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class AttributeInfoModel extends ConfigEntityInformationModel
    implements IAttributeInfoModel {
  
  private static final long                            serialVersionUID = 1L;
  
  protected String                                     defaultUnit;
  
  protected Integer                                    precision;
  
  protected HTMLVisualAttributeFrameStructureValidator validator        = new HTMLVisualAttributeFrameStructureValidator();
  
  @Override
  public String getDefaultUnit()
  {
    return defaultUnit;
  }
  
  @Override
  public void setDefaultUnit(String defaultUnit)
  {
    this.defaultUnit = defaultUnit;
  }
  
  @Override
  public Integer getPrecision()
  {
    return precision;
  }
  
  @Override
  public void setPrecision(Integer precision)
  {
    this.precision = precision;
  }
  
  @Override
  public IVisualAttributeValidator getValidator()
  {
    return this.validator;
  }
  
  @JsonDeserialize(as = HTMLVisualAttributeFrameStructureValidator.class)
  @Override
  public void setValidator(IVisualAttributeValidator validator)
  {
    this.validator = (HTMLVisualAttributeFrameStructureValidator) validator;
  }
}
