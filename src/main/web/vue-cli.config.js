/*module.exports = {
    proxy: {
        '/api': 'http://localhost/api',
        '/media': 'http://localhost/media'
    }
};*/
module.exports = {
    proxy: {
        /*'/api/ecd': 'http://localhost:8084',
        '/media/ecd': 'http://localhost:8084'*/
        '/': 'http://localhost:8084'
    }
};
