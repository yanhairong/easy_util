#!/usr/bin/env bash

echo release MVP start

sdkFile="MVP.jar"
sdkDir="../release"

srcFile="MVP-release.aar"
srcPath="../MVP/build/outputs/aar/$srcFile"

if [ ! -d "$sdkDir" ];then
mkdir "$sdkDir"
fi

cd "$sdkDir"

if [ -f "$sdkFile" ];then
rm "$sdkFile"
fi


cp "$srcPath" .
mv "$srcFile" tmp.jar
jar xf tmp.jar -x classes.jar
rm tmp.jar
mv classes.jar "$sdkFile"

echo release MVP end

