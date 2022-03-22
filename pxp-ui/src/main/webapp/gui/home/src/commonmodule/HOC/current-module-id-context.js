import { ContextList } from '../contexts/context-creator';
import React, {Component} from 'react';

export default function (View)  {

  if(!View) {
    throw new Error("UNDEFINED_VIEW_PASSED");
  }

  return class CurrentModuleIdContext extends Component {
    render () {
      return <ContextList.Consumer>
        {({currentModuleId}) => (<View {...this.props} currentModuleId = {currentModuleId}/>
        )}
      </ContextList.Consumer>;
    }
  }
}
