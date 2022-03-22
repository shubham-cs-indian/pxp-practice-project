package com.cs.core.config.interactor.model.attributiontaxonomy;

import com.cs.core.config.interactor.entity.attributiontaxonomy.MasterTaxonomy;

public class CreateMasterTaxonomyModel implements ICreateMasterTaxonomyModel {
  
  private static final long serialVersionUID = 1L;
  protected String          taxonomyId;
  protected String          parentTaxonomyId;
  protected String          parentTagId;
  protected String          tagValueId;
  protected Boolean         isNewlyCreated   = false;
  protected String          label;
  protected String          taxonomyType;
  protected String          code;
  protected String          baseType         = MasterTaxonomy.class.getName();
  protected Long            classifierIID;
  
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
  public String getTaxonomyId()
  {
    return taxonomyId;
  }
  
  @Override
  public void setTaxonomyId(String taxonomyId)
  {
    this.taxonomyId = taxonomyId;
  }
  
  @Override
  public String getParentTaxonomyId()
  {
    return parentTaxonomyId;
  }
  
  @Override
  public void setParentTaxonomyId(String parentTaxonomyId)
  {
    this.parentTaxonomyId = parentTaxonomyId;
  }
  
  @Override
  public String getParentTagId()
  {
    return parentTagId;
  }
  
  @Override
  public void setParentTagId(String parentTagId)
  {
    this.parentTagId = parentTagId;
  }
  
  @Override
  public String getTagValueId()
  {
    return tagValueId;
  }
  
  @Override
  public void setTagValueId(String tagValueId)
  {
    this.tagValueId = tagValueId;
  }
  
  @Override
  public Boolean getIsNewlyCreated()
  {
    return isNewlyCreated;
  }
  
  @Override
  public void setIsNewlyCreated(Boolean isNewlyCreated)
  {
    this.isNewlyCreated = isNewlyCreated;
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
  public String getTaxonomyType()
  {
    return taxonomyType;
  }
  
  @Override
  public void setTaxonomyType(String taxonomyType)
  {
    this.taxonomyType = taxonomyType;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public Long getClassifierIID()
  {
    return classifierIID;
  }
  
  @Override
  public void setClassifierIID(Long classifierIID)
  {
    this.classifierIID = classifierIID;
  }
}
