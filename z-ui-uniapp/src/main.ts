import { createSSRApp } from "vue";
import App from "./App.vue";
import { setupUView } from "./plugins/uview"
export function createApp() {
  const app = createSSRApp(App);
  setupUView(app)
  return {
    app,
  };
}
