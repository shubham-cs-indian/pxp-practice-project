package com.cs.core.runtime.usecase.task;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.Constants;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.AutoCreateVariantDTO;
import com.cs.core.bgprocess.idto.IAutoCreateVariantDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.variantcontext.IDefaultTimeRange;
import com.cs.core.config.interactor.model.variantcontext.IReferencedUniqueSelectorModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWrapperModel;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCreateVariantModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.variants.CreateVariantModel;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForAutoCreateVariantInstanceStrategy;
import com.cs.core.runtime.interactor.usecase.taskexecutor.IAutoCreateVariantInstanceTaskService;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.runtime.utils.threadPoolExecutor.ThreadPoolExecutorUtil;
import com.cs.core.runtime.variant.articleinstance.ICreateArticleVariantInstanceService;
import com.cs.core.runtime.variant.assetinstance.ICreateAssetInstanceVariantService;
import com.cs.core.runtime.variant.marketinstance.ICreateMarketInstanceVariantService;
import com.cs.core.runtime.variant.textassetinstance.ICreateTextAssetInstanceVariantService;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.util.VariantUtils;
import com.cs.utils.BaseEntityUtils;

@Service
public class AutoCreateVariantInstanceTaskService
    extends AbstractRuntimeService<ITechnicalImageVariantWrapperModel, IIdParameterModel>
    implements IAutoCreateVariantInstanceTaskService {
  
  @Autowired
  protected VariantUtils                                          variantUtils;
  
  @Autowired
  protected IGetConfigDetailsForAutoCreateVariantInstanceStrategy getConfigDetailsForAutoCreateVariantInstanceStrategy;
  
  @Autowired
  protected TransactionThreadData                                 transactionThreadData;

  @Autowired
  protected RDBMSComponentUtils                                   rdbmsComponentUtils;
  
  @Autowired
  protected ThreadPoolExecutorUtil threadPoolTaskExecutorUtil;
  
  @Override
  public IIdParameterModel executeInternal(ITechnicalImageVariantWrapperModel dataModel)
      throws Exception
  {
    ITransactionData transactionData = transactionThreadData.getTransactionData();
    ITechnicalImageVariantWithAutoCreateEnableModel contextWithAutoCreateEnableModel = dataModel.getVariantModel();
    Map<String, ITag> tagValueMap = contextWithAutoCreateEnableModel.getTagValueMap();
    List<IReferencedUniqueSelectorModel> uniqueSelectors = contextWithAutoCreateEnableModel.getUniqueSelectors();
    String parentId = dataModel.getParentId();
    String klassInstanceId = dataModel.getKlassInstanceId();
    
    IBaseEntityDTO baseEntityDto = rdbmsComponentUtils.getBaseEntityDTO(Long.parseLong(klassInstanceId));
    IBaseEntityDAO baseEntityDao = rdbmsComponentUtils.getBaseEntityDAO(baseEntityDto);
    
    List<String> classifiers = BaseEntityUtils.getAllReferenceTypeFromBaseEntity(baseEntityDao);
    
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    multiclassificationRequestModel.setContextId(contextWithAutoCreateEnableModel.getId());
    multiclassificationRequestModel.setOrganizationId(transactionData.getOrganizationId());
    multiclassificationRequestModel.setPhysicalCatalogId(transactionData.getPhysicalCatalogId());
    multiclassificationRequestModel.setEndpointId(transactionData.getEndpointId());
    multiclassificationRequestModel.setParentKlassIds(classifiers);
    multiclassificationRequestModel.setParentTaxonomyIds(classifiers);
    multiclassificationRequestModel.setBaseType(dataModel.getBaseType());
    IGetConfigDetailsForCreateVariantModel configDetails = getConfigDetailsForAutoCreateVariantInstanceStrategy.execute(multiclassificationRequestModel);
    String contextId = contextWithAutoCreateEnableModel.getId();
    IReferencedVariantContextModel referencedContext = configDetails.getReferencedVariantContexts().getEmbeddedVariantContexts().get(contextId);
    IDefaultTimeRange defaultTimeRange = referencedContext.getDefaultTimeRange();
    if (referencedContext.getIsTimeEnabled() && checkIsDefaultTimeRangeEmpty(defaultTimeRange)) {
      return null;
    }
    String instanceName = dataModel.getInstanceName();
    
    for (IReferencedUniqueSelectorModel uniqueSelector : uniqueSelectors) {
      List<IContentTagInstance> contextualTagInstances = variantUtils
          .getContextualTagInstancesFromUniqueSelector(uniqueSelector);
      String label = variantUtils.getLabel(instanceName, contextualTagInstances, tagValueMap);
      
      ICreateVariantModel createVaraintModel = new CreateVariantModel();
      createVaraintModel.setParentId(parentId);
      createVaraintModel.setTypes(new ArrayList<>(configDetails.getReferencedKlasses().keySet()));
      createVaraintModel.setContextId(contextId);
      createVaraintModel.setBaseType(dataModel.getBaseType());
      createVaraintModel.setName(label);
      createVaraintModel.setContextTags(contextualTagInstances);
      createVaraintModel.setId(dataModel.getKlassInstanceId());
      createVaraintModel.setTimeRange(VariantUtils.getDefaultTimeRange(referencedContext));
      submitBGPVariantInstanceTask(baseEntityDto, createVaraintModel);
    }
    return new IdParameterModel();
  }

  private boolean checkIsDefaultTimeRangeEmpty(IDefaultTimeRange defaultTimeRange)
  {
    if (defaultTimeRange == null || defaultTimeRange.getTo() == null) {
      return true;
    }
    if ((defaultTimeRange.getIsCurrentTime() == null || !defaultTimeRange.getIsCurrentTime())
        && defaultTimeRange.getFrom() == null) {
      return true;
    }
    return false;
  }
  
  /**
   * Prepare Auto Create Variant instance model and submit bgp to create the variant
   * Added this task as BGP process because issue in creating parent revision for multiple variants
   * try to create same revision number for parent in different transaction
   * @param baseEntityDto
   * @param createVaraintModel
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   * @throws RDBMSException
   * @throws CSFormatException
   */
  private void submitBGPVariantInstanceTask(IBaseEntityDTO baseEntityDto, ICreateVariantModel createVaraintModel)
      throws IllegalAccessException, InvocationTargetException, RDBMSException, CSFormatException
  {
    AutoCreateVariantDTO autoCreateVariantDTO = new AutoCreateVariantDTO();
    autoCreateVariantDTO.setBaseEntity(baseEntityDto);
    autoCreateVariantDTO.setConfigData(ObjectMapperUtil.convertValue(createVaraintModel, JSONObject.class));
    autoCreateVariantDTO.setTransaction(transactionThreadData.getTransactionData());
    BGPDriverDAO.instance().submitBGPProcess(rdbmsComponentUtils.getUserName(), IAutoCreateVariantDTO.CREATE_VARIANT_SERVICE, "",
        IBGProcessDTO.BGPPriority.HIGH, autoCreateVariantDTO.toJSONContent());
  }
  
}
