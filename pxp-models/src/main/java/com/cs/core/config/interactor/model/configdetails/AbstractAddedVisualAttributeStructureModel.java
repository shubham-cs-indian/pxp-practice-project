package com.cs.core.config.interactor.model.configdetails;

import com.cs.core.config.interactor.entity.visualattribute.IVisualAttributeFrameStructure;

public abstract class AbstractAddedVisualAttributeStructureModel extends AbstractAddedStructureModel
    implements IVisualAttributeFrameStructure {
  
  public AbstractAddedVisualAttributeStructureModel(IVisualAttributeFrameStructure structure)
  {
    super(structure);
  }
  
  @Override
  public String getCode()
  {
    return ((IVisualAttributeFrameStructure) this.entity).getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    ((IVisualAttributeFrameStructure) this.entity).setCode(code);
  }
  
  @Override
  public String getDescription()
  {
    return ((IVisualAttributeFrameStructure) this.entity).getDescription();
  }
  
  @Override
  public void setDescription(String description)
  {
    ((IVisualAttributeFrameStructure) this.entity).setDescription(description);
  }
  
  @Override
  public String getTooltip()
  {
    return ((IVisualAttributeFrameStructure) this.entity).getTooltip();
  }
  
  @Override
  public void setTooltip(String tooltip)
  {
    ((IVisualAttributeFrameStructure) this.entity).setTooltip(tooltip);
  }
  
  @Override
  public Boolean getIsMandatory()
  {
    return ((IVisualAttributeFrameStructure) this.entity).getIsMandatory();
  }
  
  @Override
  public void setIsMandatory(Boolean isMandatory)
  {
    ((IVisualAttributeFrameStructure) this.entity).setIsMandatory(isMandatory);
  }
  
  @Override
  public String getPlaceholder()
  {
    return ((IVisualAttributeFrameStructure) this.entity).getPlaceholder();
  }
  
  @Override
  public void setPlaceholder(String placeholder)
  {
    ((IVisualAttributeFrameStructure) this.entity).setPlaceholder(placeholder);
  }
  
  @Override
  public String getReferenceId()
  {
    return ((IVisualAttributeFrameStructure) this.entity).getReferenceId();
  }
  
  @Override
  public void setReferenceId(String referenceId)
  {
    ((IVisualAttributeFrameStructure) this.entity).setReferenceId(referenceId);
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return ((IVisualAttributeFrameStructure) this.entity).getIsStandard();
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    ((IVisualAttributeFrameStructure) this.entity).setIsStandard(isStandard);
  }
}
