package com.cs.core.config.interactor.usecase.rulelist;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.rulelist.IRuleListModel;
import com.cs.core.config.rulelist.ISaveRuleListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveRuleList extends AbstractSaveConfigInteractor<IRuleListModel, IRuleListModel>
    implements ISaveRuleList {
  
  @Autowired
  protected ISaveRuleListService saveRuleListService;
  
  @Override
  public IRuleListModel executeInternal(IRuleListModel dataRuleModel) throws Exception
  {
    return saveRuleListService.execute(dataRuleModel);
  }

}
