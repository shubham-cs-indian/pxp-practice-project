package com.cs.core.config.interactor.model.variantcontext;

import java.util.List;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ITagAndTagValuesIds;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.variantcontext.IDefaultTimeRange;

public interface IGetVariantContextModel extends IVariantContextModel, IConfigResponseWithAuditLogModel {
  
  public static final String ID                 = "id";
  public static final String LABEL              = "label";
  public static final String CONTEXT_TAGS       = "contextTags";
  public static final String SECTIONS           = "sections";
  public static final String STATUS_TAGS        = "statusTags";
  public static final String SUB_CONTEXTS       = "subContexts";
  public static final String ATTRIBUTE_IDS      = "attributeIds";
  public static final String UNIQUE_SELECTORS   = "uniqueSelectors";
  public static final String DEFAULT_TIME_RANGE = "defaultTimeRange";
  public static final String CONFIG_DETAILS     = "configDetails";
  public static final String IS_CLONE_CONTEXT   = "isCloneContext";
  
  public List<String> getSubContexts();
  
  public void setSubContexts(List<String> subContexts);
  
  public String getId();
  
  public void setId(String id);
  
  public List<ITagAndTagValuesIds> getContextTags();
  
  public void setContextTags(List<ITagAndTagValuesIds> contextTags);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public List<? extends ISection> getSections();
  
  public void setSections(List<? extends ISection> sections);
  
  public List<String> getStatusTags();
  
  public void setStatusTags(List<String> statusTypeTags);
  
  public List<String> getAttributeIds();
  
  public void setAttributeIds(List<String> attributeIds);
  
  public List<IUniqueSelectorModel> getUniqueSelectors();
  
  public void setUniqueSelectors(List<IUniqueSelectorModel> uniqueSelections);
  
  public IDefaultTimeRange getDefaultTimeRange();
  
  public void setDefaultTimeRange(IDefaultTimeRange defaultTimeRange);
  
  public IConfigDetailsForGetVariantContextModel getConfigDetails();
  
  public void setConfigDetails(IConfigDetailsForGetVariantContextModel configDetails);
  
  public Boolean getIsCloneContext();
  
  public void setIsCloneContext(Boolean isCloneContext);
}
