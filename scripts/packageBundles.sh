#!/bin/bash

set -ex
version=`git tag | tail -n1`

rm -rvf openSeeFace.tar.gz openSeeFace Live2DForScala-Swing/ Live2DForScala-SWT-Linux/ Live2DForScala-SWT-Win/

curl --output openSeeFace.tar.gz https://brianhsu.moe/openSeeFace.tar.gz
tar -xvzf openSeeFace.tar.gz

mkdir Live2DForScala-Swing
cp -rvf openSeeFace Live2DForScala-Swing/
cp -v modules/examples/swing/target/scala-2.13/Live2DForScala-Swing-*.jar Live2DForScala-Swing/

mkdir Live2DForScala-SWT-Linux
cp -rvf openSeeFace Live2DForScala-SWT-Linux/
cp -v modules/examples/swt-linux-bundle/target/scala-2.13/Live2DForScala-SWT-Linux-*.jar Live2DForScala-SWT-Linux/

mkdir Live2DForScala-SWT-Win
cp -rvf openSeeFace Live2DForScala-SWT-Win/
cp -v modules/examples/swt-windows-bundle/target/scala-2.13/Live2DForScala-SWT-Windows-*.jar Live2DForScala-SWT-Win/

zip -r Live2DForScala-Swing-$version.zip Live2DForScala-Swing/
zip -r Live2DForScala-SWT-Win-$version.zip Live2DForScala-SWT-Win/
tar -cvzf Live2DForScala-SWT-Linux-$version.tar.gz Live2DForScala-SWT-Linux/

rm -rvf openSeeFace.tar.gz openSeeFace Live2DForScala-Swing/ Live2DForScala-SWT-Linux/ Live2DForScala-SWT-Win/

ls -l

