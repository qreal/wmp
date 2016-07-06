package ru.math.spbu.server.connection

import groovy.transform.TupleConstructor

/**
 * Created by ageevdenis on 02-3-15.
 */

@TupleConstructor
class RobotConnectionInfo {

    String ssid
    def robotJson
    Socket socket
}
