const PROXY_CONFIG = [
    {
        context: [
            "/faidare-dev/brapi",
            "/faidare-dev/faidare",
        ],
        target: "http://localhost:8380",
        secure: false
    }
];

module.exports = PROXY_CONFIG;
