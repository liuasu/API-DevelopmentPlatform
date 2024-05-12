import '@umijs/max';
import React from 'react';
import {type ProColumns, ProTable} from "@ant-design/pro-components";
import {Modal} from "antd";

export type FormValueType = {
  target?: string;
  template?: string;
  type?: string;
  time?: string;
  frequency?: string;
} & Partial<API.RuleListItem>;
export type Props = {
  columns: ProColumns<API.RuleListItem>[];
  onCancel: (flag?: boolean, formVals?: FormValueType) => void;
  onSubmit: (values: API.InterfaceInfo) => Promise<void>;
  visible: boolean;
};
const CreatMode: React.FC<Props> = (props) => {
  const {columns, visible} = props;
  return <Modal visible={visible}>
    <ProTable type={"form"} columns={columns}/>
  </Modal>
};
export default CreatMode;
