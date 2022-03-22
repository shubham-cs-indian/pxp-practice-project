package com.cs.core.bgprocess.services.variant;

import java.util.Map;

import com.cs.constants.Constants;
import com.cs.core.bgprocess.dto.AutoCreateVariantDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.businessapi.base.IService;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.variants.CreateImageVariantsModel;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.runtime.variant.articleinstance.ICreateArticleVariantInstanceService;
import com.cs.core.runtime.variant.assetinstance.ICreateAssetInstanceVariantService;
import com.cs.core.runtime.variant.marketinstance.ICreateMarketInstanceVariantService;
import com.cs.core.runtime.variant.textassetinstance.ICreateTextAssetInstanceVariantService;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class CreateInstanceVariant extends AbstractBGProcessJob implements IBGProcessJob {
  
  private final AutoCreateVariantDTO autoCreateVariantDTO = new AutoCreateVariantDTO();
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    autoCreateVariantDTO.fromJSON(jobData.getEntryData().toString());
  }
  
  @Override
  public BGPStatus runBatch() throws RDBMSException, CSFormatException, CSInitializationException, PluginException, Exception
  {
    jobData.setStatus(BGPStatus.RUNNING);
    jobData.getLog().progress(log() + "Starting from status %s\n", jobData.getStatus());
    Map<String, Object> configData = autoCreateVariantDTO.getConfigData();
    ICreateVariantModel dataModel = (ICreateVariantModel) ObjectMapperUtil.convertValue(configData, CreateImageVariantsModel.class);
    IService<IModel, IModel> createVariantService = getBean(getVariantTaskName(dataModel.getBaseType()));
    TransactionThreadData transactionThreadData = getBean(TransactionThreadData.class);
    transactionThreadData.setTransactionData(autoCreateVariantDTO.getTransaction());
    createVariantService.execute(dataModel);
    jobData.setStatus(BGPStatus.ENDED_SUCCESS);
    jobData.getProgress().setPercentageCompletion(100);
    jobData.getLog().progress(log() + "Ended with status %s\n", jobData.getStatus());
    return jobData.getStatus();
  }
  
  @SuppressWarnings("unchecked")
  public <T> T getVariantTaskName(String baseType)
  {
    switch (baseType) {
      case Constants.ARTICLE_INSTANCE_BASE_TYPE:
        return (T) ICreateArticleVariantInstanceService.class;
      
      case Constants.ASSET_INSTANCE_BASE_TYPE:
        return (T) ICreateAssetInstanceVariantService.class;
      
      case Constants.MARKET_INSTANCE_BASE_TYPE:
        return (T) ICreateMarketInstanceVariantService.class;
      
      case Constants.TEXTASSET_INSTANCE_BASE_TYPE:
        return (T) ICreateTextAssetInstanceVariantService.class;
    }
    return null;
  }
  
}
