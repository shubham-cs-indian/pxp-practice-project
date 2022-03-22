package com.cs.core.runtime.variant;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.templating.IGetNumberOfVersionsToMaintainResponseModel;
import com.cs.core.config.interactor.model.variantcontext.BulkDeleteVariantsReturnModel;
import com.cs.core.config.strategy.usecase.globalpermissions.IGetNatureKlassPermissionStrategy;
import com.cs.core.rdbms.entity.dto.ProductDeleteDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IProductDeleteDTO;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteVariantsReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListWithVersionIdModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdsListWithVersionIdModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.variants.IDeleteVariantModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetNumberOfVersionsToMaintainStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.VariantInstanceUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public abstract class AbstractDeleteInstanceVariantsService<P extends IDeleteVariantModel, R extends IBulkDeleteVariantsReturnModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected ISessionContext                       context;
  
  @Autowired
  protected IGetNatureKlassPermissionStrategy     getNatureKlassWithPermissionStrategy;
  
  @Autowired
  protected RDBMSComponentUtils                   rdbmsComponentUtils;

  @Autowired
  protected IGetNumberOfVersionsToMaintainStrategy getNumberOfVersionsToMaintainStrategy;

  @Autowired
  protected VariantInstanceUtils                   variantInstanceUtils;

  @Override
  @SuppressWarnings("unchecked")
  protected R executeInternal(P deleteModel) throws Exception
  {
    List<String> ids = deleteModel.getIds();
    List<Long> iids = new ArrayList<>();
    List<String> successfulIds = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    IProductDeleteDTO entryData = new ProductDeleteDTO();
    
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(Long.parseLong(ids.get(0)));
    IBaseEntityDTO parentDTO = baseEntityDAO.getParent();

    for(String id : ids) {
      iids.add(Long.parseLong(id));
    }
    variantInstanceUtils.deleteVariants(iids, successfulIds, failure, entryData);
    
    IIdsListWithVersionIdModel success = new IdsListWithVersionIdModel();
    success.setIds(successfulIds);
    
    if (deleteModel.isShouldCreateRevision()) {
      IGetNumberOfVersionsToMaintainResponseModel configDetailsForDeleteTranslation = getNumberOfVersionsToMaintain(parentDTO);
      rdbmsComponentUtils.createNewRevision(parentDTO, configDetailsForDeleteTranslation.getNumberOfVersionsToMaintain());
    }

    IBulkDeleteVariantsReturnModel bulkDeleteResponseModel = new BulkDeleteVariantsReturnModel();
    bulkDeleteResponseModel.setFailure(failure);
    bulkDeleteResponseModel.setSuccess(success);
    return (R) bulkDeleteResponseModel;

  }

  protected IGetNumberOfVersionsToMaintainResponseModel getNumberOfVersionsToMaintain(
      IBaseEntityDTO baseEntityDTO ) throws Exception
  {
    List<String> classifiers = new ArrayList<>();
    classifiers.add(baseEntityDTO
        .getNatureClassifier()
        .getClassifierCode());
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    multiclassificationRequestModel.setKlassIds(classifiers);
    return getNumberOfVersionsToMaintainStrategy.execute(multiclassificationRequestModel);
  }

  /**
   * Function to handle module specific usecase before deleting the instance
   * 
   * @param baseEntityDAO
   * @throws RDBMSException 
   */
  protected void preProcessData(IBaseEntityDAO baseEntityDAO) throws Exception {
    
  }
}
