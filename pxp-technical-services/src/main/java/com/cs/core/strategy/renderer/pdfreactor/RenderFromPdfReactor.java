package com.cs.core.strategy.renderer.pdfreactor;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.entity.smartdocument.preset.ISmartDocumentPresetPdfConfiguration;
import com.cs.core.config.interactor.model.asset.IAssetServerDetailsModel;
import com.cs.core.exception.base.smartdocument.PDFReactorException;
import com.cs.core.exception.base.smartdocument.UnableToConnectToServerException;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentInput;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentRendererRequestModel;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentRendererResponseModel;
import com.cs.core.runtime.interactor.model.smartdocument.SmartDocumentRendererResponseModel;
import com.cs.core.services.CSPDFReactorServer;
import com.realobjects.pdfreactor.webservice.client.Configuration.ColorSpace;
import com.realobjects.pdfreactor.webservice.client.Configuration.Encryption;
import com.realobjects.pdfreactor.webservice.client.Configuration.Font;
import com.realobjects.pdfreactor.webservice.client.Configuration.KeyValuePair;
import com.realobjects.pdfreactor.webservice.client.Configuration.Resource;



@Component
public class RenderFromPdfReactor  {
  
  @Autowired
  protected IAssetServerDetailsModel assetServerDetails;
  
  public static final String         COLOR_SPACE_RGB = "rgb";
  public static final String         NONE            = "none";
  public static final String         BLEED           = "bleed";
  public static final String         PRINT           = "print";
  public static byte[]               bleedBox        = null;
  public static byte[]               printMarks      = null;
  
  public RenderFromPdfReactor() throws Exception
  {
    InputStream bleedBoxStream = getClass().getClassLoader().getResourceAsStream("bleedBox.css");
    bleedBox = IOUtils.toByteArray(bleedBoxStream);
    
    InputStream printMarksStream = getClass().getClassLoader().getResourceAsStream("printMarks.css");
    printMarks = IOUtils.toByteArray(printMarksStream);
  }
  
  public ISmartDocumentRendererResponseModel execute(ISmartDocumentRendererRequestModel model) throws Exception
  {
    PDFReactorConfigurationWrapper pdfReactorConfigurationWrapper = getPDFReactorConfigurationWrapper();
    String htmlFromTemplateEngine = model.getHtmlFromTemplateEngine();
    
    addCSSAndFontToConfig(pdfReactorConfigurationWrapper, model);
    
    pdfReactorConfigurationWrapper.setDocument(htmlFromTemplateEngine.getBytes(Charset.forName(StandardCharsets.UTF_8.name())));
    
    setAssetServerDetails(pdfReactorConfigurationWrapper);
    
    setLicenceInformation(model, pdfReactorConfigurationWrapper);
    
    setPdfConfiguration(pdfReactorConfigurationWrapper, model);
    
    ISmartDocumentRendererResponseModel responseModel = new SmartDocumentRendererResponseModel();
    try
    {
      byte[] pdf = CSPDFReactorServer.createInstance().convertAsBinary(pdfReactorConfigurationWrapper.getPdfReactorConfiguration());
      responseModel.setPdfBytes(pdf);
    }
    catch(UnableToConnectToServerException ex)
    {
      throw ex;
    }
    catch(Exception e)
    {
      throw new PDFReactorException(e.getMessage());
    }
    return responseModel;
  }

  private void setPdfConfiguration(PDFReactorConfigurationWrapper pdfReactorConfigurationWrapper,
      ISmartDocumentRendererRequestModel model)
  {
    ISmartDocumentPresetPdfConfiguration pdfConfiguration = model.getSmartDocumentPreset()
        .getSmartDocumentPresetPdfConfiguration();
    Boolean isPasswordSet = false;
    if(checkIfHasValue(pdfConfiguration.getPdfTitle()))
    {
      pdfReactorConfigurationWrapper.setTitle(pdfConfiguration.getPdfTitle());
    }
    if(checkIfHasValue(pdfConfiguration.getPdfSubject()))
    {
      pdfReactorConfigurationWrapper.setSubject(pdfConfiguration.getPdfSubject());
    }
    if(checkIfHasValue(pdfConfiguration.getPdfAuthor()))
    {
      pdfReactorConfigurationWrapper.setAuthor(pdfConfiguration.getPdfAuthor());
    }
    if(checkIfHasValue(pdfConfiguration.getPdfKeywords()))
    {
      pdfReactorConfigurationWrapper.setKeywords(pdfConfiguration.getPdfKeywords());
    }
    if(checkIfHasValue(pdfConfiguration.getPdfUserPassword()))
    {
      pdfReactorConfigurationWrapper.setUserPassword(pdfConfiguration.getPdfUserPassword());
      isPasswordSet = true;
    }
    if(checkIfHasValue(pdfConfiguration.getPdfOwnerPassword()))
    {
      pdfReactorConfigurationWrapper.setOwnerPassword(pdfConfiguration.getPdfOwnerPassword());
      isPasswordSet = true;
    }
    if(checkIfHasValue(pdfConfiguration.getPdfColorSpace()))
    {
      ColorSpace colorSpace = pdfConfiguration.getPdfColorSpace().equals(COLOR_SPACE_RGB) ? ColorSpace.RGB : ColorSpace.CMYK;
      pdfReactorConfigurationWrapper.setDefaultColorSpace(colorSpace);
    }
    
    if(isPasswordSet)
    {
      pdfReactorConfigurationWrapper.setEncryption(Encryption.TYPE_128);
      pdfReactorConfigurationWrapper.setAllowPrinting(pdfConfiguration.getPdfAllowPrinting());
      pdfReactorConfigurationWrapper.setAllowCopy(pdfConfiguration.getPdfAllowCopyContent());
      pdfReactorConfigurationWrapper.setAllowModifyContents(pdfConfiguration.getPdfAllowModifications());
      pdfReactorConfigurationWrapper.setAllowAnnotations(pdfConfiguration.getPdfAllowAnnotations());
    }
  }

  private void setLicenceInformation(ISmartDocumentRendererRequestModel model,
      PDFReactorConfigurationWrapper pdfReactorConfigurationWrapper)
  {
    String licenceKey = model.getRendererLicenceKey();
    if (checkIfHasValue(licenceKey)) {
      pdfReactorConfigurationWrapper.setLicenseKey(model.getRendererLicenceKey());
      pdfReactorConfigurationWrapper.setThrowLicenseExceptions(true);
    }
  }
  
  private void setAssetServerDetails(PDFReactorConfigurationWrapper pdfReactorConfigurationWrapper)
  {
    KeyValuePair pair = new KeyValuePair();
    pair.setKey("X-Auth-Token");
    pair.setValue(assetServerDetails.getAuthToken());
    pdfReactorConfigurationWrapper.setRequestHeaders(pair);
  }
  
  private void addCSSAndFontToConfig(PDFReactorConfigurationWrapper config,
      ISmartDocumentRendererRequestModel model) throws Exception
  {
    String marks = model.getSmartDocumentPreset().getSmartDocumentPresetPdfConfiguration().getPdfMarksBleeds();
    if(marks != null)
    {
      switch(marks)
      {
        case BLEED:
          config.getUserStyleSheets().add(new Resource().setData(bleedBox));
          break;
        case PRINT:
          config.getUserStyleSheets().add(new Resource().setData(bleedBox));
          config.getUserStyleSheets().add(new Resource().setData(printMarks));
          break;
      }
    }
    
    ISmartDocumentInput smartDocumentByteArrays = model.getSmartDocumentByteArrays();
    Map<String, byte[]> cssByteArrayMap = smartDocumentByteArrays.getCssFileBytes();
    for (String cssFileName : cssByteArrayMap.keySet()) {
      config.getUserStyleSheets().add(new Resource().setData(cssByteArrayMap.get(cssFileName)));
    }
    
    Map<String, byte[]> fontByteArrayMap = smartDocumentByteArrays.getFontFileBytes();
    for (String fontFileName : fontByteArrayMap.keySet()) {
      String fileBaseName = FilenameUtils.getBaseName(fontFileName);
      String fileExtention = FilenameUtils.getExtension(fontFileName);
      config.getFonts().add(new Font()
          .setSource("data:font/" + fileExtention + ";base64," + Base64.getEncoder()
              .encodeToString(fontByteArrayMap.get(fontFileName)))
          .setFamily(fileBaseName));
    }
  }

  private boolean checkIfHasValue(String stringToCheck)
  {
    return stringToCheck != null && !stringToCheck.equals("");
  }
  
  @Lookup
  public PDFReactorConfigurationWrapper getPDFReactorConfigurationWrapper()
  {
    return null;
  }
}