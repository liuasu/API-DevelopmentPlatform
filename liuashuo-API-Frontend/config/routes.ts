export default [
  {path: '/', name: '主页', icon: 'smile', component: './Index'},
  {path: '/interfaceInfo/:id', name: '详情', icon: 'smile', component: './interfaceInfo', hideInMenu: true},
  {
    path: '/user',
    layout: false,
    routes: [{name: '登录', path: '/user/login', component: './User/Login'}],
  },
  {
    path: '/admin',
    name: '管理页',
    icon: 'crown',
    access: 'canAdmin',
    routes: [
      {name: '接口管理', icon: 'table', path: '/admin/interface_info', component: './Admin/Interface_info'},
    ],
  },
  // {path: '/', redirect: '/welcome'},
  {path: '*', layout: false, component: './404'},
];
