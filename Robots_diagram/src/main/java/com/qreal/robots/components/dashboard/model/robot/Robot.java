package com.qreal.robots.components.dashboard.model.robot;

import com.qreal.robots.components.authorization.model.auth.User;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "robots")
public class Robot {

    private Integer id;
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

    public Robot(int id, String name, String ssid, User owner) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.ssid = ssid;
        owner.getRobots().add(this);
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id",
            unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
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

    @Column(name = "ssid", nullable = false, length = 45)
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

}
