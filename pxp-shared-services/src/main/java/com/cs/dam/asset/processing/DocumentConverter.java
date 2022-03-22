package com.cs.dam.asset.processing;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.json.simple.JSONObject;

import com.cs.config.interactor.entity.indsserver.IInDesignServerInstance;
import com.cs.config.standard.IStandardConfig;
import com.cs.constants.DAMConstants;
import com.cs.constants.FileExtensionConstants;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.runtime.interactor.constants.INDSConstants;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.dam.asset.exceptions.DocumentConversionException;
import com.cs.dam.asset.exceptions.ImageMagickException;
import com.cs.runtime.interactor.model.indsserver.GeneratePdfFromIndesignFileModel;
import com.cs.runtime.interactor.model.indsserver.IGeneratePdfFromIndesignFileModel;
import com.cs.runtime.interactor.model.indsserver.IINDSPingTaskRequestModel;
import com.cs.runtime.interactor.model.indsserver.INDSPingTaskRequestModel;
import com.cs.utils.dam.AssetUtils;
import com.cs.utils.dam.InDesignServerUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

public class DocumentConverter {
  
  private DocumentConverter()
  {
    // Private constructor
  }
  
  public static final String DOCUMENT_CONVERSION_EXCEPTION_MESSAGE = "Document conversion not supported";
  private static final String ORIENT_GET_ALL_INDS_INSTANCES        = "GetAllInDesignServerInstances";
  
  /**
   * This method converts doc/file to PDF.
   * @param sourcePath
   * @return
   * @throws RDBMSException 
   * @throws IOException 
   * @throws DocumentConversionException 
   * @throws DocumentException 
   * @throws Exception
   */
  public static String convertDocument(String sourcePath, Boolean isIndesignServerEnabled)
      throws RDBMSException, DocumentConversionException, IOException, DocumentException
  {
    File temp = null;
    Boolean exceptionOccured = false;
    String convertPath = sourcePath;
    String[] extensions = sourcePath.split("\\.");
    String ext = extensions[extensions.length - 1].toLowerCase();
    try {
      temp = new File(AssetUtils.getFilePath() + DAMConstants.TEMP_CONVERSION_FILE_NAME_PREFIX
          + RDBMSAppDriverManager.getDriver()
              .newUniqueID(IStandardConfig.UniquePrefix.RANDOM.getPrefix())
          + FileExtensionConstants.PDF_EXT);
      temp.createNewFile();
      
      switch (ext) {
        case FileExtensionConstants.DOC:
          convertPath = convertFromDoc(sourcePath, temp);
          break;
        case FileExtensionConstants.DOCX:
          convertPath = convertFromDocx(sourcePath, temp);
          break;
        case FileExtensionConstants.PPTX:
          convertPath = convertFromPptx(sourcePath, temp);
          break;
        case FileExtensionConstants.PPT:
          convertPath = convertFromPpt(sourcePath, temp);
          break;
        case FileExtensionConstants.INDD:
        case FileExtensionConstants.INDT:
        case FileExtensionConstants.IDLK:
        case FileExtensionConstants.IDML:
        case FileExtensionConstants.IDMS:
          if (!isIndesignServerEnabled) {
            exceptionOccured = true;
            throw new DocumentConversionException("Indesign server configuration disabled");
          }
          try {
            convertPath = convertFromIndd(sourcePath, temp);
          }
          catch (Exception e) {
            exceptionOccured = true;
            throw new DocumentConversionException("Indesign server exception:: " + e.getMessage());
          }
          break;
        default:
          exceptionOccured = true;
          throw new DocumentConversionException(DOCUMENT_CONVERSION_EXCEPTION_MESSAGE);
      }
    }
    catch(Exception e) {
      exceptionOccured = true;
      throw e;
    }
    finally {
      if (exceptionOccured) {
        AssetUtils.deleteFileAndDirectory(temp);
      }
    }
    
    return convertPath;
  }
  
  /**
   * This method converts doc to PDF
   * @param sourcePath
   * @param tempFile
   * @return
   * @throws DocumentConversionException
   * @throws IOException
   * @throws FileNotFoundException
   * @throws Exception
   */
  public static String convertFromDoc(String sourcePath, File tempFile) throws IOException, DocumentConversionException
  {
    File docFile = new File(sourcePath);
    POIFSFileSystem fs = null;
    WordExtractor we = null;
    HWPFDocument doc = null;
    FileInputStream fileIs = null;
    FileOutputStream outstream = null;
    Document document = null;
    try {
      outstream = new FileOutputStream(tempFile);
      fileIs = new FileInputStream(docFile);
      fs = new POIFSFileSystem(fileIs);
      doc = new HWPFDocument(fs);
      we = new WordExtractor(doc);
      
      document = new Document();
      PdfWriter writer = PdfWriter.getInstance(document, outstream);
      
      document.open();
      writer.setPageEmpty(true);
      document.newPage();
      writer.setPageEmpty(true);
      
      String[] paragraphs = we.getParagraphText();
      for (int i = 0; i < paragraphs.length; i++) {
        paragraphs[i] = paragraphs[i].replaceAll("\\cM?\r?\n", "");
        document.add(new Paragraph(paragraphs[i]));
      }
    }
    catch (IOException e) {
      throw e;
    }
    catch (Exception e) {
      throw new DocumentConversionException(e);
    }
    
    finally {
      if (document.isOpen())
        document.close();
      if (we != null)
        we.close();
      if (we != null)
        we.close();
      if (fileIs != null)
        fileIs.close();
      if (outstream != null)
        outstream.close();
    }
    return tempFile.getAbsolutePath();
  }
  
  /**
   * This method converts docx to pdf
   * @param sourcePath
   * @param tempFile
   * @return
   * @throws DocumentConversionException
   * @throws IOException 
   * @throws Exception
   */
  public static String convertFromDocx(String sourcePath, File tempFile)
      throws DocumentConversionException, IOException
  {
    File docFile = new File(sourcePath);
    FileOutputStream outStream = null;
    FileInputStream fileIs = null;
    PdfOptions options = PdfOptions.create();
    try {
      outStream = new FileOutputStream(tempFile);
      fileIs = new FileInputStream(docFile);
      XWPFDocument document = new XWPFDocument(fileIs);
      PdfConverter.getInstance()
          .convert(document, outStream, options);
    }
    catch (IOException e) {
      throw new DocumentConversionException(e);
    }
    
    finally {
      if (fileIs != null)
        fileIs.close();
      if (outStream != null)
        outStream.close();
    }
    
    return tempFile.getAbsolutePath();
  }
  
  /**
   * This method converts pptx to pdf.
   * @param sourcePath
   * @param tempFile
   * @return
   * @throws IOException
   * @throws DocumentException 
   * @throws Exception
   */
  public static String convertFromPptx(String sourcePath, File tempFile)
      throws DocumentConversionException, IOException, DocumentException
  {
    File docFile = new File(sourcePath);
    FileInputStream fileIs = new FileInputStream(docFile);
    XSLFSlide[] slides = null;
    XMLSlideShow ppt = new XMLSlideShow(fileIs);
    Dimension dimension = ppt.getPageSize();
    slides = ppt.getSlides();
    Dimension pgsize = dimension;
    double zoom = 2;
    AffineTransform at = new AffineTransform();
    at.setToScale(zoom, zoom);
    Document document = new Document();
    FileOutputStream outStream = new FileOutputStream(tempFile);
    PdfWriter writer = PdfWriter.getInstance(document, outStream);
    document.open();
    try {
      for (int i = 0; i < slides.length; i++) {
        BufferedImage bufImg = new BufferedImage((int) Math.ceil(pgsize.width * zoom),
            (int) Math.ceil(pgsize.height * zoom), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufImg.createGraphics();
        graphics.setTransform(at);
        graphics.setPaint(slides[i].getBackground()
            .getFillColor());
        graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
        try {
          slides[i].draw(graphics);
        }
        catch (Exception e) {
          System.out.println(docFile.getName() + " -> slide No: " + (i + 1));
        }
        Image image = Image.getInstance(bufImg, null);
        document.setPageSize(new Rectangle(image.getScaledWidth(), image.getScaledHeight()));
        document.newPage();
        image.setAbsolutePosition(0, 0);
        document.add(image);
      }
    }
    catch (Exception e) {
      throw new DocumentConversionException(e);
    }
    finally {
      if (document.isOpen())
        document.close();
      if (writer != null)
        writer.close();
      if (outStream != null)
        outStream.close();
      if (fileIs != null)
        fileIs.close();
    }
    return tempFile.getAbsolutePath();
  }
  
  /**
   * This method converts ppt to pdf
   * @param sourcePath
   * @param tempFile
   * @return
   * @throws IOException
   * @throws DocumentConversionException
   * @throws DocumentException 
   * @throws Exception
   */
  public static String convertFromPpt(String sourcePath, File tempFile)
      throws IOException, DocumentConversionException, DocumentException
  {
    File docFile = new File(sourcePath);
    FileInputStream fileIs = new FileInputStream(docFile);
    Slide[] slides = null;
    SlideShow ppt = new SlideShow(fileIs);
    Dimension dimension = ppt.getPageSize();
    slides = ppt.getSlides();
    Dimension pgsize = dimension;
    double zoom = 2;
    AffineTransform at = new AffineTransform();
    at.setToScale(zoom, zoom);
    Document document = new Document();
    FileOutputStream outStream = new FileOutputStream(tempFile);
    PdfWriter writer = PdfWriter.getInstance(document, outStream);
    document.open();
    try {
      for (int i = 0; i < slides.length; i++) {
        
        BufferedImage bufImg = new BufferedImage((int) Math.ceil(pgsize.width * zoom),
            (int) Math.ceil(pgsize.height * zoom), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufImg.createGraphics();
        graphics.setTransform(at);
        // clear the drawing area
        graphics.setPaint(slides[i].getBackground()
            .getFill()
            .getForegroundColor());
        graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
        slides[i].draw(graphics);
        
        Image image = Image.getInstance(bufImg, null);
        document.setPageSize(new Rectangle(image.getScaledWidth(), image.getScaledHeight()));
        document.newPage();
        image.setAbsolutePosition(0, 0);
        document.add(image);
      }
    }
    catch (Exception e) {
      throw new DocumentConversionException(e);
    }
    finally {
      if (document.isOpen())
        document.close();
      if (writer != null)
        writer.close();
      if (outStream != null)
        outStream.close();
      if (fileIs != null)
        fileIs.close();
    }
    
    return tempFile.getAbsolutePath();
  }
  
  /**
   * This method is used to pdf conversion
   * @param sourcePath
   * @return
   * @throws ImageMagickException
   * @throws Exception
   */
  public static BufferedImage convertPdf(String sourcePath) throws ImageMagickException
  {
    return convertPdf(sourcePath, DAMConstants.THUMBNAIL_HEIGHT, DAMConstants.THUMBNAIL_WIDTH);
  }
  
  /**
   * This method generates a thumbnail of a pdf file.
   *
   * @param sourcePath:
   *          String - source path of the video file
   * @return BufferedImage: of the generated thumbnail
   * @throws ImageMagickException
   * @throws Exception
   */
  public static BufferedImage convertPdf(String sourcePath, Integer imageHeight, Integer imageWidth)
      throws ImageMagickException
  {
    ConvertCmd cmd = new ConvertCmd();
    IMOperation op = new IMOperation();
    File temp;
    BufferedImage img;
    try {
      temp = File.createTempFile(DAMConstants.TEMP_CONVERTED_FILE_NAME,
          FileExtensionConstants.PNG_EXT);
    }
    catch (IOException e) {
      throw new ImageMagickException(e);
    }
    String tempFilePath = temp.getAbsolutePath();
    op.addImage(sourcePath + "[0]");
    
    op.adaptiveResize(imageHeight, imageWidth, '>');
    op.addImage(tempFilePath);
    try {
      cmd.run(op);
      img = ImageIO.read(temp);
    }
    catch (Exception e) {
      throw new ImageMagickException(e);
    }
    finally {
      if(temp != null) {
        AssetUtils.deleteFileAndDirectory(temp);
      }
    }
    return img;
  }
  
  /**
   * Generate preview of in design files
   * @param sourcePath
   * @param temp
   * @return
   * @throws Exception
   */
  private static String convertFromIndd(String sourcePath, File temp) throws Exception
  {
    IGeneratePdfFromIndesignFileModel model = new GeneratePdfFromIndesignFileModel();
    
    Path filePath = Paths.get(sourcePath);
    byte[] indesignFileBytes = Files.readAllBytes(filePath);
    
    // Ping indesign server instances task
    Map<String, Object> requestMapForPingServer = new HashMap<>();
    JSONObject pingTaskResponse = CSConfigServer.instance().request(requestMapForPingServer, ORIENT_GET_ALL_INDS_INSTANCES, "");
    IINDSPingTaskRequestModel pingTaskRequestModel = ObjectMapperUtil.convertValue(pingTaskResponse, INDSPingTaskRequestModel.class);
    IInDesignServerInstance indsLoadBalancerProperties = pingTaskRequestModel.getIndsLoadBalancer();
    
    if (indsLoadBalancerProperties == null || pingTaskRequestModel.getServersToPing().isEmpty()) {
      throw new ImageMagickException("InDesign Server Unavailable");
    }
    
    model.setFileByteStream(indesignFileBytes);    
    model.setIndsLoadBalancer(indsLoadBalancerProperties);
    String fileName = filePath.getFileName().toString();
    model.setDocumentName(fileName);
    model.setTaskId(INDSConstants.GENERATE_PDF_OF_INDESIGN);
    
    // Call generate preview
    String requestDataForGeneratePDF = ObjectMapperUtil.writeValueAsString(model);
    String responseString = InDesignServerUtils.sendRequest(requestDataForGeneratePDF, INDSConstants.GENERATE_PDF_OF_INDESIGN, indsLoadBalancerProperties);
    IGeneratePdfFromIndesignFileModel returnModel = ObjectMapperUtil.readValue(responseString, GeneratePdfFromIndesignFileModel.class);
    byte[] pdfFileBytes = returnModel.getFileByteStream();
    Files.write(Paths.get(temp.getAbsolutePath()), pdfFileBytes);
    return temp.getAbsolutePath();
  }
  
}
