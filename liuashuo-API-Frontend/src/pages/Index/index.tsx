import {PageContainer} from '@ant-design/pro-components';
import React, {useEffect, useState} from 'react';
import {List, message, Pagination} from "antd";
import {
  getInterfaceinfoByIdUsingGet,
  listInterfaceinfoByPageUsingGet
} from "@/services/api-backend/interfaceinfoController";
import InterfaceInfo from "@/pages/interfaceInfo";


const Index: React.FC = () => {
  const [DrawerModalOpen, handleDrawerModalOpen] = useState<boolean>(false);
  const [loading, setLoading] = useState(false);
  const [list, setList] = useState<API.InterfaceInfo[]>([]);
  const [total, setTotal] = useState<number>(0);
  const [interfaceInfo, setInterfaceInfo] = useState<API.InterfaceInfo>();
  const loadData = async (current = 1, pageSize = 10) => {
    setLoading(true)
    try {
      const res = await listInterfaceinfoByPageUsingGet({current, pageSize})
      setList(res?.data?.records ?? []);
      setTotal(res?.data?.total ?? 0);
    } catch (error: any) {
      message.error('加载失败，' + error.message);
    }
    setLoading(false)
  }
  useEffect(() => {
    loadData()
  }, []);
  const onShowSizeChange = (current, pageSize) => {
    loadData(current, pageSize)
  };
  return (
    <PageContainer title="六啊朔接口发放平台">
      <List
        className="demo-loadmore-list"
        loading={loading}
        itemLayout="horizontal"
        dataSource={list}
        renderItem={(item) => {
          return (
            <List.Item
              actions={[<a key={item.id}
                           onClick={() => {
                             getInterfaceinfoByIdUsingGet({id: Number(item.id)}).then((res) => {
                               setInterfaceInfo(res.data)
                               handleDrawerModalOpen(true)
                             })
                           }}
              >详情</a>,
                <a key="">more</a>]}
            >
              <List.Item.Meta
                title={<a onClick={() => {
                  handleDrawerModalOpen(true)
                  setInterfaceInfo(item)
                }}>{item.name}</a>}
                description={item.description}
              />
            </List.Item>
          )
        }
        }
      />
      <Pagination
        showTotal={(total) => {
          return '共' + total + '条';
        }}
        showSizeChanger
        onShowSizeChange={onShowSizeChange}
        defaultCurrent={1}
        total={total}
        onChange={(page, pageSize) => {
          loadData(page, pageSize)
        }}
      />
      <InterfaceInfo
        visible={DrawerModalOpen}
        data={interfaceInfo || {}}
        onClose={() => {
          handleDrawerModalOpen(false);
        }}

      />
    </PageContainer>
  );
};

export default Index;
