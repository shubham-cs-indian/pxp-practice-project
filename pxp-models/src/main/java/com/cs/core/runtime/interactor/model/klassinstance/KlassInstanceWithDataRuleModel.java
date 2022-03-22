package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.ProjectKlass;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.tag.DataRuleModel;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class KlassInstanceWithDataRuleModel implements IKlassInstanceWithDataRuleModel {
  
  private static final long                    serialVersionUID = 1L;
  protected IKlassInstance                     klassInstance;
  protected List<IDataRuleModel>               dataRulesOfKlass;
  protected IKlass                             typeKlass;
  protected Boolean                            isAutoCreate;
  protected IGetConfigDetailsForCustomTabModel configDetails;
  
  @Override
  public IKlassInstance getKlassInstance()
  {
    return klassInstance;
  }
  
  @Override
  public void setKlassInstance(IKlassInstance klassInstance)
  {
    this.klassInstance = klassInstance;
  }
  
  @Override
  public List<IDataRuleModel> getDataRulesOfKlass()
  {
    if (dataRulesOfKlass == null) {
      dataRulesOfKlass = new ArrayList<>();
    }
    return dataRulesOfKlass;
  }
  
  @JsonDeserialize(contentAs = DataRuleModel.class)
  @Override
  public void setDataRulesOfKlass(List<IDataRuleModel> dataRulesOfKlass)
  {
    this.dataRulesOfKlass = dataRulesOfKlass;
  }
  
  @Override
  public IKlass getTypeKlass()
  {
    return typeKlass;
  }
  
  @JsonDeserialize(as = ProjectKlass.class)
  @Override
  public void setTypeKlass(IKlass typeKlass)
  {
    this.typeKlass = typeKlass;
  }
  
  @Override
  public Boolean getIsAutoCreate()
  {
    if (isAutoCreate == null) {
      isAutoCreate = false;
    }
    return isAutoCreate;
  }
  
  @Override
  public void setIsAutoCreate(Boolean isAutoCreate)
  {
    this.isAutoCreate = isAutoCreate;
  }
  
  @Override
  public IGetConfigDetailsForCustomTabModel getConfigDetails()
  {
    return configDetails;
  }
  
  @Override
  @JsonDeserialize(as = ProjectKlass.class)
  public void setConfigDetails(IGetConfigDetailsForCustomTabModel configDetails)
  {
    this.configDetails = configDetails;
  }
}
