package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.HTMLAttribute;
import com.cs.core.config.interactor.entity.attribute.IHTMLAttribute;
import com.cs.core.config.interactor.entity.visualattribute.HTMLVisualAttributeFrameStructureValidator;
import com.cs.core.config.interactor.entity.visualattribute.IVisualAttributeValidator;
import com.cs.core.config.interactor.model.configdetails.AbstractAttributeModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class HTMLAttributeModel extends AbstractAttributeModel implements IHTMLAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public HTMLAttributeModel()
  {
    super(new HTMLAttribute(), Renderer.HTML.toString());
  }
  
  public HTMLAttributeModel(IHTMLAttribute attribute)
  {
    super(attribute, Renderer.HTML.toString());
  }
  
  @Override
  public IVisualAttributeValidator getValidator()
  {
    return ((IHTMLAttribute) attribute).getValidator();
  }
  
  @JsonDeserialize(as = HTMLVisualAttributeFrameStructureValidator.class)
  @Override
  public void setValidator(IVisualAttributeValidator validator)
  {
    ((IHTMLAttribute) attribute).setValidator(validator);
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
  
  @Override
  public String toString()
  {
    try {
      return ObjectMapperUtil.writeValueAsString(this);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
