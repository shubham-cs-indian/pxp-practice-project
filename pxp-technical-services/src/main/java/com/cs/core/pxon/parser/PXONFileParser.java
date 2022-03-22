package com.cs.core.pxon.parser;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;

/**
 * PXON File parser
 *
 * @author tauseef
 */
public class PXONFileParser implements Closeable {
  
  private final RandomAccessFile file;
  private final long fileLength;
 
  public static class PXONBlock {
    private long start = 0L;
    private long end = 0L; 
    private StringBuilder data = new StringBuilder();
    public long getStart() {
      return start;
    }
    public long getEnd() {
      return end;
    }
    public String getData() {
      return data.toString();
    }
    public boolean isCompleted() {
      return data.length() > 0 && (end > start);
    }
  }

  /**
   * @param filePath Full path the file to read
   * @throws com.cs.core.technical.exception.CSInitializationException
   */
  public PXONFileParser(String filePath) throws CSInitializationException {
    try {
      file = new RandomAccessFile(filePath, "r");
      fileLength = file.length();
    } catch ( IOException ex ) {
      throw new CSInitializationException( ex.getMessage());
    }
  }
  
  /**
   * @return the next PXON Block found in current file
   * @throws IOException read exception
   */
  private PXONBlock readNextBlock() throws IOException {
    int braceCount = 0;
    boolean endOfBlock = false;
    PXONBlock blockInfo = new PXONBlock();
    byte cbyte; // current byte read from the file
    byte pbyte = 0; // previous byte read from the file
    do {
      try {
        cbyte = file.readByte(); // current byte read from the file
      } catch ( EOFException ex ) {
        return blockInfo;
      }
      
      if ( cbyte == '{' ) {
          if( braceCount == 0 ) {
            blockInfo.start = file.getFilePointer()-1;
          }
          braceCount++;
      } 
      else if ( cbyte == '}' ) {
          braceCount--;
          endOfBlock = ( braceCount == 0 );
      }
      else if ( cbyte == '"' && pbyte != '\\' ) {
          do {
            pbyte = cbyte;

            //If braceCount > 0 then store data in block info
            if(braceCount > 0)
            {
              if(cbyte < 0) 
              {
                blockInfo.data.append(getSpecialCharacter(cbyte));
              }
              else
              {
                blockInfo.data.append( (char)cbyte);
              }
            }
            cbyte = file.readByte();
          } while (pbyte == '\\' || cbyte != '"');
      }
      if (braceCount > 0) {
        blockInfo.data.append( (char)cbyte);
      }
      if (endOfBlock) {
        blockInfo.data.append( (char)cbyte);
        blockInfo.end = file.getFilePointer();
        return blockInfo;
      }
      pbyte = cbyte;
    } while ( cbyte > 0 );
    return null;
  }

  private char getSpecialCharacter(byte cbyte)
  {
   switch(cbyte) {
     
     case -57 : return 0x00C7;
     case -4  : return 0x00FC;
     case -23 : return 0x00E9;
     case -30 : return 0x00E2;
     case -28 : return 0x00E4;
     case -32 : return 0x00E0;
     case -27 : return 0x00E5;
     case -25 : return 0x00E7;
     case -22 : return 0x00EA;
     case -21 : return 0x00EB;
     
     case -24 : return 0x00E8;
     case -17 : return 0x00EF;
     case -18 : return 0x00EE;
     case -20 : return 0x00EC;
     case -60 : return 0x00C4;
     case -59 : return 0x00C5;
     case -55 : return 0x00C9;
     case -26 : return 0x00E6;
     case -58 : return 0x00C6;
     case -12 : return 0x00F4;
     
     case -10 : return 0x00F6;
     case -14 : return 0x00F2;
     case -5  : return 0x00FB;
     case -7  : return 0x00F9;
     case -1  : return 0x00FF;
     case -42 : return 0x00D6;
     case -36 : return 0x00DC;
     case -8 :  return 0x00A2;
     case -93 : return 0x00A3;
     case -40 : return 0x00A5;
     
     case -41 : return 0x20A7;
     case -125: return 0x0192;
     case -31 : return 0x00E1;
     case -19 : return 0x00ED;
     case -13 : return 0x00F3;
     case -6  : return 0x00FA;
     case -15 : return 0x00F1;
     case -47 : return 0x00D1;
     case -86 : return 0x00AA;
     case -70 : return 0x00BA;
    
     case -65 : return 0x00BF;
     case -84 : return 0x00AC;
     case -67 : return 0x00BD;
     case -68 : return 0x00BC;
     case -95 : return 0x00A1;
     case -85 : return 0x00AB;
     case -69 : return 0x00BB;
     case -33 : return 0x00DF;
     case -75 : return 0x00B5;
     case -79 : return 0x00B1;
     
     default:
       break;
   }
   
    return (char) cbyte;
  }

  /**
   * @return the next PXON Block found in current file or null when end of file has been reached
   * @throws CSFormatException read exception
   */
  public PXONBlock getNextBlock() throws CSFormatException {
    try {
      PXONBlock block = readNextBlock();
      return ( block.isCompleted() ? block : null );
    } catch (IOException ex) {
      throw new CSFormatException( ex.getMessage());
    }
  }

  /**
   * @return Block of PXON data in string buffer
   * @throws IOException read exception
   */
  public String getBlock( long startPos, long endPos) throws IOException {
    Long length = endPos - startPos;
    file.seek(startPos);
    byte[] pxonBlock = new byte[length.intValue()];
    file.read(pxonBlock);
    return new String(pxonBlock);
  }

  @Override
  public void close() throws IOException
  {
    file.close();
  }
}
