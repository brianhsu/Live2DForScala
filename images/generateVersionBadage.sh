#!/bin/bash
version=`git tag | tail -n1`
curl "https://img.shields.io/badge/maven-$version-blue?style=for-the-badge" > version.svg

