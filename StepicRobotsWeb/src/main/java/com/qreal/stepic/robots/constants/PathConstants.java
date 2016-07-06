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

package com.qreal.stepic.robots.constants;

/**
 * Created by vladimir-zakharov on 07.08.15.
 */
public class PathConstants {

    public static final String STEPIC_PATH = System.getenv("STEPIC");
    public static final String COMPRESSOR_PATH = STEPIC_PATH + "/compressor";
    public static final String CHECKER_PATH = STEPIC_PATH + "/trikStudio-checker/bin/check-solution.sh";
    public static final String PATH_TO_GRAPHICAL_PART = "tree/graphical/RobotsMetamodel/RobotsDiagram";
    public static final String PATH_TO_LOGICAL_PART = "tree/logical/RobotsMetamodel/RobotsDiagram";
    public static final String PATH_TO_ROOT_ID = "tree/logical/ROOT_ID/ROOT_ID/ROOT_ID";

}
