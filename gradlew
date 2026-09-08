#!/bin/sh
# Bootstrap helper for source archives where the binary gradle-wrapper.jar is not included.
# Install Gradle 8.2.1 (or let GitHub Actions provide it), then run this script.
exec gradle "$@"
