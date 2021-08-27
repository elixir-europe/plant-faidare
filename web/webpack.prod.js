const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');
const MiniCssExtractPlugin = require("mini-css-extract-plugin");

const mergedConfig = merge(common, {
    mode: 'production',
    output: {
        filename: '[name].[contenthash].js'
    }
});

mergedConfig.plugins = mergedConfig.plugins.map(plugin => {
    if (plugin instanceof MiniCssExtractPlugin) {
        return new MiniCssExtractPlugin({
            filename: '[name].[contenthash].css'
        });
    } else {
        return plugin
    }
});

module.exports = mergedConfig;

