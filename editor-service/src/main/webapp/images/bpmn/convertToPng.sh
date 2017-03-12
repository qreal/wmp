for file in ./svg/*
do
 inkscape --export-png="${file%.*}".png --export-dpi=10 --export-background-opacity=0 --without-gui "${file%.*}".svg
done