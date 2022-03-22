package com.cs.core.config.interactor.model.attributiontaxonomy;

import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;

import java.util.List;
import java.util.Map;

public interface IGetAttributionTaxonomyModel extends IMasterTaxonomy, IConfigModel {
  
  public static final String REFERENCED_KLASSES             = "referencedKlasses";
  public static final String SECTIONS                       = "sections";
  public static final String DEFAULT_VALUES_DIFF            = "defaultValuesDiff";
  public static final String DELETED_PROPERTIES_FROM_SOURCE = "deletedPropertiesFromSource";
  public static final String REFERENCED_CONTEXTS            = "referencedContexts";
  public static final String REFERENCED_TAGS                = "referencedTags";
  public static final String REFERENCED_EVENTS              = "referencedEvents";
  public static final String REFERENCED_TASKS               = "referencedTasks";
  public static final String REFERENCED_DATARULES           = "referencedDataRules";
  
  public Map<String, List<String>> getDeletedPropertiesFromSource();
  
  public void setDeletedPropertiesFromSource(Map<String, List<String>> deletedPropertiesFromSource);
  
  public Map<String, IKlassInformationModel> getReferencedKlasses();
  
  public void setReferencedKlasses(Map<String, IKlassInformationModel> ReferencedKlasses);
  
  public List<? extends ISection> getSections();
  
  public void setSections(List<? extends ISection> sections);
  
  public List<IDefaultValueChangeModel> getDefaultValuesDiff();
  
  public void setDefaultValuesDiff(List<IDefaultValueChangeModel> defaultValuesDiff);
  
  public Map<String, String> getReferencedContexts();
  
  public void setReferencedContexts(Map<String, String> referencedContexts);
  
  // key:tagId
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  public Map<String, String> getReferencedEvents();
  
  public void setReferencedEvents(Map<String, String> referencedEvents);
  
  public Map<String, String> getReferencedTasks();
  
  public void setReferencedTasks(Map<String, String> referencedTasks);
  
  public Map<String, String> getReferencedDataRules();
  
  public void setReferencedDataRules(Map<String, String> referencedDataRules);
}
