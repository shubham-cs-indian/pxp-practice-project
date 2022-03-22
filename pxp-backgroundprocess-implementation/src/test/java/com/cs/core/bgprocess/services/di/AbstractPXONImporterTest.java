package com.cs.core.bgprocess.services.di;

import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.testutil.AbstractBGProcessTests;
import com.cs.core.dataintegration.dto.PXONImporterPlanDTO;
import com.cs.core.json.JSONContent;
import com.cs.core.technical.exception.CSInitializationException;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import static com.cs.core.printer.QuickPrinter.println;

public abstract class AbstractPXONImporterTest extends AbstractBGProcessTests {

  private static int fileCounter = 0;
  protected static final String SERVICE                     = "PXON_IMPORT";
  protected static final int    NB_BATCHES                  = 20;
  
  public String prepareImportData(String directory, String importData) throws CSInitializationException
  {
    String fileName = "import#" + fileCounter + ".pxon";
    fileCounter++;
    Path path = FileSystems.getDefault().getPath(directory, fileName);
    File dir = new File(directory);
    if (!dir.exists()) {
      dir.mkdirs();
    }
    try {
      FileOutputStream outputStream = new FileOutputStream(path.toFile());
      outputStream.write(importData.getBytes());
      outputStream.close();
    }
    catch (IOException exc) {
      throw new CSInitializationException("Can't open export file", exc);
    }
    return fileName;
  }
  
  public void submitImportProcess(String fileName) throws Exception
  {
    PXONImporterPlanDTO importerPlanDTO = new PXONImporterPlanDTO();
    importerPlanDTO.setRelativeAfterImportFile("rosh\\importEntities1.txt");
    importerPlanDTO.setRelativeImportFile(fileName);
    importerPlanDTO.setLocaleID(localeCatalogDto.getLocaleID());
    JSONContent entryData = new JSONContent(importerPlanDTO.toJSON());
    
    long jobIID = BGPDriverDAO.instance().submitBGPProcess("admin", SERVICE, getTestCallbackTemplateURL(), IBGProcessDTO.BGPPriority.LOW,
        entryData);
    
    this.runJobSample(NB_BATCHES);
    println("Executed samples of " + NB_BATCHES + " batches");
    displayLogContent(jobIID);
  }
  protected void deleteFile(String directory, String fileName)
  {
    File file = new File(directory+"/"+fileName);
    if(file.delete()) {
      System.out.println("file deleted");
    }
  }

  protected String getPxonAsString(String filePath) throws IOException
  {
    InputStream resourceAsStream = this.getClass().getResourceAsStream(filePath);
    return IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8.name());
  }
}
