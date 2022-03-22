package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.config.interactor.entity.attributiontaxonomy.ITagLevelEntity;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IReferencedArticleTaxonomyModel extends IModel {
  
  public static final String ID                   = "id";
  public static final String PROPERTY_COLLECTIONS = "propertyCollections";
  public static final String LABEL                = "label";
  public static final String ICON                 = "icon";
  public static final String ICON_KEY             = "iconKey";
  public static final String CHILDREN             = "children";
  public static final String PARENT               = "parent";
  public static final String TAXONOMY_TYPE        = "taxonomyType";
  public static final String TAG_LEVELS           = "tagLevels";
  public static final String PARENT_TAG_ID        = "parentTagId";
  public static final String CODE                 = "code";
  public static final String BASETYPE             = "baseType";
  public static final String CLASSIFIER_IID       = "classifierIID";
  
  public String getId();
  
  public void setId(String id);
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public List<String> getPropertyCollections();
  
  public void setPropertyCollections(List<String> propertyCollections);
  
  public String getIcon();
  
  public void setIcon(String icon);
  
  public String getIconKey();
  
  public void setIconKey(String icon);
  
  public List<IReferencedTaxonomyChildrenModel> getChildren();
  
  public void setChildren(List<IReferencedTaxonomyChildrenModel> children);
  
  public IReferencedTaxonomyParentModel getParent();
  
  public void setParent(IReferencedTaxonomyParentModel parent);
  
  public String getTaxonomyType();
  
  public void setTaxonomyType(String taxonomyType);
  
  public List<ITagLevelEntity> getTagLevels();
  
  public void setTagLevels(List<ITagLevelEntity> tagLevels);
  
  public String getParentTagId();
  
  public void setParentTagId(String parentTagId);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public long getClassifierIID();
  
  public void setClassifierIID(long classifierIID);
}
