import CS from '../../libraries/cs';
import React from 'react';

import MasterTagListContext from '../../commonmodule/HOC/master-tag-list-context';
import {view as CoreEntityTagsSummaryView} from './core-entity-tags-summary-view';
const oPropTypes = CoreEntityTagsSummaryView.propTypes;
const oEvents = CoreEntityTagsSummaryView.events;

// @MasterTagListContext
// @CS.SafeComponent
class CustomEntityTagsSummaryView extends CoreEntityTagsSummaryView {

  constructor (props) {
    super(props);
  }

/*  getBooleanTagView = (oTagValue, aWithoutIconViews, oTagValueMaster, iIndex, aNotVisibleItemInChipsIdList, aMoreFilterItems, oTagGroupMaster) => {
    if(oTagValue.relevance != 0) {
      oTagValueMaster.label = CS.getLabelOrCode(oTagGroupMaster);
      this.getTagValueView(oTagValue, oTagValueMaster, iIndex, aNotVisibleItemInChipsIdList, aWithoutIconViews, aMoreFilterItems);
    }
  };*/

}
CustomEntityTagsSummaryView.propTypes = oPropTypes;
export const view = MasterTagListContext(CustomEntityTagsSummaryView);
export const events = oEvents;
