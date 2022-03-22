package com.cs.core.bgprocess.services.variant;

import java.util.Map;

import com.cs.core.bgprocess.dto.AutoCreateVariantDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.config.interactor.model.variantcontext.TechnicalImageVariantWithAutoCreateEnableWrapperModel;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.dam.runtime.taskexecutor.IGetAssetAndCreateVariantService;

public class AutoCreateTIVInstance extends AbstractBGProcessJob implements IBGProcessJob {
  
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
    TechnicalImageVariantWithAutoCreateEnableWrapperModel dataModel = ObjectMapperUtil.convertValue(configData, TechnicalImageVariantWithAutoCreateEnableWrapperModel.class);
    IGetAssetAndCreateVariantService autoCreateTIVService = getBean(IGetAssetAndCreateVariantService.class);
    TransactionThreadData transactionThreadData = getBean(TransactionThreadData.class);
    transactionThreadData.setTransactionData(autoCreateVariantDTO.getTransaction());
    autoCreateTIVService.execute(dataModel);
    jobData.getProgress().setPercentageCompletion(100);
    jobData.setStatus(BGPStatus.ENDED_SUCCESS);
    jobData.getLog().progress(log() + "Ended with from status %s\n", jobData.getStatus());
    return jobData.getStatus();
  }
}
