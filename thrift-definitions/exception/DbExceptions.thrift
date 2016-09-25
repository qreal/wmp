namespace java com.qreal.wmp.thrift.gen

/** Used in normal cases of excution. (instead of returning null) */
exception TNotFound {
    1: string id,
    2: string message
}

/** Used in exceptional situations to inform other services and the client.*/
exception TAborted {
    1: string textCause,
    2: string message,
    3: string fullClassName
}

/** Used in case of connection errors.*/
exception TErrorConnection {
    1: string nameClient,
    2: string message
}

/**
* Used as a coding rule.
* This exception will be printed to log, but no correction process will be performed, because this exception is
* the evidence of coding mistake.
**/
exception TIdNotDefined {
    1: string message
}

/**
* Used as a coding rule.
* This exception will be printed to log, but no correction process will be performed, because this exception is
* the evidence of coding mistake.
**/
exception TIdAlreadyDefined {
    1: string message
}