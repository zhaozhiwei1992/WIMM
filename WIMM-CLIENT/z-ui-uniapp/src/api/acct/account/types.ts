export type AccountVO = {
  id: number
  createdBy: string
  createdDate: Date
  creditAccount: string
  creditAccountName: string
  debitAccount: string
  debitAccountName: string
  amt: number
  remark: string
  type: number
}

export type VoucherDetailVO = {
  id: number // 凭证分录唯一标识，注意这里使用了 number 类型，因为 ID 通常是数字类型
  createdBy: string
  createdDate: Date
  voucherNo: string // 核算凭证号
  acctClsCode: string // 预算指标核算科目代码
  acctClsName: string // 预算指标核算科目名称
  amt: number // 金额，这里使用了 number 类型来表示金额
  drCr: number // 借贷方向，1:借, -1:贷
  remark?: string // 备注，使用 ? 表示该属性是可选的
}

// AI 记账解析结果(后端 /acct/account/ai-parse 返回)
export type AiParseResultVO = {
  type: string // income=收入, expense=支出, transfer=转账
  bizAccountCode: string // 业务科目代码(支出/收入的分类; 转账=源账户)
  bizAccountName: string // 业务科目名称
  payAccountCode: string // 收付款账户代码(转账=目标账户)
  payAccountName: string // 收付款账户名称
  amt: number // 金额
  remark?: string // 备注
  fallback: boolean // 是否启用兜底(某项解析不出走了默认)
  debitAccount: string // 借方科目代码(确认后直接用于记账)
  creditAccount: string // 贷方科目代码
}
