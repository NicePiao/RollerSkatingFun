set fn=D:\program files\ImageMagick-6.9.2-Q16\convert.exe
for /f "tokens=*" %%i in ('dir/s/b *.png') do "%fn%" "%%i" -strip "%%i"