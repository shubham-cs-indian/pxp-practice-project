package com.cs.core.config.interactor.model.smartdocument.preset;

import java.util.Map;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.language.IGetLanguagesInfoModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;

public interface IGetSmartDocumentPresetModel extends ISmartDocumentPresetModel, IConfigResponseWithAuditLogModel {
  
  public static final String REFERENCED_ATTRIBUTES = "referencedAttributes";
  public static final String REFERENCED_TAGS       = "referencedTags";
  public static final String REFERENCED_KLASSES    = "referencedKlasses";
  public static final String REFERENCED_TAXONOMIES = "referencedTaxonomies";
  public static final String REFERENCED_LANGUAGES  = "referencedLanguages";
  
  public Map<String, IAttribute> getReferencedAttributes();
  
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes);
  
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  public Map<String, IIdLabelCodeModel> getReferencedKlasses();
  
  public void setReferencedKlasses(Map<String, IIdLabelCodeModel> referencedKlasses);
  
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies();
  
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies);
  
  public Map<String, IGetLanguagesInfoModel> getReferencedLanguages();
  
  public void setReferencedLanguages(Map<String, IGetLanguagesInfoModel> referencedLanguages);
}
