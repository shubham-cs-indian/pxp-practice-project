package com.cs.core.runtime.strategy.offboarding;

import com.cs.core.runtime.interactor.constants.OnboardingSystemLevelIds;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.OffboardingConstants;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.fileinstance.CreateOnboardingFileInstanceModel;
import com.cs.core.runtime.interactor.model.fileinstance.ICreateOnboardingFileInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IWriteInstancesToXLSXFileModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.utils.OffboardingFileUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

@Component("offboardingInstancesToXLSXStrategy")
public class OffboardingInstancesToXLSXStrategy implements IOffboardingInstancesToXLSXStrategy {

  @Resource(name = "assetUploadStatusMap")
  protected Map<String, String>         assetUploadStatusMap;
  @Autowired
  protected ISessionContext             context;
  @Autowired
  protected TransactionThreadData       transactionThread;
  @Autowired
  String                                exportFolderPath;

  @Override
  public ICreateOnboardingFileInstanceModel execute(IWriteInstancesToXLSXFileModel model)
      throws Exception
  {
    // TODO Need file path where file will be created now getting folder name
    // only
    String filePath = model.getfilePath();
    String workbookName = model.getfileName();
    String sheetName = model.getSheetName();
    int headerRowNumber = model.getHeaderRowNumber() - 1;
    int dataRowNumber = model.getDataRowNumber() - 1;
    String[] headerToWrite = model.getHeaderToWrite();
    List<String[]> dataToWrite = model.getDataToWrite();
    
    // File fileDirectory = new File(filePath);
    // OffboardingFileUtils.getOrCreateFileDirectory(fileDirectory);
    
    // workbookName = "ExportedFileName";
    ICreateOnboardingFileInstanceModel returnModel = null;
    filePath = exportFolderPath + "//" + workbookName + OffboardingConstants.XLSX_FILE_EXTENSION;
    File file = new File(filePath);
    if (!file.exists()) {
      returnModel = createFileInstance(model.getFileInstanceIdForExport());
      returnModel.setName(workbookName + OffboardingConstants.XLSX_FILE_EXTENSION);
    }
    XSSFWorkbook workbook = OffboardingFileUtils.getOrCreateFileXLSX(filePath);
    // if sheet does not exists then flag will be set
    boolean flagToGetRowNumber = checkIfSheetExists(workbook, sheetName);
    XSSFSheet sheet = OffboardingFileUtils.getOrCreateSheet(workbook, sheetName);
    
    if (!flagToGetRowNumber) {
      dataRowNumber = sheet.getPhysicalNumberOfRows(); // this is done for
                                                       // writing in batches.
    }
    
    OffboardingFileUtils.writeDataToXLSXSheet(headerRowNumber, dataRowNumber, headerToWrite,
        dataToWrite, sheet);
    FileOutputStream outputStream = new FileOutputStream(filePath);
    workbook.write(outputStream);
    outputStream.close();
    // fileUtil.uploadFileToSwiftContainer(model.getFileInstanceIdForExport(),
    // filePath);
    return returnModel;
  }
  
  private boolean checkIfSheetExists(XSSFWorkbook workbook, String sheetName)
  {
    XSSFSheet sheet = workbook.getSheet(sheetName);
    if (sheet == null) {
      return true;
    }
    return false;
  }
  
  private ICreateOnboardingFileInstanceModel createFileInstance(String assetKey) throws Exception
  {
    TransactionData transactionData = transactionThread.getTransactionData();
    ICreateOnboardingFileInstanceModel fileInstanceModel = new CreateOnboardingFileInstanceModel();
    fileInstanceModel.setId(assetKey);
    fileInstanceModel.setUserId(context.getUserId());
    fileInstanceModel.setEndpointId(transactionData.getEndpointId());
    fileInstanceModel.setOrganizationId(transactionData.getOrganizationId());
    fileInstanceModel.setType(OnboardingSystemLevelIds.ONBOARDING_FILE_KLASS);
    fileInstanceModel.setPhysicalCatalogId(Constants.DATA_INTEGRATION_CATALOG_IDS);
    return fileInstanceModel;
  }
}
