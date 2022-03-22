package com.cs.config.strategy.plugin.model;

import com.tinkerpop.blueprints.Vertex;
import java.util.Set;

public interface IGetConfigDetailsHelperModelForComparingInstances {
  
  public void setNatureNodes(Set<Vertex> natureNode);
  
  public Set<Vertex> getNatureNodes();
  
  public void setNonNatureNodes(Set<Vertex> nonNatureNodes);
  
  public Set<Vertex> getNonNatureNodes();
  
  public void setCollectionNodes(Set<Vertex> collectionNodes);
  
  public Set<Vertex> getCollectionNodes();
  
  public void setTaxonomyVertices(Set<Vertex> taxonomyVertices);
  
  public Set<Vertex> getTaxonomyVertices();
}
