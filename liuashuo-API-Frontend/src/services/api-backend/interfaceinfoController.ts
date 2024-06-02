// @ts-ignore
/* eslint-disable */
import {request} from '@umijs/max';

/** addInterfaceinfo POST /api/interfaceinfo/add */
export async function addInterfaceinfoUsingPost(
  body: API.InterfaceinfoAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponselong>('/api/interfaceinfo/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteInterfaceinfo POST /api/interfaceinfo/delete */
export async function deleteInterfaceinfoUsingPost(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseboolean>('/api/interfaceinfo/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getInterfaceinfoById GET /api/interfaceinfo/get */
export async function getInterfaceinfoByIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getInterfaceinfoByIdUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseInterfaceInfo>('/api/interfaceinfo/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** interfaceinfoInvoke POST /api/interfaceinfo/invoke */
export async function interfaceinfoInvokeUsingPost(
  body: API.InterfaceinfoInvokeRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseobject>('/api/interfaceinfo/invoke', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listInterfaceinfo GET /api/interfaceinfo/list */
export async function listInterfaceinfoUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listInterfaceinfoUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseListInterfaceInfo>('/api/interfaceinfo/list', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** listInterfaceinfoByPage GET /api/interfaceinfo/list/page */
export async function listInterfaceinfoByPageUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listInterfaceinfoByPageUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageInterfaceInfo>('/api/interfaceinfo/list/page', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** offlineInterfaceinfo POST /api/interfaceinfo/offline */
export async function offlineInterfaceinfoUsingPost(
  body: API.IdRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseboolean>('/api/interfaceinfo/offline', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** publishInterfaceinfo POST /api/interfaceinfo/publish */
export async function publishInterfaceinfoUsingPost(
  body: API.IdRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseboolean>('/api/interfaceinfo/publish', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updateInterfaceinfo POST /api/interfaceinfo/update */
export async function updateInterfaceinfoUsingPost(
  body: API.InterfaceinfoUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseboolean>('/api/interfaceinfo/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
