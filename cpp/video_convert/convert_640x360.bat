REM ffmpeg -y -i %1 -ac 2 -ab 64k -ar 48000 -vcodec mpeg4 -s 640x360 -padtop 0 -padbottom 0 -padleft 0 -padright 0 %1.mp4
REM ffmpeg -y -i %1 -ac 2 -ab 96.7k -ar 44100 -vcodec copy -r 30 -s 640x360 -vb 585k -aspect "16:9" -padtop 0 -padbottom 0 -padleft 0 -padright 0 %1.3.mp4
REM ffmpeg -y -i %1 -ac 2 -ab 96.7k -ar 44100 -vcodec mpeg4 -r 30 -s 640x360 -vb 585k -aspect "16:9" -padtop 0 -padbottom 0 -padleft 0 -padright 0 %1.4.mp4
ffmpeg -y -i %1 -ac 2 -ab 96.7k -ar 44100 -vcodec copy -r 30 -s 640x360 -vb 585k -aspect "16:9" -padtop 0 -padbottom 0 -padleft 0 -padright 0 %1.3.mp4