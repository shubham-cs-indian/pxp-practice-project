import { ContextList } from '../contexts/context-creator';
import React, {Component} from 'react';

export default function (View)  {

  if(!View) {
    throw new Error("UNDEFINED_VIEW_PASSED");
  }

  return class MasterTagListContext extends Component {
    render () {
      return <ContextList.Consumer>
        {
          ({masterTagList}) => (<View {...this.props} masterTagListFromContext = {masterTagList}/>)
        }
      </ContextList.Consumer>;
    }
  }
}
