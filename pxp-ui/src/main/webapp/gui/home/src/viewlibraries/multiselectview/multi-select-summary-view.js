import React from 'react';
import {view as CoreView, events as CoreEvents, propTypes as CorePropTypes} from './core-multi-select-search-view';

class MultiSelectSummaryView extends CoreView {

  constructor (props) {
    super(props);
  }

  render () {
    let sClassName = "multiSelectSearchViewWrapper" + (this.props.disabled ? " disabled" : "");
    return (
        <div className={sClassName}>
          {this.getMainMultiselectSearchView()}
        </div>
    );
  }
}

MultiSelectSummaryView.propTypes = CorePropTypes;

export const view = MultiSelectSummaryView;
export const events = CoreEvents;
export const propTypes = CorePropTypes;
