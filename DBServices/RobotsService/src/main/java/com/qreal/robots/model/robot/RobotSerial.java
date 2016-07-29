package com.qreal.robots.model.robot;

import com.qreal.robots.thrift.gen.TRobot;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * TRIK robot in dashboard service.
 */
@Entity
@Table(name = "robots")
public class RobotSerial {

    /**
     * Surrogate key for RobotSerial.
     */
    private Long id;

    /**
     * Name of robot (unique only in robot's group of owner).
     */
    private String name;

    /**
     * SSID of robot's WiFi.
     */
    private String ssid;

    /**
     * Owner of robot.
     */
    private String owner;

    public RobotSerial() {
    }

    /**
     * Constructor-converter from Thrift TRobot to RobotSerial.
     */
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

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id",
            unique = true, nullable = false)
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "username", nullable = false)
    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Column(name = "name", nullable = false, length = 45)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "ssid",
            nullable = false, length = 45)
    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof RobotSerial)) {
            return false;
        }

        RobotSerial robot = (RobotSerial) object;
        return name.equals(robot.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * Converter from RobotSerial to Thrift TRobot.
     */
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
