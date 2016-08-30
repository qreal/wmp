namespace java com.qreal.wmp.thrift.gen

exception TNotFound {
    1: string id,
    2: string message
}

//This exception will be printed to log, but no correction process will be performed, because this exception is
// evidence of coding mistake
exception TIdNotDefined {
    1: string message
}

//This exception will be printed to log, but no correction process will be performed, because this exception is
// evidence of coding mistake
exception TIdAlreadyDefined {
    1: string message
}