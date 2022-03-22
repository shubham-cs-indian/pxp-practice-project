package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.propertycollection.ISectionAttribute;
import com.cs.core.config.interactor.entity.variantcontext.AttributeContext;
import com.cs.core.config.interactor.entity.variantcontext.IAttributeContext;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ReferencedSectionAttributeForSwitchTypeModel
    extends AbstractReferencedSectionElementForSwitchTypeModel implements ISectionAttribute {
  
  private static final long   serialVersionUID = 1L;
  protected IAttribute        attribute;
  protected Integer           numberOfItemsAllowed;
  protected String            defaultValue     = "";
  protected Integer           precision;
  protected String            defaultUnit;
  protected IAttributeContext context;
  protected Boolean           isIdentifier     = false;
  protected String            valueAsHtml      = "";
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
    return valueAsHtml;
  }
  
  @Override
  public void setValueAsHtml(String valueAsHtml)
  {
    this.valueAsHtml = valueAsHtml;
  }
  
  @JsonIgnore
  @Override
  public String getPrefix()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setPrefix(String prefix)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public String getSuffix()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setSuffix(String suffix)
  {
    // TODO Auto-generated method stub
    
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
