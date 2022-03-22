package com.cs.core.bgprocess.services.dataintegration;


public class ImportBlockIdentifier {
  
   private String  identifier;
   
   private String language;

  
  public String getIdentifier()
  {
    return identifier;
  }

  
  public void setIdentifier(String identifier)
  {
    this.identifier = identifier;
  }

  
  public String getLanguage()
  {
    return language;
  }

  
  public void setLanguage(String language)
  {
    this.language = language;
  }


  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
    result = prime * result + ((language == null) ? 0 : language.hashCode());
    return result;
  }


  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ImportBlockIdentifier other = (ImportBlockIdentifier) obj;
    if (identifier == null) {
      if (other.identifier != null)
        return false;
    }
    else if (!identifier.equals(other.identifier))
      return false;
    if (language == null) {
      if (other.language != null)
        return false;
    }
    else if (!language.equals(other.language))
      return false;
    return true;
  } 
   
   
  
}
