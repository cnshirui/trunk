REM usage: convert_all.bat dir_name
cd /d %1
mkdir pure_mp4
for %%f in (*.flv) do (
	REM echo %%f
	REM ffmpeg -v 0 -y -i "%%f" -acodec copy -vcodec copy -aspect "16:9" -padtop 0 -padbottom 0 -padleft 0 -padright 0 "pure_mp4\%%f.mp4"
	ffmpeg -v 0 -y -i "%%f" -acodec copy -vcodec mpeg4 -padtop 0 -padbottom 0 -padleft 0 -padright 0 "pure_mp4\%%f.mp4"
)
cd ..