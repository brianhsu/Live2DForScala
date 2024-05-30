#!/bin/bash

version=`git tag | tail -n1`

curl "https://img.shields.io/badge/Maven-$version-blue?style=for-the-badge&logo=maven" > doc/images/version.svg

