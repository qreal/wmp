/*
 * Copyright Vladimir Zakharov
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

package com.qreal.stepic.robots.model.checker;

import java.io.Serializable;
import java.util.List;

/**
 * Created by vladimir-zakharov on 31.08.15.
 */
public class Description implements Serializable {

    private String main;
    private String note;
    private List<Hint> hints;

    public Description() {
    }

    public Description(String main) {
        this.main = main;
    }

    public Description(String main, String note) {
        this.main = main;
        this.note = note;
    }

    public Description(String main, String note, List<Hint> hints) {
        this.main = main;
        this.note = note;
        this.hints = hints;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Hint> getHints() {
        return hints;
    }

    public void setHints(List<Hint> hints) {
        this.hints = hints;
    }

}
