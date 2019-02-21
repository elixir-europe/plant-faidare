const PROXY_CONFIG = [
    {
        context: [
            "/gnpis-dev/gpds/brapi",
            "/gnpis-dev/gpds/gnpis",
        ],
        target: "http://localhost:8380",
        secure: false
    }
];

module.exports = PROXY_CONFIG;
