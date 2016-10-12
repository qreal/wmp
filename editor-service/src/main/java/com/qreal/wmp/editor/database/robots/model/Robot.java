package com.qreal.wmp.editor.database.robots.model;

import com.qreal.wmp.editor.database.users.model.User;
import com.qreal.wmp.thrift.gen.TRobot;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/** Representation of a TRIK robot in dashboard service.*/
@Data
@EqualsAndHashCode(exclude = "owner")
@ToString(exclude = "owner")
public class Robot {
    /** Surrogate key for the robot.*/
    private Long id;

    /** Name of the robot (unique only in robot's group of owner).*/
    private String name;

    /** SSID of robot's WiFi.*/
    private String ssid;

    /** Owner of the robot.*/
    private User owner;

    public Robot() {
    }

    public Robot(String name, String ssid) {
        this.name = name;
        this.ssid = ssid;
    }

    public Robot(String name, String ssid, User owner) {
        this(name, ssid);
        this.owner = owner;
        owner.getRobots().add(this);
    }

    /** Full Robot constructor.*/
    public Robot(long id, String name, String ssid, User owner) {
        this(name, ssid, owner);
        this.id = id;
        owner.getRobots().add(this);
    }

    /** Constructor-converter from Thrift TRobot to Robot.*/
    public Robot(TRobot tRobot, User owner) {
        if (tRobot.isSetId()) {
            id = tRobot.getId();
        }

        if (tRobot.isSetName()) {
            name = tRobot.getName();
        }

        if (tRobot.isSetSsid()) {
            ssid = tRobot.getSsid();
        }

        this.owner = owner;

        owner.getRobots().add(this);
    }

    /** Converter from Robot to Thrift TRobot.*/
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
            tRobot.setUsername(owner.getUsername());
        }

        return tRobot;
    }
}
