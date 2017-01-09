namespace java com.qreal.wmp.thrift.gen

include "../struct/Palette.thrift"
include "../exception/DbExceptions.thrift"

service PaletteDbService {
    i64 createPalette(1: Palette.TPalette palette) throws(1: DbExceptions.TIdAlreadyDefined alreadyDefined,
                                                           2: DbExceptions.TAborted aborted);

    Palette.TPalette loadPalette(1: i64 paletteId) throws (1: DbExceptions.TNotFound e);
    set<Palette.TPaletteView> getPaletteViewsByUserName(1: string username) throws (1: DbExceptions.TNotFound e)
}
