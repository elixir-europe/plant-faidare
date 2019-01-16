const PROXY_CONFIG = [
    {
        context: [
            "/brapi",
            "/gnpis",
        ],
        target: "http://localhost:8080/gnpis-core",
        secure: false
    }
];

module.exports = PROXY_CONFIG;
