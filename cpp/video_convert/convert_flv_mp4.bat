REM -r 30 -s 640x360 -vb 585k -aspect "16:9" -padtop 0 -padbottom 0 -padleft 0 -padright 0
REM x ffmpeg -v 0 -i %1 -acodec copy -vcodec copy %1.copy.mp4
REM ffmpeg -y -i %1 -ac 2 -ab 64k -ar 48000 -vcodec mpeg4 -s 640x360 -padtop 0 -padbottom 0 -padleft 0 -padright 0 %1.mp4
REM ffmpeg -y -i %1 -ac 2 -ab 96.7k -ar 44100 -vcodec copy -r 30 -s 640x360 -vb 585k -aspect "16:9" -padtop 0 -padbottom 0 -padleft 0 -padright 0 %1.3.mp4
REM ffmpeg -y -i %1 -ac 2 -ab 96.7k -ar 44100 -vcodec mpeg4 -r 30 -s 640x360 -vb 585k -aspect "16:9" -padtop 0 -padbottom 0 -padleft 0 -padright 0 %1.4.mp4
REM x ffmpeg -y -i %1 -ac 2 -ab 96.7k -ar 44100 -vcodec copy %1.5.mp4
REM @ ffmpeg -v 0 -i %1 -acodec copy -vcodec copy -r 30 -s 640x360 -vb 585k -aspect "16:9" -padtop 0 -padbottom 0 -padleft 0 -padright 0 %1.6.mp4
ffmpeg -v 0 -i %1 -acodec copy -vcodec copy -padtop 0 -padbottom 0 -padleft 0 -padright 0 %1.7.mp4
REM @ ffmpeg -v 0 -i %1 -acodec copy -vcodec copy -r 30 -s 640x360 -vb 585k -aspect "16:9" %1.8.mp4
REM x ffmpeg -v 0 -i %1 -acodec copy -vcodec copy %1.9.mp4