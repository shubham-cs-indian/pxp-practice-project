package com.cs.core.config.interactor.entity.propertycollection;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.variantcontext.AttributeContext;
import com.cs.core.config.interactor.entity.variantcontext.IAttributeContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class SectionAttribute extends AbstractSectionElement implements ISectionAttribute {
  
  private static final long   serialVersionUID = 1L;
  
  protected IAttribute        attribute;
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
  
  @Override
  public IAttribute getAttribute()
  {
    return this.attribute;
  }
  
  @JsonDeserialize(as = AbstractAttribute.class)
  @Override
  public void setAttribute(IAttribute attribute)
  {
    this.attribute = attribute;
  }
  
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
    if (valueAsHtml == null) {
      valueAsHtml = "";
    }
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
