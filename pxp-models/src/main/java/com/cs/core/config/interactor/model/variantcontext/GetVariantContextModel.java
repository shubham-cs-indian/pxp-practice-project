package com.cs.core.config.interactor.model.variantcontext;

import java.util.ArrayList;
import java.util.List;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ITagAndTagValuesIds;
import com.cs.core.config.interactor.entity.attributiontaxonomy.TagAndTagValuesIds;
import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.matchandmerge.AbstractSection;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.variantcontext.DefaultTimeRange;
import com.cs.core.config.interactor.entity.variantcontext.IDefaultTimeRange;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetVariantContextModel extends ConfigResponseWithAuditLogModel implements IGetVariantContextModel {
  
  private static final long                         serialVersionUID          = 1L;
  protected String                                  id;
  protected String                                  label;
  protected String                                  type;
  protected String                                  icon;
  protected String                                  iconKey;
  protected List<ITagAndTagValuesIds>               contextTags;
  protected List<String>                            statusTypeTags;
  protected List<? extends ISection>                sections                  = new ArrayList<>();
  protected Long                                    versionId;
  protected List<String>                            subContexts;
  protected List<String>                            entities;
  protected Boolean                                 isTimeEnabled             = false;
  protected View                                    defaultView;
  protected Boolean                                 isAutoCreate              = false;
  protected List<String>                            attributeIds;
  protected List<IUniqueSelectorModel>              uniqueSelections          = new ArrayList<>();
  protected Boolean                                 isStandard                = false;
  protected Boolean                                 isDuplicateVariantAllowed = false;
  protected IDefaultTimeRange                       defaultTimeRange;
  protected String                                  code;
  protected String                                  tabId;
  protected IConfigDetailsForGetVariantContextModel configDetails;
  protected Boolean                                 isCloneContext            = false;
  protected long                                    contextIID;
  
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
  public List<String> getAttributeIds()
  {
    return attributeIds;
  }
  
  @Override
  public void setAttributeIds(List<String> attributeIds)
  {
    this.attributeIds = attributeIds;
  }
  
  @Override
  public Boolean getIsAutoCreate()
  {
    return isAutoCreate;
  }
  
  @Override
  public void setIsAutoCreate(Boolean isAutoCreate)
  {
    this.isAutoCreate = isAutoCreate;
  }
  
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
  public List<ITagAndTagValuesIds> getContextTags()
  {
    return contextTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = TagAndTagValuesIds.class)
  public void setContextTags(List<ITagAndTagValuesIds> contextTags)
  {
    if (contextTags == null) {
      contextTags = new ArrayList<>();
    }
    this.contextTags = contextTags;
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
  public String getDescription()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setDescription(String description)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getTooltip()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setTooltip(String tooltip)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Boolean getIsMandatory()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setIsMandatory(Boolean isMandatory)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return isStandard;
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    this.isStandard = isStandard;
  }
  
  @Override
  public String getPlaceholder()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setPlaceholder(String placeholder)
  {
    // TODO Auto-generated method stub
    
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
  public String getIconKey()
  {
    return iconKey;
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.iconKey = iconKey;
  }
  
  @Override
  public String getType()
  {
    
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public IEntity getEntity()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public List<? extends ISection> getSections()
  {
    return sections;
  }
  
  @JsonDeserialize(contentAs = AbstractSection.class)
  @Override
  public void setSections(List<? extends ISection> sections)
  {
    this.sections = sections;
  }
  
  @Override
  public List<String> getStatusTags()
  {
    if (statusTypeTags == null) {
      statusTypeTags = new ArrayList<>();
    }
    return statusTypeTags;
  }
  
  @Override
  public void setStatusTags(List<String> statusTypeTags)
  {
    this.statusTypeTags = statusTypeTags;
  }
  
  @Override
  public List<String> getSubContexts()
  {
    if (subContexts == null) {
      return new ArrayList<String>();
    }
    return subContexts;
  }
  
  @Override
  public void setSubContexts(List<String> subContexts)
  {
    this.subContexts = subContexts;
  }
  
  @Override
  public List<String> getEntities()
  {
    if (entities == null) {
      entities = new ArrayList<>();
    }
    return entities;
  }
  
  @Override
  public void setEntities(List<String> entities)
  {
    this.entities = entities;
  }
  
  @Override
  public Boolean getIsTimeEnabled()
  {
    return isTimeEnabled;
  }
  
  @Override
  public void setIsTimeEnabled(Boolean isTimeEnabled)
  {
    this.isTimeEnabled = isTimeEnabled;
  }
  
  @Override
  public View getDefaultView()
  {
    if (defaultView == null) {
      defaultView = View.THUMBNAIL;
    }
    
    return defaultView;
  }
  
  @Override
  public void setDefaultView(View defaultView)
  {
    this.defaultView = defaultView;
  }
  
  @Override
  public Boolean getIsDuplicateVariantAllowed()
  {
    return isDuplicateVariantAllowed;
  }
  
  @Override
  public void setIsDuplicateVariantAllowed(Boolean isDuplicateVariantAllowed)
  {
    this.isDuplicateVariantAllowed = isDuplicateVariantAllowed;
  }
  
  @Override
  public List<IUniqueSelectorModel> getUniqueSelectors()
  {
    return uniqueSelections;
  }
  
  @JsonDeserialize(contentAs = UniqueSelectorModel.class)
  @Override
  public void setUniqueSelectors(List<IUniqueSelectorModel> uniqueSelections)
  {
    this.uniqueSelections = uniqueSelections;
  }
  
  @Override
  public IDefaultTimeRange getDefaultTimeRange()
  {
    return defaultTimeRange;
  }
  
  @JsonDeserialize(as = DefaultTimeRange.class)
  @Override
  public void setDefaultTimeRange(IDefaultTimeRange defaultTimeRange)
  {
    this.defaultTimeRange = defaultTimeRange;
  }
  
  
  @Override
  public String getTabId()
  {
    return tabId;
  }
  
  @Override
  public void setTabId(String tabId)
  {
    this.tabId = tabId;
  }
  
  @Override
  public IConfigDetailsForGetVariantContextModel getConfigDetails()
  {
    return configDetails;
  }
  
  @JsonDeserialize(as = ConfigDetailsForGetVariantContextModel.class)
  @Override
  public void setConfigDetails(IConfigDetailsForGetVariantContextModel configDetails)
  {
    this.configDetails = configDetails;
  }
  
  @Override
  public Boolean getIsCloneContext()
  {
    return isCloneContext;
  }
  
  @Override
  public void setIsCloneContext(Boolean isContextClone)
  {
    this.isCloneContext = isContextClone;
  }
  
  @Override
  public long getContextIID()
  {
    return contextIID;
  }
  
  @Override
  public void setContextIID(long contextIID)
  {
    this.contextIID = contextIID;
  }
}
