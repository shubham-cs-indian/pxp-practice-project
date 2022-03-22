import React from 'react';

export const ContextList = React.createContext(
    {
      currentModuleId: '',
      currentZoomValue: 2,
      masterTagList: {},
      masterAttributeList: [],
      masterUserList: [],
      staticCollectionList: []
    }
);