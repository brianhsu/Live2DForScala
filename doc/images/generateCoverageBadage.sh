#!/bin/bash

total=$1

echo $total

if (( $(echo "$total <= 50" | bc -l) )) ; then
  COLOR=red
elif (( $(echo "$total > 80" | bc -l) )); then
  COLOR=green
else
  COLOR=orange
fi

curl "https://img.shields.io/badge/coavrege-$total%25-$COLOR?style=for-the-badge&logo=scala" > doc/images/coverage.svg

