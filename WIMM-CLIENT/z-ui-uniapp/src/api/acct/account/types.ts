export type AccountVO = {
  id: number
  createdBy: string
  createdDate: Date
  creditAccount: string
  creditAccountName: string
  debitAccount: string
  debitAccountName: string
  amt: number
  remkark: string
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
