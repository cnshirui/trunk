REM usage: convert_all.bat dir_name
ffmpeg -v 0 -y -i "%1" -acodec copy -vcodec copy -padtop 0 -padbottom 0 -padleft 0 -padright 0 "%1.mp4"
pause