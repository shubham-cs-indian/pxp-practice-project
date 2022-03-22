package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;

import java.util.List;

public interface IKlassInstanceWithDataRuleModel extends IModel {
  
  public static final String KLASS_INSTANCE      = "klassInstance";
  public static final String DATA_RULES_OF_KLASS = "dataRulesOfKlass";
  public static final String TYPE_KLASS          = "typeKlass";
  public static final String IS_AUTO_CREATE      = "isAutoCreate";
  public static final String CONFIG_DETAILS      = "configDetails";
  
  public IKlassInstance getKlassInstance();
  
  public void setKlassInstance(IKlassInstance articleInstance);
  
  public List<IDataRuleModel> getDataRulesOfKlass();
  
  public void setDataRulesOfKlass(List<IDataRuleModel> dataRulesOfKlass);
  
  public IKlass getTypeKlass();
  
  public void setTypeKlass(IKlass typeKlass);
  
  public Boolean getIsAutoCreate();
  
  public void setIsAutoCreate(Boolean isAutoCreate);
  
  public IGetConfigDetailsForCustomTabModel getConfigDetails();
  
  public void setConfigDetails(IGetConfigDetailsForCustomTabModel configDetails);
}
