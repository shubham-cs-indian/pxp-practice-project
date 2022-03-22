package com.cs.loadbalancer.inds.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletResponse;

import com.cs.loadbalancer.inds.scheduler.IINDSScheduler;
import com.cs.loadbalancer.inds.task.base.BaseINDSTask;
import com.cs.loadbalancer.inds.task.base.IINDSTask;
import com.cs.runtime.interactor.model.indsserver.IINDSTaskRequestModel;

/**
 * The processing task class. It sends SOAP calls to the indesign server instances, wherein the script is passed alongwith the 
 * script arguments. SOAP response is converted into a Map & the script result is passed back. See 'performTask' method.
 */

public class INDSProcessingTask extends BaseINDSTask implements IINDSTask {
  
  HttpServletResponse httpServletResponse;

	public INDSProcessingTask(AsyncContext asyncContext, IINDSScheduler scheduler, IINDSTaskRequestModel indsTaskRequestModel, HttpServletResponse response) {
		super(asyncContext, scheduler, indsTaskRequestModel);
		this.httpServletResponse = response;
	}

	@SuppressWarnings("unchecked")
  public void performTask() throws Exception {
    /*if (inDesignServerInstance == null || inDesignServerInstance.getStatus().equals(INDSConstants.INDS_IN_ACTIVE)) {
    	throw new INDSNotAvailableException();
    }
    
    IINDSProcessingTaskRequestModel processingTaskReqModel = (IINDSProcessingTaskRequestModel) indsTaskRequestModel;
    IINDSScriptRequestModel indsScriptRequestModel = processingTaskReqModel.getScriptRequestModel();
    //String indesignFileName = scriptRequestModel.getIndesignFileName();
    
    
    
    UUID randomUUID = UUID.randomUUID();
    SOAPConnection soapConnection = null;
    BufferedOutputStream bo = new BufferedOutputStream(this.httpServletResponse.getOutputStream());
    FileInputStream fi = null;
    boolean isPreviews = indsScriptRequestModel.getIsPreviews();
    File documentTemplateZipFile = null;
    File productTemplatesZipFile = null;
    File assetsZipFile = null;
    
    try {
      String documentTemplateZipName = randomUUID + ".zip";
      documentTemplateZipFile = new File(documentTemplateZipName);
      byte[] documentTemplateByteStream = indsScriptRequestModel.getDocumentTemplateByteStream();
      FileUtils.writeByteArrayToFile(documentTemplateZipFile, documentTemplateByteStream);
      String documentTemplateZipPath = documentTemplateZipFile.getCanonicalPath();
      documentTemplateZipPath = documentTemplateZipPath.replaceAll("\\\\", "/");
      indsScriptRequestModel.setDocumentTemplateZipPath(documentTemplateZipPath);
      
      if (indsScriptRequestModel.getProductTemplatesByteStream() != null) {
        
        String productTemplatesZipName = UUID.randomUUID() + ".zip";
        productTemplatesZipFile = new File(productTemplatesZipName);
        byte[] productTemplateByteStream = indsScriptRequestModel.getProductTemplatesByteStream();
        FileUtils.writeByteArrayToFile(productTemplatesZipFile, productTemplateByteStream);
        String productTemplateZipPath = productTemplatesZipFile.getCanonicalPath();
        productTemplateZipPath = productTemplateZipPath.replaceAll("\\\\", "/");
        indsScriptRequestModel.setProductTemplatesDirectory(productTemplateZipPath);
      }
      
      if (indsScriptRequestModel.getAssetsByteStream() != null) {
        
        String assetsZipName = UUID.randomUUID() + ".zip";
        assetsZipFile = new File(assetsZipName);
        byte[] assetByteStream = indsScriptRequestModel.getAssetsByteStream();
        FileUtils.writeByteArrayToFile(assetsZipFile, assetByteStream);
        String assetsZipPath = assetsZipFile.getCanonicalPath();
        assetsZipPath = assetsZipPath.replaceAll("\\\\", "/");
        indsScriptRequestModel.setAssetsDirectory(assetsZipPath);
      }
        
      List<IINDSScriptArgumentsModel> scriptArguments = new ArrayList<IINDSScriptArgumentsModel>();
      scriptArguments = indsScriptRequestModel.getScriptArguments();
         
      indsScriptRequestModel.setScriptFileName("MainScript.jsx");
      
      INDSScriptArgumentsModel argumentsModel1 = new INDSScriptArgumentsModel();
      argumentsModel1.setName("functionName");
      argumentsModel1.setValue("readyForIndesign");
      
      // set byte stream inside model to null and then stringify.
      indsScriptRequestModel.setProductTemplatesByteStream(null);
      indsScriptRequestModel.setDocumentTemplateByteStream(null);
      indsScriptRequestModel.setAssetsByteStream(null);
      INDSScriptArgumentsModel argumentsModel2 = new INDSScriptArgumentsModel();
      argumentsModel2.setName("functionArgs");
      argumentsModel2.setValue(ObjectMapperUtil.writeValueAsString(indsScriptRequestModel));
      
      scriptArguments.add(argumentsModel1);
      scriptArguments.add(argumentsModel2);
      indsScriptRequestModel.setScriptArguments(scriptArguments);
      
    	soapConnection = SOAPConnectionFactory.newInstance().createConnection();
    			
    	SOAPMessage soapMessage = SOAPUtils.createSOAPMessage(indsScriptRequestModel);
    
    	SOAPMessage soapResponse = soapConnection.call(soapMessage, new URL("http://" + inDesignServerInstance.getHostName() + ":" + inDesignServerInstance.getPort()));
    	Map<String, Object> responseMap = SOAPUtils.extractSOAPBodyAsMapFromResponse(soapResponse);
    	//"__DTPPreview__" + iPageIndex + ".jpg"
    	Object scriptResult = responseMap.get("scriptResult");
    	Map<String, Object> scriptResultMap = null;
    	
    	String documentTemplateZipDirectory = documentTemplateZipPath.substring(0, documentTemplateZipPath.length() - 4);
    	if (scriptResult instanceof Map) {
    		scriptResultMap = (Map<String, Object>) scriptResult;
    		ObjectMapper mapper = new ObjectMapper();
    		mapper.writeValue(new File(documentTemplateZipDirectory + "/" +"scriptResult.txt"), scriptResultMap);
    	} else {
    		scriptResultMap = new HashMap<>();
    		scriptResultMap.put("scriptResult", scriptResult);
    	}
    	String outputUniqueFolder = UUID.randomUUID().toString();
      indsScriptRequestModel.setDocumentTemplateName(
          URLDecoder.decode(indsScriptRequestModel.getDocumentTemplateName().replace("%20", "+"), "UTF-8"));
      indsScriptRequestModel.setDocumentName(
          URLDecoder.decode(indsScriptRequestModel.getDocumentName().replace("%20", "+"), "UTF-8"));
      File originalDTFile = new File(documentTemplateZipDirectory + "/"
          + indsScriptRequestModel.getDocumentTemplateName() + ".indd");
    	originalDTFile.delete();
    	
    	
      File OutPutZipFile = new File(outputUniqueFolder + ".zip");
      File dir = null;
      File parentDirectory = new File(documentTemplateZipDirectory);
      try {
        // In case of previews, zip only previews folder
        if (isPreviews) {
          dir = new File(documentTemplateZipDirectory + "/" + indsScriptRequestModel.getDocumentTemplateName());
          zipDirectory(dir, OutPutZipFile.getCanonicalPath());
        }
        else {
          dir = new File(documentTemplateZipDirectory);
          zipDirectory(dir, OutPutZipFile.getCanonicalPath());
        }
        
        fi = new FileInputStream(OutPutZipFile);
        byte[] arrBytes = new byte[32768];
        this.httpServletResponse.setContentType("application/zip");
        int i;
        while ((i = fi.read(arrBytes)) > 0) {
          bo.write(arrBytes, 0, i);
        }
        
      }
      finally {
        if (parentDirectory != null && parentDirectory.exists()) {
          FileUtils.deleteDirectory(parentDirectory);
        }
        if (OutPutZipFile != null) {
          OutPutZipFile.delete();
        }
      }
      
    }
    catch (Exception e ) {
      e.printStackTrace();
    } 
    finally {
      
      // delete folder after use
      if (assetsZipFile != null && assetsZipFile.exists()) {
        assetsZipFile.delete();
      }
      if (productTemplatesZipFile != null && productTemplatesZipFile.exists()) {
        productTemplatesZipFile.delete();
      }
      if (documentTemplateZipFile != null && documentTemplateZipFile.exists()) {
        documentTemplateZipFile.delete();
      }
      if (soapConnection != null) {
        soapConnection.close();
      }
      bo.flush();
      if (fi != null) {
        fi.close();
      }
    }*/
		
  }
	
	
	private void zipDirectory(File dir, String zipDirName)
  {
    try {
      List<String> filesListInDir = new ArrayList<String>();
      filesListInDir = populateFilesList(dir, filesListInDir);
      FileOutputStream fos = new FileOutputStream(zipDirName);
      ZipOutputStream zos = new ZipOutputStream(fos);
      for (String filePath : filesListInDir) {
        ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length() + 1, filePath.length()));
        zos.putNextEntry(ze);
        FileInputStream fis = new FileInputStream(filePath);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = fis.read(buffer)) > 0) {
          zos.write(buffer, 0, len);
        }
        zos.closeEntry();
        fis.close();
      }
      zos.close();
      fos.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
	
	private List<String> populateFilesList(File dir, List<String> filesListInDir) throws IOException
  {
	  
    File[] files = dir.listFiles();
    for (File file : files) {
      if (file.isFile()) {
        String absolutePath = file.getAbsolutePath();
        absolutePath = absolutePath.replaceAll("\\\\", "/");
        filesListInDir.add(absolutePath);
      }
      else
        populateFilesList(file, filesListInDir);
    }
    
    return filesListInDir;
  }
	
}

 
