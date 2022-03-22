package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGetKlassEntityConfigDetailsModel extends IModel {
  
  public static final String REFERENCED_TAGS                                      = "referencedTags";
  public static final String REFERENCED_ATTRIBUTES                                = "referencedAttributes";
  public static final String TECHNICAL_IMAGEVARIANT_CONTEXT_WITH_AUTOCREATEENABLE = "technicalImageVariantContextWithAutoCreateEnable";
  public static final String REFERENCED_CONTEXTS                                  = "referencedContexts";
  public static final String REFERENCED_KLASSES                                   = "referencedKlasses";
  public static final String REFERENCED_TASKS                                     = "referencedTasks";
  public static final String REFERENCED_DATARULES                                 = "referencedDataRules";
  public static final String REFERENCED_TABS                                      = "referencedTabs";
  public static final String REFERENCED_TAXONOMIES                                = "referencedTaxonomies";
  public static final String REFERENCED_RELATIONSHIPS                             = "referencedRelationships";
  
  public List<ITechnicalImageVariantWithAutoCreateEnableModel> getTechnicalImageVariantContextWithAutoCreateEnable();
  
  public void setTechnicalImageVariantContextWithAutoCreateEnable(
      List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantContextWithAutoCreateEnable);
  
  public Map<String, IPropagableContextKlassInformationModel> getReferencedKlasses();
  
  public void setReferencedKlasses(
      Map<String, IPropagableContextKlassInformationModel> ReferencedKlasses);
  
  public Map<String, ? extends ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ? extends ITag> referencedTags);
  
  public Map<String, ? extends IAttribute> getReferencedAttributes();
  
  public void setReferencedAttributes(Map<String, ? extends IAttribute> referencedAttributes);
  
  public Map<String, IConfigEntityInformationModel> getReferencedRelationships();
  
  public void setReferencedRelationships(
      Map<String, IConfigEntityInformationModel> referencedRelationships);
  
  public Map<String, IConfigEntityInformationModel> getReferencedContexts();
  
  public void setReferencedContexts(Map<String, IConfigEntityInformationModel> referencedContexts);
  
  public Map<String, IConfigEntityInformationModel> getReferencedTasks();
  
  public void setReferencedTasks(Map<String, IConfigEntityInformationModel> referencedTasks);
  
  public Map<String, String> getReferencedDataRules();
  
  public void setReferencedDataRules(Map<String, String> referencedDataRules);
  
  public Map<String, IConfigEntityInformationModel> getReferencedTabs();
  
  public void setReferencedTabs(Map<String, IConfigEntityInformationModel> referencedTabs);
  
  public Map<String, IIdLabelCodeModel> getReferencedTaxonomies();
  
  public void setReferencedTaxonomies(Map<String, IIdLabelCodeModel> referencedTaxonomies);
}
