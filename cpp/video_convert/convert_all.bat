REM usage: convert_all.bat dir_name
cd /d %1
mkdir mp4.h.264
for %%f in (*.flv) do (
	REM echo %%f
	ffmpeg -v 0 -y -i "%%f" -acodec copy -vcodec copy -aspect "16:9" -padtop 0 -padbottom 0 -padleft 0 -padright 0 "mp4.h.264\%%f.mp4"
)
cd -