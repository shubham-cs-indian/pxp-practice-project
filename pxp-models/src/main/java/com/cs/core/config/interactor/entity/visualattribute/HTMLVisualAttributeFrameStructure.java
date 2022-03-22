package com.cs.core.config.interactor.entity.visualattribute;

import com.cs.core.config.interactor.entity.structure.IStructureValidator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.javers.core.metamodel.annotation.DiffIgnore;

public class HTMLVisualAttributeFrameStructure extends AbstractVisualAttributeStructure
    implements IHTMLVisualAttributeFrameStructure {
  
  private static final long                            serialVersionUID = 1L;
  
  protected String                                     id;
  
  protected String                                     label;
  
  protected String                                     icon;
  
  protected String                                     referenceId;
  
  protected HTMLVisualAttributeFrameStructureValidator validator;
  
  protected String                                     variantOf;
  
  @DiffIgnore
  protected String                                     comment;
  
  @Override
  public IStructureValidator getValidator()
  {
    return this.validator;
  }
  
  @JsonDeserialize(as = HTMLVisualAttributeFrameStructureValidator.class)
  @Override
  public void setValidator(IStructureValidator validator)
  {
    this.validator = (HTMLVisualAttributeFrameStructureValidator) validator;
  }
  
  public String getReferenceId()
  {
    return referenceId;
  }
  
  public void setReferenceId(String referenceId)
  {
    this.referenceId = referenceId;
  }
  
  @Override
  public String getComment()
  {
    return comment;
  }
  
  @Override
  public void setComment(String comment)
  {
    this.comment = comment;
  }
  
  @Override
  public String getVariantOf()
  {
    return variantOf;
  }
  
  @Override
  public void setVariantOf(String variantOf)
  {
    this.variantOf = variantOf;
  }
}
