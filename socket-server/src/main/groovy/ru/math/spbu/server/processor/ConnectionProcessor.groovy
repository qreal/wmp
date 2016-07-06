package ru.math.spbu.server.processor

/**
 * Created by ageevdenis on 28-2-15.
 */
interface ConnectionProcessor {

    def process(Socket socket, def object);
}