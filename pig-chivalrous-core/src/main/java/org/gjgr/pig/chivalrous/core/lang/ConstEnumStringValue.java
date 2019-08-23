/**
 * Copyright (c) 2011-2015, James Zhan 詹波 (jfinal@126.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gjgr.pig.chivalrous.core.lang;

import java.util.regex.Pattern;

/**
 * Global constants definition
 *
 * @author gwd
 */
public enum ConstEnumStringValue {

    CORE_VERSION("0.1"),
    DEFAULT_ENCODING("UTF-8"),
    Backslash("\\"),
    Bracket("[]"),
    LeftBracket("["),
    RightBracket("]"),
    BracketIndex("[*]"),
    Placeholder("{}"),
    LeftPlaceholder("{"),
    RightPlaceHolder("}"),
    AndPlaceHolder("{and}"),
    OrPlaceHolder("{or}"),
    PlaceholderIndex("{*}");
    private final String value;

    private ConstEnumStringValue(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String value() {
        return value;
    }

    public String quote() {
        return Pattern.quote(value);
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return getValue();
    }
}