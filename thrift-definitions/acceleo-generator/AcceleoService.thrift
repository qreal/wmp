namespace java com.qreal.wmp.thrift.gen

include "../struct/Palette.thrift"

service AcceleoServiceThrift {
    void createMetamodel(1: Palette.TPalette palette),
    void createModel(1: Palette.TModel model)
}