namespace java com.qreal.wmp.thrift.gen

include "../struct/Palette.thrift"
include "../exception/DbExceptions.thrift"

service PaletteDbService {
    /**
    * Create new language and save it.
    **/
    i64 createPalette(1: Palette.TPalette palette) throws(1: DbExceptions.TIdAlreadyDefined alreadyDefined);
    /**
    * Load palette from DB with paletteId.
    * TNotFound is thrown if the palette with specified id is not found.
    **/
    Palette.TPalette loadPalette(1: i64 paletteId) throws (1: DbExceptions.TNotFound e);
    /**
    * Show all languages of certain user.
    **/
    set<Palette.TPaletteView> getPaletteViewsByUserName(1: string username) throws (1: DbExceptions.TNotFound e)
}
