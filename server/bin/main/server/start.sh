#!/bin/sh
echo "executing server.jar..."
java -jar ../../../../build/libs/*.jar
netstat -ntlp 2>/dev/null | grep 8000 |  awk '{ print $7 }' | cut -d "/" -f 1 | xargs kill

