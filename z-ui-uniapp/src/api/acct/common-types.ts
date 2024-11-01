export type ComponentOptions = {
  label?: string
  value?: String
  id?: String
  disabled?: boolean
  key?: string | number
  children?: ComponentOptions[]
  options?: ComponentOptions[]
} & Recordable