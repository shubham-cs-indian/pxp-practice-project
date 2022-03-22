package com.cs.core.bgprocess.services.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cs.bds.config.usecase.taxonomy.ISaveTaxonomy;
import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.dto.EmbeddedInheritanceInTaxonomyDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.idto.IEmbeddedInheritanceInTaxonomyDTO;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.config.interactor.model.attributiontaxonomy.ISaveMasterTaxonomyModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.SaveMasterTaxonomyModel;
import com.cs.core.config.interactor.model.klass.ContextKlassModel;
import com.cs.core.config.interactor.model.klass.IContextKlassModel;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class SaveTaxonomyBGP extends AbstractBGProcessJob implements IBGProcessJob {
  
  private static final String BATCH_SIZE              = "batchSize";
  private static final String PROCESSED_TAXONOMY_CODE = "processedTaxonomyCode";
  
  private int                 batchSize;
  protected int               nbBatches               = 0;
  
  protected Set<String>       taxonomyCode            = new HashSet<>();
  IEmbeddedInheritanceInTaxonomyDTO     taxonomyDTO             = new EmbeddedInheritanceInTaxonomyDTO();
  protected Set<String>       processedTaxonomyCode   = new HashSet<>();
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    taxonomyDTO.fromJSON(initialProcessData.getEntryData().toString());
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d", getService(), getIID());
    
    // initialize runtime data
    this.initRuntimeData();
    
  }
  
  @Override
  public BGPStatus runBatch()
  {
    ISaveMasterTaxonomyModel saveMasterTaxonomyModel = new SaveMasterTaxonomyModel();
    List<IContextKlassModel> addedContextKlasses = new ArrayList<IContextKlassModel>();
    
    for (String addedContextKlass : taxonomyDTO.getAddedContextKlasses()) {
      IContextKlassModel contextmodel = new ContextKlassModel();
      contextmodel.setContextKlassId(addedContextKlass);
      addedContextKlasses.add(contextmodel);
    }
    
    saveMasterTaxonomyModel.setAddedContextKlasses(addedContextKlasses);
    saveMasterTaxonomyModel.setId(taxonomyDTO.getId());
    saveMasterTaxonomyModel.setCode(taxonomyDTO.getCode());
    saveMasterTaxonomyModel.setLabel(taxonomyDTO.getLabel());
    saveMasterTaxonomyModel.setIsBackgroundSaveTaxonomy(true);
    
    try {
      ISaveTaxonomy saveTaxonomy = BGProcessApplication.getApplicationContext().getBean(ISaveTaxonomy.class);
      saveTaxonomy.execute(saveMasterTaxonomyModel);
      processedTaxonomyCode.add(taxonomyDTO.getCode());
      jobData.getRuntimeData().setStringArrayField(PROCESSED_TAXONOMY_CODE, processedTaxonomyCode);
    }
    catch (Exception e) {
      e.printStackTrace();
      RDBMSLogger.instance().exception(e);
    }
    
    if (!this.processedTaxonomyCode.isEmpty()) {
      jobData.getProgress().setPercentageCompletion(100);
    }
    return (jobData.getSummary().getNBOfErrors() == 0 ? IBGProcessDTO.BGPStatus.ENDED_SUCCESS : IBGProcessDTO.BGPStatus.ENDED_ERRORS);
  }
  
  protected void initRuntimeData() throws CSFormatException, CSInitializationException
  {
    if (jobData.getRuntimeData().isEmpty()) {
      // initialize batch size
      this.initBatchSize();
      
      jobData.getRuntimeData().setField(BATCH_SIZE, batchSize);
      jobData.getRuntimeData().setStringArrayField(PROCESSED_TAXONOMY_CODE, this.processedTaxonomyCode);
    }
    else {
      IJSONContent runtimeData = jobData.getRuntimeData();
      batchSize = runtimeData.getInitField(BATCH_SIZE, batchSize);
      processedTaxonomyCode.addAll(runtimeData.getArrayField(PROCESSED_TAXONOMY_CODE, String.class));
    }
  }
  
  protected void initBatchSize() throws CSInitializationException
  {
    batchSize = CSProperties.instance().getInt(propName("batchSize"));
    batchSize = (batchSize > 0 ? batchSize : 1); // 1 minimum is taken
  }
}
