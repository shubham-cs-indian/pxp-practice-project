package com.cs.core.bgprocess.services.dataintegration;

import com.cs.config.dto.ConfigClassifierDTO;
import com.cs.config.idto.IConfigClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;

import java.util.List;
import java.util.Map;

public abstract class AbstractHierarchyTree {
  
  protected static final String ROOT                   = "root";
  
  protected void prepareHierarchyTree(Node<String> root, ConfigClassifierDTO configClassDTO, IClassifierDTO classifierDTO)
  {
    String code = classifierDTO.getCode();
    String parentCode = configClassDTO.getParentCode();
    Node<String> childNode = getNode(code, root.getChildren(), true);
    if(childNode == null){
      childNode = new Node<>(code);
    }
    childNode.setConfigClassDTO(configClassDTO);
  
    Node<String> parentNode = getNode(parentCode, root.getChildren(), false);
    if(parentNode == null) {
      parentNode = new Node<String>(parentCode);
      root.addChild(parentNode);
    }
    parentNode.addChild(childNode);
    childNode.setParent(parentNode);
  }

  protected Node<String> getNode(String code, List<Node<String>> children, Boolean isRemove)
  {
    for(Node<String> child : children) {
      if(child.getData().equals(code)) {
        if(isRemove)
          children.remove(child);
        return child;
      }else if(!child.getChildren().isEmpty()) {
         Node<String> node = getNode(code, child.getChildren(), isRemove);
         if(node != null)
           return node;
      }
    }
    return null;
  }
  
  protected <T> void prepareList(Node<T> node, List<IConfigClassifierDTO> classDTOList, Map<String, ConfigClassifierDTO> classes)
  {
    List<Node<T>> children = node.getChildren();
    children.forEach(each -> {
      String code = (String) each.getData();
      IConfigClassifierDTO classDTO = classes.get(code);
      if(classDTO != null)
        classDTOList.add(classDTO);
      prepareList(each, classDTOList, classes);
    });
  }
}
