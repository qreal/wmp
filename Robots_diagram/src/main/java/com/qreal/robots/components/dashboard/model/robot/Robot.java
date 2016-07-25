package com.qreal.robots.components.dashboard.model.robot;

import com.qreal.robots.components.authorization.model.auth.User;
import com.qreal.robots.thrift.gen.TRobot;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Struct represents TRIK robot.
 * field name -- name of robot, not unique
 * field ssid -- ssid of robot's wifi, not unique
 * field owner -- owner of robot
 * field id -- surrogate key
 */

@Entity
@Table(name = "robots")
public class Robot {

    private Long id;

    private String name;

    private String ssid;

    private User owner;

    public Robot() {
    }

    public Robot(String name, String ssid) {
        this.name = name;
        this.ssid = ssid;
    }

    public Robot(String name, String ssid, User owner) {
        this.owner = owner;
        this.name = name;
        this.ssid = ssid;
        owner.getRobots().add(this);
    }

    public Robot(long id, String name, String ssid, User owner) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.ssid = ssid;
        owner.getRobots().add(this);
    }

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    public User getOwner() {
        return this.owner;
    }

    public void setOwner(User owner) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Robot)) return false;

        Robot robot = (Robot) o;
        return name.equals(robot.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

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

        return  tRobot;
    }

}
