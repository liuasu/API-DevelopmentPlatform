import React, {useEffect, useRef, useState} from 'react';
import {Button, Card, Col, Divider, Drawer, Form, Input, message, Row} from "antd";
import {interfaceinfoInvokeUsingPost} from "@/services/api-backend/interfaceinfoController";
import {FormValueType} from "@/pages/Admin/Interface_info/components/UpdateMode";


interface DescriptionItemProps {
  title: string;
  content: React.ReactNode;
}

const DescriptionItem = ({title, content}: DescriptionItemProps) => (
  <div className="site-description-item-profile-wrapper">
    <p className="site-description-item-profile-p-label">{title}: {content}</p>
  </div>
);
export type FormValueType = {
  target?: string;
  template?: string;
  type?: string;
  time?: string;
  frequency?: string;
} & Partial<API.RuleListItem>;

export type Props = {
  onClose: (flag?: boolean, formVals?: FormValueType) => void;
  visible: boolean;
  data: API.InterfaceInfo;
};
const Index: React.FC<Props> = (props) => {
  const {visible, onClose, data} = props
  const ref = useRef<any>();
  const [invokeRes, setInvokeRes] = useState<any>()
  const [cardLoading, setCardLoading] = useState<boolean>()

  useEffect(() => {
    if (ref) {
      ref.current?.setFieldsValue(data);
    }
  }, [data])
  const onFinish = async (values: any) => {
    setCardLoading(true)
    try {
      const res = await interfaceinfoInvokeUsingPost({
        id: data.id,
        ...values
      })
      setInvokeRes(res.data)
      setTimeout(() => {
        setCardLoading(false)
        message.success('请求成功');
      }, 2000);
    } catch (error: any) {
      message.error('请求失败，' + error.message);
      return false;
    }
  };

  return (
    <Drawer placement="right" closable={false} visible={visible} width={500}
            onClose={() => {
              onClose?.()
            }}
    >
      <Card>
        <p className="site-description-item-profile-p" style={{marginBottom: 24}}>
          接口名称： {data.name}
        </p>
        <Row>
          <Col span={12}>
            <DescriptionItem title="接口状态" content={data.status == 0 ? '启用' : '关闭'}/>
          </Col>
          <Col span={12}>
            <DescriptionItem title="请求类型" content={data.method}/>
          </Col>
        </Row>
        <Row>
          <Col span={12}>
            <DescriptionItem title="请求头" content={data.requestHeader}/>
          </Col>
          <Col span={12}>
            <DescriptionItem title="响应头" content={data.responseHeader}/>
          </Col>
        </Row>
        <Row>
          <Col span={15}>
            <DescriptionItem title="接口地址" content={data.url}/>
          </Col>
          <Col span={15}>
            <DescriptionItem title="请求参数" content={data.requestParams}/>
          </Col>
        </Row>
        <Row>
          <Col span={15}>
            <DescriptionItem title="接口描述" content={data.description}/>
          </Col>
          <Col span={20}>
            <DescriptionItem title="创建时间" content={data.createTime}/>
          </Col>
        </Row>
      </Card>
      <Divider/>
      <Card title="在线调试">
        <Form
          layout="vertical"
          onFinish={onFinish}
        >
          <Form.Item label="请求参数" name="requestParams">
            <Input.TextArea/>
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit">
              调 试
            </Button>
          </Form.Item>
        </Form>
      </Card>
      <Card title="返回结果" loading={cardLoading}>
        {invokeRes}
      </Card>
      {/*<p className="site-description-item-profile-p">Company</p>
      <Row>
        <Col span={15}>
          <DescriptionItem title="Position" content="Programmer"/>
        </Col>
        <Col span={15}>
          <DescriptionItem title="Responsibilities" content="Coding"/>
        </Col>
      </Row>
      <Row>
        <Col span={15}>
          <DescriptionItem title="Department" content="XTech"/>
        </Col>
        <Col span={15}>
          <DescriptionItem title="Supervisor" content={<a>Lin</a>}/>
        </Col>
      </Row>
      <Row>
        <Col span={24}>
          <DescriptionItem
            title="Skills"
            content="C / C + +, data structures, software engineering, operating systems, computer networks, databases, compiler theory, computer architecture, Microcomputer Principle and Interface Technology, Computer English, Java, ASP, etc."
          />
        </Col>
      </Row>
      <Divider/>
      <p className="site-description-item-profile-p">Contacts</p>
      <Row>
        <Col span={15}>
          <DescriptionItem title="Email" content="AntDesign@example.com"/>
        </Col>
        <Col span={15}>
          <DescriptionItem title="Phone Number" content="+86 181 0000 0000"/>
        </Col>
      </Row>
      <Row>
        <Col span={24}>
          <DescriptionItem
            title="Github"
            content={
              <a href="http://github.com/ant-design/ant-design/">
                github.com/ant-design/ant-design/
              </a>
            }
          />
        </Col>
      </Row>*/}
    </Drawer>
  );
};

export default Index;
