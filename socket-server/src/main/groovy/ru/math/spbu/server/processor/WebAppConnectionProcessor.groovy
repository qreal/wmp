package ru.math.spbu.server.processor

import groovy.json.JsonOutput
import ru.math.spbu.server.connection.Message
import ru.math.spbu.server.connection.RobotConnectionInfo
import ru.math.spbu.server.connection.RobotsConnectionInfoManager

/**
 * Created by ageevdenis on 28-2-15.
 */
class WebAppConnectionProcessor implements ConnectionProcessor {

    public static final String SUCCESS_MESSAGE = "{\"status\":\"OK\"}"
    RobotsConnectionInfoManager robotsConnectionInfoManager = RobotsConnectionInfoManager.instance

    @Override
    def process(Socket socket, def message) {
        def result
        switch (message.type) {
            case "getOnlineRobots":
                result = getOnlineRobots(message.robots);
                break;
            case "sendDiagram":
                result = sendDiagram(message.robot)
                break
            case "closeConnection":
                result = closeConnection(message.robot)
                break
            case "sendModelConfig":
                result = sendModelConfig(message.robot)
                break
            default: result = "Unknown type of connection"
        }
        return result
    }

    def closeConnection(def robot) {
        def key = robot.ssid
        robotsConnectionInfoManager.closeConnection(key)
        return "Closed connection for robot $robot.id"
    }


    def getOnlineRobots(def robots) {
        def onlineUserRobots = []

        robots.each { robot ->
            String key = robot.ssid
            RobotConnectionInfo robotConnectionInfo = robotsConnectionInfoManager.getRobot(key)
            if (robotConnectionInfo != null) {
                onlineUserRobots.add(robotConnectionInfo.robotJson)
            }
        }

        return JsonOutput.toJson(onlineUserRobots)
    }


    def sendDiagram(def robot) {
        String key = robot.ssid
        Message message = new Message(type: "sendDiagram", text: robot.program)
        robotsConnectionInfoManager.addMessage(key, message)
        return SUCCESS_MESSAGE
    }

    def sendModelConfig(def robot) {
        String key = robot.ssid
        Message message = new Message(type: "sendModelConfig", text: robot.modelConfig)
        robotsConnectionInfoManager.addMessage(key, message)
        return SUCCESS_MESSAGE
    }
}
