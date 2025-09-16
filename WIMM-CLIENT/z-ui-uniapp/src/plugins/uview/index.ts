import type { App } from 'vue'

import uviewPlus, { setConfig } from 'uview-plus'

export const setupUView = (app: App<Element>) => {
  app.use(uviewPlus)
}
