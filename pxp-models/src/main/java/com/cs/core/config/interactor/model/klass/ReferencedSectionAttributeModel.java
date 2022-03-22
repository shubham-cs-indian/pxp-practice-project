package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.variantcontext.AttributeContext;
import com.cs.core.config.interactor.entity.variantcontext.IAttributeContext;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ReferencedSectionAttributeModel extends AbstractReferencedSectionElementModel
    implements IReferencedSectionAttributeModel {
  
  private static final long   serialVersionUID = 1L;
  
  protected Integer           numberOfItemsAllowed;
  protected String            defaultValue     = "";
  protected Integer           precision;
  protected String            defaultUnit;
  protected IAttributeContext context;
  protected Boolean           isIdentifier     = false;
  protected String            valueAsHtml      = "";
  protected String            prefix           = "";
  protected String            suffix           = "";
  protected Boolean           isVersionable;
  
  // Revert changes for defaultUnit & precision
  /*
  protected Integer         precision;
  protected String          defaultUnit;
  */
  
  @Override
  public Integer getNumberOfItemsAllowed()
  {
    return numberOfItemsAllowed;
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
  public IAttributeContext getContext()
  {
    return context;
  }
  
  @Override
  @JsonDeserialize(as = AttributeContext.class)
  public void setContext(IAttributeContext context)
  {
    this.context = context;
  }
  
  /**
   * ************************* ignored properties
   * ********************************
   */
  @JsonIgnore
  @Override
  public IAttribute getAttribute()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setAttribute(IAttribute attribute)
  {
    // TODO Auto-generated method stub
    
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
