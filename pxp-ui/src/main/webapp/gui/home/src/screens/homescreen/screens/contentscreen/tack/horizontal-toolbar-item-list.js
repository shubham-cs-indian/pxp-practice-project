var aTileGridListViewItem = [
  {
    id: 'tile_mode',
    className: 'horToolTileView',
    title: 'TILE_VIEW',
    showIcon: true,
    text: ""
  },
  {
    id: 'list_mode',
    className: 'horToolListView',
    title: 'LIST_VIEW',
    showIcon: true,
    text: ""
  }
];

var aSignalFilter = [
  {
    id: 'all_article',
    className: 'horToolAllArticle',
    title: 'ALL',
    showIcon: true,
    text: true
  },
  {
    id: 'red_article',
    className: 'horToolIndicator horToolRedArticle',
    title: 'RED',
    showIcon: true,
    text: true
  },
  {
    id: 'orange_article',
    className: 'horToolIndicator horToolOrangeArticle',
    title: 'ORANGE',
    showIcon: true,
    text: ""
  },
  {
    id: 'yellow_article',
    className: 'horToolIndicator horToolYellowArticle',
    title: 'YELLOW',
    showIcon: true,
    text: ""
  },
  {
    id: 'green_article',
    className: 'horToolIndicator horToolGreenArticle',
    title: 'GREEN_ARTICLE',
    showIcon: true,
    text: ""
  }
];

var aZoomItems = [
  {
    id: 'zoomin',
    className: 'horToolZoomIn',
    title: 'ZOOM_IN',
    showIcon: true,
    text: ""
  },
  {
    id: 'zoomout',
    className: 'horToolZoomOut',
    title: 'ZOOM_OUT',
    showIcon: true,
    text: ""
  }
];

export default {

  ArticleChildrenSection: [
    {
      id: "group2",
      items: aTileGridListViewItem
    },
    {
      id: "group1",
      items: [
        {
          id: 'select_all_content',
          className: 'horToolSelectAll',
          title: 'SELECT_ALL',
          showIcon: true,
          text: ""
        },
        {
          id: 'delete_entity',
          className: 'horToolDelete',
          title: 'DELETE',
          showIcon: true,
          text: ""
        },
        {
          id: 'all',
          className: 'horToolShowAll',
          title: 'ALL',
          showIcon: false,
          text: 'ALL'
        },
        {
          id: 'folder',
          className: 'horToolShowFolder',
          title: 'FOLDER_LABEL',
          showIcon: true,
          text: ""
        },
        {
          id: 'article',
          className: 'horToolShowArticle',
          title: 'ARTICLES_LABEL',
          showIcon: true,
          text: ""
        },
        {
          id: 'getAllChildren',
          className: 'horToolGetAllChildren',
          title: 'SHOW_ALL',
          showIcon: true,
          text: ""
        }
      ]
    },
    {
      id: "group3",
      items: aZoomItems
    },
    {
      id: "group4",
      items: aSignalFilter
    }
  ],

  ArticleChildrenGrid: [
    {
      id: "group2",
      items: aTileGridListViewItem
    },
    {
      id: "group1",
      items: [
        {
          id: 'save_entity',
          className: 'horToolSave',
          title: 'SAVE'
        },
        {
          id: 'folder',
          className: 'horToolShowFolder',
          title: 'FOLDER_LABEL'
        },
        {
          id: 'article',
          className: 'horToolShowArticle',
          title: 'ARTICLES_LABEL'
        }
      ]
    }
  ],

  ArticleTile: [
    {
      id: "group1",
      items: [
        {
          id: 'all',
          className: 'horToolShowAll',
          title: 'ALL',
          showIcon: false,
          text: 'ALL'
        },
        {
          id: 'folder',
          className: 'horToolShowFolder',
          title: 'FOLDER_LABEL',
          showIcon: true,
          text: ""
        },
        {
          id: 'article',
          className: 'horToolShowArticle',
          title: 'ARTICLES_LABEL',
          showIcon: true,
          text: ""
        },
        {
          id: 'getAllChildren',
          className: 'horToolGetAllChildren',
          title: 'SHOW_ALL',
          showIcon: true,
          text: ""
        }
      ]
    },
    {
      id: "group3",
      items: aZoomItems
    },
    {
      id: "group4",
      items: aSignalFilter
    }
  ],

  SetRoot: [
    {
      id: "group1",
      items: [
        {
          id: 'all',
          className: 'horToolShowAll',
          title: 'ALL',
          showIcon: false,
          text: 'ALL'
        },
        {
          id: 'folder',
          className: 'horToolShowFolder',
          title: 'FOLDER_LABEL',
          showIcon: true,
          text: ""
        },
        {
          id: 'article',
          className: 'horToolShowArticle',
          title: 'ARTICLES_LABEL',
          showIcon: true,
          text: ""
        },
        {
          id: 'getAllChildren',
          className: 'horToolGetAllChildren',
          title: 'SHOW_ALL',
          showIcon: true,
          text: ""
        }
      ]
    },
    {
      id: "group3",
      items: aZoomItems
    }/*,
    {
      id: "group4",
      items: aSignalFilter
    }*/
  ],

  SetMatchNMerge: [
    {
      id: "group1",
      items: [
        {
          id: 'matchMergeSummary',
          className: 'horToolMatchMergeSummary',
          title: 'SUMMARY_VIEW',
          showIcon: true,
          text: ''
        },
        {
          id: 'matchMerge',
          className: 'horToolMatchNMerge',
          title: 'MATCH_N_MERGE_VIEW',
          showIcon: true,
          text: ''
        },
        {
          id: 'comparison',
          className: 'horToolComparison',
          title: 'COMPARISON_VIEW',
          showIcon: true,
          text: ''
        }
      ]
    }
  ],

  VersionMatchNMerge: [
    {
      id: "group1",
      items: [
        {
          id: 'matchMergeSummary',
          className: 'horToolMatchMergeSummary',
          title: 'SUMMARY_VIEW',
          showIcon: true,
          text: ''
        },
        {
          id: 'matchMerge',
          className: 'horToolMatchNMerge',
          title: 'MATCH_N_MERGE_VIEW',
          showIcon: true,
          text: ''
        },
        {
          id: 'comparison',
          className: 'horToolComparison',
          title: 'COMPARISON_VIEW',
          showIcon: true,
          text: ''
        }
      ]
    }
  ],

  SetChildren: [
    {
      id: "group2",
      items: aTileGridListViewItem
    },
    {
      id: "group1",
      items: [
        {
          id: 'select_all_sets',
          className: 'horToolSelectAll',
          title: 'SELECT_ALL',
          showIcon: true,
          text: ""
        },
        {
          id: 'delete_entity',
          className: 'horToolDelete',
          title: 'DELETE',
          showIcon: true,
          text: ""
        },
        {
          id: 'all',
          className: 'horToolShowAll',
          title: 'ALL',
          showIcon: false,
          text: 'ALL'
        },
        {
          id: 'folder',
          className: 'horToolShowFolder',
          title: 'FOLDER_LABEL',
          showIcon: true,
          text: ""
        },
        {
          id: 'article',
          className: 'horToolShowArticle',
          title: 'ARTICLES_LABEL',
          showIcon: true,
          text: ""
        },
        {
          id: 'getAllChildren',
          className: 'horToolGetAllChildren',
          title: 'SHOW_ALL',
          showIcon: true,
          text: ""
        }
      ]
    },
    {
      id: "group3",
      items: aZoomItems
    }
  ],

  SetChildrenGrid: [
    {
      id: "group2",
      items: aTileGridListViewItem
    },
    {
      id: "group1",
      items: [
        {
          id: 'save_entity',
          className: 'horToolSave',
          title: 'SAVE'
        },
        {
          id: 'folder',
          className: 'horToolShowFolder',
          title: 'FOLDER_LABEL'
        },
        {
          id: 'article',
          className: 'horToolShowArticle',
          title: 'ARTICLES_LABEL'
        }
      ]
    }
  ],

  CollectionRoot: [
    {
      id: "group1",
      items: [
        {
          id: 'getAllChildren',
          className: 'horToolGetAllChildren',
          title: 'SHOW_ALL',
          showIcon: true,
          text: ""
        }
      ]
    },
    {
      id: "group3",
      items: aZoomItems
    }
  ],

  CollectionChildren: [
    {
      id: "group2",
      items: aTileGridListViewItem
    },
    {
      id: "group1",
      items: [
        {
          id: 'select_all_collections',
          className: 'horToolSelectAll',
          title: 'SELECT_ALL',
          showIcon: true,
          text: ""
        },
        {
          id: 'delete_entity',
          className: 'horToolDelete',
          title: 'DELETE',
          showIcon: true,
          text: ""
        },
        {
          id: 'getAllChildren',
          className: 'horToolGetAllChildren',
          title: 'SHOW_ALL',
          showIcon: true,
          text: ""
        }
      ]
    },
    {
      id: "group3",
      items: aZoomItems
    }
  ],

  CollectionLinkedElements: [
    {
      id: "group1",
      items: [
        {
          id: 'delete_entity',
          className: 'horToolDelete',
          title: 'DELETE',
          showIcon: true,
          text: ""
        }]
    },
    {
      id: "group2",
      items: aZoomItems
    }
  ],

  CollectionChildrenGrid: [
    {
      id: "group2",
      items: aTileGridListViewItem
    },
    {
      id: "group1",
      items: [
        {
          id: 'save_entity',
          className: 'horToolSave',
          title: 'SAVE'
        }
      ]
    }
  ],

  RelationshipToolbar: [
    {
      id: "group3",
      items: aTileGridListViewItem
    },
    {
      id: "group2",
      items: [
        {
          id: 'select_all_content',
          className: 'horToolSelectAll',
          title: 'SELECT_ALL',
          showIcon: true,
          text: ""
        },
        {
          id: 'delete_entity',
          className: 'horToolDelete',
          title: 'DELETE',
          showIcon: true,
          text: ""
        }]
    },
    {
      id: "group1",
      items: aZoomItems
    }
  ]
};

