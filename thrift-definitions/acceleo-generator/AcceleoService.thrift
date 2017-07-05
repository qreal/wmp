namespace java com.qreal.wmp.thrift.gen

include "../struct/Palette.thrift"

service AcceleoServiceThrift {
    /**
    * Create new metamodel according to palette
    **/
    void createMetamodel(1: Palette.TPalette palette),
    /**
    * Create new model and do generation code, using this model, current metamodel and rules of generation.
    **/
    void generate(1: Palette.TModel model)
}
