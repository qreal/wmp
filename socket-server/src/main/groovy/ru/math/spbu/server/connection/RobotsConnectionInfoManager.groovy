package ru.math.spbu.server.connection

import groovy.json.JsonOutput
import groovy.util.logging.Slf4j

import java.util.concurrent.ConcurrentHashMap

/**
 * @author Ageev Denis
 *  Created  02-3-15 14:50.
 */

@Slf4j
@Singleton
class RobotsConnectionInfoManager {

    Map<String, RobotConnectionInfo> robotConnections = new ConcurrentHashMap<>()
    Map<String, List<Message>> robotMessages = new ConcurrentHashMap<>()


    def createRobotConnection(String ssid, def desc, Socket socket) {
        RobotConnectionInfo robotsConnectionInfo = new RobotConnectionInfo(robotJson: desc, socket: socket, ssid: ssid)
        def key = ssid
        robotConnections.put(key, robotsConnectionInfo)
    }

    def closeConnection(String key) {
        RobotConnectionInfo robotsConnectionInfo = robotConnections.get(key)
        robotsConnectionInfo.socket.close()
        robotConnections.remove(key)
    }

    boolean isRobotConnected(String key) {
        if (!robotConnections.containsKey(key)) {
            return false
        }
        RobotConnectionInfo robotsConnectionInfo = robotConnections.get(key)
        if (isConnectionClosed(robotsConnectionInfo.socket)) {
            robotConnections.remove(key)
            return false
        }
        return true
    }

    def getRobot(String key) {
        return robotConnections.get(key)
    }


    def addMessage(String key, Message message) {
        if (robotMessages.containsKey(key)) {
            robotMessages.get(key).add(message);
        } else {
            robotMessages.put(key, [message])
        }
    }

    def hasMessage(String key) {
        if (!robotMessages.containsKey(key)) {
            return false
        }
        return robotMessages.get(key).size() > 0;
    }

    def getMessages(String key) {
        if (!robotMessages.containsKey(key)) {
            throw new IllegalArgumentException("Unable to find messages for robot $key")
        }
        return JsonOutput.toJson(robotMessages.get(key))
    }

    def markAsRead(String key) {
        if (robotMessages.containsKey(key)) {
            robotMessages.remove(key)
        }
    }


    private static boolean isConnectionClosed(Socket socket) {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println("HeartBeat")
        return out.checkError();
    }

}

