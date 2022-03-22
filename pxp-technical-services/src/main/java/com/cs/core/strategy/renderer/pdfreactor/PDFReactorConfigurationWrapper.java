package com.cs.core.strategy.renderer.pdfreactor;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.realobjects.pdfreactor.webservice.client.Configuration;
import com.realobjects.pdfreactor.webservice.client.Configuration.ColorSpace;
import com.realobjects.pdfreactor.webservice.client.Configuration.ColorSpaceSettings;
import com.realobjects.pdfreactor.webservice.client.Configuration.Encryption;
import com.realobjects.pdfreactor.webservice.client.Configuration.Font;
import com.realobjects.pdfreactor.webservice.client.Configuration.KeyValuePair;
import com.realobjects.pdfreactor.webservice.client.Configuration.Resource;


@Component("pdfReactorConfigurationWrapper")
@Scope("prototype")
public class PDFReactorConfigurationWrapper{
  
  private Configuration pdfConfiguration;
  
  private PDFReactorConfigurationWrapper()
  {
    pdfConfiguration = new Configuration();
  }
  
  public Configuration getPdfReactorConfiguration()
  {
    return pdfConfiguration;
  }
  
  public void setDocument(String htmlFromTemplateEngine)
  {
    pdfConfiguration.setDocument(htmlFromTemplateEngine);
  }
  
  public void setDocument(byte[] bytes)
  {
    pdfConfiguration.setDocument(bytes);
  }
  
  public void setTitle(String title)
  {
    pdfConfiguration.setTitle(title);
  }
  
  public void setSubject(String subject)
  {
    pdfConfiguration.setSubject(subject);
  }
  
  public void setAuthor(String author)
  {
    pdfConfiguration.setAuthor(author);
  }
  
  public void setKeywords(String keywords)
  {
    pdfConfiguration.setKeywords(keywords);
  }
  
  public void setUserPassword(String userPassword)
  {
    pdfConfiguration.setUserPassword(userPassword);
  }
  
  public void setOwnerPassword(String ownerPassword)
  {
    pdfConfiguration.setOwnerPassword(ownerPassword);
  }
  
  public void setEncryption(Encryption encryption)
  {
    pdfConfiguration.setEncryption(encryption);
  }
  
  public void setAllowPrinting(Boolean isAllowPrinting)
  {
    pdfConfiguration.setAllowPrinting(isAllowPrinting);
  }
  
  public void setAllowCopy(Boolean isAllowCopy)
  {
    pdfConfiguration.setAllowCopy(isAllowCopy);
  }
  
  public void setAllowModifyContents(Boolean isAllowModifyContents)
  {
    pdfConfiguration.setAllowModifyContents(isAllowModifyContents);
  }
  
  public void setAllowAnnotations(Boolean isAllowAnnotations)
  {
    pdfConfiguration.setAllowAnnotations(isAllowAnnotations);
  }
  
  public void setLicenseKey(String licenseKey)
  {
    pdfConfiguration.setLicenseKey(licenseKey);
  }
  
  public void setThrowLicenseExceptions(Boolean isThrowLicenseExceptions)
  {
    pdfConfiguration.setThrowLicenseExceptions(isThrowLicenseExceptions);
  }
  
  public void setRequestHeaders(KeyValuePair pair)
  {
    pdfConfiguration.setRequestHeaders(pair);
  }
  
  public List<Resource> getUserStyleSheets()
  {
    return pdfConfiguration.getUserStyleSheets();
  }
  
  public List<Font> getFonts()
  {
    return pdfConfiguration.getFonts();
  }
  
  public void setDefaultColorSpace(ColorSpace colorSpace)
  {
    pdfConfiguration.setDefaultColorSpace(colorSpace);
    
    ColorSpaceSettings colorSpaceSettings = new ColorSpaceSettings();
    colorSpaceSettings.setTargetColorSpace(colorSpace);
    colorSpaceSettings.setConversionEnabled(true);
    pdfConfiguration.setColorSpaceSettings(colorSpaceSettings);
  }

}