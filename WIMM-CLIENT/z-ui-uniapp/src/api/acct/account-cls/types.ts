export type AccountClsVO = {
  id: number
  createdBy: string
  createdDate: Date
  setYear: string // 核算年度
  code: string // 预算指标核算科目代码
  name: string // 预算指标核算科目名称
  balanceDir: number // 余额方向
  isEnabled: number // 是否启用
  remark?: string // 备注
  isStandard: number // 是否标准
  parentId?: number // 上级科目唯一标识
  levelNo: number // 级次
  isLeaf: number // 是否末级
  isLastInst: number // 是否终审
  icon?: string // 图标
  children: AccountClsVO[]
}
