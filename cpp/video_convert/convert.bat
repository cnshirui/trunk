REM ffmpeg -v 0 -y -i "%%f" -acodec copy -vcodec copy -s 480x320 -aspect "16:9" -padtop 0 -padbottom 0 -padleft 0 -padright 0 "output\%%f.mp4"
REM ffmpeg -v 0 -y -i %1 -acodec copy -vcodec copy -s 320x240 -aspect "16:9"  -padtop 0 -padbottom 0 -padleft 0 -padright 0 %1.mp4
REM ffmpeg -y -i %1 -ac 2 -ab 64k -ar 48000 -vcodec mpeg4 -s 320x240 -padtop 0 -padbottom 0 -padleft 0 -padright 0 %1.mp4
REM ffmpeg -y -i %1 -acodec copy -vcodec mpeg4 -s 320x240 -padtop 0 -padbottom 0 -padleft 0 -padright 0 %1.mp4
REM ffmpeg -y -i %1 -acodec copy -vcodec mpeg4 -s 464x326 -padtop 0 -padbottom 0 -padleft 0 -padright 0 %1.mp4
REM -s 480x320

ffmpeg -y -i %1 -acodec copy -vcodec 3g2 -padtop 0 -padbottom 0 -padleft 0 -padright 0 %2
