#!/bin/sh

#ls -1 | while read f; do java YourClass $f > "$f.new"; done
ls */bin*/* | 
while read f; 
	do
		echo $f;
		if [ -d $f ]
		then
			echo ---------------- is a directory !;
		else
			rm $f;
			echo $f OK !;
		fi
done
