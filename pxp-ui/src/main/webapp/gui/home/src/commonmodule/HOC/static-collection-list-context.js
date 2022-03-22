import { ContextList } from '../contexts/context-creator';
import React, {Component} from 'react';

export default function (View)  {

  if(!View) {
    throw new Error("UNDEFINED_VIEW_PASSED");
  }

  return class StaticCollectionListContext extends Component {
    render () {
      return <ContextList.Consumer>
        {({staticCollectionList}) => (<View {...this.props} staticCollectionList = {staticCollectionList}/>
        )}
      </ContextList.Consumer>;
    }
  }
}
