package com.cs.core.bgprocess.services.dataintegration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.pxon.parser.PXONFileParser;

/**
 * Map of information blocks obtained from parsing a PXON file
 *
 * @author vallee
 */
class PXONImporterBlocksMap<E extends Enum> {

  public enum ImportBlockStatus {
    PENDING, IN_PROGRESS, DONE;
  }
  
  /**
   * Tracking structure to follow up status of blocks
   */
  public static class ImportBlockInfo {
    private long startPos;
    private long endPos;
    private ImportBlockStatus status;

    public long getStartPos() {
      return startPos;
    }
    
    public long getEndPos() {
      return endPos;
    }
    
    public ImportBlockStatus getStatus() {
      return status;
    }
    
    public void setStatus( ImportBlockStatus status ) {
      this.status = status;
    }
  }
  
  private final  Map<E, Map<ImportBlockIdentifier, ImportBlockInfo>> importBlocks = new LinkedHashMap<>();

  /**
   * Declare a block in the map
   * @param step the importer step that manages the block
   * @param identifier the object identified of the block
   * @param block the block information
   */
  ImportBlockInfo registerBlock( E step, ImportBlockIdentifier identifier, ImportBlockInfo block) {
    if ( !importBlocks.containsKey(step) )
      importBlocks.put( step, new LinkedHashMap<>());
    importBlocks.get(step).put( identifier, block);
    return block;
  }

  /**
   * Declare a new block in the map from parsed block information
   * @param step the importer step that manages the block
   * @param identifier the object identified of the block
   * @return the block information in PENDING status
   */
  ImportBlockInfo registerBlock(E step, ImportBlockIdentifier identifier, PXONFileParser.PXONBlock parsedBlock) {
    ImportBlockInfo block = new ImportBlockInfo();
    block.startPos = parsedBlock.getStart();
    block.endPos = parsedBlock.getEnd();
    block.status = ImportBlockStatus.PENDING;
    return registerBlock(step, identifier, block);
  }
  
  /**
   * @return the declared steps
   */
  Set<E> getSteps() {
    return importBlocks.keySet();
  }
  
  /**
   * @param step
   * @return the blocks registered for a step or an empty content
   */
   Map<ImportBlockIdentifier, ImportBlockInfo> getStepBlocks( E step) {
    if ( !importBlocks.containsKey(step) )
      return new HashMap<>(); // Empty
    return importBlocks.get(step);
  }
  
  /**
   * @return a report on content (steps and number of blocks per step)
   */
  List<String> reportSteps() {
    List<String> report = new ArrayList<>();
    importBlocks.forEach( (step, map) ->  { report.add( step.toString() + ": " + map.size());});
    return report;
  }

  static String getPXONBlockFromFile(PXONFileParser pxonFileParser,
      ImportBlockInfo baseEntityMetaInfo) throws IOException
  {
    long offset = baseEntityMetaInfo.getStartPos();
    long length = baseEntityMetaInfo.getEndPos();
    return pxonFileParser.getBlock(offset, length);
  }
}
