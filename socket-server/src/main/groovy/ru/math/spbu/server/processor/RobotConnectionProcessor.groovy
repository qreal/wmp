package ru.math.spbu.server.processor

import groovy.util.logging.Slf4j
import ru.math.spbu.server.connection.RobotsConnectionInfoManager

/**
 * Created by ageevdenis on 28-2-15.
 */
@Slf4j
class RobotConnectionProcessor implements ConnectionProcessor {

    RobotsConnectionInfoManager connectionInfoManager = RobotsConnectionInfoManager.instance

    @Override
    def process(Socket socket, def message) {
        def result
        switch (message.type) {
            case "connect":
                result = connect(socket, message.robot)
                break;
            default: result = "Unknown type of message"
        }
        return result
    }

    def connect(Socket socket, def robot) {
        connectionInfoManager.createRobotConnection(robot.ssid, robot, socket)
        String key = robot.ssid
        log.info "The connection is established"
        log.info "Robot $robot.ssid accepted"
        socket.withStreams { input, output ->
            def w = new BufferedWriter(new OutputStreamWriter(output))

            sendMessage "Robot accepted", w

            while (connectionInfoManager.isRobotConnected(key)) {

                while (connectionInfoManager.isRobotConnected(key) && !connectionInfoManager.hasMessage(key)) {
                    Thread.sleep(3000);
                }

                if (connectionInfoManager.hasMessage(key)) {
                    sendMessage connectionInfoManager.getMessages(key), w
                    connectionInfoManager.markAsRead(key)
                }
            }

            log.info "$robot.ssid disconnected"
        }


    }

    def sendMessage(msg, writer) {
        log.info "Sending: >" + msg + "<"
        writer.writeLine(msg.toString())
        writer.flush();
    }
}
