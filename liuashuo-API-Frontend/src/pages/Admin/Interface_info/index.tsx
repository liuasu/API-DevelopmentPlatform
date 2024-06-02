import {PlusOutlined} from '@ant-design/icons';
import type {ActionType, ProColumns, ProDescriptionsItemProps} from '@ant-design/pro-components';
import {FooterToolbar, PageContainer, ProDescriptions, ProTable,} from '@ant-design/pro-components';
import '@umijs/max';
import {Button, Drawer, message} from 'antd';
import React, {useRef, useState} from 'react';
import {
  addInterfaceinfoUsingPost,
  deleteInterfaceinfoUsingPost,
  listInterfaceinfoByPageUsingGet,
  offlineInterfaceinfoUsingPost,
  publishInterfaceinfoUsingPost,
  updateInterfaceinfoUsingPost
} from "@/services/api-backend/interfaceinfoController";
import type {SortOrder} from "antd/lib/table/interface";
import CreatMode from "@/pages/Admin/Interface_info/components/CreatMode";
import UpdateMode from "@/pages/Admin/Interface_info/components/UpdateMode";

const TableList: React.FC = () => {
  /**
   * @en-US Pop-up window of new window
   * @zh-CN 新建窗口的弹窗
   *  */
  const [createModalOpen, handleModalOpen] = useState<boolean>(false);
  /**
   * @en-US The pop-up window of the distribution update window
   * @zh-CN 分布更新窗口的弹窗
   * */
  const [updateModalOpen, handleUpdateModalOpen] = useState<boolean>(false);
  const [showDetail, setShowDetail] = useState<boolean>(false);
  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<API.InterfaceInfo>();
  const [selectedRowsState, setSelectedRows] = useState<API.InterfaceInfo[]>([]);

  /**
   * @en-US Add node
   * @zh-CN 添加节点
   * @param fields
   */
  const handleAdd = async (fields: API.InterfaceInfo) => {
    const hide = message.loading('正在添加');
    try {
      await addInterfaceinfoUsingPost({
        ...fields,
      });
      hide();
      message.success('接口添加成功');
      // 关闭模态框
      handleModalOpen(false);
      return true;
    } catch (error: any) {
      hide();
      message.error('添加失败，' + error.message);
      return false;
    }
  };

  /**
   * @en-US Update node
   * @zh-CN 更新节点
   *
   * @param fields
   */
  const handleUpdate = async (fields: API.InterfaceInfo) => {
    const hide = message.loading('编辑');
    if (!currentRow) {
      return;
    }
    try {
      await updateInterfaceinfoUsingPost({
        id: currentRow?.id,
        ...fields
      });
      hide();
      message.success('编辑成功');
      return true;
    } catch (error: any) {
      hide();
      message.error('编辑失败，' + error.message);
      return false;
    }
  };

  /**
   *  Delete node
   * @zh-CN 删除节点
   *
   * @param fields
   */
  const handleRemove = async (fields: API.InterfaceInfo) => {
    const hide = message.loading('正在删除');
    if (!fields) return true;
    try {
      await deleteInterfaceinfoUsingPost({
        id: fields.id
      });
      hide();
      message.success('操作成功');
      actionRef.current?.reload();
      return true;
    } catch (error: any) {
      hide();
      message.error('操作失败，' + error.message);
      return false;
    }
  };

  /**
   *
   * @zh-CN 下线
   *
   * @param fields
   */
  const handleOffline = async (fields: API.IdRequest) => {
    const hide = message.loading('操作中');
    if (!fields) return true;
    try {
      await offlineInterfaceinfoUsingPost({
        id: fields.id
      });
      hide();
      message.success('操作成功');
      actionRef.current?.reload();
      return true;
    } catch (error: any) {
      hide();
      message.error('操作失败，' + error.message);
      return false;
    }
  };

  /**
   * @zh-CN 发布
   *
   * @param fields
   */
  const handlePublish = async (fields: API.IdRequest) => {
    const hide = message.loading('发布中');
    if (!fields) return true;
    try {
      await publishInterfaceinfoUsingPost({
        id: fields.id
      });
      hide();
      message.success('操作成功');
      actionRef.current?.reload();
      return true;
    } catch (error: any) {
      hide();
      message.error('操作失败，' + error.message);
      return false;
    }
  };

  /**
   * @en-US International configuration
   * @zh-CN 国际化配置
   * */

  const columns: ProColumns<API.RuleListItem>[] = [
    {
      title: 'id',
      dataIndex: 'id',
      valueType: 'text',
      // hideInTable: true,
      hideInForm: true,
    },
    {
      title: '请求名称',
      dataIndex: 'name',
      valueType: 'text',
      formItemProps: {
        rules: [
          {required: true}
        ]
      }
    },
    {
      title: '请求类型',
      dataIndex: 'method',
      valueType: 'text',
      formItemProps: {
        rules: [
          {required: true}
        ]
      }
    },
    {
      title: '接口地址',
      dataIndex: 'url',
      valueType: 'textarea',
      formItemProps: {
        rules: [
          {required: true}
        ]
      }
    },
    {
      title: '请求参数',
      dataIndex: 'requestParams',
      valueType: 'jsonCode',
      formItemProps: {
        rules: [
          {required: true}
        ]
      }
    },
    {
      title: '请求头',
      dataIndex: 'requestHeader',
      valueType: 'jsonCode',
      formItemProps: {
        rules: [
          {required: true}
        ]
      }
    },
    {
      title: '响应头',
      dataIndex: 'responseHeader',
      valueType: 'jsonCode',
      formItemProps: {
        rules: [
          {required: true}
        ]
      }
    },
    {
      title: '描述',
      dataIndex: 'description',
      valueType: 'textarea',
    },
    // {
    //   title: '服务调用次数',
    //   dataIndex: 'callNo',
    //   sorter: true,
    //   hideInForm: true,
    //   renderText: (val: string) => `${val}${'万'}`,
    // },
    {
      title: '状态',
      dataIndex: 'status',
      hideInForm: true,
      valueEnum: {
        0: {
          text: '关闭',
          status: 'Default',
        },
        1: {
          text: '启用',
          status: 'Processing',
        },
      },
    },
    {
      title: '创建时间',
      sorter: true,
      dataIndex: 'createTime',
      valueType: 'dateTime',
      hideInForm: true,
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => [
        <Button
          type="text"
          key="config"
          onClick={() => {
            handleUpdateModalOpen(true);
            setCurrentRow(record);
          }}
        >
          修改
        </Button>,
        // eslint-disable-next-line eqeqeq
        record.status == 0 ? <Button
          type="text"
          key="config"
          onClick={() => {
            handlePublish(record)
          }}
        >
          发布
        </Button> : null,
        record.status == 1 ? <Button
          type="text"
          danger
          key="config"
          onClick={() => {
            handleOffline(record)
          }}
        >
          下线
        </Button> : null,
        <Button
          type="text"
          danger
          key="config"
          onClick={() => {
            handleRemove(record)
          }}
        >
          删除
        </Button>,
      ],
    },
  ];
  return (
    <PageContainer>
      <ProTable<API.RuleListItem, API.PageParams>
        headerTitle={'查询表格'}
        actionRef={actionRef}
        rowKey="key"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <Button
            type="primary"
            key="primary"
            onClick={() => {
              handleModalOpen(true);
            }}
          >
            <PlusOutlined/> 新建
          </Button>,
        ]}
        request={async (params, sort: Record<string, SortOrder>, filter: Record<string, (string | number)[] | null>) => {
          const res = await listInterfaceinfoByPageUsingGet({
            ...params
          });
          if (res?.data) {
            return {
              data: res?.data.records || [],
              success: true,
              total: res?.data.total
            }
          } else {
            return {
              data: [],
              success: false,
              total: 0
            }
          }
        }}
        columns={columns}
        rowSelection={{
          onChange: (_, selectedRows) => {
            setSelectedRows(selectedRows);
          },
        }}
      />
      {selectedRowsState?.length > 0 && (
        <FooterToolbar
          extra={
            <div>
              已选择{' '}
              <a
                style={{
                  fontWeight: 600,
                }}
              >
                {selectedRowsState.length}
              </a>{' '}
              项 &nbsp;&nbsp;
              <span>
                服务调用次数总计 {selectedRowsState.reduce((pre, item) => pre + item.callNo!, 0)} 万
              </span>
            </div>
          }
        >
          <Button
            onClick={async () => {
              await handleRemove(selectedRowsState);
              setSelectedRows([]);
              actionRef.current?.reloadAndRest?.();
            }}
          >
            批量删除
          </Button>
          <Button type="primary">批量审批</Button>
        </FooterToolbar>
      )
      }
      <UpdateMode
        onSubmit={async (value) => {
          const success = await handleUpdate(value);
          if (success) {
            handleUpdateModalOpen(false);
            setCurrentRow(undefined);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
        onCancel={() => {
          handleUpdateModalOpen(false);
          if (!showDetail) {
            setCurrentRow(undefined);
          }
        }}
        visible={updateModalOpen}
        values={currentRow || {}}
        columns={columns}
      />

      <Drawer
        width={600}
        open={showDetail}
        onClose={() => {
          setCurrentRow(undefined);
          setShowDetail(false);
        }}
        closable={false}
      >
        {currentRow?.name && (
          <ProDescriptions<API.RuleListItem>
            column={2}
            title={currentRow?.name}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.name,
            }}
            columns={columns as ProDescriptionsItemProps<API.RuleListItem>[]}
          />
        )}
      </Drawer>
      <CreatMode columns={columns}
                 onCancel={() => {
                   handleModalOpen(false)
                 }}
                 onSubmit={(value) => {
                   handleAdd(value)
                 }}
                 visible={createModalOpen}
      />
    </PageContainer>
  )
    ;
};
export default TableList;
