package com.cs.core.bgprocess.services.config;

import com.cs.core.bgprocess.dto.PropertyDeleteDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.idto.IPropertyDeleteDTO;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PropertyDelete extends AbstractBGProcessJob implements IBGProcessJob {
  
  private static final String       BATCH_SIZE                   = "batchSize";
  private static final String       PROCESSED_ATTRIBUTE_PROPERTY = "processedAttributeProperty";
  private static final String       PROCESSED_TAG_PROPERTY       = "processedTagProperty";
  private static final String       PROCESSED_RELATION_PROPERTY  = "processedRelationProperty";
  
  private enum STEPS
  {
    DELETE_ATTRIBUTE, DELETE_TAG, DELETE_RELATION;
    
    private static STEPS[] values = values();
    
    public static STEPS valueOf(int idx)
    {
      return values[idx];
    }
    
    private static String[] labels = { "attributes delete", "tags delete", "relations delete" };
    
    public String getLabel()
    {
      return String.format(labels[this.ordinal()]);
    }
    
    public static List<String> getLabels()
    {
      return Arrays.stream(values()).map(STEPS::getLabel).collect(Collectors.toList());
    }
  }
  
  private int                        batchSize;
  protected int                      nbBatches                  = 0;
  
  protected final IPropertyDeleteDTO propertyDelete             = new PropertyDeleteDTO();
  protected Set<Long>                attributeProperty          = new HashSet<>();
  protected Set<Long>                tagProperty                = new HashSet<>();
  protected Set<Long>                relationProperty           = new HashSet<>();
  protected Set<Long>                processedAttributeProperty = new HashSet<>();
  protected Set<Long>                processedTagProperty       = new HashSet<>();
  protected Set<Long>                processedRelationProperty  = new HashSet<>();
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    
    //initialize entry data
    this.initEntryData();
    
    //initialize runtime data
    this.initRuntimeData();
    
    //initialize process data
    this.initProcessData();
    
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d",
            getService(), getIID());
  }
  
  protected void initBatchSize() throws CSInitializationException
  {
    batchSize = CSProperties.instance().getInt(propName("batchSize"));
    batchSize = (batchSize > 0 ? batchSize : 1); // 1 minimum is taken
  }
  
  protected void initEntryData() throws CSFormatException
  {
    propertyDelete.fromJSON(jobData.getEntryData()
        .toString());
    
    propertyDelete.getProperties()
        .forEach(property -> {
          if (property.getPropertyType()
              .getSuperType() == SuperType.ATTRIBUTE) {
            this.attributeProperty.add(property.getPropertyIID());
          }
          else if (property.getPropertyType()
              .getSuperType() == SuperType.TAGS) {
            this.tagProperty.add(property.getPropertyIID());
          }
          else if (property.getPropertyType()
              .getSuperType() == SuperType.RELATION_SIDE) {
            this.relationProperty.add(property.getPropertyIID());
          }
        });
  }
  
  protected void initRuntimeData() throws CSFormatException, CSInitializationException
  {
    if (jobData.getRuntimeData()
        .isEmpty()) {
      
      //initialize batch size
      this.initBatchSize();
      
      jobData.getRuntimeData()
          .setField(BATCH_SIZE, batchSize);
      jobData.getRuntimeData()
          .setLongArrayField(PROCESSED_ATTRIBUTE_PROPERTY, this.processedAttributeProperty);
      jobData.getRuntimeData()
          .setLongArrayField(PROCESSED_TAG_PROPERTY, this.processedTagProperty);
      jobData.getRuntimeData()
          .setLongArrayField(PROCESSED_RELATION_PROPERTY, this.processedRelationProperty);
    }
    else {
      IJSONContent runtimeData = jobData.getRuntimeData();
      batchSize = runtimeData.getInitField(BATCH_SIZE, batchSize);
      processedAttributeProperty
          .addAll(runtimeData.getArrayField(PROCESSED_ATTRIBUTE_PROPERTY, Long.class));
      processedTagProperty.addAll(runtimeData.getArrayField(PROCESSED_TAG_PROPERTY, Long.class));
      processedRelationProperty
          .addAll(runtimeData.getArrayField(PROCESSED_RELATION_PROPERTY, Long.class));
    }
  }
  
  protected void initProcessData()
  {
	if(jobData.getProgress().getStepNames().size() == 1) {
	  jobData.getProgress().initSteps(STEPS.getLabels().toArray(new String[0]));
	}
  }
  
  @Override
  public BGPStatus runBatch() throws RDBMSException, CSFormatException, CSInitializationException
  {
    if (jobData.getProgress().getPercentageCompletion() == 100) {
      jobData.setStatus(BGPStatus.ENDED_SUCCESS);
      return jobData.getStatus();
    }
    
    int currentStepNo = jobData.getProgress().getCurrentStepNo();
    STEPS currentStep = STEPS.valueOf(currentStepNo-1);
    
    switch (currentStep) {
      case DELETE_ATTRIBUTE:
        attributePropertyDelete();
        break;
      case DELETE_TAG:
        tagPropertyDelete();
        break;
      case DELETE_RELATION:
        relationPropertyDelete();
        break;
      default:
        throw new RDBMSException( 100, "Programm Error", "Unexpected step-no: " + currentStepNo);
    }
    
    // Return of status
    IBGProcessDTO.BGPStatus status = null;
    if (jobData.getProgress().getPercentageCompletion() == 100)
      status = jobData.getSummary().getNBOfErrors() == 0 ? IBGProcessDTO.BGPStatus.ENDED_SUCCESS : IBGProcessDTO.BGPStatus.ENDED_ERRORS;
    else
      status = BGPStatus.RUNNING;
    
    return status;
  }
  
  protected Collection<Long> getProcessPropertyInBatch(Collection<Long> totalProperty, Collection<Long> processedProperty)
  {
    Collection<Long> remProperty = new HashSet<>();
    remProperty.addAll(totalProperty);
    remProperty.removeAll(processedProperty);
    Collection<Long> processPropertyInBatch = new HashSet<>();
    Iterator<Long> remPropertyItr = remProperty.iterator();
    for (int i = 0; i < batchSize; i++) {
      if (!remPropertyItr.hasNext())
        break;
      processPropertyInBatch.add(remPropertyItr.next());
    }
    
    return processPropertyInBatch;
  }
  
  protected void attributePropertyDelete() throws RDBMSException
  {
    if(this.attributeProperty.isEmpty()) {
      jobData.getProgress().incrStepNo();
      return;
    }
    
    Collection<Long> processPropertyInBatch = this.getProcessPropertyInBatch(this.attributeProperty, this.processedAttributeProperty);
    this.runPropertyDelete("sp_removeattributeproperty", processPropertyInBatch);
    this.processedAttributeProperty.addAll(processPropertyInBatch);
    jobData.getRuntimeData()
    .setLongArrayField(PROCESSED_ATTRIBUTE_PROPERTY, this.processedAttributeProperty);
    if (this.attributeProperty.size() == this.processedAttributeProperty.size()) {
      jobData.getProgress().incrStepNo();
    }
  }
  
  protected void tagPropertyDelete() throws RDBMSException
  {
    if(this.tagProperty.isEmpty()) {
      jobData.getProgress().incrStepNo();
      return;
    }
    
    Collection<Long> processPropertyInBatch = this.getProcessPropertyInBatch(this.tagProperty, this.processedTagProperty);
    this.runPropertyDelete("sp_removetagproperty", processPropertyInBatch);
    this.processedTagProperty.addAll(processPropertyInBatch);
    jobData.getRuntimeData()
    .setLongArrayField(PROCESSED_TAG_PROPERTY, this.processedTagProperty);
    if (this.tagProperty.size() == this.processedTagProperty.size()) {
      jobData.getProgress().incrStepNo();
    }
  }
  
  protected void relationPropertyDelete() throws RDBMSException
  {
    if(this.relationProperty.isEmpty()) {
      jobData.getProgress().incrStepNo();
      return;
    }
    
    Collection<Long> processPropertyInBatch = this.getProcessPropertyInBatch(this.relationProperty, this.processedRelationProperty);
    this.runPropertyDelete("sp_removerelationproperty", processPropertyInBatch);
    this.processedRelationProperty.addAll(processPropertyInBatch);
    jobData.getRuntimeData()
    .setLongArrayField(PROCESSED_RELATION_PROPERTY, this.processedRelationProperty);
    if (this.relationProperty.size() == this.processedRelationProperty.size()) {
      jobData.getProgress().incrStepNo();
    }
  }
  
  protected void runPropertyDelete(String callDeclaration, Collection<Long> processPropertyInBatch)
      throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          currentConn.getProcedure( callDeclaration)
              .setInput(ParameterType.IID_ARRAY, processPropertyInBatch)
              .execute();
        });
  }
}
