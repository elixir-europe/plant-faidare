const PROXY_CONFIG = [
    {
        context: [
            "/brapi",
            "/gnpis",
        ],
        target: "https://urgi.versailles.inra.fr/beta/gnpis-core-srv",
        secure: false
    }
];

module.exports = PROXY_CONFIG;
