const PROXY_CONFIG = [
    {
        context: [
            "/gpds-dev/brapi",
            "/gpds-dev/gnpis",
        ],
        target: "http://localhost:8380",
        secure: false
    }
];

module.exports = PROXY_CONFIG;
