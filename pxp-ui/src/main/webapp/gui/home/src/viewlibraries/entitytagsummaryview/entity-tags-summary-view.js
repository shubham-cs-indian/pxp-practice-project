import CS from '../../libraries/cs';
import React from 'react';

import MasterTagListContext from '../../commonmodule/HOC/master-tag-list-context';
import {view as CoreEntityTagsSummaryView} from './core-entity-tags-summary-view';
const oPropTypes = CoreEntityTagsSummaryView.propTypes;
const oEvents = CoreEntityTagsSummaryView.events;

// @MasterTagListContext
// @CS.SafeComponent
class EntityTagsSummaryView extends CoreEntityTagsSummaryView {

  constructor (props) {
    super(props);
  }
}
EntityTagsSummaryView.propTypes = oPropTypes;
export const view = MasterTagListContext(EntityTagsSummaryView);
export const events = oEvents;
