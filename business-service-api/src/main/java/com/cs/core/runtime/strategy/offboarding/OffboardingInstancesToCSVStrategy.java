package com.cs.core.runtime.strategy.offboarding;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.klassinstance.IWriteInstancesToCSVFileModel;
import com.cs.core.runtime.interactor.utils.OffboardingFileUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("offboardingInstancesToCSVStrategy")
public class OffboardingInstancesToCSVStrategy implements IOffboardingInstanceToCSVFileStrategy {
  
  @Override
  public IModel execute(IWriteInstancesToCSVFileModel model) throws Exception
  {
    String filePath = model.getfilePath();
    String fileName = model.getfileName();
    String outputFile = filePath + fileName;
    int headerRowNumber = model.getHeaderRowNumber();
    int dataRowNumber = model.getDataRowNumber();
    String[] headerToWrite = model.getHeaderToWrite();
    List<String[]> dataToWrite = model.getDataToWrite();
    
    OffboardingFileUtils.writeDataToCSVFile(headerRowNumber, dataRowNumber, outputFile,
        headerToWrite, dataToWrite);
    
    return null;
  }
}
