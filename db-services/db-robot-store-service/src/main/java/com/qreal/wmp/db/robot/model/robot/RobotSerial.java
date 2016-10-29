package com.qreal.wmp.db.robot.model.robot;

import com.qreal.wmp.thrift.gen.TRobot;
import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/** Representation of a TRIK robot in dashboard service. */
@Entity
@Table(name = "robots")
@Data
public class RobotSerial {
    /** Surrogate key for RobotSerial.*/
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    /** Name of the robot (unique only in robot's group of owner).*/
    @Column(name = "name", nullable = false, length = 45)
    private String name;

    /** SSID of robot's WiFi.*/
    @Column(name = "ssid", nullable = false, length = 45)
    private String ssid;

    /** Owner of the robot.*/
    @Column(name = "username", nullable = false)
    private String owner;

    public RobotSerial() {
    }

    /** Constructor-converter from Thrift TRobot to RobotSerial.*/
    public RobotSerial(TRobot tRobot) {
        if (tRobot.isSetId()) {
            id = tRobot.getId();
        }

        if (tRobot.isSetName()) {
            name = tRobot.getName();
        }

        if (tRobot.isSetSsid()) {
            ssid = tRobot.getSsid();
        }

        if (tRobot.isSetUsername()) {
            owner = tRobot.getUsername();
        }
    }

    /** Converter from RobotSerial to Thrift TRobot.*/
    public TRobot toTRobot() {
        TRobot tRobot = new TRobot();

        if (id != null) {
            tRobot.setId(id);
        }

        if (name != null) {
            tRobot.setName(name);
        }

        if (ssid != null) {
            tRobot.setSsid(ssid);
        }

        if (owner != null) {
            tRobot.setUsername(owner);
        }

        return tRobot;
    }

}
