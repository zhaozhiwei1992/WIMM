module.exports = {
  '*.{js,jsx,ts,tsx,vue}': ['eslint --fix', 'prettier --write'],
  '*.{css,less,postcss,scss,vue}': ['stylelint --fix'],
  '*.{json,md,html}': ['prettier --write']
}
