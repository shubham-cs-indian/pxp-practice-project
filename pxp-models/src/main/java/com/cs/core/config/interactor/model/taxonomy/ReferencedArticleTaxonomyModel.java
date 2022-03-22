package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.config.interactor.entity.attributiontaxonomy.ITagLevelEntity;
import com.cs.core.config.interactor.entity.attributiontaxonomy.TagLevelEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(value = "versionId")
public class ReferencedArticleTaxonomyModel implements IReferencedArticleTaxonomyModel {
  
  private static final long                        serialVersionUID    = 1L;
  
  protected String                                 id;
  protected String                                 label;
  protected String                                 icon                = "";
  protected String                                 iconKey             = "";
  protected List<String>                           propertyCollections = new ArrayList<>();
  protected List<IReferencedTaxonomyChildrenModel> children            = new ArrayList<>();
  protected IReferencedTaxonomyParentModel         parent;
  protected String                                 taxonomyType;
  protected List<String>                           tags                = new ArrayList<>();
  protected String                                 parentTagId;
  protected List<ITagLevelEntity>                  tagLevels;
  protected String                                 code;
  protected String                                 baseType;
  protected long                                   classifierIID;
  
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
  public List<String> getPropertyCollections()
  {
    if (propertyCollections == null) {
      propertyCollections = new ArrayList<>();
    }
    return propertyCollections;
  }
  
  @Override
  public void setPropertyCollections(List<String> propertyCollections)
  {
    this.propertyCollections = propertyCollections;
  }
  
  @Override
  public String getIcon()
  {
    return icon;
  }
  
  @Override
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
  
  @Override
  public List<IReferencedTaxonomyChildrenModel> getChildren()
  {
    return children;
  }
  
  @JsonDeserialize(contentAs = ReferencedTaxonomyChildrenModel.class)
  @Override
  public void setChildren(List<IReferencedTaxonomyChildrenModel> children)
  {
    this.children = children;
  }
  
  @Override
  public IReferencedTaxonomyParentModel getParent()
  {
    return parent;
  }
  
  @JsonDeserialize(as = ReferencedTaxonomyParentModel.class)
  @Override
  public void setParent(IReferencedTaxonomyParentModel parent)
  {
    this.parent = parent;
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
  public List<ITagLevelEntity> getTagLevels()
  {
    return tagLevels;
  }
  
  @Override
  @JsonDeserialize(contentAs = TagLevelEntity.class)
  public void setTagLevels(List<ITagLevelEntity> tagLevels)
  {
    this.tagLevels = tagLevels;
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
  
  public String getBaseType()
  {
    return baseType;
  }
  
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public long getClassifierIID()
  {
    return classifierIID;
  }
  
  @Override
  public void setClassifierIID(long classifierIID)
  {
    this.classifierIID = classifierIID;
  }

  @Override
  public String getIconKey()
  {
    return iconKey;
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.iconKey = iconKey;
  }
}
