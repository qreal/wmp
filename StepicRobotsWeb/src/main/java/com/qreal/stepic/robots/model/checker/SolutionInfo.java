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

/**
 * Created by vladimir-zakharov on 21.03.16.
 */
public class SolutionInfo {

    private String filename;
    private String kit;
    private String taskId;
    private String uuidStr;

    public SolutionInfo(String filename, String kit, String taskId, String uuidStr) {
        this.filename = filename;
        this.kit = kit;
        this.taskId = taskId;
        this.uuidStr = uuidStr;
    }

    public String getFilename() {
        return filename;
    }

    public String getKit() {
        return kit;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getUuidStr() {
        return uuidStr;
    }

    public String getFilenameWithoutExtension() {
        return filename.substring(0, filename.length() - 4);
    }

}
