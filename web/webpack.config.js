const path = require('path');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const CssMinimizerPlugin = require("css-minimizer-webpack-plugin");

module.exports = (env, argv) => ({
    context: path.resolve(__dirname, '.'),
    // inline source maps only in development mode
    devtool: argv.mode === 'production' ? undefined : 'inline-source-map',
    plugins: [
        // allows extracting the CSS into a CSS file instead of bundling it in a JS file
        new MiniCssExtractPlugin({
            filename: argv.mode === 'production' ? '[name].[contenthash].css' : '[name].css'
        }),
        // cleans the output directory before each build
        new CleanWebpackPlugin({
            // and the empty, useless style.js after each build
            protectWebpackAssets: false,
            cleanAfterEveryBuildPatterns: ['style*.js']
        })
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
        // the output is stored in build/dist/assets
        path: path.resolve(__dirname, 'build/dist/assets'),
        filename: argv.mode === 'production' ? '[name].[contenthash].js' : '[name].js'
    },
    optimization: {
        minimizer: [
            '...',
            new CssMinimizerPlugin()
        ]
    },
    performance: {
        maxAssetSize: 300000,
        maxEntrypointSize: 300000
    }
});
