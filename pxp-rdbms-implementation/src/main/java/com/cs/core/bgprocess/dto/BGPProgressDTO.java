package com.cs.core.bgprocess.dto;

import com.cs.core.bgprocess.idto.IBGPProgressDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.simple.JSONArray;

/**
 * Job progress data DTO
 *
 * @author vallee
 */
public class BGPProgressDTO extends SimpleDTO implements IBGPProgressDTO {
  
  private static final String COMPLETED            = "[COMPLETED]";
  private static final String COMPLETION           = "completion";
  private int                 percentageCompletion = 0;
  private static final String STEP_NO              = "stepNo";
  private int                 currentStepNo        = 1;
  private static final String BATCH_NO             = "batchNo";
  private int                 batchNo              = 0;
  private static final String STEPS                = "steps";
  private final List<String>  stepNames            = new ArrayList<>();
  
  /**
   * Build a new percentage based progress DTO
   *
   * @param service the name of the BGP service used as unique step
   */
  public BGPProgressDTO(String service)
  {
    stepNames.add(service); // The name of the service is inserted as step 0
  }
  
  @Override
  public void initSteps(String... stepNames)
  {
    this.stepNames.addAll(Arrays.asList(stepNames));
  }
  
  @Override
  public void fromJSON(JSONContentParser parser)
  {
    percentageCompletion = parser.getInt(COMPLETION);
    currentStepNo = parser.getInt(STEP_NO);
    currentStepNo = ( currentStepNo <= 0 ? 1 : currentStepNo );
    batchNo = parser.getInt(BATCH_NO);
    stepNames.clear();
    JSONArray names = parser.getJSONArray(STEPS);
    names.forEach((name) -> {
      stepNames.add((String) name);
    });
    if (percentageCompletion == 100)
      currentStepNo = stepNames.size() + 1; // make internal data consistent
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(
        JSONBuilder.newJSONField(COMPLETION, getPercentageCompletion()),
        JSONBuilder.newJSONField(STEP_NO, getCurrentStepNo()),
        JSONBuilder.newJSONField(BATCH_NO, batchNo),
        JSONBuilder.newJSONStringArray(STEPS, stepNames));
  }
  
  @Override
  public boolean isPercentageBased()
  {
    return stepNames.size() == 1;
  }
  
  @Override
  public int getPercentageCompletion()
  {
    return isPercentageBased() ? percentageCompletion
        : (getCurrentStepNo() * 100) / stepNames.size();
  }
  
  @Override
  public void setPercentageCompletion(int percentage)
  {
    if (isPercentageBased())
      percentageCompletion = Math.min(percentage, 100);
  }
  
  @Override
  public int getCurrentStepNo()
  {
    return Math.min(currentStepNo, stepNames.size());
  }
  
  @Override
  public void incrStepNo()
  {
    if(currentStepNo < stepNames.size())
    currentStepNo++;
  }
  
  @Override
  public int getTotalStepNb()
  {
    return stepNames.size();
  }
  
  @Override
  public String getCurrentStepName()
  {
    return currentStepNo >= stepNames.size() ? COMPLETED : stepNames.get(currentStepNo);
  }
  
  @Override
  public List<String> getStepNames()
  {
    return stepNames;
  }
  
  @Override
  public int getBatchNo()
  {
    return batchNo;
  }
  
  /**
   * Increment the batch No
   */
  public void incrBatchNo()
  {
    batchNo++;
  }
  
  /**
   * @param no
   *          overwritten batch no
   */
  public void setBatchNo(int no)
  {
    batchNo = no;
  }
  
  /**
   * Set the progress as fully completed
   */
  public void setCompleted()
  {
    if (isPercentageBased())
      percentageCompletion = 100;
    else
      currentStepNo = stepNames.size() + 1;
  }
}
