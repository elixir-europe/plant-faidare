const PROXY_CONFIG = [
    {
        context: [
            "/faidare-dev/brapi",
            "/faidare-dev/gnpis",
        ],
        target: "http://localhost:8380",
        secure: false
    }
];

module.exports = PROXY_CONFIG;
