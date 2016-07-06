<%--
  ~ Copyright Vladimir Zakharov
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~      http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

<h2><spring:message code="label.task"/>: ${title}. ${name}</h2>
<br>

<h4><spring:message code="label.description"/>: ${description.getMain()}
</h4>

<c:if test="${not empty description.getNote()}">
    <h5 class="note">
        <spring:message code="label.note"/>: ${description.getNote()}
    </h5>
</c:if>

<c:set var="hints" value="${description.getHints()}"/>
<c:if test="${not empty hints}">
    <c:forEach var="hint" items="${hints}">
        <div class="myTooltip textTooltip">
            ${hint.getName()}
            <div>
                <span>${hint.getText()}</span>
            </div>
        </div>
    </c:forEach>
</c:if>

