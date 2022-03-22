package com.cs.core.runtime.abstrct.versions;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.BaseEntityPlanDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBaseEntityPlanDTO;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.json.JSONContent;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdAndTypeModel;
import com.cs.core.runtime.interactor.model.marketingarticleinstance.BulkCreateOrRestoreModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;

@Component
public abstract class AbstractRestoreInstanceService<P extends IIdsListParameterModel, R extends IBulkResponseModel>
    extends AbstractRuntimeService<P, R> {
  
  protected abstract String getBaseType();
  
  @Autowired
  protected RDBMSComponentUtils rdbmsComponentUtils;

  private static final String SERVICE_FOR_RESTORE = "RESTORE_INSTANCE_TASK";

  @SuppressWarnings("unchecked")
  @Override
  protected R executeInternal(P idListParameterModel) throws Exception
  {
    List<String> idsToRestore = idListParameterModel.getIds();
    validate(idsToRestore);
    for (String id : idsToRestore) {
      IIdAndTypeModel idAndTypeModel = new IdAndTypeModel();
      idAndTypeModel.setId(id);
      idAndTypeModel.setType(getBaseType());
      prepareMessageData(id, idAndTypeModel);
    }
    return (R) new BulkCreateOrRestoreModel();
  }
  
  protected void prepareMessageData(String id, IIdAndTypeModel idAndTypeModel) throws Exception
  {
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.MEDIUM;

    IBaseEntityPlanDTO dto = new BaseEntityPlanDTO();
    dto.setBaseEntityIIDs(Arrays.asList(Long.parseLong(id)));
    dto.setLocaleID(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getLocaleID());
    dto.setCatalogCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getCatalogCode());
    dto.setOrganizationCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getOrganizationCode());
    dto.setUserId(transactionThread.getTransactionData().getUserId());
    BGPDriverDAO.instance().submitBGPProcess(rdbmsComponentUtils.getUserName(), SERVICE_FOR_RESTORE, "", userPriority,
        new JSONContent(dto.toJSON()));
  }
  
  protected void validate(List<String> ids) throws Exception
  {
  }
}
