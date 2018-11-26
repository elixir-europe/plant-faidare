// Protractor configuration file, see link for more information
// https://github.com/angular/protractor/blob/master/lib/config.ts

const { SpecReporter } = require('jasmine-spec-reporter');

exports.config = {
    allScriptsTimeout: 11000,
    specs: [
        './src/**/*.e2e-spec.ts'
    ],
    directConnect: true,
    baseUrl: 'http://localhost:4200/',
    framework: 'jasmine',
    multiCapabilities: [
        {
            browserName: 'chrome',
            chromeOptions: {
                args: ["--headless", "--disable-gpu"]
            }
        },
        {
            browserName: 'firefox',
            'moz:firefoxOptions': {
                args: ["--headless"]
            }
        }
    ],
    jasmineNodeOpts: {
        showColors: true,
        defaultTimeoutInterval: 30000,
        print: function () {
        }
    },
    onPrepare() {
        require('ts-node').register({
            project: require('path').join(__dirname, './tsconfig.e2e.json')
        });
        jasmine.getEnv().addReporter(
            new SpecReporter({
                spec: { displayStacktrace: true }
            })
        );
    }
};
