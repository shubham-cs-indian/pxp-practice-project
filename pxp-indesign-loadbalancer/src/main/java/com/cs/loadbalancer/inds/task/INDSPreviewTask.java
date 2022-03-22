package com.cs.loadbalancer.inds.task;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;

import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.loadbalancer.inds.scheduler.IINDSScheduler;
import com.cs.loadbalancer.inds.task.base.BaseINDSTask;
import com.cs.loadbalancer.inds.task.base.IINDSTask;
import com.cs.loadbalancer.inds.utils.INDSConstants;
import com.cs.loadbalancer.inds.utils.INDSLoadBalancerUtils;
import com.cs.loadbalancer.inds.utils.SOAPUtils;
import com.cs.runtime.interactor.model.indsserver.GeneratePdfFromIndesignFileModel;
import com.cs.runtime.interactor.model.indsserver.IGeneratePdfFromIndesignFileModel;
import com.cs.runtime.interactor.model.indsserver.IINDSScriptArgumentsModel;
import com.cs.runtime.interactor.model.indsserver.IINDSScriptRequestModel;
import com.cs.runtime.interactor.model.indsserver.IINDSTaskRequestModel;
import com.cs.runtime.interactor.model.indsserver.INDSScriptArgumentsModel;
import com.cs.runtime.interactor.model.indsserver.INDSScriptRequestModel;

import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPConnectionFactory;
import jakarta.xml.soap.SOAPMessage;


/**
 * 
 * @author hridya.joshi
 * Description: Task to convert Indesign document asset to pdf for preview generation.
 */
public class INDSPreviewTask extends BaseINDSTask implements IINDSTask {
  
  
  public INDSPreviewTask(AsyncContext asyncContext, IINDSScheduler scheduler,
      IINDSTaskRequestModel indsTaskRequestModel)
  {
    super(asyncContext, scheduler, indsTaskRequestModel);   
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void performTask() throws Exception
  {
    SOAPConnection soapConnection = null;
    File assetDirectory = null;
    try {
      
      UUID randomUUID = UUID.randomUUID();
      soapConnection = SOAPConnectionFactory.newInstance()
          .createConnection();      
      IGeneratePdfFromIndesignFileModel previewTaskRequestModel = (IGeneratePdfFromIndesignFileModel) indsTaskRequestModel;
      IINDSScriptRequestModel indsScriptRequestModel = new INDSScriptRequestModel();
      byte[] assetsByteStream = previewTaskRequestModel.getFileByteStream();
      indsScriptRequestModel.setAssetsByteStream(assetsByteStream);
      
      // getting document name to get extensions of different files to set inddAssetName (eg: "document1.indd", "document2.idml", etc)
      String documentName = previewTaskRequestModel.getDocumentName();
      indsScriptRequestModel.setDocumentName(documentName);
      
      // asset directory - Directory to store both indesign file and its converted pdf.
      assetDirectory = new File(randomUUID.toString());      
      File assetFile = null;
      String inddAssetFilePath = assetDirectory.getCanonicalPath() + "/" + documentName;
      assetFile = new File(inddAssetFilePath);
      INDSLoadBalancerUtils.writeByteArrayToFile(assetFile, assetsByteStream, false);
      indsScriptRequestModel.setAssetsDirectory(assetFile.getCanonicalPath());
      
      // Model(IINDSScriptArgumentsModel) to send scriptRequestModel to Indesign server as an argument
      List<IINDSScriptArgumentsModel> scriptArguments = new ArrayList<IINDSScriptArgumentsModel>();
      scriptArguments = indsScriptRequestModel.getScriptArguments();
      INDSScriptArgumentsModel argumentsModel = new INDSScriptArgumentsModel();
      argumentsModel.setName("functionArgs");
      argumentsModel.setValue(ObjectMapperUtil.writeValueAsString(indsScriptRequestModel));
      scriptArguments.add(argumentsModel);
      
      //PreviewScript.jsx - Script file to convert indesign file to pdf
      indsScriptRequestModel.setScriptFileName("PreviewScript.jsx");
      
      if (inDesignServerInstance == null) {
        return;
      }
      
      //connection with Indesign Server
      SOAPMessage soapMessage = SOAPUtils.createSOAPMessage(indsScriptRequestModel);
      SOAPMessage soapResponse = null;      
      
      try {
        soapResponse = soapConnection.call(soapMessage, new URL("http://"
            + inDesignServerInstance.getHostName() + ":" + inDesignServerInstance.getPort()));
      }
      catch (Exception e) {
        inDesignServerInstance.setStatus(INDSConstants.INDS_IN_ACTIVE);
      }
      
      Map<String, Object> responseMap = SOAPUtils.extractSOAPBodyAsMapFromResponse(soapResponse);      
      Map<String, Object> scriptResultMap = (Map<String, Object>) responseMap.get("scriptResult");
      if(!scriptResultMap.isEmpty()) {
        scriptResultMap = (Map<String, Object>) scriptResultMap.get("data");
      }
      
      //setting the byte stream of converted pdf in response model if successfully converted.
      if(scriptResultMap.containsValue("success")) {
        String extension = documentName.substring(documentName.lastIndexOf("."));
        String pdfFilePath = assetDirectory.getCanonicalPath() + "/"
            + documentName.replace(extension, ".pdf");
        Path filePath = Paths.get(pdfFilePath);
        byte[] indesignFileBytes = Files.readAllBytes(filePath);        
        IGeneratePdfFromIndesignFileModel responseModel = new GeneratePdfFromIndesignFileModel();
        responseModel.setFileByteStream(indesignFileBytes);
        ServletResponse response = asyncContext.getResponse();
        response.getWriter().println(ObjectMapperUtil.writeValueAsString(responseModel));
        
      }
      
    }
    catch(Exception e) {
     throw e;
    }
    finally {
      if (soapConnection != null) {
        soapConnection.close();
      }
      if(assetDirectory != null) {
        INDSLoadBalancerUtils.forceDelete(assetDirectory);
      }
    }
  }
  
}
