const PROXY_CONFIG = [
    {
        context: [
            "/gnpis-core/brapi",
            "/gnpis-core/gnpis",
        ],
        target: "http://localhost:8080",
        secure: false
    }
];

module.exports = PROXY_CONFIG;
