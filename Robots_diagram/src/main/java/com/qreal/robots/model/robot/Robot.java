/*
 * Copyright Denis Ageev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qreal.robots.model.robot;

import com.qreal.robots.model.auth.User;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by dageev on 07.03.15.
 */

@Entity
@Table(name = "robots")
public class Robot {

    private Integer id;
    private String name;
    private String ssid;
    private User owner;

    public Robot() {
    }

    public Robot(String name, String ssid, User owner) {
        this.owner = owner;
        this.name = name;
        this.ssid = ssid;
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
