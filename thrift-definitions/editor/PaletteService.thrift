namespace java com.qreal.wmp.thrift.gen

include "../struct/Palette.thrift"

service PaletteServiceThrift {
//CRUD Palletes
    i64 createPalette(1: Palette.TPalette palette)
}