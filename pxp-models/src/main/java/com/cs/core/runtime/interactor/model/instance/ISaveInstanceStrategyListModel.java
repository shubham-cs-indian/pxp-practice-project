package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.config.interactor.model.role.IRoleModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;

import java.util.Collection;
import java.util.Map;

public interface ISaveInstanceStrategyListModel extends IModel {
  
  public static final String KLASS_INSTANCE                 = "klassInstance";
  public static final String VARIANT_AND_BRANCH_LIST_MAP    = "variantAndBranchListMap";
  public static final String IS_ROLLBACK                    = "isRollback";
  public static final String ROLES                          = "roles";
  public static final String CONFIG_DETAILS                 = "configDetails";
  public static final String IS_TYPE_SWITCH                 = "isTypeSwitch";
  public static final String IS_CREATE_TANSLATABLE_INSTANCE = "isCreateTanslatableInstance";
  
  public IKlassInstanceSaveModel getKlassInstance();
  
  // TODO; change the function name
  // FIXME: change IModel to ISaveInstanceStrategyModel
  public void setKlassInstance(IKlassInstanceSaveModel klassInstance);
  
  // TODO:ask ajit can we move it to ISaveArticleKlassInstanceStrategyModel
  public Map<String, Object> getVariantAndBranchListMap();
  
  public void setVariantAndBranchListMap(Map<String, Object> variantAndBranchListMap);
  
  public Boolean getIsRollback();
  
  public void setIsRollback(Boolean isRollback);
  
  public Collection<? extends IRoleModel> getRoles();
  
  public void setRoles(Collection<? extends IRoleModel> collection);
  
  public IGetConfigDetailsModel getConfigDetails();
  
  public void setConfigDetails(IGetConfigDetailsModel configDetails);
  
  public Boolean getIsTypeSwitch();
  
  public void setIsTypeSwitch(Boolean isTypeSwitch);
  
  public Boolean getIsCreateTanslatableInstance();
  
  public void setIsCreateTanslatableInstance(Boolean isCreateTanslatableInstance);
}
