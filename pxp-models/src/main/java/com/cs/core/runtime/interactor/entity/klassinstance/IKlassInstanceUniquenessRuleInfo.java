package com.cs.core.runtime.interactor.entity.klassinstance;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import java.util.List;

public interface IKlassInstanceUniquenessRuleInfo extends IEntity {
  
  public static final String RULE_ID                         = "ruleId";
  public static final String KLASS_INSTANCE_ID_BASETYPE_LIST = "klassInstanceIdBasetypeList";
  
  public String getRuleId();
  
  public void setRuleId(String ruleId);
  
  public List<IIdAndBaseType> getKlassInstanceIdBasetypeList();
  
  public void setKlassInstanceIdBasetypeList(List<IIdAndBaseType> klassInstanceIdBasetypeList);
}
