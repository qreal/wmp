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

package com.qreal.stepic.robots.model.diagram;

import java.io.Serializable;

/**
 * Created by vladimir-zakharov on 09.06.15.
 */
public class SubmitResponse implements Serializable {

    private String message;
    private Report report;
    private String trace;
    private String fieldXML;

    public SubmitResponse(String message, Report report, String trace, String fieldXML) {
        this.message = message;
        this.report = report;
        this.trace = trace;
        this.fieldXML = fieldXML;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }

    public String getFieldXML() {
        return fieldXML;
    }

    public void setFieldXML(String fieldXML) {
        this.fieldXML = fieldXML;
    }

}
