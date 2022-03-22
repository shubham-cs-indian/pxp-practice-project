package com.cs.core.config.interactor.entity.attributiontaxonomy;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public interface ITagTaxonomy extends ITag, ITaxonomy {
  
  public static final String DATA_RULES         = "dataRules";
  public static final String TASKS              = "tasks";
  public static final String EMBEDDED_KLASS_IDS = "embeddedKlassIds";
  public static final String TAG_LEVELS         = "tagLevels";
  public static final String IS_TAG             = "isTag";
  public static final String TAG_LEVEL_SEQUENCE = "tagLevelSequence";
  public static final String IS_TAXONOMY        = "isTaxonomy";
  
  public List<String> getDataRules();
  
  public void setDataRules(List<String> dataRules);
  
  public List<String> getTasks();
  
  public void setTasks(List<String> tasks);
  
  public List<String> getEmbeddedKlassIds();
  
  public void setEmbeddedKlassIds(List<String> embeddedKlassIds);
  
  public List<ITagLevelEntity> getTagLevels();
  
  public void setTagLevels(List<ITagLevelEntity> tagLevels);
  
  public Boolean getIsTag();
  
  public void setIsTag(Boolean isTaxonomyAndTag);
  
  public Boolean getIsTaxonomy();
  
  public void setIsTaxonomy(Boolean isTaxonomyAndTag);
  
  public List<String> getTagLevelSequence();
  
  public void setTagLevelSequence(List<String> tagLevelSequence);
}
