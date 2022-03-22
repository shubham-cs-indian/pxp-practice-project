import React from 'react';
import ReactPropTypes from 'prop-types';
import CS from '../../libraries/cs';

import CouplingConstants from './../../commonmodule/tack/coupling-constans';
import { getTranslations as getTranslation }from '../../commonmodule/store/helper/translation-manager.js';
import TooltipView from './../../viewlibraries/tooltipview/tooltip-view';

const oPropTypes = {
  oEntityInstance: ReactPropTypes.object,
  bIsForNotification: ReactPropTypes.bool,
  oSectionElement: ReactPropTypes.object,
  fHandler: ReactPropTypes.func,
  fRef: ReactPropTypes.func,
  sHierarchyContext: ReactPropTypes.string,
};

function PropertyConflictingValuesIconView ({oEntityInstance, bIsForNotification, oSectionElement, fHandler, fRef}) {
  let oConflictingSources = oSectionElement.conflictingSources;
  if (oSectionElement.couplingType == CouplingConstants.READ_ONLY_COUPLED || (CS.isEmpty(oEntityInstance.conflictingValues) && CS.isEmpty(oConflictingSources))) {
    return null;
  }
  let sIconClassSuffix = bIsForNotification ? " notificationIcon" : " conflictingIcon";
  let sTooltipLabel = bIsForNotification ? getTranslation().NOTIFICATION : getTranslation().CONFLICTING_VALUES;
  let aConflictingValuesIconView = [];

  let bShowPossibleConflictingSourcesWithValuesIcon = true;
  let isConflictResolved = CS.find(oEntityInstance.conflictingValues, function(conflict) {
    if (conflict.isResolved == true) return true;
  })
  // Added check isConflictResolved, if conflict is already resolved dont show conflict icon
  if (CS.isEmpty(oEntityInstance.notification) || isConflictResolved) {
    bShowPossibleConflictingSourcesWithValuesIcon = (oEntityInstance.conflictingValues && oEntityInstance.conflictingValues.length > 1);
  }

  if (bShowPossibleConflictingSourcesWithValuesIcon) {
    aConflictingValuesIconView.push(
        <TooltipView label={sTooltipLabel} placement="bottom">
          <div className={"clickableIcon" + sIconClassSuffix + " conflictingSourcesWithValues"}
               ref={fRef}
               onClick={fHandler.bind(this, false)}>
          </div>
        </TooltipView>
    );
  }

  return aConflictingValuesIconView;
}

PropertyConflictingValuesIconView.propTypes = oPropTypes;

export default CS.SafeComponent(PropertyConflictingValuesIconView);
