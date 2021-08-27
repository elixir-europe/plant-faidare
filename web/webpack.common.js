const path = require('path');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const MiniCssExtractPlugin = require("mini-css-extract-plugin");

module.exports = {
    plugins: [
        // cleans the output directory before each build
        new CleanWebpackPlugin(),
        // allows extracting the CSS into a CSS file instead of bundling it in a JS file
        new MiniCssExtractPlugin()
    ],
    entry: {
        // a JS bundle is generated for the index.ts entry point. Since the application is really small
        // and does not have much JavaScript logic, a single bundle is sufficient
        script: './src/index.ts',
        // A CSS bundle is generated for the style.scss entry point.
        style: './src/style/style.scss'
    },
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                use: 'ts-loader',
                exclude: /node_modules/,
            },
            {
                // .scss files are loaded by the sass-loader, which transforms them into css loaded by the css-loader
                // which are then bundled into a css file by the MiniCssExtractPlugin loader
                test: /\.scss$/i,
                use: [
                    MiniCssExtractPlugin.loader,
                    'css-loader',
                    'sass-loader'
                ],
            },
        ],
    },
    resolve: {
        // our files are .ts files, but libraries are bundled in .js files
        extensions: ['.ts', '.js'],
    },
    output: {
        path: path.resolve(__dirname, 'build/dist/assets'),
        filename: '[name].js'
    }
};
