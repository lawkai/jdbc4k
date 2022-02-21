# Changelog

# [1.0.0-beta.4](https://github.com/lawkai/jdbc4k/compare/v1.0.0-beta.3...v1.0.0-beta.4) (2022-02-21)


### Bug Fixes

* signing artifact with proper env. variables ([992ff9d](https://github.com/lawkai/jdbc4k/commit/992ff9dcfe7d5952be43835890b14ff7799af070))

# [1.0.0-beta.3](https://github.com/lawkai/jdbc4k/compare/v1.0.0-beta.2...v1.0.0-beta.3) (2022-02-21)


### Bug Fixes

* signing artifact ([0e69336](https://github.com/lawkai/jdbc4k/commit/0e693361bcd5955ccbb0900db303646717c84e89))

# [1.0.0-beta.2](https://github.com/lawkai/jdbc4k/compare/v1.0.0-beta.1...v1.0.0-beta.2) (2022-02-21)


### Bug Fixes

* renamed package to io.github.lawkai instead of com.github.lawkai ([8a4199a](https://github.com/lawkai/jdbc4k/commit/8a4199a6622065397c20fc3d1a3bac366e29bb6c))

# 1.0.0-beta.1 (2022-02-21)


### Features

* added DataSource4k with unit tests to demonstrate how to use the library. ([b0831ad](https://github.com/lawkai/jdbc4k/commit/b0831ada516a365d4a5087fda26a6939edf315cb))
* added some useful PreparedStatement extension functions to complement JDBC ([d8e9bd2](https://github.com/lawkai/jdbc4k/commit/d8e9bd2982fd143a16b9bdbb49780a493a5be06d))
* updated query function to pass in a PreparedStatement instead of a Connection ([4e4604c](https://github.com/lawkai/jdbc4k/commit/4e4604c8d0c0381c59c62d095776a5b531c9ccfd))
* updated transaction function to remove Connection as a given fn parameter ([b785ce0](https://github.com/lawkai/jdbc4k/commit/b785ce05e3ab6f66da515a87c6abc13d842a041f))