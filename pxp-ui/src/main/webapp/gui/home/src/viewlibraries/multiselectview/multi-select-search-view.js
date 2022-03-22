
import React from 'react';
import {view as CoreView, events as CoreEvents, propTypes as CorePropTypes} from './core-multi-select-search-view';

class MultiSelectSearchView extends CoreView {
  constructor(props) {
    super(props);
  }
}

MultiSelectSearchView.propTypes = CorePropTypes;

export const view = MultiSelectSearchView;
export const events = CoreEvents;
export const propTypes = CorePropTypes;
