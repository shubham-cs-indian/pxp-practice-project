package com.cs.core.bgprocess.dto;

import com.cs.core.bgprocess.idto.IBGPSummaryDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;

/**
 * Job Summary DTO
 *
 * @author vallee
 */
public class BGPSummaryDTO extends SimpleDTO implements IBGPSummaryDTO {
  
  private static final String NB_ERRORS        = "errors";
  private int                 nbOfErrors       = 0;
  private static final String NB_WARNINGS      = "warnings";
  private int                 nbOfWarnings     = 0;
  private static final String LOG_SIZE         = "logsize";
  private int                 nbOfLogLines     = 0;
  private static final String EXCEPTION        = "exception";
  private String              exceptionMessage = "";
  private static final String TOTAL_COUNT      = "totalCount";
  private int                 totalCount       = 0;
  private static final String SUCCESS_COUNT    = "successCount";
  private int                 successCount       = 0;
  
  @Override
  public void fromJSON(JSONContentParser parser)
  {
    nbOfErrors = parser.getInt(NB_ERRORS);
    nbOfWarnings = parser.getInt(NB_WARNINGS);
    nbOfLogLines = parser.getInt(LOG_SIZE);
    exceptionMessage = parser.getString(EXCEPTION);
    totalCount = parser.getInt(TOTAL_COUNT);
    successCount = parser.getInt(SUCCESS_COUNT);
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(JSONBuilder.newJSONField(NB_ERRORS, nbOfErrors),
        JSONBuilder.newJSONField(NB_WARNINGS, nbOfWarnings), JSONBuilder.newJSONField(LOG_SIZE, nbOfLogLines),
        !exceptionMessage.isEmpty() ? JSONBuilder.newJSONField(EXCEPTION, exceptionMessage) : JSONBuilder.VOID_FIELD,
        JSONBuilder.newJSONField(TOTAL_COUNT, totalCount), JSONBuilder.newJSONField(SUCCESS_COUNT, successCount));
  }
  
  @Override
  public int getNBOfErrors()
  {
    return nbOfErrors;
  }
  
  @Override
  public int getNbOfWarnings()
  {
    return nbOfWarnings;
  }
  
  @Override
  public int getNbOfLogLines()
  {
    return nbOfLogLines;
  }
  
  @Override
  public String getExceptionMessage()
  {
    return exceptionMessage;
  }
  
  /**
   * increment the number of errors
   */
  public void incNbOfErrors()
  {
    nbOfErrors++;
  }
  
  /**
   * @param nbOfAdditionalErrors
   *          additional number of errors to add
   */
  public void incNbOfErrors(int nbOfAdditionalErrors)
  {
    nbOfErrors += nbOfAdditionalErrors;
  }
  
  /**
   * increment the number of warnings
   */
  public void incNbOfWarning()
  {
    nbOfWarnings++;
  }
  
  /**
   * @param nbOfAdditionalWarnings
   *          additional number of warnings to add
   */
  public void incNbOfWarning(int nbOfAdditionalWarnings)
  {
    nbOfWarnings += nbOfAdditionalWarnings;
  }
  
  /**
   * increment the number of log lines
   */
  public void incNbOfLogLines()
  {
    nbOfLogLines++;
  }
  
  /**
   * @param nbOfAdditionalLines
   *          additional number of log lines to add
   */
  public void incNbOfLogLines(int nbOfAdditionalLines)
  {
    nbOfLogLines += nbOfAdditionalLines;
  }
  
  /**
   * @param exc
   *          an exception to report in the summary
   */
  public void setException(Exception exc)
  {
    exceptionMessage = exc.getMessage();
  }

  @Override
  public int getTotalCount()
  {
    return totalCount;
  }
  
  /**
   *@param totalCount
   *      set total number of entities count 
   */
  public void setTotalCount(int totalCount)
  {
    this.totalCount = +totalCount;
  }

  @Override
  public int getSuccessCount()
  {
    return successCount;
  }

  /**
   *@param successCount
   *  set success entities count
   */
  public void setSuccessCount(int successCount)
  {
    this.successCount = +successCount;
  }
}
