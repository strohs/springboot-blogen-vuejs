module.exports = {
  devServer: {
    port: 8085,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        ws: true, // proxy websockets?
        changeOrigin: true
      }
    }
  }
}
