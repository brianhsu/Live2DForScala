#!/bin/bash

set -ex
version=`git tag | tail -n1`

rm -rvf openSeeFace.tar.gz openSeeFace Live2DForScala-Swing/ Live2DForScala-SWT-Linux/ Live2DForScala-SWT-Win/

curl --output openSeeFace.tar.gz https://brianhsu.moe/openSeeFace.tar.gz
tar -xzf openSeeFace.tar.gz

mkdir Live2DForScala-Swing
cp -rf openSeeFace Live2DForScala-Swing/
cp -v modules/examples/swing/target/scala-2.13/Live2DForScala-Swing-*.jar Live2DForScala-Swing/Live2DForScala-Swing-${version}.jar

mkdir Live2DForScala-SWT-Linux
cp -rf openSeeFace Live2DForScala-SWT-Linux/
cp -v modules/examples/swt-linux-bundle/target/scala-2.13/Live2DForScala-SWT-Linux-*.jar Live2DForScala-SWT-Linux/Live2DForScala-SWT-Linux-${version}.jar

mkdir Live2DForScala-SWT-Windows
cp -rf openSeeFace Live2DForScala-SWT-Windows
cp -v modules/examples/swt-windows-bundle/target/scala-2.13/Live2DForScala-SWT-Windows-*.jar Live2DForScala-SWT-Windows/Live2DForScala-SWT-Windows-${version}.jar

zip -rq Live2DForScala-Swing-$version.zip Live2DForScala-Swing/
zip -rq Live2DForScala-SWT-Windows-$version.zip Live2DForScala-SWT-Windows
tar -czf Live2DForScala-SWT-Linux-$version.tar.gz Live2DForScala-SWT-Linux/

rm -rf openSeeFace.tar.gz openSeeFace Live2DForScala-Swing/ Live2DForScala-SWT-Linux/ Live2DForScala-SWT-Win/
pwd
ls -l

