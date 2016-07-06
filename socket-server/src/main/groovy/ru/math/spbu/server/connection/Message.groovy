package ru.math.spbu.server.connection

import groovy.transform.TupleConstructor

/**
 * Created by dageev on 04.04.15.
 */

@TupleConstructor
class Message {
    def type
    def text
}
