package com.cs.core.strategy.templateengine.velocity;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import com.cs.core.exception.base.smartdocument.JsonConfigurationException;
import com.cs.core.exception.base.smartdocument.TemplateEngineException;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentInput;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentTemplateEngineRequestModel;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentTemplateEngineResponseModel;
import com.cs.core.runtime.interactor.model.smartdocument.SmartDocumentTemplateEngineResponseModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;

@SuppressWarnings("unchecked")
@Component
public class VelocityTemplateEngineStrategy {
  
  @Autowired
  protected VelocityDateToolWrapper velocityDateToolWrapper;
  
  @Autowired
  protected VelocityEngine          velocityEngine;
  
  public static final String        VELOCITY_ERROR_LOG = "VelocityErrorLog";
  public static final String        INSTANCES          = "instances";
  public static final String        DATE_TOOL          = "dateTool";
  public static final String        FETCH_DATA         = "fetchData";
  public static final String        HTML_START         = "<HTML><BODY>";
  public static final String        HTML_END           = "</HTML></BODY>";
  
  public ISmartDocumentTemplateEngineResponseModel execute(
      ISmartDocumentTemplateEngineRequestModel model) throws Exception
  {
    StringBuilder stringBuilder = new StringBuilder();
    
    VelocityContextWrapper velocityContextWrapper = getVelocityContextWrapper();
    velocityContextWrapper.put(INSTANCES, model.getSmartDocumentKlassInstancesDataModel());
    velocityContextWrapper.put(DATE_TOOL, velocityDateToolWrapper.getDateToolObject());
    velocityContextWrapper.put(FETCH_DATA, model.getFetchDataObject().get(FETCH_DATA));
    ISmartDocumentInput smartDocumentInputStreams = model.getSmartDocumentByteArrays();
    Map<String, byte[]> templateByteArrayMap = smartDocumentInputStreams.getTemplateFileBytes();
    
    stringBuilder.append(HTML_START);
    byte[] jsonByteArrayMap = smartDocumentInputStreams.getJsonFileBytes();
    if(jsonByteArrayMap==null || templateByteArrayMap.size() == 1)
    {
      for (String templateFileName : templateByteArrayMap.keySet()) {
        appendTemplateToHtml(stringBuilder, velocityContextWrapper, templateByteArrayMap, templateFileName);
      }
    }
    else
    {
      Map<String, Object> jsonMap = ObjectMapperUtil.readValue(new String(jsonByteArrayMap), Map.class);
      for (int index=1; index <= jsonMap.size(); index++) {
        String templateFileName = (String) jsonMap.get(Integer.toString(index));
        appendTemplateToHtml(stringBuilder, velocityContextWrapper, templateByteArrayMap, templateFileName);
      }
    }
    stringBuilder.append(HTML_END);
    ISmartDocumentTemplateEngineResponseModel responseModel = new SmartDocumentTemplateEngineResponseModel();
    responseModel.setHtmlFromTemplateEngine(stringBuilder.toString());
    return responseModel;
  }
  
  private void appendTemplateToHtml(StringBuilder stringBuilder,
      VelocityContextWrapper velocityContextWrapper, Map<String, byte[]> templateByteArrayMap,
      String templateFileName) throws JsonConfigurationException, TemplateEngineException, IOException
  {
    StringWriter swOut = new StringWriter();
    byte[] byteArray = templateByteArrayMap.get(templateFileName);
    if (byteArray == null) {
      throw new JsonConfigurationException();
    }
    Reader templateReader = new StringReader(new String(byteArray));
    try
    {
      velocityEngine.evaluate(velocityContextWrapper.getVelocityContext(), swOut,
          VELOCITY_ERROR_LOG + String.valueOf(System.currentTimeMillis()), templateReader);
    }
    catch(Exception e)
    {
      throw new TemplateEngineException(e.getMessage());
    }
    stringBuilder.append(swOut);
    templateReader.close();
  }
  
  @Lookup
  public VelocityContextWrapper getVelocityContextWrapper()
  {
    return null;
  }
}