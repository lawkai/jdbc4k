# Changelog

# [1.3.0](https://github.com/lawkai/jdbc4k/compare/v1.2.1...v1.3.0) (2022-03-11)


### Features

* added support for named parameter sql. ([2740e73](https://github.com/lawkai/jdbc4k/commit/2740e73af0eb0c2dcfad8262cafbdd30c05feb52))

## [1.2.1](https://github.com/lawkai/jdbc4k/compare/v1.2.0...v1.2.1) (2022-03-05)


### Bug Fixes

* PreparedStatement4k#executeQuery should return a ResultSet4k instead of ResultSet ([7aa5cf5](https://github.com/lawkai/jdbc4k/commit/7aa5cf5ae67a4cea824c79731eae351162a81714))

# [1.2.0](https://github.com/lawkai/jdbc4k/compare/v1.1.0...v1.2.0) (2022-03-05)


### Features

* removed PreparedStatement and ResultSet extension functions, created wrapper class instead. ([53f3dc7](https://github.com/lawkai/jdbc4k/commit/53f3dc72d0ad4da604696c7579a1e0adcf57356b))

# [1.1.0](https://github.com/lawkai/jdbc4k/compare/v1.0.0...v1.1.0) (2022-03-05)


### Features

* transaction lambda can be run using the same CoroutineScope. ([2fb2eff](https://github.com/lawkai/jdbc4k/commit/2fb2eff276545446ca7b02262c7e3dd02a29e023))

# 1.0.0 (2022-02-21)


### Bug Fixes

* adding scm section in maven pom ([a26739f](https://github.com/lawkai/jdbc4k/commit/a26739f98f6f613855d63b71cc9d5114e227bfdc))
* make sure it if build and publish on the tag ([cfd24cb](https://github.com/lawkai/jdbc4k/commit/cfd24cb8ce081d551f1338d173789b35c06c94f2))
* publish credentials ([9b0736c](https://github.com/lawkai/jdbc4k/commit/9b0736c41ace2680bd8ac98b26857cdd4a81bd83))
* renamed package to io.github.lawkai instead of com.github.lawkai ([8a4199a](https://github.com/lawkai/jdbc4k/commit/8a4199a6622065397c20fc3d1a3bac366e29bb6c))
* signing artifact ([0e69336](https://github.com/lawkai/jdbc4k/commit/0e693361bcd5955ccbb0900db303646717c84e89))
* signing artifact with proper env. variables ([992ff9d](https://github.com/lawkai/jdbc4k/commit/992ff9dcfe7d5952be43835890b14ff7799af070))


### Features

* added DataSource4k with unit tests to demonstrate how to use the library. ([b0831ad](https://github.com/lawkai/jdbc4k/commit/b0831ada516a365d4a5087fda26a6939edf315cb))
* added some useful PreparedStatement extension functions to complement JDBC ([d8e9bd2](https://github.com/lawkai/jdbc4k/commit/d8e9bd2982fd143a16b9bdbb49780a493a5be06d))
* updated query function to pass in a PreparedStatement instead of a Connection ([4e4604c](https://github.com/lawkai/jdbc4k/commit/4e4604c8d0c0381c59c62d095776a5b531c9ccfd))
* updated transaction function to remove Connection as a given fn parameter ([b785ce0](https://github.com/lawkai/jdbc4k/commit/b785ce05e3ab6f66da515a87c6abc13d842a041f))

# [1.0.0-beta.7](https://github.com/lawkai/jdbc4k/compare/v1.0.0-beta.6...v1.0.0-beta.7) (2022-02-21)


### Bug Fixes

* adding scm section in maven pom ([a26739f](https://github.com/lawkai/jdbc4k/commit/a26739f98f6f613855d63b71cc9d5114e227bfdc))

# [1.0.0-beta.6](https://github.com/lawkai/jdbc4k/compare/v1.0.0-beta.5...v1.0.0-beta.6) (2022-02-21)


### Bug Fixes

* make sure it if build and publish on the tag ([cfd24cb](https://github.com/lawkai/jdbc4k/commit/cfd24cb8ce081d551f1338d173789b35c06c94f2))

# [1.0.0-beta.5](https://github.com/lawkai/jdbc4k/compare/v1.0.0-beta.4...v1.0.0-beta.5) (2022-02-21)


### Bug Fixes

* publish credentials ([9b0736c](https://github.com/lawkai/jdbc4k/commit/9b0736c41ace2680bd8ac98b26857cdd4a81bd83))

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
