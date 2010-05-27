REM usage: convert_all.bat dir_name
REM D:\tech\read\architecture\2010_arch_jpg
REM cd /d %1
cd /d D:\tech\read\architecture\2010_arch_jpg
mkdir trim_edge
for %%f in (*.jpg) do (
	REM convert -trim 572.jpg 572.0.jpg
	convert -trim "%%f" "trim_edge\%%f.jpg"
	REM echo %%f
)
pause