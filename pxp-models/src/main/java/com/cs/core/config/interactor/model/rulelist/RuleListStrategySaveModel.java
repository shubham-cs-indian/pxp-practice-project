package com.cs.core.config.interactor.model.rulelist;

import java.util.ArrayList;
import java.util.List;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.tag.DataRuleModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class RuleListStrategySaveModel extends ConfigResponseWithAuditLogModel
    implements IRuleListStrategySaveModel {
  
  private static final long      serialVersionUID   = 1L;
  protected IRuleList            entity;
  protected List<String>         klassIds;
  protected List<IDataRuleModel> dataRuleList;
  protected Boolean              isRuleListModified = false;
  
  public RuleListStrategySaveModel()
  {
    this.entity = new RuleList();
    this.klassIds = new ArrayList<>();
  }
  
  public RuleListStrategySaveModel(IRuleList entity, List<String> klassIds)
  {
    this.entity = entity;
    this.klassIds = klassIds;
  }
  
  @Override
  public String getCode()
  {
    return entity.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    entity.setCode(code);
  }
  
  @Override
  public IEntity getEntity()
  {
    return entity;
  }
  
  @Override
  public String getId()
  {
    return entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    entity.setId(id);
  }
  
  @Override
  public Long getVersionId()
  {
    return entity.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.entity.setVersionId(versionId);
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return entity.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.entity.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return entity.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.entity.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public String getLabel()
  {
    return entity.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    this.entity.setLabel(label);
  }
  
  @Override
  public List<String> getList()
  {
    return entity.getList();
  }
  
  @Override
  public void setList(List<String> list)
  {
    this.entity.setList(list);
  }
  
  @Override
  public List<String> getKlassIds()
  {
    if (klassIds == null) {
      klassIds = new ArrayList<>();
    }
    return klassIds;
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    this.klassIds = klassIds;
  }

 
  @Override
  public List<IDataRuleModel> getDataRuleList()
  {
    if(dataRuleList == null) {
      dataRuleList = new ArrayList<>();
    }
    return dataRuleList;
  }

  @Override
  @JsonDeserialize(contentAs = DataRuleModel.class)
  public void setDataRuleList(List<IDataRuleModel> dataRuleList)
  {
    this.dataRuleList = dataRuleList;
  }

  @Override
  public Boolean getIsRuleListModififed()
  {
    return isRuleListModified;
  }

  @Override
  public void setIsRuleListModified(Boolean isRuleListModified)
  {
    this.isRuleListModified = isRuleListModified;
  }
}
