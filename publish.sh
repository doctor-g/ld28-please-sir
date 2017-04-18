#!/bin/bash
DEST=/home/pvg/www/games/2013/ld28
echo Publishing to $DEST
cd html/target/ld28-html-1.0-SNAPSHOT
rsync -avz --exclude 'META-INF' --exclude 'WEB-INF' --delete . $DEST/
