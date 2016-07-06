package ru.math.spbu.server

import groovy.json.JsonSlurper
import groovy.transform.TupleConstructor
import groovy.util.logging.Slf4j
import ru.math.spbu.server.processor.ConnectionProcessor
import ru.math.spbu.server.processor.RobotConnectionProcessor
import ru.math.spbu.server.processor.WebAppConnectionProcessor

/**
 * Created by ageevdenis on 28-2-15.
 */
@TupleConstructor
@Slf4j
class WorkerRunnable implements Runnable {

    private Socket socket
    private def jsonSlurper = new JsonSlurper()
    private ConnectionProcessor webAppProcessor = new WebAppConnectionProcessor()
    private ConnectionProcessor robotProcessor = new RobotConnectionProcessor()


    @Override
    void run() {
        log.info "Client accepted with ip $socket.inetAddress"
        socket.withStreams { input, output ->
            def r = new BufferedReader(new InputStreamReader(input))
            def w = new BufferedWriter(new OutputStreamWriter(output))

            String receivedMessage = r.readLine()
            if (receivedMessage == null) {
                socket.close()
                log.info "Client with ip $socket.inetAddress disconnected "
                return
            }

            if (messageInOkFormat(receivedMessage)) {
                def json = jsonSlurper.parseText(receivedMessage)
                switch (json.from) {
                    case "WebApp":
                        sendMessage(webAppProcessor.process(socket, json), w)
                        break
                    case "Robot":
                        robotProcessor.process(socket, json)
                        break
                    default:
                        sendMessage "Unknown 'from' type. Should be [Robot, WebApp]", w
                }

            } else {
                sendMessage "Message is in wrong format. Should be JSON", w
            }

            log.info "Client with ip $socket.inetAddress disconnected "


        }
    }

    def sendMessage(msg, writer) {
        log.info "Sending: >" + msg + "<"
        writer.writeLine(msg.toString())
        writer.flush();
    }

    boolean messageInOkFormat(String message) {
        try {
            def receivedObject = jsonSlurper.parseText(message)
            return receivedObject.from != null
        } catch (JsonException) {
            return false
        }


    }

}


