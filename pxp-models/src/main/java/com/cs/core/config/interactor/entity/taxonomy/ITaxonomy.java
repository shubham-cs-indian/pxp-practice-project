package com.cs.core.config.interactor.entity.taxonomy;

import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterPropertyEntity;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.propertycollection.ISection;

import java.util.List;
import java.util.Map;

public interface ITaxonomy extends IConfigMasterPropertyEntity, ITreeEntity {
  
  public static final String BASE_TYPE            = "baseType";
  public static final String TAXONOMY_TYPE        = "taxonomyType";
  public static final String APPLIED_KLASSES      = "appliedKlasses";
  public static final String SECTIONS             = "sections";
  public static final String CODE                 = "code";
  public static final String CHILD_COUNT          = "childCount";
  public static final String LINKED_MASTER_TAG_ID = "linkedMasterTagId";
  public static final String LINKED_LEVELS        = "linkedLevels";
  public static final String LINKED_LEVEL_ID      = "linkedLevelId";
  public static final String CLASSIFIER_IID       = "classifierIID";
  public static final String IS_ROOT              = "isRoot";

  
  public List<String> getAppliedKlasses();
  
  public void setAppliedKlasses(List<String> klasses);
  
  public List<? extends ISection> getSections();
  
  public void setSections(List<? extends ISection> sections);
  
  public String getCode();
  
  public void setCode(String code);
  
  public Integer getChildCount();
  
  public void setChildCount(Integer childCount);
  
  public String getLinkedMasterTagId();
  
  public void setLinkedMasterTagId(String linkedMasterTagId);
  
  public Map<String, String> getLinkedLevels();
  
  public void setLinkedLevels(Map<String, String> linkedLevel);
  
  public String getTaxonomyType();
  
  public void setTaxonomyType(String taxonomyType);
  
  public String getLinkedLevelId();
  
  public void setLinkedLevelId(String linkedLevelId);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public Long getClassifierIID();
  
  public void setClassifierIID(Long classifierIID);
  
  public Boolean getIsRoot();
  
  public void setIsRoot(Boolean isRoot);
}
