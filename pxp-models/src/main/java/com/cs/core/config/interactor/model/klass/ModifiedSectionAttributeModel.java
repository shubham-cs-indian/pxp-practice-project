package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.model.variantcontext.IModifiedContextModel;
import com.cs.core.config.interactor.model.variantcontext.ModifiedContextModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ModifiedSectionAttributeModel extends AbstractModifiedSectionElementModel
    implements IModifiedSectionAttributeModel {
  
  private static final long       serialVersionUID = 1L;
  
  protected Integer               numberOfItemsAllowed;
  protected String                defaultValue     = "";
  protected Integer               precision;
  protected String                defaultUnit;
  protected IModifiedContextModel context;
  protected Boolean               isIdentifier     = false;
  protected String                valueAsHtml      = "";
  protected String                prefix;
  protected String                suffix;
  protected Boolean               isVersionable;
  
  
  @Override
  public Integer getNumberOfItemsAllowed()
  {
    return this.numberOfItemsAllowed;
  }
  
  @Override
  public void setNumberOfItemsAllowed(Integer numberOfItemsAllowed)
  {
    this.numberOfItemsAllowed = numberOfItemsAllowed;
  }
  
  @Override
  public String getDefaultValue()
  {
    return defaultValue;
  }
  
  @Override
  public void setDefaultValue(String defaultValue)
  {
    this.defaultValue = defaultValue;
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
  public IModifiedContextModel getContext()
  {
    return context;
  }
  
  @Override
  @JsonDeserialize(as = ModifiedContextModel.class)
  public void setContext(IModifiedContextModel context)
  {
    this.context = context;
  }
  
  @Override
  public Boolean getIsIdentifier()
  {
    return isIdentifier;
  }
  
  @Override
  public void setIsIdentifier(Boolean isIdentifier)
  {
    this.isIdentifier = isIdentifier;
  }
  
  @Override
  public String getValueAsHtml()
  {
    return valueAsHtml;
  }
  
  @Override
  public void setValueAsHtml(String valueAsHtml)
  {
    this.valueAsHtml = valueAsHtml;
  }
  
  @Override
  public String getPrefix()
  {
    return prefix;
  }
  
  @Override
  public void setPrefix(String prefix)
  {
    this.prefix = prefix;
  }
  
  @Override
  public String getSuffix()
  {
    return suffix;
  }
  
  @Override
  public void setSuffix(String suffix)
  {
    this.suffix = suffix;
  }
  
  @Override
  public Boolean getIsVersionable()
  {
    return isVersionable;
  }

  @Override
  public void setIsVersionable(Boolean isVersionable)
  {
    this.isVersionable = isVersionable;
  }
}
