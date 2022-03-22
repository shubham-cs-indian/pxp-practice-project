package com.cs.core.config.datarule;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractDeleteConfigService;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.DataRuleDeleteDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.bgprocess.idto.IDataRuleDeleteDTO;
import com.cs.core.config.strategy.usecase.datarule.IDeleteDataRuleStrategy;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.runtime.interactor.model.bulkpropagation.ContentDiffModelToPrepareDataForBulkPropagation;
import com.cs.core.runtime.interactor.model.bulkpropagation.IContentDiffModelToPrepareDataForBulkPropagation;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;

@Service
public class DeleteDataRuleService extends AbstractDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteDataRuleService {
  
  private static final String       DATA_RULE_DELETE = "DATA_RULE_DELETE";
  
  private static final BGPPriority  BGP_PRIORITY     = BGPPriority.LOW;
  
  @Autowired
  private RDBMSComponentUtils rdbmsComponentUtils;
  
  @Autowired
  protected IDeleteDataRuleStrategy orientDBDeleteDataRuleStrategy;

  @Override
  public IBulkDeleteReturnModel executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    IBulkDeleteReturnModel responseModel = orientDBDeleteDataRuleStrategy.execute(dataModel);
    List<String> deletedDataRuleIds = dataModel.getIds();
    if (!deletedDataRuleIds.isEmpty()) {
      IContentDiffModelToPrepareDataForBulkPropagation contentDiffModelToPrepareDataForBulkPropagation = new ContentDiffModelToPrepareDataForBulkPropagation();
      contentDiffModelToPrepareDataForBulkPropagation.setDeletedDataRuleIds(deletedDataRuleIds);
      
      // BGP
      deleteDataRuleViolations(responseModel);
    }
    return responseModel;
  }
  
  private void deleteDataRuleViolations(IBulkDeleteReturnModel responseModel)
      throws Exception
  {
    IDataRuleDeleteDTO dataRuleDeleteDTO = new DataRuleDeleteDTO();
    ILocaleCatalogDTO localeCatalogDTO = rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO();
    
    dataRuleDeleteDTO.setLocaleID(localeCatalogDTO.getLocaleID());
    dataRuleDeleteDTO.setCatalogCode(localeCatalogDTO.getCatalogCode());
    
    dataRuleDeleteDTO.setRuleCodes(new HashSet<String>((List<String>) responseModel.getSuccess()));
    BGPDriverDAO.instance().submitBGPProcess(context.getUserName(), DATA_RULE_DELETE, "", BGP_PRIORITY,
        new JSONContent(dataRuleDeleteDTO.toJSON()));
  }
}
