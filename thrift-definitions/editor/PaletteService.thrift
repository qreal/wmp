namespace java com.qreal.wmp.thrift.gen

include "../struct/Palette.thrift"

service PaletteServiceThrift {
//CRUD Palettes
    i64 createPalette(1: Palette.TPalette palette),
    Palette.TPalette loadPalette(1: i64 paletteId),
    set<Palette.TPaletteView> getPaletteViews(),
    void createMetamodel(1: Palette.TPalette palette),
    void generate(1: Palette.TModel model)
}
