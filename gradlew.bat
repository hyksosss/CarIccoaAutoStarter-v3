@echo off
REM Bootstrap helper for source archives where the binary gradle-wrapper.jar is not included.
REM Install Gradle 8.2.1 and make it available on PATH.
gradle %*
