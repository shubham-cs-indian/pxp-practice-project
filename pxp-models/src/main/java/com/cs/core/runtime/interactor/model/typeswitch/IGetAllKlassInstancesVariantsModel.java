package com.cs.core.runtime.interactor.model.typeswitch;

import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.role.IRoleModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Collection;
import java.util.List;

public interface IGetAllKlassInstancesVariantsModel extends IModel {
  
  public static final String DIMENSIONAL_TAG_IDS = "dimensionalTagIds";
  public static final String CURRENT_USER_ID     = "currentUserId";
  public static final String ROLES               = "roles";
  public static final String IDS                 = "ids";
  public static final String VARIANT_ID          = "variantId";
  public static final String PARENT              = "parent";
  public static final String DATA_RULES_OF_KLASS = "dataRulesOfKlass";
  
  public Collection<String> getDimensionalTagIds();
  
  public void setDimensionalTagIds(Collection<String> dimensionalTagIds);
  
  public String getCurrentUserId();
  
  public void setCurrentUserId(String currentUserId);
  
  public List<IRoleModel> getRoles();
  
  public void setRoles(List<IRoleModel> roles);
  
  public List<String> getIds();
  
  public void setIds(List<String> getAllChildren);
  
  public int getVariantId();
  
  public void setVariantId(int variantId);
  
  public String getParent();
  
  public void setParent(String parent);
  
  public List<IDataRuleModel> getDataRulesOfKlass();
  
  public void setDataRulesOfKlass(List<IDataRuleModel> dataRulesOfKlass);
}
