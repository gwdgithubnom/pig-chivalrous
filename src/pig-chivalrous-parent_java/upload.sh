#!/bin/bash
BASEDIR=`dirname $0`/..
BASEDIR=`(cd "$BASEDIR"; pwd)`
readonly module="$BASEDIR/pig-chivalrous-parent_java"
TargetDir=`find ${module} -name target -type d`
for m in ${TargetDir};
    do
        for var in $(find ${m} -maxdepth 1 -iname pig-*0.0.3*.jar);
        do
            echo ${var##*/} ${var%/*}
            put ${var}
        done
    done
# find . -iname micsql-*0.0.2*.jar | xargs -L 1 basename
