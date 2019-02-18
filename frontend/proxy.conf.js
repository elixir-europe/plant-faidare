const PROXY_CONFIG = [
    {
        context: [
            "/gnpis/gpds/brapi",
            "/gnpis/gpds/gnpis",
        ],
        target: "http://localhost:8380",
        secure: false
    }
];

module.exports = PROXY_CONFIG;
