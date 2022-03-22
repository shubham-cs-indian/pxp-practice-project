import React from 'react';
import ReactPropTypes from 'prop-types';
import CS from '../../libraries/cs';

import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import AttributeTypeDictionary  from '../../commonmodule/tack/attribute-type-dictionary-new';
import HierarchyTypesDictionary from '../../commonmodule/tack/hierarchy-types-dictionary';
import CouplingConstants from '../../commonmodule/tack/coupling-constans';
import { view as ClickTooltipWrapperView } from './../tooltipview/click-tooltip-wrapper-view';

const oPropTypes = {
  oEntityInstance: ReactPropTypes.object,
  oSectionElement: ReactPropTypes.object,
  oAllConflictingSources: ReactPropTypes.object,
  sHierarchyContext: ReactPropTypes.string,
  fHandler: ReactPropTypes.func,
  fRef: ReactPropTypes.func
};

/** Functional Component **/
/**
 * @class PropertyCouplingIconsView - Use to Display coupling icon on properties.
 * @memberOf Views
 */
function PropertyCouplingIconsView ({oEntityInstance, oSectionElement, oAllConflictingSources, sHierarchyContext, fHandler, fRef}) {

  let checkTagValuesEquality = (oConflictingSourceValue, oEntityInstance) => {
    let oConflictingSourceValueClone = CS.cloneDeep(oConflictingSourceValue);
    let bIsEqual = true;
    CS.forEach(oEntityInstance.tagValues, function (oTagValue) {
      let aConflictingTagValues = CS.remove(oConflictingSourceValueClone.tagValues, {tagId: oTagValue.tagId});
      if (CS.isEmpty(aConflictingTagValues)) {
        if (oTagValue.relevance === 0) {
          return true;// continue
        } else {
          bIsEqual = false;
          return false;// break
        }
      } else if (aConflictingTagValues[0].relevance !== oTagValue.relevance) {
        bIsEqual = false;
        return false;// break
      }
    });
    if (!CS.isEmpty(oConflictingSourceValueClone.tagValues)) {
      if (CS.filter(oConflictingSourceValueClone.tagValues, {relevance: 0}).length !== oConflictingSourceValueClone.tagValues.length) {
        bIsEqual = false;
      }
    }
    return bIsEqual;
  };

  let isInstanceAndConflictingValueEqual = (oConflictingSourceValue, oEntityInstance) => {
    let sElementType = oSectionElement.type;
    switch (sElementType) {
      case 'attribute':
        if (oSectionElement.attribute.type === AttributeTypeDictionary.HTML) {
          return oConflictingSourceValue.valueAsHtml === oEntityInstance.value;
        }
        return oConflictingSourceValue.value === oEntityInstance.value;
      case 'tag':
        return checkTagValuesEquality(oConflictingSourceValue, oEntityInstance);
      case 'relationship':
        return oConflictingSourceValue.isResolved && oConflictingSourceValue == oEntityInstance.conflictingValues[0];
      default:
        return false;
    }
  };

  let getAllPossibleConflictingSourcesTooltipView = (oEntityInstance) => {
    let bShowAppliedConflict = false;
    let aInstanceConflictingValues = oEntityInstance.conflictingValues;
    if (CS.size(aInstanceConflictingValues) === 1) {
      bShowAppliedConflict = true;
    }
    let aElementConflictingSources = oSectionElement.conflictingSources;
    let aConflictingSourcesRows = [];

    CS.forEach(aElementConflictingSources, function (oElementConflictingSource) {
      let oConflictingSource = oAllConflictingSources[oElementConflictingSource.id];
      let sClassLabel = "";
      let sConflictOccursInClassName = "";
      let bShowAppliedIcon = false;
      if (!CS.isEmpty(oConflictingSource)) {
        sConflictOccursInClassName = oConflictingSource.conflictOccursIn;
        bShowAppliedIcon = bShowAppliedConflict && aInstanceConflictingValues[0].source.id === oElementConflictingSource.id && isInstanceAndConflictingValueEqual(aInstanceConflictingValues[0], oEntityInstance);
        sClassLabel = CS.getLabelOrCode(oConflictingSource);
      }
      let sSelectorClassName = "conflictSourceSelector";
      let oConflictSourceSelector = bShowAppliedIcon ? <span className={sSelectorClassName}/> : null;
      let sConflictingSourcesContainerClassName = "conflictingSourcesContainer" + (bShowAppliedIcon ? " selected" : "");

      aConflictingSourcesRows.push(
        <div className={sConflictingSourcesContainerClassName}>
            <span className={"conflictOccursIn " + sConflictOccursInClassName}/>
            <span className={"conflictSourceLabel"}>{getTranslation().FROM + " " + sClassLabel}</span>
            {oConflictSourceSelector}
          </div>
      )
    });
    return aConflictingSourcesRows;
  };

  let sCouplingType = oSectionElement.couplingType;
  if (sCouplingType === "looselyCoupled") {
    return null;
  }
  
  let bIsConflictResolved = false;
  let aInstanceConflictingValues = oEntityInstance.conflictingValues;
  if (CS.size(aInstanceConflictingValues) === 1 && isInstanceAndConflictingValueEqual(aInstanceConflictingValues[0], oEntityInstance)) {
    bIsConflictResolved = true;
  }
  let sCouplingLabel = "";
  let sCouplingClass = "";
  let oCouplingStyle = {"fontSize": "11px", "textAlign": "center"};
  let oSourceStyle = {"fontSize": "11px"};
  if (sHierarchyContext === HierarchyTypesDictionary.COLLECTION_HIERARCHY || sHierarchyContext === HierarchyTypesDictionary.TAXONOMY_HIERARCHY) {
    return null;
  } else {
    if (sCouplingType === CouplingConstants.DYNAMIC_COUPLED) {
      sCouplingLabel = getTranslation().DYNAMIC_COUPLED;
      sCouplingClass = "dynamicCoupled" + (bIsConflictResolved ? " applied" : "");
    } else if (sCouplingType === CouplingConstants.TIGHTLY_COUPLED) {
      sCouplingLabel = getTranslation().TIGHT_COUPLED;
      sCouplingClass = "tightlyCoupled" + (bIsConflictResolved ? " applied" : "");
    } else if (sCouplingType === CouplingConstants.READ_ONLY_COUPLED) {
      sCouplingLabel = getTranslation().READ_ONLY;
      sCouplingClass = sCouplingType;
    }
    let sSourceInfoLabel = getAllPossibleConflictingSourcesTooltipView(oEntityInstance);
    if (!CS.isEmpty(sSourceInfoLabel)) {
      sCouplingLabel = (
          <span>
                <div style={oCouplingStyle}>{sCouplingLabel}</div>
                <span style={oSourceStyle}>{sSourceInfoLabel}</span>
              </span>);
    }
    return (
        <ClickTooltipWrapperView couplingLabel={sCouplingLabel}>
          <div className={"clickableIcon " + sCouplingClass}
               ref={fRef}
               onClick={fHandler.bind(this, bIsConflictResolved)}>
          </div>
        </ClickTooltipWrapperView>
    );
  }
};

PropertyCouplingIconsView.propTypes = oPropTypes;

export default CS.SafeComponent(PropertyCouplingIconsView);