import '@umijs/max';
import React, {useEffect, useRef} from 'react';
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
  values: API.InterfaceInfo;
};
const CreatMode: React.FC<Props> = (props) => {
  const {columns, visible, onSubmit, onCancel, values} = props;
  const ref = useRef<any>();
  useEffect(() => {
    if (ref) {
      ref.current?.setFieldsValue(values);
    }
  }, [values])

  return <Modal visible={visible}
                onCancel={() => {
                  onCancel?.()
                }}
                footer={null}
  >
    <ProTable type={"form"}
              columns={columns}
              onSubmit={async (value) => {
                onSubmit?.(value)
              }}
              formRef={ref}
    />
  </Modal>
};
export default CreatMode;
